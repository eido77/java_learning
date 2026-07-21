package chapter07;

public class ToStringTest {
    /*
    toString()的使用
    Object类中toString()的定义：
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    开发中的使用场景
    > 平时我们在调用System.out.println()打印对象引用变量时，其实就调用了对象的toString()
    子类使用说明：
    > 自定义的类，在没有重写Object类的toString()的情况下，默认返回的是当前对象的地址值。
    > 像String、File、Date或包装类等Object的子类，它们都重写了Object类的toString()，在调用toString()时，返回当前对象的实体内容。
    开发中使用说明：
    > 习惯上，开发中对于自定义的类在调用toString()时，也希望显示其对象的实体内容，而非地址值。这时候，就需要重写Object类中的toString()
     */

    public static void main(String[] args) {
        User2 user2 = new User2("Tom", 12);
        // 以下两行输出相同：
        // 未重写toString()时：chapter07.User2@5acf9800
        // 重写toString()后：User2{name='Tom', age=12}
        System.out.println(user2.toString()); // 显式调用：把 .toString() 写出来了。
        System.out.println(user2); // 隐式调用，println内部自动调用toString()：没写，但 Java 替调了。
    }
}

class User2 {
    String name;
    int age;

    public User2() {
    }

    public User2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 手动实现toString()重写
//    @Override
//    public String toString() {
//        return "User2{name = " + name + ", age = " + age + "}";
//    }

    // IDEA自动生成
    @Override
    public String toString() {
        return "User2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
