package chapter09;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ThrowableTest {
    /*
    1. 什么是异常？
    指的是程序在执行过程中，出现的非正常情况，如果不处理最终会导致JVM的非正常停止。
    2. 异常的抛出机制
    Java中把不同的异常用不同的类表示，一旦发生某种异常，就`创建该异常类型的对象`，并且抛出（throw）。
    然后程序员可以捕获(catch)到这个异常对象，并处理；如果没有捕获(catch)这个异常对象，那么这个异常
    对象将会导致程序终止。
    3. 如何对待异常
     对于程序出现的异常，一般有两种解决方法：一是遇到错误就终止程序的运行。另一种方法是程序员在编写程序时，
     就充分考虑到各种可能发生的异常和错误，极力预防和避免。实在无法避免的，要编写相应的代码进行异常的检测、
     以及`异常的处理`，保证代码的`健壮性`。
    4. 异常的体系结构
    java.lang.Throwable:异常体系的根父类
        |---java.lang.Error:错误。Java虚拟机无法解决的严重问题。如：JVM系统内部错误、资源耗尽等严重情况。
                             一般不编写针对性的代码进行处理。
                   |---- StackOverflowError、OutOfMemoryError

        |---java.lang.Exception:异常。我们可以编写针对性的代码进行处理。
                   |----编译时异常：(受检异常)在执行javac.exe命令时，出现的异常。
                        |----- ClassNotFoundException
                        |----- FileNotFoundException
                        |----- IOException
                   |----运行时异常：(非受检异常)在执行java.exe命令时，出现的异常。
                        |---- ArrayIndexOutOfBoundsException
                        |---- NullPointerException
                        |---- ClassCastException
                        |---- NumberFormatException
                        |---- InputMismatchException
                        |---- ArithmeticException
    【面试题】说说你在开发中常见的异常都有哪些？
    开发1-2年：
    |----编译时异常：(受检异常)在执行javac.exe命令时，出现的异常。
        |----- ClassNotFoundException
        |----- FileNotFoundException
        |----- IOException
    |----运行时异常：(非受检异常)在执行java.exe命令时，出现的异常。
        |---- ArrayIndexOutOfBoundsException
        |---- NullPointerException
        |---- ClassCastException
        |---- NumberFormatException
        |---- InputMismatchException
        |---- ArithmeticException
    开发3年以上：
    OOM。
     */

    // main 加上 throws，这样下面 File 那段代码就能编译通过（编译时异常必须处理，
    // 要么 throws 抛出去，要么 try-catch 捕获。这里演示用 throws 最简单）
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // =============Error=============
        // 举例1：栈内存溢出的错误 StackOverflowError
        //   原因：方法无限递归调用自己，每次调用都在“栈”里压入一层，栈空间被撑爆。
//        main(args);

        // 举例2：OutOfMemoryError: Java heap space
        //   原因：一次性申请的对象太大（这里申请 100MB 字节数组），超过了“堆”的最大内存。
//        byte[] arr = new byte[1024 * 1024 * 100];

        // ===========Exception============
        // 运行时异常
        // ArrayIndexOutOfBoundsException（数组下标越界）
        //   数组长度10，合法下标是 0~9，访问 arr[10] 就越界了。
//        int[] arr = new int[10];
//        System.out.println(arr[10]);

        // NullPointerException（空指针异常）
        //   str 被赋值为 null（不指向任何对象），再调用它的方法就会报空指针。
//        String str = "hello";
//        str = null;
//        System.out.println(str.toString());

        // 数组本身是 null，访问它的元素也是空指针。
//        int[] arr = new int[10];
//        arr = null;
//        System.out.println(arr[0]);

        // 二维数组：外层长度10创建好了，但内层还没 new，arr[0] 是 null，再访问 arr[0][0] 空指针。
//        int[][] arr = new int[10][];
//        System.out.println(arr[0][0]);

        // ClassCastException（类型转换异常）
        //   obj 实际存的是 String 对象，硬转成 Date 类型，类型对不上就报错。
//        Object obj = new String();
//        String str = (String) obj; // 正常
//        Date date = (Date) obj;

        // NumberFormatException（数字格式化异常）
        //   "abc" 不是数字，parseInt 无法把它转成 int。
//        String str = "123";
//        str = "abc";
//        int i = Integer.parseInt(str);
//        System.out.println(i);

        // InputMismatchException（输入类型不匹配异常）
        //   要求输入 int，但你输入了字母/小数等非整数内容。
//        Scanner input = new Scanner(System.in);
//        int num = input.nextInt();
//        System.out.println(num);

        // ArithmeticException（算术异常）
        //   整数除以 0
//        System.out.println(10 / 0);

        // 编译时异常
        //  ClassNotFoundException
//        Class c = Class.forName("java.lang.String");

        /*
        // FileNotFoundException / IOException
         ==========================================================================
         1. 准备两个测试文件
             文件A：hello.txt
             ┌─────────────┐
             │ hello       │   <-- 文件里就只写这 5 个英文字母（不含引号）
             └─────────────┘

             文件B：ni_hao.txt（内容是中文）
             ┌─────────────┐
             │ 你好         │   <-- 文件里就只写这两个中文字（不含引号）
             └─────────────┘
         2. 为什么会报 FileNotFoundException？
             如果 new File("hello.txt") 找不到这个文件（放错位置、名字写错、
             或者根本没创建），FileInputStream 打开时就会抛 FileNotFoundException。
             解决：确认文件真实存在，且路径正确（用绝对路径最容易排查）。
         3. 为什么 "hello" 正常，"你好" 会乱码？（重点）
             fis.read() 是【字节流】，一次只读【1 个字节(byte)】，
             然后我们用 (char) 把这个字节强转成字符打印。
             - 英文 "hello"：每个英文字母在编码里【正好占 1 个字节】，
               1 字节 == 1 个字符，所以一个一个读出来拼起来正好是 hello，正常！
             - 中文 "你好"：在 UTF-8 编码里，【一个汉字占 3 个字节】（GBK 里占 2 个字节）。
               而我们一次只读 1 个字节，等于把一个完整汉字“切成了好几段”，
               每一段单独 (char) 转出来都不是一个完整汉字，
               于是打印出来就是看不懂的乱码（比如 ä½ å¥½ 这种）。
             ★ 结论：字节流(FileInputStream)适合读【图片/视频/英文等】非中文/二进制内容；
               读中文文本应该用【字符流】 FileReader，或者用带指定编码的 InputStreamReader，
               它们能按“一个完整字符”来读，就不会把汉字切断，也就不乱码了。
         ==========================================================================
         */
        // 读取英文文件：正常输出 hello
//        File file = new File("hello.txt");
//        FileInputStream fis = new FileInputStream(file); // 可能报 FileNotFoundException（文件不存在时）
//        int data = fis.read();                            // 可能报 IOException（读取过程出错时）
//        while (data != -1) {                              // read() 读到文件末尾会返回 -1
//            System.out.print((char) data);
//            data = fis.read();                            // 可能报 IOException
//        }
//        fis.close();                                      // 可能报 IOException（关闭时出错）

        // 读取中文文件：用字节流会乱码，用来对比演示
//        File file2 = new File("ni_hao.txt");
//        FileInputStream fis2 = new FileInputStream(file2); // 可能报 FileNotFoundException（文件不存在时）
//        int data2 = fis2.read();                           // 可能报 IOException（读取过程出错时）
//        while (data2 != -1) {
//            System.out.print((char) data2);                // 中文被按字节切开，打印出乱码
//            data2 = fis2.read();                           // 可能报 IOException
//        }
//        fis2.close();                                      // 可能报 IOException（关闭时出错）
    }
}
