package chapter03;
//package 语句(必须在最顶部,如果有)，import 语句(紧跟在 package 后面)，类的声明(public class ...)
import java.util.Scanner; // 整个文件只写这一次,放最顶部
        /*
        如何从键盘获取不同类型（基本数据类型、String类型）的变量：使用Scanner类。
        Scanner 就是属于「引用数据类型」里面的「类(class)」这一种。
        使用Scanner获取不同类型数据的步骤：
        步骤1.导包 import java.util.Scanner
        步骤2.提供（或创建）一个Scanner类的实例
        步骤3.调用Scanner类中的方法，获取指定类型的变量
        步骤4.关闭资源，调用Scanner类的close()
         */

        //案例：小明注册某交友网站，要求录入个人相关信息。如下：
        //     请输入你的网名、你的年龄、你的体重、你是否单身、你的性别等情况。
public class ScannerTest {
    public static void main(String[] args) {

        //步骤2.提供（或创建）一个Scanner类的实例
        // 变量名，都连键盘(System.in)→ 来源相同 → 用一个就够,多个无意义
        // 各连不同的文件/不同的字符串→ 来源不同 → 每个都有独立用途,这才是多个 Scanner 的真正价值
        Scanner scanner = new Scanner(System.in);

        //步骤3.调用Scanner类中的方法，获取指定类型的变量
        //https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html
        // 这个点"."的作用是「调用某个对象的方法」
        // next() 这个方法的括号里不能放任何东西，它必须是空的。
        //可以先在读取输入之前，打印提示语
        System.out.println("欢迎光临你来我往交友网");
        System.out.println("请输入您的网名：");
        String name = scanner.next();
        System.out.println("请输入您的年龄：");
        int age = scanner.nextInt();
        System.out.println("请输入您的体重：");
        double weight = scanner.nextDouble();
        System.out.println("您是否是单身（true/false）：");
        boolean isSingle = scanner.nextBoolean();
        System.out.println("请输入您的性别："); // 输入123的话
        //Scanner类中没有提供获取char类型变量的方法，需要使用next().charAt(0);
        //scanner.next() 先读取你输入的整个字符串,也就是 123。这一步 Scanner 接收的是一个 String
        //.charAt(0) 再从这个字符串里取出第 0 个字符(也就是第一个字符),结果是 '1'。
        //最后只把这一个字符 '1' 赋值给 char gender。
        char gender = scanner.next().charAt(0); // 输入123的话输出1

        System.out.println("name = " + name);
        System.out.println("age = " + age);
        System.out.println("weight = " + weight);
        System.out.println("isSingle = " + isSingle);
        System.out.println("gender = " + gender);
        System.out.println("注册完成，欢迎继续进入交友网");

        //步骤4.关闭资源，调用Scanner类的close()
        //"一个结束写一个 close" 在 System.in 上行不通，真实开发用 try-with-resources,让程序自动关
        //try (Scanner scanner = new Scanner(System.in)) { }。
        // 1.也需要import java.util.Scanner;这一行
        // 2.把创建放进 try 后面的小括号里，使用必须放在 { } 里面，自动 close

        //scanner.close(); // 因为后面需要用，所以我自行关闭了
        // close() 是要关闭具体的对象(也就是建的 scanner),所以要用对象名来调用
        // 但是close 关的不是变量,是System.in， System.in整个程序只有一个。只要后面还要读输入,就别提前 close

        //关于是否帅的问题，使用String类型接收
        // import java.util.Scanner; // 写在了 main 方法内部，这是不允许的。（整个文件只写一次,放最顶部）
        try (Scanner scanner2 = new Scanner(System.in)) {
            System.out.println("帅吗？（是/否）：");
            String handsome = scanner2.next();

            // == 比较的是两个东西是不是同一个对象(在内存里是不是同一个地址)。
            // equals 比较的是两个对象的内容是不是相同(对 String 来说就是"文字是不是一样")。
            // 判断 String 不能用 ==
            //if (handsome == "是") {
            if (handsome.equals("是")) {
                    System.out.println("嫁给他");
                }
            }


        scanner.close(); // 必须放在 main 方法里面。能放在try里面，但是不必放，这个try是自动关

    }
}
