package com.example.senior.dao.impl;

import com.example.senior.dao.BaseDao;
import com.example.senior.dao.EmployeeDao;
import com.example.senior.pojo.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeDaoImpl extends BaseDao implements EmployeeDao {
    @Override
    public List<Employee> selectAll() {
        try {
            String sql = "SELECT emp_id AS empId,emp_name AS empName,emp_salary AS empSalary,emp_age AS empAge FROM t_emp";
            return executeQuery(Employee.class, sql, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee selectByEmpId(Integer empId) {
        try {
            String sql = "SELECT emp_id AS empId,emp_name AS empName,emp_salary AS empSalary,emp_age AS empAge FROM t_emp WHERE emp_id = ?";
            return executeQueryBean(Employee.class, sql, empId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insert(Employee employee) {
        try {
            String sql = "INSERT INTO t_emp(emp_name,emp_salary,emp_age) VALUES (?,?,?)";
            return executeUpdate(sql, employee.getEmpName(), employee.getEmpSalary(), employee.getEmpAge());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Employee employee) {
        try {
            String sql = "UPDATE t_emp SET emp_salary = ? WHERE emp_id = ?";
            return executeUpdate(sql, employee.getEmpSalary(), employee.getEmpId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Integer empId) {
        try {
            String sql = "DELETE FROM t_emp WHERE emp_id = ?";
            return executeUpdate(sql, empId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
