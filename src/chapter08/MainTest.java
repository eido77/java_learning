package chapter08;

public class MainTest {
    /*
    main()方法的剖析
    public static void main(String args[]){}
    1. 理解1：看做是一个普通的静态方法
       理解2：看做是程序的入口，格式是固定的。
    2. 与控制台交互
    如何从键盘获取数据？
    >方式1：使用Scanner
    >方式2：使用main()的形参进行传值。
     */

    // 程序的入口
    public static void main(String[] args) {
        // 理解1：main() 是普通静态方法，可以被显式调用并传参
        String[] arr = new String[]{"A", "B", "C", "D"};
        Main.main(arr); // A B C D

        // MainDemo
        // 理解2：main() 是程序入口，args 接收命令行/IDEA 中配置的参数
        // 运行方式：java MainTest Tom Jerry
        // 或 IDEA: Edit Configurations → Program arguments
        System.out.println("---- 命令行参数 ----");
        for (int i = 0; i < args.length; i++) {
            System.out.println("hello:" + args[i]);
        }
    }
}

class Main {
    // 看作是普通的静态方法
    public static void main(String[] args) {
        System.out.println("Main的main()调用");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
}
