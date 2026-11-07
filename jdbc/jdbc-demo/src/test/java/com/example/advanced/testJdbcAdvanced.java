package com.example.advanced;

import com.example.advanced.pojo.Employee;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class testJdbcAdvanced {
    @Test
    public void testOrm() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id, emp_name, emp_salary, emp_age FROM t_emp WHERE emp_id = ?");

        preparedStatement.setInt(1, 1);

        ResultSet resultSet = preparedStatement.executeQuery();

        /*
         * 先声明变量 employee，并初始化为 null，用来存放查询结果。
         * 如果 resultSet.next() 返回 true，说明查到了数据，再创建 Employee 对象。
         * 如果查不到数据，if 不执行，employee 保持为 null，打印时会输出 null，不会报错。
         *
         * 【赋 null】：避免局部变量可能未初始化导致的编译错误。
         * 【不提前 new】：避免查不到数据时，仍然得到一个各属性都是默认值的空对象。
         *
         * 整体作用：将数据库中的一行记录转换成一个 Java 对象，
         * 这种做法叫 ORM 映射（对象关系映射）的雏形。
         */
        Employee employee = null;

        /*
         * 用 while 时，如果有多行数据，后一行会覆盖前一行，最后只保留最后一行。
         * 如果要查询多行，应该改用 List<Employee>，每次循环都 new 一个对象并 add 到集合中。
         */
        if (resultSet.next()) {
            employee = new Employee();

            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            double empSalary = resultSet.getDouble("emp_salary");
            int empAge = resultSet.getInt("emp_age");

            // 为对象的属性赋值
            employee.setEmpId(empId);
            employee.setEmpName(empName);
            employee.setEmpSalary(empSalary);
            employee.setEmpAge(empAge);

//            employee = new Employee(empId, empName, empSalary, empAge);
        }

        // 查不到数据时，employee 为 null，会打印 null，不会报错。
        System.out.println(employee);

        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testOrmList() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", "root", "");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id, emp_name, emp_salary, emp_age FROM t_emp");

        ResultSet resultSet = preparedStatement.executeQuery();

        Employee employee = null;

        /*
         * List<Employee>：用于存储多个 Employee 对象。
         * 查询结果可能有多行，每行封装成一个 Employee 对象，
         * 使用 List 集合统一保存，方便后续遍历、查询和处理所有对象。
         */
        List<Employee> employeeList = new ArrayList<>();

        while (resultSet.next()) {
            employee = new Employee();

            int empId = resultSet.getInt("emp_id");
            String empName = resultSet.getString("emp_name");
            double empSalary = resultSet.getDouble("emp_salary");
            int empAge = resultSet.getInt("emp_age");

            employee.setEmpId(empId);
            employee.setEmpName(empName);
            employee.setEmpSalary(empSalary);
            employee.setEmpAge(empAge);

            // 将每次循环封装的一行数据的对象存储在集合里
            employeeList.add(employee);
        }

        // 处理结果，遍历集合，逐个获取并处理封装好的 Employee 对象
        for (Employee emp : employeeList) {
            System.out.println(emp);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
