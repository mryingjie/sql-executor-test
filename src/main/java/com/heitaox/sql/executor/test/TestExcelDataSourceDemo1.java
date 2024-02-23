package com.heitaox.sql.executor.test;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.source.file.ExcelDataSource;
import joinery.DataFrame;
import org.apache.commons.collections4.CollectionUtils;
import sun.net.www.content.image.png;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author ZhengYingjie
 * @Date 2019-08-28
 * @Description
 */
@SuppressWarnings("all")
public class TestExcelDataSourceDemo1 {

    public static void main(String[] args) throws Exception {

        //准备数据源

        //文件数据源默认读取进来的数据都是String类型 如果要更改为别的类型，需要定义schema
        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();
        SQLExecutor sqlExecutor = builder
                .putDataSource("t_source", new ExcelDataSource("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/trademark_license_new.xlsx", new HashMap<>()))
                // .putDataSource("t_result", new ExcelDataSource("/Users/kiko/Documents/result.xlsx", new HashMap<>()))
                .enableCache()
                .enableFilterBeforeJoin()
                .build();
        String field = "trademark_license";
        String tableName = "tb_supplier_brand";
        String baseUrl = "https://a1.eastbuy.com/eastbuy-supplier-service/release/";
        StringBuilder sb = new StringBuilder();
        DataFrame dataFrame = sqlExecutor.executeQuery("select biz_id, " + field + " from t_source");
        for (int i = 0; i < dataFrame.length(); i++) {
            String bizId = (String) dataFrame.get(i, "biz_id");
            String file = (String) dataFrame.get(i, field);
            String[] split = file.split(";");
            String fileResult = list2String(Arrays.stream(split).map(new Function<String, String>() {
                @Override
                public String apply(String s) {
                    if (!s.contains("pdf")) {
                        return s;
                    }
                    // 剪切文件名
                    String fileName = getFileName(s);
                    return baseUrl + fileName.replaceAll("pdf","png");
                }
            }).collect(Collectors.toList()),",");
            sb.append("update " + tableName + " set ")
                    .append(field).append("=").append("'").append(fileResult).append("',")
                    .append("updated_time = updated_time ")
                    .append("where biz_id =").append("'"+bizId+"'").append(";").append("\n");

        }
        System.out.println(sb.toString());

    }

    public static String list2String (List<String> list, String symbols){
        if(CollectionUtils.isEmpty(list)){
            return "";
        }
        return Joiner.on(symbols).join(list);
    }

    public static String getFileName(String originUrl) {
        if (StrUtil.isBlankOrUndefined(originUrl)) {
            return originUrl;
        }
        URL oUrl = null;
        String ooUrl = new String(originUrl);// 原先的url 或 文本
        if (!originUrl.contains("http")) {
            originUrl = "https://" + originUrl;
        }
        try {
            oUrl = new URL(originUrl);
        } catch (MalformedURLException e) {
            return ooUrl;  // 文本 直接返回
        }
        String path = oUrl.getPath();
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        fileName = fileName.contains("?") ? fileName.substring(0, fileName.indexOf("?")) : fileName;
        return fileName;
    }
}
