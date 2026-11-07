package com.example.base;

import org.junit.Test;

import java.math.BigDecimal;
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

    @Test
    public void testQuerySingleRow() throws SQLException {
        // 1.注册驱动（可以省略）

        // 2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        // 3.预编译SQL语句获得PreparedStatement对象
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id,emp_name,emp_salary,emp_age FROM t_emp WHERE emp_id = ?");

        // 4.为占位符赋值，然后执行，并接收结果
        /*
         * 签名：void setInt(int parameterIndex, int x) throws SQLException。
         * 第一个参数 parameterIndex：给第几个 ? 赋值，从 1 开始数（不是 0）。
         * 这条 SQL 只有一个 ?，所以这里只能写 1；写 0 或 2 都会抛 SQLException。有几个 ? 最大就能写几。
         * 第二个参数 x：给这个 ? 设置（绑定）的值，不是"修改数据库"的意思。
         * 第二次设置会覆盖第一次设置的值。
         * 其他类型对应 setString、setBigDecimal 等，也可用通用的 setObject(index, value)。
         */
        preparedStatement.setInt(1, 5);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5.处理结果
        while (resultSet.next()) {
            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            /*
             * double 是二进制浮点数，很多小数无法精确表示（如 0.1 + 0.2 得到 0.30000000000000004），计算时可能有误差；
             * BigDecimal 用十进制精确表示，与数据库 DECIMAL 类型对应，金额计算不会丢精度。
             * 若 emp_salary 在表里是 DOUBLE 类型，用 BigDecimal 也无法找回已丢失的精度。
             */
//            double empSalary = resultSet.getDouble("emp_salary");
            BigDecimal empSalary = resultSet.getBigDecimal("emp_salary");
            int empAge = resultSet.getInt("emp_age");

            System.out.println(empId + "\t" + empName + "\t" + empSalary + "\t" + empAge);
        }

        // 6.资源释放
        /*
         * try (Connection conn = DriverManager.getConnection(url, user, pwd);
         *      PreparedStatement ps = conn.prepareStatement(sql)) {
         *     ps.setInt(1, 5);
         *     try (ResultSet rs = ps.executeQuery()) {
         *         if (rs.next()) { ... }
         *     }
         * }
         */
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
