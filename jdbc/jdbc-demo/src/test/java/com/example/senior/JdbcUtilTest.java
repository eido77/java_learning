package com.example.senior;

import com.example.senior.dao.BankDao;
import com.example.senior.dao.EmployeeDao;
import com.example.senior.dao.impl.BankDaoImpl;
import com.example.senior.dao.impl.EmployeeDaoImpl;
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

    @Test
    public void testEmployeeDao() {
        // 1.创建DAO实现类对象
        EmployeeDao employeeDao = new EmployeeDaoImpl();

        /*
        // 2.调用查询所有方法
        List<Employee> employeeList = employeeDao.selectAll();

        // 3.处理结果
        for (Employee employee : employeeList) {
            System.out.println("employee = " + employee);
        }
         */

        // 调用根据id查询单个员工方法
        /*
        Employee employee = employeeDao.selectByEmpId(1);
        System.out.println("employee = " + employee);
         */

        // 调用添加员工的方法
        /*
        Employee employee = new Employee(null, "Tom", 300.65, 38);
        int insert = employeeDao.insert(employee);
        System.out.println("insert = " + insert);
         */

        // 调用更新员工薪资的方法
        /*
        Employee employee = new Employee(20008, "Tom", 656.65, 38);
        int update = employeeDao.update(employee);
        System.out.println("update = " + update);
         */

        // 根据员工id删除员工
        int delete = employeeDao.delete(20008);
        System.out.println("delete = " + delete);
    }

    @Test
    public void testTransaction() {
        BankDao bankDao = new BankDaoImpl();

        Connection connection = null;
        try {
            // 1.获取连接，将连接的事务提交改为手动提交
            connection = JdbcUtilV2.getConnection();
            connection.setAutoCommit(false); // 开启事务，当前连接的自动提交关闭。改为手动提交

            // 2.操作减钱
            bankDao.subMoney(1, 100);

            // 制造算术异常（ArithmeticException，运行时异常）
            int i = 10 / 0;

            // 3.操作加钱
            bankDao.addMoney(2, 100);

            // 4.前置的多次dao操作，没有异常，提交事务
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            JdbcUtilV2.release();
        }
    }
}
