package chapter08;

public class OOPInnerTest {
    /*
    类的成员之五：内部类
    1. 什么是内部类？
    将一个类A定义在另一个类B里面，里面的那个类A就称为`内部类（InnerClass）`，类B则称为`外部类（OuterClass）`。
    2. 为什么需要内部类？
    具体来说，当一个事物A的内部，还有一个部分需要一个完整的结构B进行描述，而这个内部的完整的结构B又只为外部事物A
    提供服务，不在其他地方单独使用，那么整个内部的完整结构B最好使用内部类。
    总的来说，遵循`高内聚、低耦合`的面向对象开发原则。
    3. 内部类使用举例：
    Thread类内部声明了State类，表示线程的生命周期
    HashMap类中声明了Node类，表示封装的key和value
    4. 内部类的分类：（参考变量的分类）
        > 成员内部类：直接声明在外部类的里面（与属性、方法平级）
            > 使用static修饰的：静态的成员内部类
            > 不使用static修饰的：非静态的成员内部类
        > 局部内部类：声明在方法内、构造器内、代码块内的内部类
            > 匿名的局部内部类
            > 非匿名的局部内部类
    5. 内部类这节要讲的知识：
      > 成员内部类的理解
      > 如何创建成员内部类的实例
      > 如何在成员内部类中调用外部类的结构
      > 局部内部类的基本使用
    6. 关于成员内部类的理解：
       > 从类的角度看：
            - 内部可以声明属性、方法、构造器、代码块、内部类等结构
            - 此内部类可以声明父类，可以实现接口
            - 可以使用final修饰，表示此内部类不能被继承
            - 可以使用abstract修饰，表示此内部类不能被实例化
       > 从外部类的成员的角度看：
            - 在内部可以调用外部类的结构。比如：属性、方法等
            - 除了使用public、缺省权限修饰之外，还可以使用private、protected修饰
            - 可以使用static修饰（此时为静态成员内部类，其内部不能调用外部类的非静态结构）
    7. 关于局部内部类的说明：
        > 局部内部类是声明在方法内、构造器内、代码块内的内部类，作用域仅限于其所在的方法/构造器/代码块内。
        > 局部内部类中不能使用权限修饰符和static修饰（因为它不是类的成员）。
        > 局部内部类若使用了它所在方法（构造器/代码块）的局部变量，则该局部变量默认是final的（JDK8以后可省略final关键字）。
        > 开发中常见的用法：在方法中返回一个实现了某个接口（或继承了某个类）的对象。见下方 OuterClass1。
     */
    public static void main(String[] args) {
        // 创建Person2的静态成员内部类Dog的实例：new 外部类.内部类()
        Person2.Dog dog = new Person2.Dog();
        dog.eat(); // eating

        // 创建Person2的非静态成员内部类Bird的实例：需先创建外部类对象，再用“外部类对象.new 内部类()”创建
        // 下面这行是错误写法：非静态内部类不能直接通过 new 外部类.内部类() 创建，必须依赖外部类对象
//        Person2.Bird bird = new Person2.Bird();
        // 左边写类型 Person2.Bird，右边靠对象 p1.new Bird()
        Person2 p1 = new Person2();
        Person2.Bird bird = p1.new Bird(); // 正确写法：外部类对象.new 内部类()
        //  └──┬──┘  └┬┘    └───┬───┘
        //    左边   变量名     右边
        // 【左边】Person2.Bird —— 只是在写"变量的类型"（类型的完整名字）
        //        Bird 住在 Person2 里，所以完整名字就是 Person2.Bird
        //        这里只是"起名字"，用 . 表示嵌套关系，合法！
        // 【右边】p1.new Bird() —— 这是在真正"创建对象"
        //        p1. 的意思是：这只 Bird 依附在 p1 这个 Person2 对象上
        //        必须指定依附对象，所以不能写成 new Person2.Bird()
        bird.eat(); // eating1

        bird.show("Jenny"); // age = 1 / name = Jenny / name1 = 啄木鸟 / name2 = Tom
        bird.show1(); // eating1 / eating1 / Person2 eating
    }
}

class OuterClass {
    class InnerClass {
    }
}

// 外部类
class Person2 {
    String name = "Tom";
    int age = 1;

    // 静态的成员内部类
    static class Dog {
        public void eat() {
            System.out.println("eating");
        }
    }

    // 非静态的成员内部类
    class Bird {
        String name = "啄木鸟"; // 与外部类的属性 name 重名

        public void eat() {
            System.out.println("eating1");
        }

        public void show(String name) {
            /*
             > 如何在成员内部类中调用外部类的结构？
                - 调用外部类的属性：Person2.this.属性名（当不重名时，可省略 Person2.this.）
                - 调用外部类的方法：Person2.this.方法名()
                - 当内部类与外部类属性/方法重名时：
                  属性名        → 内部类的属性（就近原则）
                  this.属性名   → 内部类的属性
                  外部类.this.属性名 → 外部类的属性
             */
            System.out.println("age = " + age); // 1，外部类属性，不重名，省略了Person2.this.
            System.out.println("name = " + name); // Jenny，就近原则，访问的是方法的形参
            System.out.println("name1 = " + this.name); // 啄木鸟，访问当前内部类 Bird 的属性
            System.out.println("name2 = " + Person2.this.name); // Tom，访问外部类 Person2 的属性
        }

        public void show1() {
            eat(); // eating1，调用内部类 Bird 的 eat()
            this.eat(); // eating1，等同上一行，this 指当前内部类对象
            Person2.this.eat(); // Person2 eating，调用外部类 Person2 的 eat()
        }
    }

    public void eat() {
        System.out.println("Person2 eating");
    }

    public void method() {
        // 局部内部类
        class InnerClass1 {
        }
    }

    public Person2() {
        // 局部内部类
        class InnerClass1 {
        }
    }

    {
        // 局部内部类
        class InnerClass1 {
        }
    }
}

class OuterClass1 {
    // 说明：局部内部类的使用
    public void method1() {
        // 局部内部类
        class A {
            // 可以声明属性、方法等
        }
    }

    // 开发中的场景
    // 这是局部内部类在开发中的典型应用——返回一个实现了 Comparable 接口的对象，下面给出4种由繁到简的写法
    public Comparable getInstance() {
        // 方式1：提供了接口的实现类（非匿名）的对象（非匿名）
//        class MyComparable implements Comparable {
//            @Override
//            public int compareTo(Object o) {
//                return 0;
//            }
//        }
//        MyComparable myComparable = new MyComparable();
//        return myComparable;

        // 方式2：提供了接口的实现类（非匿名）的匿名对象
//        class MyComparable implements Comparable {
//            @Override
//            public int compareTo(Object o) {
//                return 0;
//            }
//        }
//        return new MyComparable();

        // 方式3：提供了接口的匿名实现类的对象（非匿名）
//        Comparable c = new Comparable() {
//            @Override
//            public int compareTo(Object obj) {
//                return 0;
//            }
//        };
//        return c;

        // 方式4：提供了接口的匿名实现类的匿名对象（开发中最常用、最简洁）
        return new Comparable() {
            @Override
            public int compareTo(Object obj) {
                return 0;
            }
        };
    }
}
