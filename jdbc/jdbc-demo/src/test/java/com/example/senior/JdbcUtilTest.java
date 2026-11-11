package com.example.senior;

import com.example.senior.util.JdbcUtil;
import com.example.senior.util.JdbcUtilV2;
import org.junit.Test;

import java.sql.Connection;

public class JdbcUtilTest {
    @Test
    public void testGetConnection() {
        Connection connection = JdbcUtil.getConnection();
        System.out.println(connection);

        // CRUD

        JdbcUtil.release(connection);
    }

    @Test
    public void testJdbcV2() {
        Connection connection1 = JdbcUtil.getConnection();
        Connection connection2 = JdbcUtil.getConnection();
        Connection connection3 = JdbcUtil.getConnection();

        System.out.println(connection1);
        System.out.println(connection2);
        System.out.println(connection3);

        System.out.println("=============================");

        Connection connection4 = JdbcUtilV2.getConnection();
        Connection connection5 = JdbcUtilV2.getConnection();
        Connection connection6 = JdbcUtilV2.getConnection();

        System.out.println(connection4);
        System.out.println(connection5);
        System.out.println(connection6);
    }
}
