package com.heitaox.sql.executor.test;

import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.source.file.ExcelDataSource;
import com.heitaox.sql.executor.source.nosql.ElasticsearchDataSource;
import joinery.DataFrame;
import org.apache.http.HttpHost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZhengYingjie
 * @Date 2019-08-28
 * @Description
 */
@SuppressWarnings("all")
public class TestElasticsearchDataSource {

    public static void main(String[] args) throws Exception {
        Map<String, Class> schema = new HashMap<>();
        schema.put("年龄", int.class);

        ElasticsearchDataSource elasticsearchDataSource = new ElasticsearchDataSource(Arrays.asList(
                new HttpHost("localhost", 9200, "http")
        ));

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();

        SQLExecutor sqlExecutor = builder
                .putDataSource("t_base",  new ExcelDataSource(TestMongoDatasourceDemo.class.getClassLoader().getResource("t_base.csv").getPath(), schema))
                .putDataSource("t_score", elasticsearchDataSource)
                .enableFilterFirst()
                // .enableFilterBeforeJoin()
                .enableCache()
                .build();

        //插入数据
        // sqlExecutor.executeInsert("INSERT INTO t_base (姓名,家庭住址,电话,年龄,性别) VALUES ('刘可', '北京西城', '1881881888', '22', '女'), ('李斯', '北京东城', '1991991999', '23', '女'), ('王海', '北京朝阳', '1661661666', '23', '男'), ('陶俊', '北京海淀', '1771771777', '24', '男');");
        // int i = sqlExecutor.executeInsert("INSERT INTO t_score (id,name,subject,fraction) VALUES ('11', '刘海', '语文', '86'), ('12', '王水', '数学', '83'), ('13', '王大', '英语', '93')");
        // System.out.println(i);


        // System.out.println(sqlExecutor.executeQuery("select * from t_score"));
        // System.out.println(sqlExecutor.executeQuery("select * from t_base"));


        String sql = "select * from t_score where name not in ('刘','海')";


        // String sql = "select a.姓名 ,c.电话,b.subject,c.年龄 from \n" +
        //         " t_base a left join  t_score b \n" +
        //         " on a.姓名 = b.name left join  t_base c\n" +
        //         " on c.姓名 = b.name\n" +
        //         " where a.姓名 in('刘可','陶俊') and  b.subject = '语文'"+
        //         " order by a.姓名;";
        DataFrame df = sqlExecutor.executeQuery(sql);

        System.out.println(df);
        elasticsearchDataSource.close();
    }

}
