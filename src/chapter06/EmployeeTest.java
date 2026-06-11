package chapter06;

public class EmployeeTest {
    public static void main(String[] args) {
        /*
        案例：
        声明员工类Employee，包含属性：编号id、姓名name、年龄age、薪资salary。
        声明EmployeeTest测试类，并在main方法中，创建2个员工对象，并为属性赋值，并打印两个员工的信息。
        */

        //创建类的实例（或创建类的对象，类的实例化）
        Employee emp1 = new Employee();
        System.out.println(emp1); // chapter06.Employee@5acf9800
        System.out.println(emp1.name); // null
        System.out.println(emp1.age); // 0
        System.out.println(emp1.salary); // 0.0

        emp1.name = "Rose";
        emp1.age = 18;
        emp1.salary = 1000;
        emp1.id = 1001;
        System.out.println(emp1.name); // Rose
        System.out.println(emp1.age); // 18
        System.out.println(emp1.salary); // 1000.0
        System.out.println(emp1.id); // 1001

        Employee emp3 = emp1; // 没有创建新对象，只是复制地址

        Employee emp2 = new Employee();

        System.out.println(emp2.name); // null
        System.out.println(emp2.age); // 0
        System.out.println(emp2.salary); // 0.0
        System.out.println(emp2.id); // 0

        emp2.name = "Tom";
        emp2.age = 24;
        emp2.salary = 1200;
        emp2.id = 1002;
        System.out.println(emp2.name); // Tom
        System.out.println(emp2.age); // 24
        System.out.println(emp2.salary); // 1200
        System.out.println(emp2.id); // 1002

    }
}
