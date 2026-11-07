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

    @Test
    public void testQueryMoreRow() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id,emp_name,emp_salary,emp_age FROM t_emp WHERE emp_age > ?");

        // 为占位符赋值，执行SQL语句，接受结果
        preparedStatement.setInt(1, 25);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            BigDecimal empSalary = resultSet.getBigDecimal("emp_salary");
            int empAge = resultSet.getInt("emp_age");

            System.out.println(empId + "\t" + empName + "\t" + empSalary + "\t" + empAge);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testInsert() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        /*
         * emp_id     int auto_increment comment '员工编号' primary key,
         * auto_increment 表示自增：插入时不给 emp_id 赋值，MySQL 会自动生成“当前最大计数 + 1”的值。
         */
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO t_emp(emp_name, emp_salary, emp_age) VALUES (?,?,?)");

        preparedStatement.setString(1, "Roes");
        /*
         * new BigDecimal(345.67) 传入的是 double，而 double 本身无法精确表示 345.67，
         * 实际得到的是 345.6699999999999590727384202182292938232421875 这样的值。
         * 推荐写法（得到精确的 345.67）：
         * BigDecimal.valueOf(345.67)
         */
        preparedStatement.setBigDecimal(2, new BigDecimal("345.67"));
        /*
         * 345.67 是 double 字面量。
         * BigDecimal 是普通类，不是基本类型的包装类，Java 不会把 double 自动转成 BigDecimal，所以编译会报错。
         */
//        preparedStatement.setBigDecimal(2, 345.67);
        preparedStatement.setInt(3, 28);

        /*
         * executeUpdate 用来执行 INSERT、UPDATE、DELETE（增删改），返回 int，表示“受影响的行数”。
         * executeQuery 只用于 SELECT，查询会得到一张结果表，返回 ResultSet，再用 getXxx 读取每列。
         * INSERT 不会得到结果表，只需要知道改动了几行，所以没有 ResultSet，也就没有 getXxx 的需求。
         */
        int result = preparedStatement.executeUpdate();

        // 根据受影响行数，做判断，得到成功或失败
        // result 是受影响的行数。这里插入一行，成功时 result 为 1，所以用 result > 0 判断是否成功。
        if (result > 0) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }

        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE t_emp SET emp_salary = ? WHERE emp_id = ?");

        preparedStatement.setBigDecimal(1, new BigDecimal("888.88"));
        preparedStatement.setInt(2, 6);

        int result = preparedStatement.executeUpdate();

        if (result > 0) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }

        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM t_emp WHERE emp_id = ?");

        // emp_id 是 INT 类型，但 MySQL 支持不同数值类型之间的隐式转换，因此也能使用 BigDecimal 或 Double 进行数值比较。
        preparedStatement.setBigDecimal(1, new BigDecimal("6"));

        int result = preparedStatement.executeUpdate();

        if (result > 0) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }

        preparedStatement.close();
        connection.close();
    }
}
