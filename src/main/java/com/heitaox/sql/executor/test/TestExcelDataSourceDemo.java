package com.heitaox.sql.executor.test;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.source.RDBMSDataSource;
import com.heitaox.sql.executor.source.file.ExcelDataSource;
import com.heitaox.sql.executor.source.rdbms.RDBMSDataSourceProperties;
import com.heitaox.sql.executor.source.rdbms.StandardSqlDataSource;
import joinery.DataFrame;

import java.util.*;

/**
 * @Author ZhengYingjie
 * @Date 2019-08-28
 * @Description
 */
@SuppressWarnings("all")
public class TestExcelDataSourceDemo {

    public static void main(String[] args) throws Exception {

        //准备数据源

        //文件数据源默认读取进来的数据都是String类型 如果要更改为别的类型，需要定义schema

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();
        SQLExecutor sqlExecutor = builder
                .putDataSource("t_result", new ExcelDataSource("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/tmp/名单.xlsx", new HashMap<>()))
                .putDataSource("t_bd", new ExcelDataSource("/Users/kiko/IdeaProjects/self/sql-executor-test/src/main/resources/tmp/bd.xlsx", new HashMap<>()))
                .enableCache()
                .enableFilterBeforeJoin()
                .build();
        //插入数据

        String sql1 = "select * from t_result";
        String sql2 = "select * from t_bd";
        DataFrame resultDF = sqlExecutor.executeQuery(sql1);
        DataFrame bdDF = sqlExecutor.executeQuery(sql2);
        Map<String,Set<String>> map = new HashMap<>();
        for (int i = 0; i < bdDF.length(); i++) {
            String supplierId = bdDF.get(i, "supplierid").toString();
            String dy_bd_ids = bdDF.get(i, "dy_bd_ids").toString();
            JSONArray jsonArray;
            try {

                jsonArray = JSON.parseArray(dy_bd_ids);
            }catch (Exception e){
                continue;
            }
            Set<String> dy_bd_idSet = new HashSet<>();
            for (Object o : jsonArray) {
                dy_bd_idSet.add((String)o);
            }
            map.put(supplierId,dy_bd_idSet);
        }
        List<String> notexistId = new ArrayList<>();
        List<String> filterId = new ArrayList<>();
        for (int i = 0; i < resultDF.length(); i++) {
            String supplierId = resultDF.get(i, "供应商id").toString();
            String addBdName = resultDF.get(i, "抖音商务新增bd姓名").toString();
            String addBdId = resultDF.get(i, "抖音商务新增bd域账号").toString();
            Set<String> existBdIds = map.get(supplierId);
            if(Objects.isNull(existBdIds)){
                notexistId.add(supplierId);
                continue;
            }
            if(existBdIds.contains(addBdId)){
                filterId.add(supplierId + "-" + addBdId);
                continue;
            }
            String format = "update tb_supplier set dy_bd_ids = json_array_append(dy_bd_ids,'$','%s'), dy_bd_names = json_array_append(dy_bd_names,'$','%s') where biz_id = '%s';";
            String sql = String.format(format,addBdId,addBdName,supplierId);
            System.out.println(sql);
        }

        System.out.println(notexistId);
        System.out.println(filterId);
        System.out.println(filterId.size());


    }
}
