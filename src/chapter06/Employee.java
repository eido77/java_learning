package chapter06;

public class Employee {
    /*
    案例：
    声明员工类Employee，包含属性：编号id、姓名name、年龄age、薪资salary。
    声明EmployeeTest测试类，并在main方法中，创建2个员工对象，并为属性赋值，并打印两个员工的信息。
    */
    //属性（或成员变量）
    int id;
    String name;
    int age;
    double salary;
    /*
    案例：
    （1）声明一个MyDate类型，有属性：年year，月month，日day
    （2）声明一个Employee类型，包含属性：编号、姓名、年龄、薪资、生日（MyDate类型）
    （3）在EmployeeTest测试类中的main()中，创建两个员工对象，并为他们的姓名和生日赋值，并显示
     */
    //自己定义的类,而类本身就是一种数据类型(引用数据类型)。
    MyDate birthday;

}
