package com.example.senior;

import com.example.senior.util.JdbcUtil;
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
}
