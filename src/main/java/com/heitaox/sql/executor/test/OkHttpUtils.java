package com.heitaox.sql.executor.test;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.heitaox.sql.executor.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class OkHttpUtils {
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, Object> paramMap = new TreeMap<>();
    private Object paramObj;
    private String url;
    private Request.Builder request;

    /**
     * 请求签名key
     */
    private String signKey;
    /**
     * 请求加签
     */
    private boolean sign = false;
    /**
     * 请求加签函数
     */
    private Function<String, String> signMethod;
    /**
     * 返回签名key
     */
    private String checkSignKey;
    /**
     * 返回值验签
     */
    private boolean checkSign;

    /**
     * 返回值验签函数
     */
    private Function<String, String> checkSignMethod;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtils() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {

                    //设置代理方式
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));

                    TrustManager[] trustManagers = buildTrustManagers();
                    okHttpClient = new OkHttpClient.Builder()
                            //设置连接超时时间
                            .connectTimeout(15, TimeUnit.SECONDS)
                            //写入超时时间
                            .writeTimeout(20, TimeUnit.SECONDS)
                            //从连接成功到响应的总时间
                            .readTimeout(20, TimeUnit.SECONDS)
                            //跳过ssl认证(https)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(true)
//                            .proxy(proxy)//代理ip
                            //设置连接池  最大连接数量  , 持续存活的连接
                            .connectionPool(new ConnectionPool(50, 10, TimeUnit.MINUTES))
                            .build();
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtils.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static OkHttpUtils builder() {
        return new OkHttpUtils();
    }

    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtils url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addParam(String key, Object value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    public OkHttpUtils addParam(Map<String, Object> param) {
        if (MapUtils.isNotEmpty(param)) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(param.size());
            }
        }
        paramMap.putAll(param);
        return this;
    }

    public OkHttpUtils addParam(Object obj) {
        this.paramObj = obj;
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }


    /**
     * 初始化get方法
     *
     * @return
     */
    // public OkHttpUtils get() {
    //     request = new Request.Builder().get();
    //     StringBuilder urlBuilder = new StringBuilder(url);
    //     if (paramMap != null) {
    //         urlBuilder.append("?");
    //         for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
    //             urlBuilder.append(URLEncoder.encode(entry.getKey(), String.valueOf(StandardCharsets.UTF_8))).
    //                     append("=").
    //                     append(URLEncoder.encode(entry.getValue().toString(), String.valueOf(StandardCharsets.UTF_8))).
    //                     append("&");
    //         }
    //         urlBuilder.deleteCharAt(urlBuilder.length() - 1);
    //     }
    //     request.url(urlBuilder.toString());
    //     return this;
    // }

    public OkHttpUtils postJson() {
        RequestBody requestBody;
        String json = "";
        if (Objects.nonNull(paramObj)) {
            json = JSON.toJSONString(paramObj);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            request = new Request.Builder().post(requestBody).url(url);
            return this;
        }
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request = new Request.Builder().post(requestBody).url(url);
        return this;

    }

    public OkHttpUtils postForm() {
        RequestBody requestBody;
        FormBody.Builder formBody = new FormBody.Builder();
        if (paramMap != null) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue().toString());
            }
        }
        requestBody = formBody.build();
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        Response response = null;
        try {
            Request requestBody = request.build();
            // System.out.println("http请求 url:{} request:{} header:{} " + requestBody.url().url() +( Objects.nonNull(paramObj) ? JSONUtil.toJsonStr(paramObj) : JSONUtil.toJsonStr(paramMap))+JSONUtil.toJsonStr(requestBody.headers().toString()));

            response = okHttpClient
                    .newCall(requestBody)
                    .execute();
        } catch (IOException e) {
        }
        if (!response.isSuccessful()) {
        }
        ResponseBody body = response.body();
        if (Objects.isNull(body)) {
        }
        if (checkSign) {
            // 验签
            String responseSign = response.header(checkSignKey);
            if (!StringUtils.isNotEmpty(responseSign)) {
            }
            try {
                String bodyString = body.string();
                String signResult = checkSignMethod.apply(bodyString);
                if (!Objects.equals(responseSign, signResult)) {
                }
                return bodyString;
            } catch (IOException e) {
            }
        }
        try {
            return body.string();
        } catch (IOException e) {

        }
        return null;
    }


    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody body = response.body();
                if (Objects.isNull(body)) {
                }
                if (checkSign) {
                    // 验签
                    String responseSign = response.header(checkSignKey);
                    if (!StringUtils.isNotEmpty(responseSign)) {
                    }
                    try {
                        String bodyString = body.string();
                        String signResult = checkSignMethod.apply(bodyString);
                        if (!Objects.equals(responseSign, signResult)) {
                        }
                    } catch (IOException e) {
                    }
                }
                callBack.onSuccessful(call, response);
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (sign) {
            if (Objects.nonNull(paramObj)) {
                request.addHeader(signKey, signMethod.apply(JSON.toJSONString(paramObj)));
                return;
            }
            if (Objects.nonNull(paramMap)) {
                request.addHeader(signKey, signMethod.apply(JSON.toJSONString(paramMap)));
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    public OkHttpUtils sign(String signKey, Function<String, String> signMethod) {
        sign = true;
        this.signKey = signKey;
        this.signMethod = signMethod;
        return this;
    }


    public OkHttpUtils checkResponseSign(String signKey, Function<String, String> checkSignMethod) {
        checkSign = true;
        this.checkSignKey = signKey;
        this.checkSignMethod = checkSignMethod;
        return this;
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, Response response);

        void onFailure(Call call, String errorMsg);

    }



    public static void main(String[] args) {
        String addrressListStr = "[\"陕西省西安市市辖区高新区科技二路65号F座1楼\",\"四川省凉山彝族自治州会理县鹿厂镇凤营乡红云村3组\",\"陕西省西安市市辖区西安市经济开发区凤城三路33号\",\"陕西省西安市市辖区西安经济技术开发区凤城七路89号陕西粮农集团办公大楼5层508室\",\"福建省漳州市龙海市龙海区东园镇田厝村28号\",\"江苏省南通市崇川区胜利路168号10幢1层\",\"山东省青岛市市辖区山东省青岛市崂山区松岭路393号北京航空航天大学青岛研究院3号楼4楼401室\",\"浙江省杭州市江干区丁桥镇同协路28号1-13楼820室\",\"浙江省杭州市江干区浙江省杭州市江干区佰富时代中心6幢11层1106室\",\"湖北省荆州市监利县监利经济开发区章华大道185号\",\"浙江省杭州市市辖区经济技术开发区白杨街道10号大街东方科技城1713\",\"浙江省杭州市下城区浙江省杭州市下城区中大广场1号楼12楼1202室\",\"山东省日照市市辖区秦楼街道荷泽路天宁双子座1701\",\"广东省深圳市市辖区深圳市前海深港合作区前湾一路1号A栋201室（入 驻深圳市前海商务秘书有限公司）\",\"辽宁省大连市市辖区大连保税区海富路9-1号\",\"浙江省湖州市市辖区浙江省湖州市康山街道红丰路1366号3幢1219-15\",\"浙江省杭州市江干区九环路9号4号楼11楼1111室\",\"福建省三明市沙县青州镇青州村南路23号 \",\"陕西省安康市市辖区高新技术产业区开发区八里村二组82号\",\"浙江省杭州市市辖区浙江省杭州经济技术开发区丽江大厦3幢14033室\",\"陕西省安康市市辖区高新技术产业区开发区八里村二组82号\",\"浙江省金华市市辖区金华市金华旅游经济区赤松镇五联街227号1号厂房\",\"浙江省杭州市市辖区浙江省杭州市钱塘区下沙街道益丰路所:117号4幢8层8106室\",\"内蒙古自治区呼和浩特市市辖区如意开发区南区沙尔沁工业园209国道与开放大街交叉口\",\"四川省成都市市辖区天府新区华阳华府大道一段1号2栋2单元16层2号\",\"山东省聊城市市辖区山东省聊城市高新区许营镇蓝天幼儿园南侧\",\"湖北省随州市市辖区高新回龙寺路9号\",\"浙江省嘉兴市市辖区经济开发区华隆广场2幢北1107室-1\",\"福建省漳州市龙海市东园镇凤鸣村阳光512号\",\"湖南省永州市祁阳县浯溪工业新村\",\"浙江省杭州市江干区环丁路1428号2栋1301室\",\"四川省成都市市辖区四川省成都市高新区天府大道中段530号2栋24层2410号\",\"浙江省杭州市江干区下沙\",\"四川省宜宾市市辖区临港经开区沙坪街道宜宾港路北段7号\",\"浙江省杭州市市辖区临平区乔司街道汀兰街266号6幢A座3层3017室\",\"陕西省西安市市辖区陕西省西安市经开区未央路以西凤城五路南侧富力广场赛高国际街区3幢2单元5层21512室\",\"浙江省杭州市江干区众邦直播电商产业园\",\"山东省烟台市市辖区中国（山东）自由贸易试验区烟台片区长江路61号内4号楼内306号\",\"安徽省淮北市市辖区凤凰山开发区凤冠路8号\",\"陕西省安康市市辖区高新技术产业开发区高新七路18号高新产业孵化园2-4号厂房\",\"四川省成都市市辖区天府大道中段1388号11栋15层8号\",\"广东省珠海市市辖区唐家湾镇大学路101号清华科技园综合服务楼A区(座C216单元\",\"浙江省杭州市江干区杭州市钱塘区白杨街道6号大街260号19幢811室\",\"安徽省芜湖市繁昌县繁阳镇金桥五区商住楼5号网点\",\"福建省漳州市龙海市东园镇风山村乌烟厝132号2室\",\"广西壮族自治区南宁市市辖区南宁六景工业园区广西华冠茧丝业有限公司\",\"福建省漳州市龙海市 福建省漳州市龙海区石码镇紫光路43号水晶花园9幢1003室\",\"安徽省芜湖市弋江区复地南都会8楼安徽先创电器销售有限公司\",\"安徽省芜湖市弋江区复地南都会8楼安徽先创电器销售有限公司\",\"山东省潍坊市市辖区山东省潍坊高新区新城街道大观社区樱前街11188号大观天下一期商铺9-08\",\"浙江省杭州市市辖区上海市松江区新桥镇莘砖公路518号11幢202室-1\",\"四川省成都市市辖区中和花鸟街35号\",\"福建省泉州市市辖区福建省泉州市安溪县凤城镇兴安路368号龙苑小区8栋A106室\",\"山东省滨州市市辖区佃户屯办事处纬四东路浩海工贸公司院内\",\"浙江省杭州市市辖区临平区乔司街道红普北路13号1幢224室\",\"陕西省宝鸡市凤翔县柳林镇西大街11号\",\"福建省漳州市龙海市福建省龙海市石码镇锦江道246号九龙新城28幢3单元2312室内\",\"福建省漳州市龙海市漳州台商投资区角美镇龙江村丁洋170号海川花公公有限公司一层\",\"浙江省杭州市江干区临平区南苑街道泽怀广场6楼\",\"福建省漳州市龙海市海澄镇豆巷五社354-3\",\"河南省郑州市市辖区明理路266号正商木华广场1号楼9层932号\",\"福建省漳州市龙海市海城镇豆巷村五社345号\",\"江苏省宿迁市市辖区洋河新区洋河镇东工业园108号\",\"江苏省扬州市市辖区鼎兴路96号\",\"江苏省南京市市辖区江北新区大桥北路1号华侨广场1单元1806室\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区天府大道中段666号2栋15层1506号\",\"海南省海口市市辖区海口市保税区海口综合保税区联检大楼四楼A105-151室\",\"福建省漳州市市辖区福建省漳州市龙海区浮宫镇田头村前坑121号\",\"福建省漳州市龙海市龙海区东园镇南边村下田157号\",\"浙江省杭州市市辖区浙江省杭州市钱塘新区白杨街道学源街 18号浙江财经大学大学生创业园 107-125\",\"福建省三明市沙县凤岗六三路213号办公室501室\",\"山西省运城市市辖区风陵渡镇赵村\",\"河南省郑州市市辖区郑东新区康平路79号郑东商业中心1号楼16层1605\",\"河南省商丘市市辖区河南省商丘市睢阳区勒马乡工业园区 8号\",\"河南省驻马店市市辖区鹏宇金茂中心2311-2312\",\"湖北省宜昌市市辖区高新区\",\"四川省绵阳市市辖区经开区贾家店街89号（南湖双创园一期）C栋二楼\",\"四川省达州市市辖区达川区三里坪街道韩家巷176号2楼17、18、19号\",\"浙江省杭州市市辖区钱塘区白杨街道世茂江滨商业中心1幢2单元1201室\",\"广西壮族自治区南宁市市辖区高新大道80号\",\"福建省漳州市龙海市白水镇西凤村下坑西 1 0 3\",\"江苏省南通市崇川区南通市崇川区五一路1号13幢102室\",\"安徽省芜湖市市辖区芜湖市湾沚区安徽新芜经济开发区工业大道1499号绿庄4号楼403室\",\"湖南省株洲市市辖区株洲市高新技术产业园\",\"浙江省杭州市江干区白杨街道2号大街519号4--906\",\"浙江省杭州市江干区浙江省杭州市钱塘新区白杨街道2号大街515号2幢420室\",\"江苏省南通市崇川区紫琅路30号十号楼2楼\",\"江苏省常州市市辖区虞山街道方塔街45号1110\",\"山东省潍坊市市辖区王家庄街道黄景路与王宿路交叉口往西100米路南\",\"浙江省杭州市江干区白杨街道2号大街501号2幢1410室\",\"浙江省杭州市江干区郡原蓝湖国际\",\"浙江省杭州市下城区浙江省杭州市钱塘新区万晶湖畔中心园区2幢704室\",\"浙江省宁波市市辖区梅山保税港区国际商贸区一号办公楼1313室\",\"山西省长治市市辖区城西路75号\",\"河南省郑州市市辖区郑州市管城区南四环与京广路交叉口西南角百荣世贸商城D座四层D4-5-005号\",\"山东省淄博市市辖区山东省淄博市高新区鲁泰大道9号名尚城市广场1号楼11层1-1104\",\"福建省漳州市龙海市海澄镇前厝村后厝360号\",\"浙江省杭州市下城区杭州新天地商务中心4幢东楼1302室\",\"安徽省芜湖市弋江区芜湖高新技术产业开发区中山南路717号科技产业园二期2栋4层北\",\"山西省晋中市市辖区山西省晋中市山西示范区晋中开发区汇通产业园区凤栖大街与农谷大道交叉口\",\"辽宁省大连市市辖区辽宁省大连保税区自贸大厦813室\",\"浙江省杭州市江干区杭州市经济开发区海达南路656-318室\",\"浙江省杭州市江干区浙江省杭州市江干区笕桥街道浜河部落 4幢433室\",\"广东省潮州市市辖区广东省潮州市潮安区庵埠镇傲露村南千渠庵霞一道一横7号\",\"云南省昆明市市辖区中国（云南）自由贸易试验区昆明片区经开区洛阳街道办小新册社区大梨园路奥斯迪（昆明）电子商务交易产业园K区32栋3层\",\"浙江省杭州市江干区同协支路28号5幢2楼201室\",\"浙江省杭州市江干区钱塘区白杨街道2号大街519号2-301\",\"山西省忻州市市辖区顿村度假村奇顿路\",\"湖北省武汉市市辖区武汉东湖新技术开发区光谷三路777号6号楼（自贸区武汉片区）\",\"福建省厦门市市辖区中国(福建）自由贸易试验区厦门片区象屿路97号厦门国际航运中心D栋8层03单元A之六\",\"福建省漳州市龙海市福建省龙海市石码镇紫崴路42号泷澄大厦19层\",\"江苏省南通市崇川区南通市崇川区观音山街道太平路558号产品加工区1#F1工业厂房\",\"浙江省杭州市江干区浙江省杭州市江干区14号大街368号\",\"福建省漳州市龙海市东园镇凤山村123号\",\"广西壮族自治区南宁市市辖区白沙大道109号龙光普罗旺斯紫罗兰庄园15号楼3单元3-0201号\",\"福建省漳州市长泰县长泰区武安镇安盛南路1号\",\"浙江省宁波市市辖区宁波新材料创新中心东区2幢20号501-3\",\"山东省潍坊市市辖区经济开发区民主街13099号\",\"浙江省杭州市江干区白杨接道科技园路20号12幢\",\"福建省漳州市龙海市台商投资区角美镇福龙工业园仁和西路38号\",\"福建省漳州市龙海市海澄镇崎沟村河边78号\",\"江苏省徐州市市辖区经济开发区横山桥镇东方东路77号B区103室\",\"广东省肇庆市市辖区福安街2号\",\"福建省漳州市龙海市福建省漳州市龙海区东园镇凤山村福中福新村23号\",\"陕西省延安市市辖区陕西省延安市宝塔区东关街经贸大楼\",\"江苏省苏州市市辖区昆山市玉山镇元丰路113号\",\"江苏省南通市海门市三星镇，汇南村22组\",\"福建省宁德市市辖区东侨经济开发区泰禾红树林12号楼2102\",\"福建省漳州市龙海市海澄镇崎沟村河边190号\",\"浙江省杭州市江干区白杨街道2号大街501号\",\"浙江省杭州市市辖区钱塘区白杨街道宝龙商业甲心1幢1509室\",\"四川省凉山彝族自治州会理县会太路\",\"浙江省杭州市江干区浙江省杭州市钱塘新区白杨街道科技园路2号5幢1225室\",\"湖北省武汉市市辖区武汉东湖新技术开发区民族大道888号锦绣龙城36栋2-06室\",\"江苏省苏州市市辖区自由贸易试验区苏州片区药亭大道668号11幢瑞奇大厦708室\",\"浙江省杭州市下城区绍兴路389号\",\"浙江省杭州市市辖区浙江省杭州市钱塘新区临江街道临江电商创业园8-578\",\"福建省漳州市龙海市海澄镇大众北路27号\",\"江西省南昌市市辖区新褀周管理处桑海北大道1118号A座\",\"江苏省苏州市市辖区竹园路209号\",\"福建省厦门市市辖区厦门火炬高新区信息光电园金尚路2388号\",\"广西壮族自治区南宁市市辖区中国（广西）自由贸易试验区南宁片区五象大道399号龙光国际2号楼三十三层3315、3316号办公\",\"浙江省杭州市江干区白杨街道6号大街452号2幢2316室\",\"福建省漳州市龙海市东园镇\",\"河南省信阳市市辖区鸭河工区皇路店镇鸭河村九组10号\",\"四川省成都市市辖区成都高新区天目路77号10栋1单元2层 216号\",\"浙江省杭州市市辖区中国（浙江）自由贸易试验区杭州市钱塘区白杨街道科技园路57号15幢810-811号\",\"浙江省杭州市市辖区浙江省杭州市钱塘新区前进街道江东一路5000 号诚智商务中心 5 号楼前进众智创业园6577室\",\"陕西省西安市市辖区高新区高科广场A座901\",\"浙江省杭州市江干区浙江省杭州市钱塘区白杨街道科技园路57号17幢312-313室\",\"陕西省宝鸡市凤翔县雍城大道京城物流园\",\"广东省深圳市市辖区深圳市前海深港合作区前湾一路A栋201室\",\"海南省海口市市辖区澄迈县老城镇高新技术产业示范区海南生态软件园孵化楼五楼\",\"安徽省亳州市市辖区高新区蔷薇路222号院内A2栋\",\"四川省成都市市辖区高新区科新路6号4栋1层\",\"内蒙古自治区巴彦淖尔市市辖区经济开发区内蒙古易中易农业科技有限公司一楼东厅\",\"福建省厦门市市辖区中国（福建）自由贸易实验区厦门片区（保税港区）海景路278号319室之十八\",\"云南省昆明市市辖区中国(云南) 自由贸易试验区昆明片区经开区阿拉街道办顺通社区出口加工区A4--1号地块新广丰工业标准厂房11-3幢4层401号\",\"浙江省杭州市江干区浙江省杭州市钱塘区世茂江滨商业中心4幢1单元1503室\",\"江苏省南通市崇川区中南世纪城20幢2106\",\"云南省昆明市市辖区中国(云南)自由贸易试验区昆明片区经开区洛羊街道办事处呈黄路39号小新册农贸市场内866号\",\"四川省成都市市辖区自由贸易试验区成都高新区吉泰三路8号1栋1单元20层7号\",\"浙江省杭州市市辖区钱塘区白杨街道4号大街30号2幢305室\",\"福建省漳州市龙海市海澄镇崎沟村河边82号\",\"浙江省杭州市市辖区浙江省杭州市钱塘新区前进街道江东一路5000号诚智商务中心5号楼前进众智创业园5807室\",\"浙江省杭州市江干区21号大街210号奥普家居股份有限公司\",\"浙江省杭州市江干区杭州市钱塘新区白杨街道21号大街600号6幢217室\",\"山东省潍坊市市辖区综合保税区高新二路东规划以北1号325-4室\",\"云南省昆明市市辖区中国（云南）自由贸易试验区昆明片区经开区阿拉街道办顺通社区鼎南路19号产业大厦8层003-5室\",\"江苏省扬州市市辖区长春路1号1\",\"四川省成都市市辖区成都市高新区科园南路88号9栋2层201号\",\"陕西省西安市市辖区沣东新城沣东大道(东段)2196号自贸新天地创星社D0846\",\"浙江省杭州市江干区下沙十六街区商城1幛1401室\",\"浙江省杭州市市辖区浙江省杭州市临平区东湖街道钱江经济开发区康信路589号1幢101室\",\"河南省三门峡市市辖区城乡一体化示范区禹王路汇智空间313室\",\"浙江省杭州市江干区浙江省杭州技术开发区白杨街道4号大街17-6号6楼688室\",\"江苏省徐州市市辖区苏州工业园区仁爱路99号D209室\",\"福建省漳州市龙海市海澄镇崎沟村河边110号\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区萃华路89号1栋2单元22层2204号\",\"云南省昆明市市辖区昆明片区经开区新加坡产业园区II-3-6号地块\",\"云南省昆明市市辖区中国（云南）自由贸易试验区昆明片区经开区昆明鼎盛轮胎汽配总部区内7栋2单元6层601号\",\"广西壮族自治区北海市市辖区北海软件园3幢5层012号\",\"山东省聊城市市辖区滨城区黄河十路渤海十二路滨城数字经济产业园606室\",\"四川省成都市市辖区高新区紫竹北街85号1栋2楼251号\",\"广东省珠海市市辖区南屏科技工业园虹达路1号\",\"浙江省杭州市市辖区临平区南苑街道余之城2幢1102室\",\"山东省泰安市市辖区开发区南天门大街中段中国泰山高端人才创业基地\",\"辽宁省大连市市辖区保税区海富路9-1号3118室\",\"海南省海口市市辖区海南省老城高新技术产业示范区海南生态软件园A17幢一层1001\",\"福建省泉州市市辖区台商投资区张坂镇玉埕村惠南工业区滨湖南路668号\",\"浙江省嘉兴市市辖区经济技术开发区鸣羊路176号兴汇大厦9层\",\"浙江省绍兴市市辖区浙江省绍兴市绍三线永仁路\",\"福建省厦门市市辖区厦门市软件园三期溪西山尾路1号1502之一单元（法律文书送达地址）\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区观东三街270号\",\"广东省阳江市市辖区阳江高新区福冈工业园福冈大道9号\",\"浙江省杭州市江干区浙江省杭州市钱塘新区前进街道绿荫路222号综合办公楼220-88室\",\"四川省成都市市辖区高新区中和新上街18号\",\"广东省深圳市市辖区前海深港合作区前湾一路1号A栋201室（入驻深圳市前海商务秘书有限公司）\",\"浙江省杭州市下城区华丰路1号A座\",\"陕西省宝鸡市市辖区高新开发区马营镇新苑路（高新六路）西宝路555号东方一品\",\"浙江省杭州市江干区钱塘区下沙街道学源街998号浙江传媒学院生活区L楼一层A104-A105室\",\"浙江省温州市温州经济技术开发区兴平路18号\",\"河南省许昌市市辖区前进路祥和小区D16栋\",\"广东省肇庆市市辖区大旺高新技术开发区正隆一街二号\",\"浙江省宁波市市辖区高新区东青路355号二楼\",\"浙江省杭州市江干区中国(浙江)自由贸易试验区杭州市钱塘区白杨街道2号大街515号2幢410室\",\"广东省珠海市市辖区横琴新区宝华路6号105室-71219（集中办公区）\",\"江西省赣州市市辖区江西省吉安市青原区赣江大道以南新界吉安义乌小商品市场3号楼4167号\",\"福建省三明市沙县福建省三明市沙县区凤岗新城西路14号沙村4幢3楼\",\"山东省聊城市市辖区山东省聊城经济开发区牡丹江路8号海创工贸公司院内\",\"四川省成都市市辖区四川省成都市高新区锦和西一街217号1层\",\"四川省成都市市辖区四川省成都市天府新区华阳街道协和下街588号14栋8楼807号\",\"浙江省杭州市江干区白杨街道世茂江滨商业中心1幢1301\",\"浙江省杭州市江干区钱塘区白杨街道4号大街17-6号4楼412室\",\"浙江省杭州市江干区深圳市龙岗区园山街道金源路16号\",\"山东省潍坊市市辖区峡山区王家庄街道驻地龙池街南侧\",\"浙江省杭州市江干区杭州经济技术开发区学源街1158-1号文创大厦9楼923室\",\"四川省凉山彝族自治州会理县鹿厂镇凤营乡红云村3组\",\"陕西省西安市市辖区国际港务区港务大道9号陆港大厦A座1514室\",\"河南省漯河市市辖区河南省漯河市双汇路1号\",\"江苏省南通市崇川区幸福新城11幢302室\",\"湖南省岳阳市市辖区营盘岭路113号\",\"湖北省武汉市市辖区东湖新技术开发区华中科技大学科技园六路\",\"浙江省杭州市市辖区临平区乔司街道红普北路13号\",\"浙江省杭州市江干区科技园路65号6层609\",\"浙江省杭州市江干区临江街道临江电商企业园2-789\",\"河南省驻马店市市辖区兴业路与朗陵路交叉口东驻马店文化科技产业园1504号楼1楼\",\"浙江省杭州市市辖区临平区乔司街道三胜街237号1幢4层408-2室\",\"四川省眉山市市辖区经济开发区东区通江路1号\",\"浙江省杭州市江干区浙江省杭州市钱塘新区下沙街道松合时代商住城1幢 2402 室\",\"浙江省杭州市市辖区钱塘区临江街道临江电商创业园2-986\",\"浙江省杭州市江干区浙江省杭州市上城区普盛巷9号东谷创业中心\",\"浙江省杭州市市辖区中国（浙江）自由贸易试验区杭州市钱塘区白杨街道2号大街519号4-1009\",\"福建省漳州市龙海市龙海区浮宫镇后宝村丹溪194号\",\"四川省成都市市辖区泸州市龙马潭区龙光路9号\",\"云南省昆明市市辖区中国(云南)自由贸易试验区昆明片区经开区顺通大道39号云南紫云青鸟珠宝加工贸易基地14幢603号\",\"福建省南平市市辖区南平市工业路118号A座4层\",\"浙江省杭州市江干区钱塘区下沙街道元成路199号4幢1109\",\"浙江省杭州市下城区艮山支三路5号1幢108室\",\"浙江省杭州市江干区钱塘区白杨街道17号大街100号1幢145室\",\"山东省聊城市市辖区北城街道办事处北城小学东50米路北\",\"浙江省杭州市江干区九和路11号2幢2楼231室\",\"河南省郑州市市辖区明理路266号木华广场3号楼B座6层612\",\"江苏省南京市市辖区江北新区七里桥北路1号南京市江北新区人力资源服务产业园一期17栋105-7室\",\"江西省南昌市市辖区高新技术产业开发区艾溪湖北路77号\",\"山东省潍坊市市辖区潍坊综合保税区政务服务中心二楼15—116号\",\"浙江省杭州市江干区钱塘新区白杨街道科技园路2号4幢819室\",\"安徽省芜湖市弋江区芜湖高新技术产业开发区中山南路717号科技产业园二期2栋4层北\",\"四川省成都市市辖区四川省成都市高新区冯家湾工业园科园南路3号1栋1单元1楼1号\",\"浙江省杭州市江干区浙江省杭州市钱塘新区白杨街道科技园路2号5幢\",\"四川省成都市市辖区高新区迎江路23号\",\"浙江省杭州市江干区下沙18号大街795号海弘微智造园21层\",\"广西壮族自治区柳州市市辖区东环大道282号\",\"江苏省南通市崇川区狼山镇街道城山路129号3幢\",\"浙江省杭州市江干区浙江省杭州市钱塘区松乔街2号\",\"浙江省杭州市市辖区浙江省杭州市临平区东湖街道顺达路513号-523号2幢204\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区益州大道中段888号1栋1单元28层2808号\",\"福建省福州市市辖区福建省福州市长乐区杭城街道祥洲村中心路399号\",\"四川省凉山彝族自治州会理县彰冠镇代管村5组21号\",\"浙江省杭州市市辖区浙江省杭州市临平区余杭经济技术开发区顺达路513号\",\"安徽省芜湖市弋江区高新技术产业开发区中山南路717号科技产业园二期2栋3楼\",\"河南省周口市市辖区商水县商宁路中段电商产业园A-6346号\",\"河南省郑州市市辖区河南省郑州市市辖区郑东新区尚贤街北、湖心环路东正东龙润国际1003号\",\"福建省漳州市龙海市港尾镇卓岐材新区59号\",\"浙江省杭州市江干区国脉科技园2栋508室\",\"云南省昆明市市辖区云南省昆明市普吉路125号\",\"浙江省杭州市江干区汉嘉大厦3501室-2\",\"江苏省南通市崇川区秦灶街道国强路29号\",\"浙江省杭州市江干区深圳市福田区福田街道岗厦社区福华三路88号财富大厦28H3\",\"浙江省杭州市江干区白杨街道2号大街501号2幢307室\",\"安徽省合肥市市辖区高新区习友路3333号中国国际智能语音产业园研发中心楼609-113室\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区府城大道西段399号10栋1-2001-01号\",\"浙江省杭州市市辖区世茂江滨商业中心4幢1单元1806室\",\"福建省漳州市龙海市海澄镇埭新村东河131-1号\",\"西藏自治区拉萨市市辖区金珠西路158号阳光新城2幢5单元2楼2号\",\"四川省绵阳市市辖区高新区石桥铺高新国际创意联邦电商产业园8栋1单元6楼\",\"安徽省芜湖市弋江区芜湖高新技术产业开发区久盛路8号 \",\"河南省郑州市市辖区郑东新区祥盛街10号（聚龙城）4号楼1014室\",\"内蒙古自治区呼和浩特市市辖区内蒙古呼和浩特市和林格尔盛乐经济园区\",\"安徽省淮南市市辖区国庆东路20号淮南爱德食品有限公司\",\"浙江省杭州市市辖区大江东产业集聚区长福杭路827号\",\"浙江省杭州市江干区浙江杭州市余杭区南苑街道 藕花洲大街479号时间名座2-1501\",\"浙江省杭州市江干区普大福地商业中心4幢902\",\"江苏省无锡市市辖区经济开发区高浪东路999-8-C1-1201\",\"四川省成都市市辖区中国（四川）自由贸易试验区川南临港片区云台路一段68号西南商贸城16区二街二楼C214-C218，C221-C225\",\"福建省漳州市龙海市福建省漳州高新区颜厝镇水头村水头597号\",\"浙江省杭州市江干区盈都大厦1幢906\",\"江苏省南通市海门市海门街道秀山西路1555号内2号房\",\"湖北省武汉市市辖区东湖新技术开发区关东街道东信路光谷创业街3栋10层01号-（06）（自贸区武汉片区）\",\"浙江省杭州市市辖区经济技术开发区万亚名城1幢1110室\",\"浙江省杭州市江干区九华路2号6幢\",\"浙江省杭州市江干区上海市奉贤区海坤路1号1幢\",\"福建省厦门市市辖区中国（福建）自由贸易试验区厦门片区象屿路93号\",\"浙江省杭州市江干区瑞晶国际商务中心203室\",\"辽宁省沈阳市市辖区辽宁省沈抚示范区翔宇路中立诚悦府60号2603-11\",\"福建省厦门市市辖区中国（福建）自由贸易试验区厦门区象屿路93号厦门国际航运中心C栋4层431单元H\",\"浙江省杭州市江干区钱塘新区东部创智大厦4幢2101室\",\"山东省日照市市辖区山东西路北茶小镇\",\"福建省漳州市龙海市海澄镇豆巷村五社338-2号\",\"福建省漳州市龙海市海澄镇豆巷村五社338-2号\",\"新疆维吾尔自治区乌鲁木齐市市辖区新疆乌鲁木齐经济开发技术区嵩山街229号601室\",\"浙江省杭州市市辖区临平区东湖街道余杭经济开发区新洲路836号\",\"四川省成都市市辖区高新区天府大道北段1700号3栋1单元14层1421号\",\"海南省海口市市辖区江东新区海涛大道海南师范大学国家大学科技园B栋405-2室\",\"浙江省杭州市市辖区浙江省杭州市钱塘区白杨街道2号大街501号海聚中心2-4层202室\",\"山东省济宁市市辖区高新区长虹小区南门4楼401室\",\"浙江省杭州市江干区浙江省杭州经济技术开发区3号大街32号2幢6楼B区\",\"福建省福州市市辖区国际航运中心C幢4层\",\"安徽省芜湖市三山区龙湖街道龙湖新城南1号楼1078室\",\"广东省广州市市辖区萝岗区科学城科汇一街\",\"福建省漳州市龙海市榜山镇长洲村马崎956号\",\"浙江省杭州市江干区白杨街道科技园路20号8壮305-307室\",\"江苏省南京市市辖区珠江路657号锦创数字产业园B座506室\",\"福建省厦门市市辖区保税区象屿路97号厦门国际航运中心d栋8层05单元x\",\"安徽省芜湖市弋江区天井山路2号\",\"浙江省杭州市江干区下沙街道幸福南路122号2层127室\",\"浙江省杭州市江干区金沙湖畔商业中心\",\"山西省大同市市辖区山西省大同市 东信广场\",\"四川省成都市市辖区天府大道北段1288号1栋1单元4层402\",\"广西壮族自治区北海市市辖区广西壮族自治区北海市工业园区北海大道东延线199号中国电子北部湾信息港08-1-A塔楼9-42室\",\"江苏省南通市市辖区通州区金新街道文山村二组8号\",\"浙江省杭州市市辖区浙江省诸暨市街亭镇新华村蓝田257号\",\"浙江省杭州市江干区钱塘新区白杨街道2号大街501号2幢301室\",\"河南省郑州市市辖区中兴南路凯利国际中心a座610室\",\"浙江省杭州市江干区钱塘新区金沙湖畔商业中心6幢10层-6\",\"四川省成都市市辖区中和上街33号1层\",\"河南省濮阳市市辖区城乡一体化示范区医学高等专科青年公寓楼2165室\",\"浙江省杭州市江干区白杨街道科技园路20号8幢808-809室\",\"浙江省杭州市江干区钱塘区2号大街501号海聚中心2幢1202室\",\"福建省漳州市龙海市海澄镇豆巷村五社-342——2\",\"浙江省杭州市江干区盛康街366号群嘉大楼1懂6层\",\"海南省海口市市辖区 南海大道226号创业孵化中心A楼5层A20-524\",\"浙江省杭州市江干区经济开发区白杨街道科技园路2号2幢1501-1507室\",\"福建省漳州市龙海市浮宫镇圳兴路10号25#楼\",\"江苏省泰州市市辖区洋河新区洋河镇徐淮路64-2号\",\"江苏省宿迁市市辖区洋河新区洋河镇屠园岔路口向西300米\",\"安徽省马鞍山市市辖区经济技术开发区红旗南路123号\",\"江苏省南通市港闸区江苏省南通市港闸区秦灶街道富美路11号\",\"海南省海口市市辖区金盘开发区建设三横路2号\",\"江西省赣州市市辖区蓉江新区潭东镇白鹭湖出口加工区标准厂房A-1栋3层1-16\",\"西藏自治区拉萨市市辖区柳梧新区顿珠金融城万达广场西楼12F西户8号\",\"福建省厦门市市辖区中国（福建）自由贸易试验区厦门片区港中路1692号万翔国际商务中心2号楼北楼406-239\",\"江苏省南通市崇川区胜利路168号10幢1层\",\"四川省成都市市辖区中国（四川）自有贸易试验区成都高新区天府大道北段[700号]栋2单元18层1801号\",\"福建省漳州市龙海市福建省漳州市龙海市海澄镇仓头村后河84号\",\"云南省昆明市市辖区云南省昆明市国家昆明经济技术开发区\",\"福建省漳州市龙海市白水镇楼埭村楼仔前158号\",\"江苏省南通市海门市三星镇工贸园区A区（召良村二十二组）\",\"浙江省杭州市江干区下沙街道财通中心2228-1室\",\"浙江省杭州市江干区白杨街道科技园路1幢3-601室至602室\",\"山东省潍坊市市辖区新城街道玉龙社区银枫路1066号17号楼17-4室\",\"四川省成都市市辖区高新区新雅横街5号附407号2楼\",\"福建省漳州市龙海市白水镇庄林村庄林259号\",\"浙江省杭州市下城区潮鸣街道凤起路75-2号\",\"山东省滨州市市辖区经济技术开发区长江四路777号\",\"广西壮族自治区南宁市横县横县横州镇长安路396号\",\"福建省漳州市龙海市福建漳州龙海区白水镇祥和路113号\",\"江苏省南通市港闸区石桥路317号\",\"福建省漳州市龙海市南溪湾创业园A区二期24幢502号\",\"安徽省芜湖市弋江区久盛路8号三只松鼠\",\"湖北省襄阳市市辖区湖北省襄阳市高新技术开发区刘集街道办事处武坡三组\",\"河南省洛阳市孟津县河南省洛阳市孟津区朝阳镇游王村十四组中部医药电商港期2号楼2层201室\",\"浙江省杭州市江干区白杨街道17号大街\",\"安徽省芜湖市弋江区中央城学府金座33-1009\",\"浙江省杭州市市辖区钱塘区白杨街道11号大街58号2幢151室\",\"河南省濮阳市市辖区河南省濮阳市绿城路与兴濮路交叉口西150米路北\",\"浙江省杭州市江干区下沙街道东城大厦3幢1307室\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都 高新区世纪城南路 599 号 6 栋 12 楼 1201 号 58781000\",\"浙江省杭州市江干区浙江省杭州市钱塘区白杨街道科技园路57号17幢312-313室\",\"江苏省南通市海门市德三公路26号\",\"福建省漳州市龙海市紫泥镇下楼村南寮287-1\",\"福建省漳州市龙海市白水镇楼埭村地头\",\"浙江省杭州市江干区钱塘区前进街道江东一路5000号诚智商务中心5号楼前进众智创业园7651室\",\"江西省南昌市市辖区高新技术开发区宏光一路168号\",\"福建省漳州市龙海市东园镇凤山村乌烟厝89号1室\",\"江西省抚州市市辖区抚州高新技术产业开发区迎宾大道218号\",\"福建省三明市三元区唯美电子商务经营部\",\"江苏省常州市市辖区上海市嘉定区众仁路375号3层309室\",\"安徽省芜湖市三山区梅地亚大道79号\",\"浙江省杭州市江干区白杨街道6号大街452号2幢A901号\",\"陕西省西安市市辖区西安市莲湖区西华门1号凯爱大厦B座20810室\",\"浙江省杭州市江干区浙江省杭州市萧山区靖江街道和顺村靖江驿淘互联网产业园2栋301\",\"福建省泉州市市辖区泉州经济开发区雅泰路4号佩斯卡拉工业区B栋厂房2楼\",\"浙江省杭州市江干区浙江省杭州经济技术开发区1号大街68号1幢2层f201室\",\"安徽省芜湖市芜湖县安徽省合肥市肥东县樶镇镇新安社区工业园食品区4号厂房4层\",\"四川省成都市市辖区中国（四川）自由贸易试验区成都高新区交子大道365号1栋5层503号\",\"安徽省芜湖市弋江区安徽省芜湖市弋江区澛港街道讯飞大厦B904-03号\",\"浙江省杭州市市辖区钱塘新区世茂江滨商业中心4幢2单元202室\",\"广东省广州市市辖区广州市黄埔区石化路219号340室（仅限办公）\",\"陕西省西安市市辖区经济技术开发区凤城六路旺景国际大厦第1幢1单元10层1012室\",\"广东省东莞市市辖区广东省东莞市南城街道鸿福路200号第一国际汇一城4栋1406室\",\"江苏省常州市市辖区武进区牛塘镇白家村\",\"广东省深圳市市辖区深圳市前海深港合作区前湾一路1号A栋201室\",\"浙江省杭州市市辖区浙江杭州拱墅区体育场路347号\",\"江苏省南京市市辖区南京市栖霞区马群街道紫东路2号紫东国际创意园A7栋405室\",\"广东省深圳市市辖区深圳市福田区沙头街道天安社区泰然四路26号泰然科技园劲松大厦20C\",\"浙江省杭州市市辖区浙江省诸暨市陶朱街道迎宾路6号3号楼\",\"浙江省杭州市市辖区金沙大道97号金沙印象城16楼-17楼\",\"江苏省南通市市辖区南通市经济技术开发区通达路18号\",\"湖南省长沙市市辖区谷苑路168号\",\"广东省广州市市辖区广州市白云区鹤龙街启德路20号1815-1816房\",\"广东省深圳市市辖区深圳市龙岗区吉华街道甘坑社区秀峰工业城A1栋2C\",\"山东省泰安市市辖区山东省泰安市东平县经济开发区兴园路13号\",\"湖南省长沙市市辖区湖南省长沙市岳麓区观沙岭街道滨江路53号楷林国际大厦C栋808房\",\"浙江省宁波市市辖区浙江省宁波保税区兴业一路5号1幢7楼701室（甬保市场公司托管924号）\",\"贵州省安顺市市辖区贵州省安顺市开发区川渝工业园内双基地2号，3号，厂房内\",\"湖北省荆门市市辖区东宝区竹园路浏河小区5栋101室\",\"四川省成都市市辖区四川省成都市高新区两江国际B座1301\",\"浙江省湖州市市辖区凤凰西区西风路1318号2号楼\",\"浙江省杭州市市辖区浙江省杭州市钱塘区前进街道江东一路5000号诚智商务中心5号楼前进众智创业园13322室\",\"浙江省丽水市市辖区庆元县工业园区五都工业园曙光路11号\",\"浙江省杭州市市辖区白场街道4号大街17-6号8楼811室\",\"江苏省南京市市辖区南京江北新区学府路23号\",\"浙江省杭州市市辖区浙江省杭州市临平区星桥街道星星路6号1幢 5、6楼\",\"广西壮族自治区北海市市辖区广西壮族自治区北海市四川路299号穗丰金湾1幢0401号-430室\",\"安徽省淮南市市辖区福建省南平市浦城县浦城工业园区二期6号\",\"安徽省芜湖市市辖区安徽省芜湖市弋江区芜湖高新技术产业开发区科技产业园13栋2217\"]";
        List<String> addressList = JSON.parseArray(addrressListStr, String.class);
        for (String address : addressList) {

        }


    }


}


