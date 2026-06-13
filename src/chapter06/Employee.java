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

    //案例：将属性测试中关于员工信息的输出内容放到方法中。通过调用方法显示。
    //定义一个方法，用于显示员工的属性信息
    public void show() {
        System.out.println(id);
        System.out.println(name);
        System.out.println(age);
        System.out.println(salary);
    }

    public String show1() {
        //System.out.println() 的返回类型是 void，所以你不能把它当成 String return 出去
        //return System.out.println(id);
        return "id = " + id + ", name = " + name + ", age = " + age + ", salary = " + salary;
    }

}
