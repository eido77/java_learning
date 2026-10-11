package chapter18;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TryWithResourcesTest {
    /*
    1. JDK7新特性：
    try (资源对象的声明和初始化) {
        业务逻辑代码,可能会产生异常
    } catch (异常类型1 e) {
        处理异常代码
    } catch (异常类型2 e) {
        处理异常代码
    }
    2. JDK9新特性：
    A a = new A();
    B b = new B();
    try (a;b) {
        可能产生的异常代码
    } catch (异常类名 变量名) {
        异常处理的逻辑
    }
     */

    /**
     * 举例1：JDK7 之前的"手动关闭"写法
     */
    /*
     * 用 FileWriter + BufferedWriter 向 test.txt 写一行字符串。
     * JDK7 之前没有自动关闭机制，必须把 close() 放在 finally 里，
     * 保证无论是否抛异常都会释放文件句柄（操作系统资源有限，不关会泄漏）。
     *
     * 流变量必须声明在 try 外面，否则 finally 里访问不到；
     * 所以初始化失败时变量是 null，close() 前必须判空；
     * close() 自己还会抛 IOException，所以 finally 里又要套一层 try-catch，这才导致代码这么啰嗦。
     */
    @Test
    public void test01() {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            /*
             * 直接写文件名，是因为 FileWriter 提供了【多个重载构造器】：
             *   FileWriter(String fileName)、FileWriter(File file)、FileWriter(String fileName, boolean append) ...
             * 这个单参构造器是【覆盖写】，会清空原有内容；想追加要用 new FileWriter("test.txt", true)。
             */
            fw = new FileWriter("test.txt");
            bw = new BufferedWriter(fw);

            bw.write("hello,世界");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*
             * 【异常被覆盖】
             * 如果 try 体抛了业务异常，而 finally 里的 close() 又抛异常，
             * 会让后者"盖掉"前者，排查问题时丢失真实原因；
             * try-with-resources 用抑制异常机制（getSuppressed()）解决了这个问题。
             */
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 举例1：JDK7中的写法
     */
    @Test
    public void test02() {
        try (FileWriter fw = new FileWriter("test.txt");
             BufferedWriter bw = new BufferedWriter(fw);
        ) {
            /*
             * fw、bw 的作用域被限定在 try 块（{ } 之内），
             * 出了大括号编译器就不认识这两个变量了（连 catch 块里也用不了）。
             * 而且它们是隐式 final 的：在 try 体里写 bw = new BufferedWriter(fw); 会直接编译报错。
             */
            bw.write("hello,世界123");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 举例2：JDK7 之前写法，完成一次【转码复制】。
     * 从test.txt(utf-8)文件中，读取内容，写出到abc.txt(gbk)文件中
     */
    /*
     * 链路：FileInputStream(字节) → InputStreamReader(解码 UTF-8) → BufferedReader(按行读)
     *      FileOutputStream(字节) ← OutputStreamWriter(编码 GBK) ← BufferedWriter(带缓冲/换行)
     */
    @Test
    public void test03() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            br = new BufferedReader(isr);

            FileOutputStream fos = new FileOutputStream("abc.txt");
            OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
            bw = new BufferedWriter(osw);

            String str;
            while ((str = br.readLine()) != null) {
                bw.write(str);
                // 写入一个换行符
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 举例2：JDK7 try-with-resources 版的转码复制，与 test03 功能等价。
     * 从test.txt(utf-8)文件中，读取内容，写出到abc.txt(gbk)文件中
     */
    /*
     * 小括号里 6 个资源会按【声明的倒序】自动关闭，
     * 即 bw → osw → fos → br → isr → fis，天然满足"外层先关"的要求。
     */
    @Test
    public void test04() {
        try (
                FileInputStream fis = new FileInputStream("test.txt");
                InputStreamReader isr = new InputStreamReader(fis, "utf-8");
                BufferedReader br = new BufferedReader(isr);

                FileOutputStream fos = new FileOutputStream("abc.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
                BufferedWriter bw = new BufferedWriter(osw);
        ) {
            String str;
            while ((str = br.readLine()) != null) {
                bw.write(str);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * jdk9中举例
     */
    /*
     * 演示 JDK9 对 try-with-resources 的增强
     * 小括号里可以直接写【已有变量名】，不必在小括号内声明并初始化。
     *
     * 本例包装的是 System.in / System.out，关闭它们会导致标准输入输出流不可再用，
     * 实际开发中【不要】把 System.in/System.out 放进 try-with-resources。
     */
    @Test
    public void test05() {
        /*
         * JDK9 这种写法只在"资源不是本地新建"时才用，例如方法参数传进来的流、
         * 由 if/switch 选出来的资源、或者资源创建逻辑比较复杂需要提前写好。
         */
        InputStreamReader reader = new InputStreamReader(System.in);
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        try (reader; writer) {
            // reader、writer是final的，不可再被赋值
            /*
             * 必须 final：try-with-resources 结束时要调用"同一个对象"的 close()。
             *            如果中途允许改引用，就会出现"关闭的不是打开的那个对象"，造成资源泄漏，
             *            所以语言层面直接禁止重新赋值。
             */
//            reader = new InputStreamReader(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
