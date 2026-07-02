package chapter06;

public class Student1Test {
    public static void main(String[] args) {
        /*
        案例：
        (1)定义Student类,有4个属性：
          String name;
          int age;
          String school;
          String major;
        (2)定义Student类的3个构造器:
        - 第一个构造器Student(String n, int a)设置类的name和age属性；
        - 第二个构造器Student(String n, int a, String s)设置类的name, age 和school属性；
        - 第三个构造器Student(String n, int a, String s, String m)设置类的name, age ,school和major属性；
        (3)在main方法中分别调用不同的构造器创建的对象，并输出其属性值。
         */

        // 用无参构造器：属性都是默认值，getInfo 输出会是 null 和 0
        Student1 s1 = new Student1();
        System.out.println(s1.getInfo()); // name = null, age = 0, school = null, major = null

        // 用有参构造器：创建时就把属性填好了，getInfo 直接有值
        Student1 s2 = new Student1("Tom", 50, "中国人民大学", "社会学");
        System.out.println(s2.getInfo()); // name = Tom, age = 50, school = 中国人民大学, major = 社会学

        Student1 s3 = new Student1("Jenny", 30, "清华大学");
        System.out.println(s3.getInfo()); // name = Jenny, age = 30, school = 清华大学, major = null

    }
}
