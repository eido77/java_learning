package chapter06;

public class UserTest {
    public static void main(String[] args) {
    /*
    一、类中属性（当前仅考虑实例变量）赋值过程：
    1. 在类的属性中，可以有哪些位置给属性赋值？
    ① 默认初始化；
    ② 显式初始化；
    ③ 构造器中初始化；
    （上面是对象创建的时候「称作初始化」，下面是对象创建以后）
    ④ 通过"对象.方法"的方式赋值；
    ⑤ 通过"对象.属性"的方式赋值；
    2. 这些位置执行的先后顺序是怎样？
    ① - ② - ③ - ④/⑤
    3. 以上操作在对象创建过程中可以执行的次数如何？
    > 只能执行一次：①、②、③
    > 可以多次执行：④、⑤
    二、JavaBean的理解
    所谓JavaBean，是指符合如下标准的Java类：
    - 类是公共的
    - 有一个无参的公共的构造器
    - 有属性，且有对应的get、set方法
    三、读懂UML类图
     */
        User u1 = new User();
        System.out.println(u1.age); // 1

        User u2 = new User(2);
        System.out.println(u2.age); // 2
    }

}

class User {
    // 属性（或实例变量）
    String name;
    int age = 1;

    public User() {

    }

    public User(int age) {
        this.age = age;
    }

    // 静态变量
    // 上面的u1 u2公用一个
    static int age1;

}
