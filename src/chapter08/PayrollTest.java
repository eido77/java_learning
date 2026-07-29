package chapter08;

import java.util.Scanner;

public class PayrollTest {
    /*
    编写工资系统，实现不同类型员工(多态)的按月发放工资。如果当月出现某个Employee对象的生日，则将该雇员的工资增加100元。
    实验说明：
    （1）定义一个Employee类，该类包含：
    private成员变量name,number,birthday，其中birthday 为MyDate类的对象；
    提供必要的构造器；
    abstract方法earnings(),返回工资数额；
    toString()方法输出对象的name,number和birthday。
    （2）MyDate类包含:
    private成员变量year,month,day；
    提供必要的构造器；
    toDateString()方法返回日期对应的字符串：xxxx年xx月xx日
    （3）定义SalariedEmployee类继承Employee类，实现按月计算工资的员工处理。
    该类包括：private成员变量monthlySalary；
    提供必要的构造器；
    实现父类的抽象方法earnings(),该方法返回monthlySalary值；
    toString()方法输出员工类型信息及员工的name，number,birthday。
        比如：SalariedEmployee[name = '',number = ,birthday=xxxx年xx月xx日]
    （4）参照SalariedEmployee类定义HourlyEmployee类，实现按小时计算工资的员工处理。该类包括：
    private成员变量wage和hour；
    提供必要的构造器；
    实现父类的抽象方法earnings(),该方法返回wage*hour值；
    toString()方法输出员工类型信息及员工的name，number,birthday。
    （5）定义PayrollSystem类，创建Employee变量数组并初始化，该数组存放各类雇员对象的引用。
    利用循环结构遍历数组元素，输出各个对象的类型,name,number,birthday,以及该对象生日。
    当键盘输入本月月份值时，如果本月是某个Employee对象的生日，还要输出增加工资信息。
    //提示：
    //定义People类型的数组People c1[]=new People[10];
    //数组元素赋值
    c1[0]=new People("John","0001",20);
    c1[1]=new People("Bob","0002",19);
    //若People有两个子类Student和Officer，则数组元素赋值时，可以使父类类型的数组元素指向子类。
    c1[0]=new Student("John","0001",20,85.0);
    c1[1]=new Officer("Bob","0002",19,90.5);
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 声明一个 Employee(父类)类型的数组，长度为2。数组本身只存"引用/地址"，此时两个元素默认是 null
        Employee[] emps = new Employee[2];
        // new 可以嵌套使用
        // new 的作用是创建一个对象，它返回的是这个对象的引用（地址）。既然它返回的是一个对象，那么这个对象就可以直接被当作参数传给另一个方法或构造器。
        // 多态：父类类型的数组元素，指向子类对象（父类引用指向子类对象）
        emps[0] = new SalariedEmployee("Tom", 1001, new MyDate1(2000, 1, 2), 18000);
        emps[1] = new HourlyEmployee("Jenny", 1002, new MyDate1(2001, 2, 3), 240, 100);

        System.out.println("请输入当前的月份：");
        // nextInt() 的作用：读取用户输入的下一个整数（int类型）
        int month = input.nextInt();

        for (int i = 0; i<emps.length; i++) {
            // 因为是父类数组，编译时看的是 Employee 的方法；运行时实际调用的是子类重写后的 toString()
            //（这就是多态：同样一行代码，不同子类表现不同）
            System.out.println(emps[i].toString());
            // earnings() 是抽象方法，运行时会调用各子类各自实现的版本
            System.out.println("工资为：" + emps[i].earnings());

            if (month == emps[i].getBirthday().getMonth()) {
                System.out.println("生日快乐");
            }
        }
        input.close();
    }
}

// abstract：抽象类，不能被 new 实例化，只能被继承。它是所有员工的父类（公共模板）
abstract class Employee {
    private String name;
    private int number;
    private MyDate1 birthday;

    public Employee() {
    }

    public Employee(String name, int number, MyDate1 birthday) {
        this.name = name;
        this.number = number;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public MyDate1 getBirthday() {
        return birthday;
    }

    public void setBirthday(MyDate1 birthday) {
        this.birthday = birthday;
    }

    public abstract double earnings();

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", birthday=" + birthday.toDateString() +
                '}';
    }
}

class MyDate1 {
    private int year;
    private int month;
    private int day;

    public MyDate1() {
    }

    public MyDate1(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String toDateString() {
        return year + "-" + month + "-" + day;
    }
}

class SalariedEmployee extends Employee {
    private double monthlySalary;

    public SalariedEmployee() {
    }

    public SalariedEmployee(String name, int number, MyDate1 birthday, double monthlySalary) {
        super(name, number, birthday);
        this.monthlySalary = monthlySalary;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double earnings() {
        return monthlySalary;
    }

    @Override
    public String toString() {
        return "SalariedEmployee[" + super.toString() + "]";
    }
}

class HourlyEmployee extends Employee {
    private double wage; // 单位小时工资
    private double hour; // 月工作小时数

    public HourlyEmployee() {
    }

    public HourlyEmployee(String name, int number, MyDate1 birthday, double wage, double hour) {
        super(name, number, birthday);
        this.wage = wage;
        this.hour = hour;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }

    @Override
    public double earnings() {
        return wage * hour;
    }

    @Override
    public String toString() {
        return "HourlyEmployee[" + super.toString() + "]";
    }
}
