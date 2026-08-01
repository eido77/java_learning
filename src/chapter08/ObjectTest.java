package chapter08;

public class ObjectTest {
    /*
    编写一个匿名内部类，它继承Object，并在匿名内部类中，声明一个方法public void test()打印尚硅谷。
    请编写代码调用这个方法。
     */
    public static void main(String[] args) {
        // 方式一：先创建一个有名字的子类，再new对象调用
        SubObject sub1 = new SubObject();
        sub1.test();

        // 方式二：不定义具名子类，直接使用【匿名子类 + 匿名对象】
        // 语法：new 父类() { 类体 }，编译时会自动生成一个继承Object的匿名类
        // 因为对象没有赋给变量，用完即弃，所以叫“匿名对象”；只能调用一次方法
        // 提供一个继承于Object的匿名子类的匿名对象
        new Object() {
            public void test() {
                System.out.println("尚硅谷");
            }
        }.test();
    }
}

// SubObject 是 Object 的显式子类（所有类默认继承Object，这里的 extends Object 可省略）
class SubObject extends Object{
    public void test() {
        System.out.println("尚硅谷");
    }
}

// 本类系统演示匿名内部类的 6 种常见写法，分别针对：接口、抽象类、普通类
class OuterClassTest {
    public static void main(String[] args) {
        // ========== 一、针对【接口】的写法 ==========
        // 举例1：常规做法——先定义具名实现类SubA1，再创建对象
        SubA1 subA1 = new SubA1();
        subA1.method1();

        // 举例2：提供接口匿名实现类的对象
        // 没有单独写一个实现A1的类，而是直接在new的同时写出实现体
        // 对象赋给了变量a1，所以可以多次调用
        A1 a1 = new A1() {
            @Override
            public void method1() {
                System.out.println("匿名实现类重写的方法method1()");
            }
        };
        a1.method1();

        // 举例3：提供接口匿名实现类的匿名对象
        // 相比举例2，对象没有赋给变量，直接 .method1() 调用，用完即弃
        new A1() {
            @Override
            public void method1() {
                System.out.println("匿名实现类重写的方法method1()");
            }
        }.method1();

        // ========== 二、针对【抽象类】的写法 ==========
        // 举例4：常规做法——先定义具名子类SubB1继承抽象类B1，再创建对象
        SubB1 subB1 = new SubB1();
        subB1.method2();

        // 举例5：提供了继承于抽象类的匿名子类的对象
        // B1是抽象类不能直接new，但可以new它的匿名子类（在{}中重写抽象方法）
        B1 b = new B1() {
            @Override
            public void method2() {
                System.out.println("继承于抽象类的子类调用的方法");
            }
        };
        b.method2();
        // 匿名类没有名字，编译后由系统按顺序命名为 外部类$数字
        System.out.println(b.getClass()); // class chapter08.OuterClassTest$3
        // 匿名子类的父类就是抽象类B1
        System.out.println(b.getClass().getSuperclass()); // class chapter08.B1

        // 举例6：继承于抽象类的匿名子类的【匿名对象】（对比举例5，无变量接收）
        new B1() {
            @Override
            public void method2() {
                System.out.println("继承于抽象类的子类调用的方法1");
            }
        }.method2();

        // ========== 三、针对【普通类】的写法 ==========
        // 举例7：常规做法——直接创建普通类C1的对象
        C1 c1 = new C1();
        c1.method3();

        // 举例8：提供了一个继承于C1的匿名子类的对象
        // 由于没重写method3()，调用的仍是从父类C1继承来的方法
        C1 c = new C1() {};
        c.method3();
        System.out.println(c.getClass()); // class chapter08.OuterClassTest$5
        System.out.println(c.getClass().getSuperclass()); // class chapter08.C1

        // 举例9：普通类C1的匿名子类对象，并重写了method3()
        // 因为发生了重写，运行时执行的是子类重写后的CCC（体现多态）
        C1 c2 = new C1() {
            @Override
            public void method3() {
                System.out.println("CCC");
            }
        };
        c2.method3(); // CCC
    }
}

interface A1 {
    public void method1();
}

class SubA1 implements A1{
    @Override
    public void method1() {
        System.out.println("SubA1");
    }
}

abstract class B1 {
    public abstract void method2();
}

class SubB1 extends B1{
    @Override
    public void method2() {
        System.out.println("SubB1");
    }
}

class C1 {
    public void method3() {
        System.out.println("C");
    }
}
