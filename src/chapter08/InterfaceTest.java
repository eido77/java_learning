package chapter08;

public class InterfaceTest {
    /*
    接口的使用
    1. 接口的理解：接口的本质是契约、标准、规范，就像我们的法律一样。制定好后大家都要遵守。
    2. 定义接口的关键字：interface
    3. 接口内部结构的说明：
       > 可以声明：
            属性：必须使用public static final修饰
                说明：写属性时 public static final 可以省略，编译器会自动补上，
                      但它本质上一定是全局常量，不能修改。
            方法：jdk8之前：声明抽象方法，修饰为public abstract
                 jdk8:声明静态方法、默认方法
                 jdk9:声明私有方法
                 说明：静态方法用"接口名.方法名"调用；默认方法用 default 修饰，
                      实现类可以直接用，也可以重写。
       > 不可以声明：构造器、代码块等
            原因：接口不能实例化，所以没有构造器；也没有对象状态，所以没有实例变量和代码块。
    4. 接口与类的关系 ：实现关系
    5. 格式：class A extends SuperA implements B,C{}
    注意：extends 必须写在 implements 前面。
    A相较于SuperA来讲，叫做子类
    A相较于B,C来讲，叫做实现类
    6. 满足此关系之后，说明：
    > 类可以实现多个接口。
    > 类针对于接口的多实现，一定程度上就弥补了类的单继承的局限性。
    > 类必须将实现的接口中的所有的抽象方法都重写（或实现），方可实例化。否则，此实现类必须声明为抽象类。
    对应本例：Planet 没有实现 Flyable 的 fly()，所以它必须是 abstract class。
    7. 接口与接口的关系：继承关系，且可以多继承
    对应本例：interface CC extends AA, BB 就是接口的多继承。
    8. 接口的多态性： 接口名 变量名 = new 实现类对象;
    好处：方法的参数、返回值都可以用接口类型来接收，做到"面向接口编程"，
         调用者只依赖接口（标准），不关心具体是哪个实现类，扩展性强。
         对应本例：Computer.transferData(USB usb) 就是典型应用。
    9. 面试题：区分抽象类和接口
    > 共性：都可以声明抽象方法
           都不能实例化
    > 不同：① 抽象类一定有构造器。接口没有构造器
           ② 类与类之间继承关系，类与接口之间是实现关系，接口与接口之间是多继承关系
       一个类只能继承一个抽象类，但可以实现多个接口。
     */
    public static void main(String[] args) {
        // 访问接口中的全局常量：用"接口名.常量名"
        System.out.println(Flyable.MIN_SPEED);
        System.out.println(Flyable.MAX_SPEED);

        // 常量不能被修改（final），下面这行编译报错：
        // cannot assign a value to final variable MAX_SPEED
//        Flyable.MAX_SPEED = 100;

        // 创建实现类对象，正常调用其重写的方法
        Bullet bullet = new Bullet();
        bullet.fly(); // 让子弹飞一会儿
        bullet.attack(); // 子弹可以击穿身体

        // 接口的多态性：父接口类型的变量指向子类（实现类）对象
        Flyable f = new Bullet();
        f.fly(); // 让子弹飞一会儿
        // 注意：此时 f 只能调用 Flyable 中声明的 fly()，不能调用 attack()。

        // 【USB 案例】演示接口作为方法参数 + 匿名实现类的 4 种写法
        // 1.创建接口实现类的对象
        Computer computer = new Computer();
        Printer printer = new Printer();
        computer.transferData(printer); // 设备连接成功 打印机开始工作 数据传输细节操作 打印机结束工作

        // 2.创建接口实现类的匿名对象（对象只用一次，不起名字）
        computer.transferData(new Camera()); // 设备连接成功 照相机开始工作 数据传输细节操作 照相机结束工作

        // 3.创建接口匿名实现类的对象
        // 说明：没有专门写一个 class 去 implements USB，而是"临时"造了一个类并直接 new 出对象，
        // 这个临时的类没有名字，就叫"匿名实现类"，usb1 是它的对象。
        USB usb1 = new USB() {
            @Override
            public void start() {
                System.out.println("U盘开始工作");
            }

            @Override
            public void stop() {
                System.out.println("U盘结束工作");
            }
        };
        computer.transferData(usb1); // 设备连接成功 U盘开始工作 数据传输细节操作 U盘结束工作

        // 4.创建接口匿名实现类的匿名对象
        // 说明：匿名实现类 + 匿名对象，直接把 new 出来的对象当参数传进去，最简洁的写法。
        computer.transferData(new USB() {
            @Override
            public void start() {
                System.out.println("扫描仪开始工作");
            }

            @Override
            public void stop() {
                System.out.println("扫描仪结束工作");
            }
        }); // 设备连接成功  扫描仪开始工作 数据传输细节操作 扫描仪结束工作
    }
}

interface Flyable { // 接口
    // 全局常量（写全的规范写法）
    public static final int MIN_SPEED = 0;
    // 全局常量（省略写法，编译器会自动补上 public static final）
    int MAX_SPEED = 7900;

    // 抽象方法（public abstract 可省略，下面两种等价）
//    public abstract void fly();
    void fly();
}

interface Attackable { // 接口
    public abstract void attack();
}

// Planet 实现了 Flyable 但没有重写 fly()，所以未实现全部抽象方法，必须声明为抽象类
abstract class Planet implements Flyable {
}

// 一个类实现多个接口的例子：Bullet 同时具备"可飞行"和"可攻击"两种能力
class Bullet implements Flyable, Attackable {
    @Override
    public void fly() {
        System.out.println("让子弹飞一会儿");
    }

    @Override
    public void attack() {
        System.out.println("子弹可以击穿身体");
    }
}

// 测试接口的继承关系
interface AA {
    void method1();
}

interface BB {
    void method2();
}

// 接口可以多继承（CC 同时继承 AA 和 BB）
interface CC extends AA, BB {
}

// DD 实现 CC，就必须把 CC 从 AA、BB 那里继承来的抽象方法全部实现
class DD implements CC {
    @Override
    public void method1() {
    }

    @Override
    public void method2() {
    }
}

interface USB { // 接口：定义 USB 设备的统一标准（能开始工作、能结束工作）
    // 声明常量（这里省略了，例如 USB 的长宽高尺寸等）

    // 方法
    public abstract void start();

    void stop();
}

// Computer 面向"USB 接口"编程，不关心具体接的是打印机还是相机，扩展性强
class Computer {
    // 多态：形参 USB usb 可接收任意 USB 实现类对象
    public void transferData(USB usb) { // 多态：USB usb = new Printer();
        System.out.println("设备连接成功");
        usb.start();
        System.out.println("数据传输细节操作");
        usb.stop();
    }
}

class Printer implements USB {
    @Override
    public void start() {
        System.out.println("打印机开始工作");
    }

    @Override
    public void stop() {
        System.out.println("打印机结束工作");
    }
}

class Camera implements USB {
    @Override
    public void start() {
        System.out.println("照相机开始工作");
    }

    @Override
    public void stop() {
        System.out.println("照相机结束工作");
    }
}
