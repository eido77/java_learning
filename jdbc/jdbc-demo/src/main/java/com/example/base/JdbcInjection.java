package com.example.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// 用 Statement 拼接字符串执行 SQL，会产生安全漏洞。
public class JdbcInjection {
    public static void main(String[] args) throws SQLException {
        // 1.注册驱动（可以省略）

        // 2.获取数据库连接对象
        /*
         * getConnection 返回一个 Connection 对象，代表 Java 程序和数据库之间的一次连接（会话）。
         *
         * "jdbc:mysql:///atguigu" 中三个斜杠是省略了 localhost:3306 的写法，
         * 完整写法是 "jdbc:mysql://localhost:3306/atguigu"。
         */
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        // 3.获取执行SQL语句对象
        /*
         * 两个方法都由 Connection 提供，作用都是"创建一个用来执行 SQL 的对象"：
         * createStatement()：不带参数，返回 Statement。执行时才把完整的 SQL 字符串交给它。
         * prepareStatement(String sql)：创建时就传入带 ? 占位符的 SQL，数据库先把语句结构固定下来（预编译），
         *   返回 PreparedStatement，之后用 setXxx 给 ? 填值。
         *
         * PreparedStatement 是 Statement 的子接口。
         * Statement：只能执行拼接好的完整 SQL，容易被 SQL 注入，带变量时写引号也容易出错。
         * PreparedStatement：用 ? 占位，参数只会被当作"值"处理，能防止注入；同一条 SQL 反复执行时也可能更快。
         */
        Statement statement = connection.createStatement();

        System.out.println("请输入员工姓名：");
        Scanner scanner = new Scanner(System.in);
        /*
         * next()：遇到空白字符（空格、Tab、换行）就停止，只能读到一个"单词"。输入 "张 三" 只会得到 "张"。
         * nextLine()：读取一整行，直到回车为止，可以包含空格。
         * 例如注入演示中输入 abc' or '1'='1 这种带空格的内容时，用 next() 只能读到 abc'，演示就失败了。
         */
        String name = scanner.nextLine();

        // 4.编写SQL语句，并执行，接收返回的结果
        /*
         * 【输入 abc' or '1'='1】
         * 这就是 SQL 注入，必须避免。拼接后的 SQL 变成：
         *   ... WHERE emp_name = 'abc' or '1'='1'
         * 用户输入中的单引号把字符串提前"闭合"了，后面的 or '1'='1' 被当成了 SQL 语法。
         * AND/OR 中 '1'='1' 恒为真，所以 WHERE 条件对每一行都成立，结果就查出了全部数据。
         * 如果是登录校验，攻击者不知道密码也能登录。
         */
        String sql = "SELECT emp_id,emp_name,emp_salary,emp_age FROM t_emp WHERE emp_name = '" + name + "'";
        /*
         * executeQuery 的调用方式不同，是因为 SQL 交出去的时机不同：
         *   Statement 创建时没有拿到 SQL，所以执行时用 executeQuery(sql) 传入；
         *   PreparedStatement 创建时已经拿到 SQL，所以执行时用无参的 executeQuery()。
         * 两者都返回 ResultSet，用来执行查询（SELECT）。增删改要用 executeUpdate，它返回受影响的行数。
         *
         * 用 Statement 时，语法上只能把 SQL 放进 executeQuery(sql)，因为没有别的地方可以放。
         * 实际开发中不用 Statement，统一用 prepareStatement(带?的sql) + 无参 executeQuery()。
         * 注意：PreparedStatement 也能调用继承来的 executeQuery(String sql)，但这样做会绕过预编译，
         * 而且 MySQL 驱动会直接抛异常。所以不要给 PreparedStatement 的 executeQuery 传 SQL。
         */
        ResultSet resultSet = statement.executeQuery(sql);

        // 5.处理结果，遍历resultSet
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

        // 6.释放资源
        /*
         * try (Connection conn = DriverManager.getConnection(url, user, pwd);
         *      PreparedStatement ps = conn.prepareStatement(sql)) {
         *     ps.setString(1, name);
         *     try (ResultSet rs = ps.executeQuery()) {
         *         while (rs.next()) { ... }
         *     }
         * }
         * getConnection 开启连接，createStatement/prepareStatement 开启语句对象，
         * executeQuery 开启结果集，依赖关系是 Connection → Statement → ResultSet，所以关闭顺序反过来。
         *
         * Scanner(System.in) 一般不关闭，因为关闭它会连带关闭 System.in，之后程序就无法再读取键盘输入。
         */
        resultSet.close();
        statement.close();
        connection.close();
    }
}
