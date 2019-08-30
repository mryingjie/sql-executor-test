package com.heitaox.sql.executor.test;

import com.heitaox.sql.executor.SQLExecutor;
import com.heitaox.sql.executor.source.RDBMSDataSource;
import com.heitaox.sql.executor.source.rdbms.RDBMSDataSourceProperties;
import com.heitaox.sql.executor.source.rdbms.StandardSqlDataSource;
import joinery.DataFrame;

/**
 * @Author ZhengYingjie
 * @Date 2019-08-28
 * @Description
 */
@SuppressWarnings("all")
public class TestRDBMSDatasourceDemo {

    public static void main(String[] args) throws Exception {
        RDBMSDataSourceProperties dataSourceProperties = new RDBMSDataSourceProperties();
        dataSourceProperties.setUrl("jdbc:mysql://localhost:3306/tests?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8");
        dataSourceProperties.setUsername("root");
        dataSourceProperties.setPassword("root");
        dataSourceProperties.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSourceProperties.setInitialSize(5);
        dataSourceProperties.setTestOnReturn(false);
        dataSourceProperties.setMinEvictableIdleTimeMillis(50000L);
        RDBMSDataSource rdbmsDataSource = new StandardSqlDataSource(dataSourceProperties);

        SQLExecutor.SQLExecutorBuilder builder = new SQLExecutor.SQLExecutorBuilder();

        SQLExecutor sqlExecutor = builder
                .putDataSource("t_base", rdbmsDataSource)
                .putDataSource("t_score", rdbmsDataSource)
                .enableCache()
                .build();

        // sqlExecutor.executeInsert("INSERT INTO t_base (姓名,家庭住址,电话,年龄,性别) VALUES ('刘可', '北京西城', '1881881888', '22', '女'), ('李斯', '北京东城', '1991991999', '23', '女'), ('王海', '北京朝阳', '1661661666', '23', '男'), ('陶俊', '北京海淀', '1771771777', '24', '男');");
        // sqlExecutor.executeInsert("INSERT INTO t_score (id,name,subject,fraction) VALUES ('1', '王海', '语文', '86'), ('2', '王海', '数学', '83'), ('3', '王海', '英语', '93'), ('4', '陶俊', '语文', '88'), ('5', '陶俊', '数学', '84'), ('6', '陶俊', '英语', '94'), ('7', '刘可', '语文', '80'), ('8', '刘可', '数学', '86'), ('9', '刘可', '英语', '88'), ('10', '吴宇', '英语', '99'), ('14', '吴宇', '数学', '99'), ('15', '吴宇', '语文', '99')");

        System.out.println(sqlExecutor.executeQuery("select * from t_base"));
        System.out.println(sqlExecutor.executeQuery("select * from t_score"));


        DataFrame dataFrame = sqlExecutor.executeQuery("select " +
                "'a' as num ," +
                "姓名 name," +
                "年龄 age," +
                "concat(姓名,'|',年龄)"+
                // "CONCAT_WITH_PIPE_CHARACTER(姓名,时间) as concatName ," +
                // "sum(年龄) sumAge, " +
                // "count(*) " +
                "from t_base " +
                " where 姓名 in('王海','陶俊')AND(年龄 > 20) "+
                // " group by 姓名 " +
                // "having sumAge > 40 " +
                "order by age desc");
        System.out.println(dataFrame);


    }

}
