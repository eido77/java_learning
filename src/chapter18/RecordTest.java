package chapter18;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

public class RecordTest {
    /*
     * record（记录类）：专门表示"不可变的纯数据载体"（DTO / 值对象）。
     * 把原本必须手写的 private final 字段 + 全参构造器 + 读取器(accessor)
     * + equals() / hashCode() / toString() 全部交给编译器自动生成，
     * 消灭样板代码（boilerplate）。
     * 涉及的 Java 机制：
     * 1. record 是一种特殊的类，隐式继承 java.lang.Record，并且隐式 final；
     * 2. 圆括号里的叫"记录组件(record component)"，它唯一地定义了该类的状态；
     * 3. 编译器据此生成：同名无 get 前缀的读取器、规范构造器、基于全部组件的
     *    equals/hashCode/toString。
     * 版本演进：JDK14 预览 → JDK15 二次预览 → JDK16 正式转正（JDK16+ 可直接用）。
     */
}

/**
 * 没有record结构前的写法
 */
class Order {
    // 属性：private final修饰
    private final int orderId;
    private final String orderName;

    // 构造器中初始化属性
    public Order(int orderId, String orderName) {
        this.orderId = orderId;
        this.orderName = orderName;
    }

    // 提供属性的getter方法
    /*
     * 注意：这里故意命名为 orderId() / orderName() 而不是传统 JavaBean 的
     * getOrderId() / getOrderName()，目的是和下面 record 自动生成的读取器保持一致，
     * 方便对照。
     */
    public int orderId() {
        return orderId;
    }

    public String orderName() {
        return orderName;
    }

    // equals()和hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return orderId == order.orderId && Objects.equals(orderName, order.orderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderName);
    }

    // toString()
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                '}';
    }
}

/*
 * 允许写的东西：
 *    静态字段、静态方法、静态初始化块、实例方法、构造器（规范/紧凑/非规范）、
 *    嵌套类型、实现接口（可以 implements，只是不能 extends）。
 * 禁止的东西：
 *    实例字段、abstract、显式父类、被继承（隐式 final）、给组件加 setter。
 * 使用场景：方法多返回值、DTO/VO、Map.Entry 这类元组、switch 模式匹配的解构对象。
 *
 * 组件是数组时，equals 比的是数组引用而不是内容（Objects.equals→数组的 equals）；
 * 组件是 List/Date 等可变对象时，record 只保证"引用不可变"，
 *       对象内部仍可被外部改动，想真正不可变要在构造器里做防御性拷贝；
 * record 不是"轻量级类"，它仍是普通类，不要误以为没有对象头开销。
 */
record Order1(int orderId, String orderName) {
}

class OrderTest {
    /**
     * 测试手写类 Order
     */
    @Test
    public void test1() {
        Order order1 = new Order(1001, "orderAA");
        // 测试toString()
        System.out.println(order1); // Order{orderId=1001, orderName='orderAA'}

        // 测试getter()
        System.out.println(order1.orderId()); // 1001
        System.out.println(order1.orderName()); // orderAA

        Order order2 = new Order(1001, "orderAA");
        // 测试equals()
        /*
         * order1 和 order2 是两个不同的对象（地址不同），
         * 但因为重写了 equals()，比较的是"内容相等"而非"引用相等"。
         * 如果不重写，Object.equals 用的是 ==，这里会打印 false。
         */
        System.out.println(order1.equals(order2)); // true

        // 测试hashCode()和equals()
        /*
         * 借 HashSet 的"去重"行为来间接验证：
         *    ① HashSet.add 先用 hashCode() 定位桶；若该桶已有元素，再用 equals() 比较；
         *    ② 两者都认为"相同"时，第二次 add 返回 false，元素不会被加入。
         * 所以这里 set.size() == 1，打印结果只有一个 Order{...} —— 说明 hashCode
         * 和 equals 都被正确重写且彼此一致。
         */
        HashSet<Order> set = new HashSet<>();
        set.add(order1);
        set.add(order2);
        System.out.println(set); // [Order{orderId=1001, orderName='orderAA'}]
    }

    /**
     * 测试 record Order1
     */
    @Test
    public void test2() {
        Order1 order1 = new Order1(1001, "orderAA");
        // 测试toString()
        System.out.println(order1);

        // 测试getter()
        /*
         * 这里调用的是编译器自动生成的"读取器(accessor)"，不是 JavaBean 的 getter。
         * 命名规则：与组件同名、无 get 前缀、public、无参、返回组件类型。
         * 注意：读取器可以被手动覆写（比如返回防御性拷贝），但返回值类型和方法名不能改。
         */
        System.out.println(order1.orderId());
        System.out.println(order1.orderName());

        Order1 order2 = new Order1(1001, "orderAA");
        // 测试equals()
        System.out.println(order1.equals(order2));

        // 测试hashCode()和equals()
        HashSet<Order1> set = new HashSet<>();
        set.add(order1);
        set.add(order2);
        System.out.println(set);
    }

    /*
     * 所有 record 都隐式继承 java.lang.Record。
     * Java 单继承，父类槽位已被 Record 占用 --> record 不能 extends 任何类
     * record 是隐式 final 的 --> 不能被继承
     */
    @Test
    public void test3() {
        Class<Person1> clazz = Person1.class;
        System.out.println(clazz.getSuperclass()); // class java.lang.Record
    }
}

record Person1(int id, String name) {
    // 可以在record声明的类中定义静态字段、静态方法、构造器或实例方法。
    static String info = "我是一个人";

    public static void show() {
        System.out.println("我是一个人！");
    }

    /*
     * 任何"非规范构造器"的第一条语句必须委派给另一个构造器（最终到达规范构造器），
     * 也就是必须以 this(...) 开头 —— 因为只有规范构造器才能给 final 字段赋值。
     * 如果写成 { this.id = 0; this.name = null; } 会直接编译报错。
     */
    public Person1() {
        this(0, null);
    }

    public void eat() {
        System.out.println("人吃饭");
    }

    // 不能在record声明的类中定义实例字段；类不能声明为abstract；不能声明显式的父类等。
//    final int age;
}

/*
 * record 隐式 final，而 final 与 abstract 互斥。
 * abstract 意味着"必须被继承实现"，final 意味着"禁止被继承"，逻辑矛盾。
 * 而且 record 是具体的数据载体，不存在"抽象的数据"这种概念。
 */
//abstract record Dog(int id) {
//}

/*
 * record 已隐式继承 java.lang.Record，Java 单继承，
 * 父类槽位被占满，所以不能再 extends 任何类。
 * 注意：不能 extends，但【可以 implements 接口】
 */
//record Cat(int id) extends Thread {
//}

/*
 * Person1 是 record，隐式 final，不能被任何类继承。
 * 需要复用时的替代方案：用组合而不是继承，例如
 * record Student(Person1 person, String school) { }
 */
//class Student extends Person1 {
//}
