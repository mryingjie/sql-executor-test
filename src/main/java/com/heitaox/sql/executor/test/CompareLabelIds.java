package com.heitaox.sql.executor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.core.util.StringUtils;
import com.heitaox.sql.executor.source.file.ExcelDataSource;
import joinery.DataFrame;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yingjie Zheng
 * @date 2023/7/5 08:57
 * @description
 */
public class CompareLabelIds {

    public static void main(String[] args) throws Exception {

        //准备数据源

        //文件数据源默认读取进来的数据都是String类型 如果要更改为别的类型，需要定义schema

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();
        SQLExecutor sqlExecutor = builder
                .putDataSource("supplier", new ExcelDataSource("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/supplier.csv", new HashMap<>()))
                .putDataSource("threebody",new ExcelDataSource("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/threebody.csv", new HashMap<>()))
                .enableCache()
                .enableFilterBeforeJoin()
                .build();



        //插入数据
        DataFrame supplierDF = sqlExecutor.executeQuery("select * from supplier");
        DataFrame threebodyDF = sqlExecutor.executeQuery("select * from threebody");

        Map<String,List<String>> threebodyMap = new HashMap<>();
        for (int i = 0; i < threebodyDF.length(); i++) {
            String bizId = (String) threebodyDF.get(i, 0);
            bizId = bizId.replace("\"","");
            String labelIdsStr = (String) threebodyDF.get(i, 1);
            labelIdsStr = labelIdsStr.replace("\"", "");
            if(labelIdsStr.contains("null")){
                System.out.println("biz:"+bizId+" 被过滤");
                continue;
            }
            List<String> labelIdList = new ArrayList<>();
            if(StringUtils.isNotEmpty(labelIdsStr)){
                String[] split = labelIdsStr.split(";");
                labelIdList.addAll(Arrays.stream(split).collect(Collectors.toList()));
            }
            threebodyMap.put(bizId,labelIdList);
        }
        List<String> bizIdList = new ArrayList<>();
        for (int i = 0; i < supplierDF.length(); i++) {
            String bizId = (String) supplierDF.get(i, 0);
            String labelIdsStr = (String) supplierDF.get(i, 1);
            List<String> labelIdList = new ArrayList<>();
            if(StringUtils.isNotEmpty(labelIdsStr)){
                String replace = labelIdsStr.replace(";", ",");
                labelIdList = JSON.parseArray(replace,String.class);
            }
            List<String> threebodyLableIdList = threebodyMap.get(bizId);
            if(Objects.isNull(threebodyLableIdList)){
                continue;
            }
            if (!compareListSameNull(labelIdList,threebodyLableIdList)) {
                labelIdList.addAll(threebodyLableIdList);
                labelIdList = labelIdList.stream().distinct().collect(Collectors.toList());
                labelIdList = labelIdList.stream().map(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return "\"" + s + "\"";
                    }
                }).collect(Collectors.toList());
                String format = String.format("update tb_supplier set label_ids = '%s',updated_time = updated_time where biz_id = %s;", labelIdList,("'" + bizId +"'"));
                System.out.println(format);

            }


        }

        // 1581497686249380100    []
        // 538375388725338    ["1673516382502322177"]
        // 1663139314187970050    ["1584511971339517954"]
        // 1091581208890680000000    []
        // 1601067435776869890    []
        // 1091581207810950000000    []
        // 1091581203964990000000    ["1584511953038266369"]
        // 1628678266705080060    ["1584511915953733634"
        // 1597989128656329980    []
        // 1620010839783830020    []
        // 1582306737026139900    []


    }

    /**
     * 比较两个集合元素是否完全相同 空和空集合认为是相同的
     * @param c1
     * @param c2
     */
    public static <T> boolean compareListSameNull(Collection<T> c1, Collection<T> c2) {
        if(CollectionUtils.isEmpty(c1) && CollectionUtils.isEmpty(c2)){
            return true;
        }
        if(CollectionUtils.isEmpty(c1)){
            return false;
        }
        if(CollectionUtils.isEmpty(c2)){
            return false;
        }
        if(c1.size() != c2.size()){
            return false;
        }
        for (T t : c1) {
            if(!c2.contains(t)){
                return false;
            }
        }
        for (T t : c2) {
            if(!c1.contains(t)){
                return false;
            }
        }
        return true;
    }

}
