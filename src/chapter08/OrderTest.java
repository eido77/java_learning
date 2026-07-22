package chapter08;

public class OrderTest {
    // 判断如下程序运行时是否会报错？
    // 答案：不报错。输出 hello! 和 1
    public static void main(String[] args) {
        Order order = null;

        // 【不报错】hello() 是 static 方法。
        // 编译器编译成字节码时，会根据变量 order 的【声明类型】(Order)，
        // 生成 invokestatic Order.hello() 这条指令
        // 指令里只有类名，order 这个引用压根不参与，
        // 所以它是不是 null 无所谓。
        // （源码文本不变，变的是编译产物 .class 里的指令）
        order.hello(); // hello!

        // 【同理不报错】count 是 static 变量，属于类不属于对象。
        // 字节码里是 getstatic Order.count，同样不读取 order 这个引用。
        System.out.println(order.count); // 1
    }
}

// static 只看左边（声明类型），实例方法才看右边（真实对象）。
// —— 为什么这里没有多态？——
// 多态（运行时看"右边"真实对象的类型来决定调哪个方法）只适用于【实例方法】。
// 因为实例方法必须先找到对象，才能知道调用谁 —— order 是 null，没有对象可找，就会 NPE。
//
// static 方法/变量属于【类】，不属于对象。编译期就按变量的声明类型
// (Order) 确定好了，运行时根本不去看 order 指向什么，
// 所以 null 也照常执行，也谈不上多态。

class Order {
    public static int count = 1;

    public static void hello() {
        System.out.println("hello!");
    }
}
