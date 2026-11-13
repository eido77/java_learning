package com.example.senior.pojo;

// 类名就是数据库表的t_后面的单词全写
// 类名使用大驼峰命名，t_ 是数据库表名前缀，不需要写入类名。
public class Employee {
    // 属性使用小驼峰命名，保留 emp 是为了对应数据库字段 emp_id（下划线转驼峰）。
    private Integer empId; // emp_id
    private String empName; // emp_name
    private Double empSalary; // emp_salary
    private Integer empAge; // emp_age

    // 说明：BaseDao.executeQuery 用反射创建对象，要求类必须有无参构造方法，所以这个构造方法不能删除。
    public Employee() {
    }

    public Employee(Integer empId, String empName, Double empSalary, Integer empAge) {
        this.empId = empId;
        this.empName = empName;
        this.empSalary = empSalary;
        this.empAge = empAge;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Double getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(Double empSalary) {
        this.empSalary = empSalary;
    }

    public Integer getEmpAge() {
        return empAge;
    }

    public void setEmpAge(Integer empAge) {
        this.empAge = empAge;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", empSalary=" + empSalary +
                ", empAge=" + empAge +
                '}';
    }
}
