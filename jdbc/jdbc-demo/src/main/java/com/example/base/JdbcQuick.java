package com.example.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQuick {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 1.注册驱动
        /*
         * JDBC 4.0（JDK 6+）起，驱动 jar 里有 META-INF/services/java.sql.Driver，
         * 驱动会被 SPI 自动注册，所以 Class.forName 在 Connector/J 8.x 上可以省略，不是必须的。
         */
        /*
         * forName(String className)
         * 作用：按"字符串类名"让类加载器加载并初始化这个类，返回它的 Class 对象，但它不创建对象。
         * 这行做的是"把 MySQL 驱动登记到 DriverManager 的驱动列表里"。
         *
         * 圆括号里是驱动的"全限定类名"（包名 + 类名），不是文件路径，也不是数据库地址。
         * Connector/J 8.x（mysql-connector-j）固定写 com.mysql.cj.jdbc.Driver；
         * 5.x 是 com.mysql.jdbc.Driver，写错就会抛 ClassNotFoundException。
         * 别的数据库完全不同，比如 PostgreSQL 是 org.postgresql.Driver。
         */
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.获取数据库连接
        /*
         * 签名：static Connection getConnection(String url, String user, String password) throws SQLException
         * DriverManager 内部保存着所有已注册的驱动，根据 url 前缀（jdbc:mysql:）
         * 挑出对应驱动去真正建立 TCP 连接，所以不需要自己 new 驱动。
         * 另外还有两个重载：getConnection(url) 和 getConnection(url, Properties info)。
         *
         * url规范格式：jdbc:mysql://主机:端口/数据库名?参数名=值&参数名=值
         * 例：jdbc:mysql://localhost:3306/atguigu?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
         * 3306 是默认端口可省略；数据库名就是 schema 名（这里是 atguigu）。参数部分可选。
         */
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu", "root", "");

        // 3.创建Statement对象
        /*
         * 签名：PreparedStatement prepareStatement(String sql) throws SQLException
         * 圆括号里传的是要执行的 SQL 字符串，
         * 驱动此时会把它发给服务器做语法检查和预编译，并返回一个 PreparedStatement 对象。
         * PreparedStatement 支持 ? 占位符，一次编译可多次执行，能防 SQL 注入。
         */
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id,emp_name,emp_salary,emp_age FROM t_emp");

        // 4.编写SQL语句并执行，获取结果
        /*
         * SQL 在第 3 步已经交给 prepareStatement 编译过了，这里只负责"执行"，所以不带参数。
         *
         * 签名：ResultSet executeQuery() throws SQLException，是 PreparedStatement 的方法。
         * 规范就是 SQL 放 prepareStatement（参数用 ? 占位 + setXxx 填值），执行时调无参 executeQuery。
         * 若把 SQL 放进 executeQuery，那是 Statement 的 ResultSet executeQuery(String sql)，
         * 每次执行都会重新编译，而且常见写法是字符串拼接 → 有 SQL 注入风险，所以不推荐。
         */
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5.处理结果
        /*
         * next() 把游标下移一行：有数据返回 true，走到末尾返回 false。还有下一行就继续循环。
         * 因为结果集初始位置在第一行之前，所以必须先调用 next() 才能读第一行。
         */
        while (resultSet.next()) {
            /*
             * 两个重载：int getInt(int columnIndex) 和 int getInt(String columnLabel)。
             * columnIndex：列索引（通过位置获取）；columnLabel：列标签（通过名称获取）
             * 序号从 1 开始（写 0 会抛异常），列名是查询结果里的列名或别名，不区分大小写。
             * 坑：数据库里是 NULL 时 getInt 返回 0，要区分"0"和"NULL"得再用 resultSet.wasNull() 判断。
             */
            int empId = resultSet.getInt("emp_id");
            /*
             * 两个重载：String getString(int columnIndex) 和 String getString(String columnLabel)。
             * 序号从 1 开始；数据库里是 NULL 时返回 null。
             */
            String empName = resultSet.getString("emp_name");
            /*
             * 建议：如果 emp_salary 是 DECIMAL 类型，用 getString 会把它当文本取，参与计算还得再转。
             * 可以改用 getBigDecimal，语义更准确。
             */
            String empSalary = resultSet.getString("emp_salary");
            int empAge = resultSet.getInt("emp_age");
            System.out.println(empId + "\t" + empName + "\t" + empSalary + "\t" + empAge);
        }

        // 6.释放资源(先开后关原则)
        /*
         * 开了三个资源：getConnection 开的 connection、prepareStatement 开的 preparedStatement、
         * executeQuery 开的 resultSet。所以关闭顺序要反过来：resultSet → preparedStatement → connection。
         *
         *   try (Connection conn = DriverManager.getConnection(url, user, pwd);
         *        PreparedStatement ps = conn.prepareStatement(sql);
         *        ResultSet rs = ps.executeQuery()) {
         *       while (rs.next()) { ... }
         *   }
         */
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
