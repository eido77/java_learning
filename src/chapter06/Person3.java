package chapter06;

public class Person3 {
    //属性
    String name;
    int age;

    //构造器
    public Person3() {
        System.out.println("Person3()....");
    }

    //声明其它的构造器
    public Person3(int a) {
        age = a;
    }

    public Person3(String n) {
        name = n;
    }

    public Person3(String n, int a) {
        name = n;
        age = a;
    }

    //方法
    public void eat() {
        System.out.println("人吃饭");
    }

    public void sleep(int hour) {
        System.out.println("每天睡眠" + hour + "小时");
    }
}
