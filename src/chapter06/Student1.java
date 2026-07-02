package chapter06;

public class Student1 {
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
    String name;
    int age;
    String school;
    String major;

    /**
     * 无参构造器
     * 意义：用 new Student1() 创建对象时，属性不赋值，全部保持默认值
     *      （引用类型为 null，int 为 0）。对象先造出来，数据以后再单独填。
     * 说明：一旦类中手动定义了任何构造器（如下面的带参构造器），
     *      编译器就不再自动提供默认无参构造器。
     *      因此如果还想用 new Student1() 这种无参方式创建对象，
     *      必须像这样手动把无参构造器写回来。
     *      和有参构造器独立共存。
     */
    public Student1() {
    }

    // 构造器一：设置 name 和 age
    // 创建对象的同时就把 name、age 初始化好，一步到位，不用像无参那样造完再手动一个个赋值。
    public Student1(String name, int age){
        this.name = name;
        this.age = age;
    }

    // 构造器二：设置 name、age 和 school
    public Student1(String name, int age, String shool) {
        this.name = name;
        this.age = age;
        this.school = shool;
    }

    // 构造器三：设置 name、age、school 和 major
    public Student1(String name, int age, String school, String major) {
        this.name = name;
        this.age = age;
        this.school = school;
        this.major = major;
    }

    public String getInfo() {
        return "name = " + name + ", age = " + age + ", school = " + school + ", major = " + major;
    }

}
