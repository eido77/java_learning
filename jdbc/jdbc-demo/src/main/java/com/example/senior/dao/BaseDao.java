package com.example.senior.dao;

import com.example.senior.util.JdbcUtilV2;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将共性的数据库的操作代码封装在BaseDao里
 */
public class BaseDao {
    /**
     * 通用的增删改的方法。
     *
     * @param sql    调用者要执行的SQL语句
     * @param params SQL语句中的占位符要赋值的参数
     * @return 受影响的行数
     */
    /*
     * Object... params 叫“可变参数”（varargs）：调用时可以传 0 个、1 个或多个值，
     * 编译器会自动把它们装进一个 Object[] 数组，方法内部的 params 就是这个数组。
     * 可变参数必须放在参数列表最后。
     * sql 是带 ? 占位符的 SQL“模板”，params 是要填进各个 ? 的具体值。
     * 作用：不管是增、删还是改，JDBC 步骤都一样，只有 SQL 和参数不同，
     *      所以一个方法就能执行所有增删改，这就是“通用”的意义。
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        // 1.通过JDBCUtilV2获取数据库连接
        Connection connection = JdbcUtilV2.getConnection();

        // 2.预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 4.为占位符赋值，执行SQL，接受返回结果
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                // 占位符是从1开始的。参数的数组是从0开始的
                /*
                 * setObject 的签名：void setObject(int parameterIndex, Object x) throws SQLException。
                 * 第一个参数是第几个 ?，第二个参数是要填进去的值。
                 * 个别数据库驱动对 setObject(i, null) 可能报错，遇到时可改用 setNull。
                 */
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        /*
         * executeUpdate() 的签名：int executeUpdate() throws SQLException，
         * 返回 INSERT/UPDATE/DELETE 影响了多少行。
         */
        int row = preparedStatement.executeUpdate();

        // 5.释放资源
        preparedStatement.close();

        // getAutoCommit() 签名：boolean getAutoCommit() throws SQLException，判断当前连接是否为自动提交模式。
        // true：操作完成后释放连接；false：保留连接，供后续事务提交或回滚使用。
        if (connection.getAutoCommit()) {
            JdbcUtilV2.release();
        }

        // 6.返回结果
        return row;
    }

    /**
     * 通用的查询：多行多列、单行多列、单行单列
     * 多行多列：List<Employee>
     * 单行多列：Employee
     * 单行单列：封装的是一个结果。Double、Integer、。。。。。
     * 封装过程：
     * 1、返回的类型：泛型：类型不确定，调用者知道，调用时，将此次查询的结果类型告知BaseDao就可以了。
     * 2、返回的结果：通用，List  可以存储多个结果，也可以存储一个结果 get(0)
     * 3、结果的封装：反射，要求调用者告知BaseDAO要封装对象的类对象。 Class
     */
    // Class<T> clazz：告诉方法“每行数据要封装成哪个类的对象”，例如 Employee.class。
    /*
     * 增删改：数据从 Java 流向数据库，回来的只有一个行数。
     * 查询：数据从数据库流回 Java，所以需要知道怎么还原成对象。
     *
     * 【为什么查询需要 Class，而增删改不需要？】
     * 1. 增删改只返回“受影响的行数”（int），不需要构造对象，所以不用类型信息。
     * 2. 查询要把每一行数据封装成对象，必须知道要创建哪个类（newInstance）、
     *    要给哪些属性赋值（getDeclaredField）。
     * 3. 泛型在运行时会被擦除，方法里无法用 new T()，
     *    所以只能由调用者传入 Class<T>（例如 Employee.class）。
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws Exception {
        // 获取连接
        Connection connection = JdbcUtilV2.getConnection();

        // 预编译SQL语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 设置占位符的值
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        // 执行SQL，并接受返回的结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        // 获取结果集中的元数据对象，包含了 列的数量、每个列的名称
        /*
         * getMetaData 签名：ResultSetMetaData getMetaData() throws SQLException，是 ResultSet 的方法，
         * 用来获取这个结果集的结构信息。返回的 ResultSetMetaData 就是装着这些信息的对象，
         * 可以查列数、列名或别名、列类型等。
         */
        ResultSetMetaData metaData = resultSet.getMetaData();
        /*
         * getColumnCount 签名：int getColumnCount() throws SQLException，返回结果集一共有几列。
         * columnCount 控制内层 for 循环的次数（每一列处理一次）。
         */
        int columnCount = metaData.getColumnCount();

        List<T> list = new ArrayList<>();
        // 处理结果
        while (resultSet.next()) {
            // 循环一次，代表有一行数据，通过反射创建一个对象
            T t = clazz.getDeclaredConstructor().newInstance();
            // 循环遍历当前行的列，循环几次，看有多少列
            for (int i = 1; i <= columnCount; i++) {
                // 通过下表获取列的值
                Object value = resultSet.getObject(i);

                // 获取列的value值，这个值就是t这个对象中的某一个属性
                // 获取当前拿到的列的名字 = 对象的属性名
                /*
                 * getColumnLabel 签名：String getColumnLabel(int column) throws SQLException，
                 * 返回该列的“标签”：SQL 里写了 AS 别名就返回别名，没写就返回列名。
                 */
                String fieldName = metaData.getColumnLabel(i);

                // 通过类对象和fieldName获取要封装的对象的属性
                /*
                 * getDeclaredField 的签名是 Field getDeclaredField(String name) throws NoSuchFieldException，
                 * 是 Class 的方法，按名字找到该类自己声明的属性（包括 private，不包括父类的），返回表示该属性的 Field 对象。
                 */
                Field field = clazz.getDeclaredField(fieldName);

                // 突破封装的private
                /*
                 * setAccessible 的签名：void setAccessible(boolean flag)。
                 * 传 true 表示跳过 Java 的访问权限检查，这样 private 属性也能通过反射读写。
                 */
                field.setAccessible(true);
                /*
                 * Field 的 set 签名：void set(Object obj, Object value)
                 * throws IllegalArgumentException, IllegalAccessException。
                 * 作用是把对象 obj 的这个属性设置为 value，
                 */
                field.set(t, value);
            }
            list.add(t);
        }
        resultSet.close();
        preparedStatement.close();

        if (connection.getAutoCommit()) {
            JdbcUtilV2.release();
        }

        return list;
    }

    /**
     * 通用查询：在上面查询的集合结果中获取第一个结果。 简化了获取单行单列的获取、单行多列的获取
     */
    public <T> T executeQueryBean(Class<T> clazz, String sql, Object... params) throws Exception {
        List<T> list = this.executeQuery(clazz, sql, params);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
