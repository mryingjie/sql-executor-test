package com.heitaox.sql.executor.test;

import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.source.file.ExcelDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Yingjie Zheng at 2019-08-30 09:31
 */
public class TestFunc {


    public static void main(String[] args) {
        Map<String, Class> schema = new HashMap<>();
        schema.put("年龄", int.class);

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();
        SQLExecutor sqlExecutor = builder
                .putDataSource("t_base", new ExcelDataSource(TestExcelDataSourceDemo.class.getClassLoader().getResource("t_base.csv").getPath(), schema))
                .enableCache()
                .enableFilterBeforeJoin()
                .build();

        System.out.println(sqlExecutor.executeQuery("select YEAR(datetimetodate('2019-08-23 12:21:21')) as now"));

    }
}
