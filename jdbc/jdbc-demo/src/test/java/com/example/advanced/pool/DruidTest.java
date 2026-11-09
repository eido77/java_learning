package com.example.advanced.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidTest {
    /**
     * 硬编码
     */
    @Test
    public void testHardCodeDruid() throws SQLException {
        // 1、创建DruidDataSource连接池对象
        DruidDataSource druidDataSource = new DruidDataSource();

        // 2、设置连接池的配置信息【必须 | 非必须】
        // 2.1 必须设置的配置
        /*
         * setDriverClassName：指定用哪个数据库驱动类来真正建立连接（这里是 MySQL 驱动）。
         * com.mysql.cj.jdbc.Driver 是 MySQL 官方驱动（Connector/J 8.x 及以上）里的一个类，
         * 它实现了 JDK 定义的 java.sql.Driver 接口，负责“怎样和 MySQL 服务器通信”。
         */
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // setUrl：数据库地址，格式为 jdbc:mysql://主机:端口/数据库名
        druidDataSource.setUrl("jdbc:mysql:///atguigu");
        // setUsername / setPassword：登录 MySQL 用的账号和密码
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("");

        // 2.2 非必须设置的配置
        /*
         * 不设时使用默认值：initialSize 默认 0，maxActive 默认 8。
         * initialSize：连接池初始化时预先创建多少个连接。
         * maxActive：池里同时被借出、正在使用的连接最多有多少个。
         *
         * initialSize 不能大于 maxActive，否则初始化时会报错。
         * 只写 setInitialSize(10)，maxActive 仍为默认的 8，就会出错。
         */
        druidDataSource.setInitialSize(10);
        druidDataSource.setMaxActive(20);

        // 3、通过连接池获取连接对象
        /*
         * 两个 getConnection 同名，但来源不同：
         * - DriverManager.getConnection(url, user, pwd) 是 DriverManager 类的静态方法，
         *   每次调用都会新建一条真实的数据库连接，用完关闭就彻底断开，开销大。
         * - getConnection() 来自接口 javax.sql.DataSource（JDK 定义的标准“数据源”接口），
         *   DruidDataSource 实现了这个接口。它是从池里取出一条现成的连接，速度快。
         * 返回类型 DruidPooledConnection 是 Druid 对 Connection 接口的实现，
         * 一般建议声明成 Connection，让代码不依赖具体的连接池实现。
         */
        DruidPooledConnection connection = druidDataSource.getConnection();

        System.out.println(connection);

        // 基于connection进行CRUD
        /*
         * CRUD：Create（增，INSERT）、Read（查，SELECT）、
         *       Update（改，UPDATE）、Delete（删，DELETE）。
         */

        // 4、回收连接【不是释放连接，而是将连接归还给连接池，给其他线程进行复用】
        /*
         * 连接池给的是“包装后”的连接（DruidPooledConnection），它重写了 close()，
         * 只把连接标记为空闲、放回池里，底层的真实连接仍然保持着，以便下次复用。
         *
         * 多态：同样调用 close()，具体做什么取决于对象的实际类型。
         */
        connection.close();
    }

    /**
     * 软编码
     */
    @Test
    public void testResourcesDruid() throws Exception {
        // 1.创建Properties集合，用于存储外部配置文件的key和value值
        /*
         * 把配置放到外部文件，代码只负责读取。
         * 这样修改数据库地址或密码时只需要改配置文件，不用改代码、也不用重新编译，这就是“解耦”。
         *
         * Properties 是 JDK 自带的类（java.util.Properties，继承自 Hashtable），
         * 可以看作专门存放字符串键值对的 Map，并且自带读取 .properties 文件的 load 方法。
         */
        Properties properties = new Properties();

        // 2.读取外部配置文件，获取输入流，加载到Properties集合里
        /*
         * .getClassLoader()
         * 签名：public ClassLoader getClassLoader()
         * 返回负责加载这个类的类加载器。类加载器知道 classpath（类路径）在哪里。
         *
         * .getResourceAsStream("db.properties")
         * 签名：public InputStream getResourceAsStream(String name)
         * 在 classpath 根目录下查找这个文件，并返回一个可读取文件内容的输入流。
         * Maven 项目中，src/main/resources 或 src/test/resources 里的文件编译后会被复制到 classpath 根目录。
         * 找不到文件时它不报错，而是返回 null，之后 load(null) 会抛出 NullPointerException。
         *
         * 使用 DruidTest.class 只是为了获取当前类的类加载器，其他能访问该资源的类也可以。
         */
        InputStream inputStream = DruidTest.class.getClassLoader().getResourceAsStream("db.properties");
        /*
         * 签名：public void load(InputStream inStream) throws IOException，
         * 逐行解析 key=value，并把结果存入 properties。
         */
        properties.load(inputStream);

        // 3.基于Properties集合构建DruidDataSource连接池
        /*
         * 多态：变量声明为接口类型 DataSource，实际指向的是 DruidDataSource 对象。
         *
         * 签名：public static DataSource createDataSource(Properties properties) throws Exception。
         * 静态工厂方法（专门负责创建对象的静态方法）：
         *   在内部 new 一个 DruidDataSource，读取 properties 里的 driverClassName、url 等 key，
         *   再自动调用对应的 setXxx。
         */
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        // 4.通过连接池获取连接对象
        /*
         * 签名：Connection getConnection() throws SQLException，定义在 javax.sql.DataSource 接口中。
         * 作用是从连接池里借出一条连接。编译时只知道它是 Connection；
         * 运行时实际对象是 DruidPooledConnection，打印出来就能看到。
         */
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

        // 5.开发CRUD

        // 6.回收连接
        connection.close();
        inputStream.close();
    }
}
