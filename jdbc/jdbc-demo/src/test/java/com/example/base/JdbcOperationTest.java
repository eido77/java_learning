package com.example.base;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcOperationTest {
    @Test
    public void testQuerySingleRowAndCol() throws SQLException {
        // 1.注册驱动 （可以省略）

        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        // 3.预编译SQL语句得到PreparedStatement对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM t_emp");

        // 4.执行SQL语句，获取结果
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5.处理结果(如果自己明确一定只有一个结果，那么resultSet最少要做一次next的判断，才能拿到我们要的列的结果)
        /*
         * ResultSet 内部有一个"游标"，刚拿到时它指向第一行之前，没有指向任何数据；
         * 必须先调用 next() 把游标移到第一行才能取值。
         */
        while (resultSet.next()) {
            int count = resultSet.getInt("count");
            System.out.println(count);
        }

        // 6.释放资源
        /*
         * try (Connection connection = DriverManager.getConnection(url, user, pwd);
         *      PreparedStatement ps = connection.prepareStatement(sql);
         *      ResultSet rs = ps.executeQuery()) {
         *     if (rs.next()) {
         *         System.out.println(rs.getInt("count"));
         *     }
         * }
         */
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
