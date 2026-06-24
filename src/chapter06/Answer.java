package chapter06;

import java.io.PrintStream;

public class Answer {
    /*
    public class Test {
    Public static void main(String[] args) {
        int a=10;
        int b=10;
        method(a,b)；//需要在method方法被调用之后，仅打部出a=100,b=280
        请写出method方法的代码
        System.out.println("a="+a);
        System.out.print1n("b="+b);
        //代码编写处
    }
     */

    //错误做法
    public static void method0(int a, int b) {
        a *= 10; // a 变成 100，但只是局部变量
        b *= 20; // b 变成 200，但只是局部变量
        // 然后方法结束，什么都没打印
        // 回到 main，main 里的 a、b 还是 10、10（值传递）
        // → main 打印出 a=10, b=10，失败
    }

    //正确做法一：自己抢先打印，再退出程序
    public static void method1(int a, int b) {
        // 既然改不了 main 里的 a、b，那干脆在 method 里自己把 a=100、b=200 打印出来
        // 然后直接退出程序，让 main 后面那两行 println 根本没机会执行。
        a = a * 10; // a 变成 100
        b = b * 20; // b 变成 200
        System.out.println(a); // 自己打印 100
        System.out.println(b); // 自己打印 200
        System.exit(0); // 直接退出JVM，不让 main 后面的代码运行
    }

    //正确做法二：篡改输出流，让 main 真的执行 println，但打印的内容被偷换
    public static void method2(int a, int b) {
        // 注意：此时 a、b 仍是 main 传来的 10、10（值传递，没被改）
        // 所以稍后 main 里 "a="+a 拼出来的字符串就是 "a=10"，正好被下面拦截

        // 临时造一个 PrintStream 的子类对象（匿名内部类），只改写 println 的行为
        PrintStream ps = new PrintStream(System.out) {
            @Override
            public void println(String x) {

                // 用 字面量.equals(变量) 的写法，可避免 x 为 null 时报空指针
                if ("a=10".equals(x)) { // 如果要打印的是 "a=10"
                    x = "a=100"; // 偷偷换成 "a=100"
                } else if ("b=10".equals(x)) {
                    x = "b=200"; // "b=10" 换成 "b=200"
                }
                super.println(x); // 再用父类的真正打印逻辑输出
            }
        };
        System.setOut(ps); // 把系统默认输出流换成上面这个，之后 main 的 println 全走它
    }

}
