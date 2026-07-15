package chapter07;

public class OOPInheritanceTest {
    public static void main(String[] args) {
        Person2 person2 = new Person2();
        person2.name = "Tom";
        person2.eat();

        // toString()是从父类 Object 继承来的默认实现
        System.out.println(person2.toString()); // chapter07.Person2@5acf9800
        // 获取所属类的父类
        System.out.println(person2.getClass().getSuperclass()); // class java.lang.Object

        Student student = new Student();
        student.name = "Danny";
        student.eat();

    }
}
