package com.example.advanced.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariTest {
    /**
     * 硬编码
     */
    @Test
    public void testHardCodeHikari() throws SQLException {
        // 1.创建HikariDataSource连接池对象
        // 用无参构造创建时，连接池会延迟初始化：第一次调用 getConnection() 时才真正启动。
        HikariDataSource hikariDataSource = new HikariDataSource();

        // 2.设置连接池的配置信息【必须 | 非必须】
        // 2.1 必须设置的配置
        // 新版 Hikari 能根据 jdbcUrl 自动识别驱动，这行通常可以不写。
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql:///atguigu");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("");

        // 2.2 非必须设置的配置
        /*
         * 设置连接池中至少保持多少个空闲连接。
         * Hikari：public void setMinimumIdle(int minIdle)，不设置时默认等于 maximumPoolSize。
         * Druid：public void setMinIdle(int value)，作用相近。
         * Druid 还有 setInitialSize(int)，用来设置启动时创建的连接数；
         * Hikari 没有这个配置，启动后会按 minimumIdle 自动补足空闲连接。
         */
        hikariDataSource.setMinimumIdle(10);
        /*
         * 设置连接池最多容纳多少个连接，包括空闲连接和正在使用的连接。
         * Hikari：public void setMaximumPoolSize(int maxPoolSize)，默认值是 10。
         * Druid：public void setMaxActive(int maxActive)，默认值是 8。
         */
        hikariDataSource.setMaximumPoolSize(20);

        // 3.通过连接池获取连接对象
        Connection connection = hikariDataSource.getConnection();
        System.out.println(connection);

        // 4.回收连接
        /*
         * 连接池给出的 Connection 是代理对象。这里的 close() 不会真正断开数据库连接，
         * 而是把连接归还给连接池。
         */
        connection.close();
        hikariDataSource.close();
    }

    /**
     * 软编码
     */
    @Test
    public void testResourcesHikari() throws IOException, SQLException {
        // 1.创建Properties集合，用于存储外部配置文件的key和value值
        Properties properties = new Properties();

        // 2.读取外部配置文件，获取输入流，加载到Properties集合里
        InputStream inputStream = HikariTest.class.getClassLoader().getResourceAsStream("hikari.properties");
        properties.load(inputStream);

        /*
         * 根据 Properties 中的配置创建连接池。
         * Druid：public static DataSource createDataSource(Properties properties) throws Exception
         *   这是工厂类的静态方法，一步完成读取配置和创建连接池，返回 DataSource 接口类型。
         * Hikari 分为两步：
         *   第3步 public HikariConfig(Properties properties)
         *     这个构造方法只负责把配置读取到配置对象中，还不会创建连接。
         *   第4步 public HikariDataSource(HikariConfig configuration)
         *     这个构造方法会校验配置，并立即启动连接池（立即初始化）。
         * Hikari 把“配置”和“连接池”分开设计。
         * 配置对象可以在创建连接池前修改、复用，也可以通过 new HikariConfig("/hikari.properties") 直接读取文件。
         * Druid 则通过工厂方法隐藏了这些步骤，使用起来更简单。
         */
        // 3.创建HikariConfig连接池配置对象，将Properties集合传进去
        HikariConfig hikariConfig = new HikariConfig(properties);

        // 4.基于HikariConfig连接池配置对象，构建HikariDataSource
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        // 5.获取连接
        Connection connection = hikariDataSource.getConnection();
        System.out.println(connection);

        // 6.回收连接
        connection.close();
        inputStream.close();
        hikariDataSource.close();
    }
}
