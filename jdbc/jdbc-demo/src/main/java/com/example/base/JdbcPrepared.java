package com.example.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JdbcPrepared {
    public static void main(String[] args) throws Exception {
        // 1.注册驱动（可以省略）

        // 2.获取数据库连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        // 3.获取执行SQL语句对象
        /*
         * PreparedStatement 是预编译语句：SQL 结构先发给数据库，? 是参数占位符，参数值之后再单独传入。
         * 用户输入只会被当作“值”，不会被拼进 SQL 结构，所以能防止 SQL 注入。
         */
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id,emp_name,emp_salary,emp_age FROM t_emp WHERE emp_name = ?");

        System.out.println("请输入员工姓名：");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        // 4.为?占位符赋值，并执行SQL语句，接收返回的结果
        /*
         * setString：给指定位置的 ? 绑定一个字符串值。
         *   签名：void setString(int parameterIndex, String x) throws SQLException
         * setNString：JDBC 4.0 新增的方法，专门给 NCHAR、NVARCHAR、NCLOB 这类“国家字符集”列传值。
         *   签名：void setNString(int parameterIndex, String value) throws SQLException
         * 两个方法的参数含义相同：parameterIndex 是第几个 ?（从 1 开始），第二个参数是要绑定的值；传入 null 会设成 SQL NULL。
         * 如果索引越界（比如只有 1 个 ?，却写了 setString(2, ...)），会抛出 SQLException。
         */
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5.处理结果，遍历resultSet
        while (resultSet.next()) {
            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            String empSalary = resultSet.getString("emp_salary");
            int empAge = resultSet.getInt("emp_age");
            System.out.println(empId + "\t" + empName + "\t" + empSalary + "\t" + empAge);
        }

        // 6.释放资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
