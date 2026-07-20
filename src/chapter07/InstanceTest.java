package chapter07;

public class InstanceTest {
    /*
    案例：
    建立InstanceTest 类，在类中定义方法method(Person e);
    在method中:
    (1)根据e的类型调用相应类的getInfo()方法。
    (2)根据e的类型执行：
    如果e为Person类的对象，输出：
    “a person”;
    如果e为Student类的对象，输出：
    “a student”
    “a person ”
    如果e为Graduate类的对象，输出：
    “a graduated student”
    “a student”
    “a person”
     */
    public static void main(String[] args) {
        InstanceTest instanceTest = new InstanceTest();

        instanceTest.method(new Student2()); // Name: person age: 50 school: pku a student a person
    }

    // instanceof 判断链的顺序规则：必须从最具体的子类往父类写（继承树从下往上）。
    // 能用多态解决的（重写方法）就别用 instanceof；只有当行为无法通过重写表达时，才退而使用 instanceof + 强转。
    public void method(Person5 e) {
        // (1) 虚方法调用：编译看左边(Person5)，运行看右边(实际对象)
        //     传 Graduate -> 调 Graduate.getInfo()
        //     传 Student2 -> 调 Student2.getInfo()
        //     多态自动完成，无需 instanceof
        System.out.println(e.getInfo());
        // (2) 类型判断：无对应重写方法，必须用 instanceof
        //     顺序：子 -> 父（继承树从下往上）
        // 方式1
//        if (e instanceof Graduate) {
//            System.out.println("a graduated student");
//            System.out.println("a student");
//            System.out.println("a person");
//        } else if (e instanceof Student2) {
//            System.out.println("a student");
//            System.out.println("a person");
//        } else {
//            System.out.println("a person");
//        }
        // 方式2
        if (e instanceof Graduate) {
            System.out.println("a graduated student");
        }

        if (e instanceof Student2) {
            System.out.println("a student");
        }

        if (e instanceof Person5) {
            System.out.println("a person");
        }
    }
}

class Person5 {
    protected String name = "person";
    protected int age = 50;

    public String getInfo() {
        return "Name: " + name + "\n" + "age: " + age;
    }
}

class Student2 extends Person5 {
    protected String school = "pku";

    public String getInfo() {
        return "Name: " + name + "\nage: " + age
                + "\nschool: " + school;
    }
}

class Graduate extends Student2 {
    public String major = "IT";

    public String getInfo() {
        return "Name: " + name + "\nage: " + age
                + "\nschool: " + school + "\nmajor:" + major;
    }
}
