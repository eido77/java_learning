package com.example.senior.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC工具类（V2.0）：
 * 1.维护一个连接池对象、维护一个线程绑定变量的ThreadLocal对象
 * 2.对外提供在ThreadLocal中获取连接的方法
 * 3.对外提供回收连接的方法，回收过程中，将要回收的连接从ThreadLocal中移除
 * 注意：工具类仅对外提供共性的功能代码，所以方法均为静态方法
 * 注意：使用ThreadLocal就是为了一个线程在多次数据库操作过程中，使用的是同一个连接
 */
public class JdbcUtilV2 {
    // 创建连接池引用，因为要提供给当前项目的全局使用，所以创建为静态的
    private static DataSource dataSource;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 在项目启动时，即创建连接池对象，赋值给dataSource
    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = JdbcUtilV2.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);

            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 对外提供在连接池中获取连接的方法
    public static Connection getConnection() {
        try {
            // 在ThreadLocal中获取Connection
            /*
             * ThreadLocal 叫"线程本地变量"：它给每个线程各准备一个独立的"小格子"，
             *   每个线程只能存取自己的格子，互不干扰。
             * get() 的作用：到"当前线程"的格子里取出之前存进去的 Connection；
             *   如果当前线程还没存过，就返回 null。
             * 所以下面才要判断 connection == null：null 表示这个线程是第一次要连接。
             *
             * dataSource.getConnection() 每调用一次，通常就会从池里借出一个不同的连接，
             * 所以必须写成"先 get，为空才取并 set"的形式，光有 ThreadLocal 本身不会自动复用。
             */
            Connection connection = threadLocal.get();

            // threadLocal里没有存储Connection，也就是第一次获取
            if (connection == null) {
                // 在连接池中获取一个连接，存储在threadLocal里
                connection = dataSource.getConnection();
                /*
                 * 把刚从连接池拿到的连接，存进"当前线程"自己的格子里。
                 *
                 * 1) 第一次调用：get() 得到 null，于是从连接池取连接，并用 set() 存起来；
                 * 2) 同一线程之后再调用 getConnection()：get() 能取到之前存的连接，直接返回，不再向连接池要新的。
                 */
                threadLocal.set(connection);
            }

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 对外提供回收连接的方法
    public static void release() {
        try {
            // get()：拿到当前线程绑定的那个连接，才能对它做归还操作。
            Connection connection = threadLocal.get();
            if (connection != null) {
                // 从threadLocal中移除当前已经存储的Connection对象
                /*
                 * 清空"当前线程"格子里存的 Connection。
                 * 【为什么必须移除】
                 * 1) connection.close() 对连接池来说是"归还"，连接会被别的线程再借走使用。
                 *    如果不 remove，本线程的格子里还留着这个已归还的连接，
                 *    下次 getConnection() 会直接拿到它，可能和别的线程同时使用同一个连接，出现错误。
                 * 2) Web 服务器通常用线程池，线程会被反复复用，不清理的话旧数据会残留，还会造成内存泄漏。
                 * 所以顺序是：先 remove，再 close。
                 */
                threadLocal.remove();

                // 如果开启了事务的手动提交，操作完毕后，归还给连接池之前，要将事务的自动提交改为true
                connection.setAutoCommit(true);

                // 将Connection对象归还给连接池
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
