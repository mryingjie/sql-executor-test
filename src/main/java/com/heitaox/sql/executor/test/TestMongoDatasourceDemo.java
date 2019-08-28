package com.heitaox.sql.executor.test;

import com.heitaox.sql.executor.source.SQLExecutor;
import com.heitaox.sql.executor.source.file.ExcelDataSource;
import com.heitaox.sql.executor.source.nosql.MongoDataSource;
import com.mongodb.ServerAddress;
import joinery.DataFrame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZhengYingjie
 * @Date 2019-08-28
 * @Description
 */
@SuppressWarnings("all")
public class TestMongoDatasourceDemo {

    public static void main(String[] args) throws Exception {

        //定义MongoDB数据源的几种方式
        // MongoDataSourceProperties mongoDataSourceProperties = new MongoDataSourceProperties();
        // mongoDataSourceProperties.setDbName("test");
        // mongoDataSourceProperties.setServerAddress(
        //         Arrays.asList(new ServerAddress("10.58.84.178",27017))
        // );

        // MongoDataSource mongoDataSource = new MongoDataSource("10.58.84.178",27017,"test");
        MongoDataSource mongoDataSource = new MongoDataSource(Arrays.asList(new ServerAddress("10.58.84.178",27017)),"test");

        Map<String, Class> schema = new HashMap<>();
        schema.put("年龄", int.class);

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();
        SQLExecutor sqlExecutor = builder
                .putDataSource("t_base",  new ExcelDataSource(TestMongoDatasourceDemo.class.getClassLoader().getResource("t_base.csv").getPath(), schema))
                .putDataSource("t_score", mongoDataSource)
                .enableCache()
                .build();

        //插入数据
        // sqlExecutor.executeInsert("INSERT INTO t_base (姓名,家庭住址,电话,年龄,性别) VALUES ('刘可', '北京西城', '1881881888', '22', '女'), ('李斯', '北京东城', '1991991999', '23', '女'), ('王海', '北京朝阳', '1661661666', '23', '男'), ('陶俊', '北京海淀', '1771771777', '24', '男');");
        // sqlExecutor.executeInsert("INSERT INTO t_score (id,name,subject,fraction) VALUES ('1', '王海', '语文', '86'), ('2', '王海', '数学', '83'), ('3', '王海', '英语', '93'), ('4', '陶俊', '语文', '88'), ('5', '陶俊', '数学', '84'), ('6', '陶俊', '英语', '94'), ('7', '刘可', '语文', '80'), ('8', '刘可', '数学', '86'), ('9', '刘可', '英语', '88'), ('10', '吴宇', '英语', '99'), ('14', '吴宇', '数学', '99'), ('15', '吴宇', '语文', '99')");

        //验证是否插入成功
        System.out.println(sqlExecutor.executeQuery("select * from t_score"));
        System.out.println(sqlExecutor.executeQuery("select * from t_base"));


        String sql = "select a.姓名 ,c.电话,b.subject,c.年龄 from \n" +
                " t_base a left join  t_score b \n" +
                " on a.姓名 = b.name left join  t_base c\n" +
                " on c.姓名 = b.name\n" +
                " where a.姓名 in('刘可','陶俊') and  b.subject = '语文'"+
                " order by a.姓名;";
        DataFrame df = sqlExecutor.executeQuery(sql);

        System.out.println(df);
    }
}
