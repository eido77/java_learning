package chapter08;

public class StaticTest {
    /*
    static关键字的使用
    静态变量的内存解析.png
    1. static: 静态的
    2. static 用来修饰的结构：属性、方法; 代码块、内部类；
    3. static修饰属性
       3.1 复习：变量的分类
       方式1：按照数据类型：基本数据类型、引用数据类型
       方式2：按照类中声明的位置：
            成员变量：按照是否使用static修饰进行分类：
                使用static修饰的成员变量：静态变量、类变量
                不使用static修饰的成员变量：非静态变量、实例变量
            局部变量：方法内、方法形参、构造器内、构造器形参、代码块内等。
       3.2 静态变量：类中的属性使用static进行修饰。
           对比静态变量与实例变量：
           ① 个数
            >静态变量：在内存空间中只有一份，被类的多个对象所共享。
            >实例变量：类的每一个实例（或对象）都保存着一份实例变量。
           ② 内存位置
            >静态变量：jdk6及之前：存放在方法区。 jdk7及之后：存放在堆空间
            >实例变量：存放在堆空间的对象实体中。
           ③ 加载时机
            >静态变量：随着类的加载而加载，由于类只会加载一次，所以静态变量也只有一份。
            >实例变量：随着对象的创建而加载。每个对象拥有一份实例变量。
           ④ 调用者
            >静态变量：可以被类直接调用，也可以使用对象调用。
            >实例变量：只能使用对象进行调用。
           ⑤ 判断是否可以调用 ---> 从生命周期的角度解释
                        类变量         实例变量
            类            yes            no
            对象          yes            yes
           ⑥ 消亡时机
            >静态变量：随着类的卸载而消亡
            >实例变量：随着对象的消亡而消亡
    4. static修饰方法：（类方法、静态方法）
    > 随着类的加载而加载
    > 可以通过“类.静态方法”的方式，直接调用静态方法
    > 静态方法内可以调用静态的属性或静态的方法。（属性和方法的前缀使用的是当前类，可以省略）
              不可以调用非静态的结构。（比如：属性、方法）
    >                     类方法         实例方法
              类            yes            no
              对象          yes            yes
    > static修饰的方法内，不能使用this和super
    > 补充：在类的非静态方法中，可以调用当前类中的静态结构（属性、方法）或非静态结构（属性、方法）
    5. 开发中，什么时候需要将属性声明为静态的？
        > 判断当前类的多个实例是否能共享此成员变量，且此成员变量的值是相同的。
        > 开发中，常将一些常量声明是静态的。比如：Math类中的PI
       什么时候需要将方法声明为静态的？
        > 方法内操作的变量如果都是静态变量（而非实例变量）的话，则此方法建议声明为静态方法
        > 开发中，常常将工具类中的方法，声明为静态方法。比如：Arrays类、Math类
     */
    public static void main(String[] args) {
        System.out.println(Chinese.nation); // 中国
        Chinese.show(); // I'm from China

        Chinese c1 = new Chinese();
        c1.name = "姚明";
        c1.age = 40;
        c1.nation = "China";

        Chinese c2 = new Chinese();
        c2.name = "刘翔";
        c2.age = 39;

        System.out.println("c1 " + c1 + " c2 " + c2); // c1 Chinese{name='姚明', age=40} c2 Chinese{name='刘翔', age=39}
        System.out.println(c1.nation); // China
        System.out.println(c2.nation); // China

        c2.nation = "CN";
        System.out.println(c1.nation); // CN
        System.out.println(c2.nation); // CN
    }
}

class Chinese {
    // 非静态变量、实例变量
    String name;
    int age;

    // 静态变量、类变量
    static String nation = "中国";

    @Override
    public String toString() {
        return "Chinese{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void eat(String food) {
        System.out.println(food);
    }

    public static void show() {
        System.out.println("I'm from China");

        // 调用静态结构
        System.out.println("nation = " + nation);
        method1();

        // 调用非静态结构
        // 原因：静态方法内不存在 this，而访问实例成员必须依赖 this
        // 编译器解析到实例字段 name 后，需补隐式 this，此时失败
//        System.out.println("name = " + name);
//        eat("饺子");
    }

    public static void method1() {
        System.out.println("静态测试方法");
    }

    public void method2() {
        System.out.println("非静态测试方法");

        // 调用非静态结构
        // 这里的 name 实际是 this.name 的省略写法
        System.out.println("name = " + name);
        eat("饺子");
    }
}
