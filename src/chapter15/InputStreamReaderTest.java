package chapter15;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InputStreamReaderTest {
    /*
    1. 复习
    字符编码：字符、字符串、字符数组---> 字节、字节数组（从能看得懂的 ---> 看不懂的）
    字符解码：字节、字节数组 ---> 字符、字符串、字符数组（从看不懂的 ---> 能看得懂的）
    2. 如果希望程序在读取文本文件时，不出现乱码，需要注意什么？
    > 解码时使用的字符集必须与当初编码时使用的字符集得相同。
    > 拓展：解码集必须要与编码集兼容。比如：文件编码使用的是GBK，解码时使用的是utf-8。如果文件中只有abc等英文字符，此情况下
          也不会出现乱码。因为GBK和utf-8都向下兼容了ASCII (或 ascii)
    3. 转换流：
    ① 作用：实现字节与字符之间的转换
    ② API:
    InputStreamReader: 将一个输入型的字节流转换为输入型的字符流。
    OutputStreamWriter: 将一个输出型的字符流转换为输出型的字节流。
    4. 关于字符集的理解
    4.1 在存储的文件中的字符：
     * 半角字符(英文字母a-z、数字0-9、半角标点)：ascii/GBK/utf-8 下都占 1 个字节。
     * 汉字：GBK 占 2 个字节，utf-8 占 3 个字节。
     * 全角符号(如“，”“。”"！"等中文标点、全角字母)：GBK 占 2 个字节，utf-8 占 3 个字节(等同汉字规则)。
    ascii:主要用来存储a、b、c等英文字符和1、2、3、常用的标点符号。每个字符占用1个字节。
    iso-8859-1:了解，每个字符占用1个字节。向下兼容ascii。
    gbk:用来存储中文简体繁体、a、b、c等英文字符和1、2、3、常用的标点符号等字符。
        中文字符使用2个字节存储的。向下兼容ascii，意味着英文字符、1、2、3、标点符号仍使用1个字节。
    utf-8:可以用来存储世界范围内主要的语言的所有的字符。使用1-4个不等的字节表示一个字符。
        中文字符使用3个字节存储的。向下兼容ascii，意味着英文字符、1、2、3、标点符号仍使用1个字节。
    4.2 在内存中的字符：
    一个字符(char)占用2个字节。在内存中使用的字符集称为Unicode字符集。
     */
    /**
     * 用 UTF-8 解码集读取 UTF-8 文件并打印(不乱码)。
     */
    @Test
    public void test1() throws IOException {
        // 创建File对象
        File file1 = new File("/Users/Shared/java_learning/io/dbcp_utf-8.txt");

        // 创建流对象
        FileInputStream fis = new FileInputStream(file1);
        /*
         * 下面被注释掉的写法用“IDE/系统默认字符集”解码；带 "UTF-8" 的写法是显式指定字符集。
         * 依赖默认字符集不可移植：换一台机器(比如某些 Windows 默认 GBK)可能就乱码。
         */
//        InputStreamReader isr = new InputStreamReader(fis); // 此时使用的是IDEA默认的UTF-8的字符集
        InputStreamReader isr1 = new InputStreamReader(fis, "UTF-8"); // 显式的使用UTF-8的字符集

        // 读入操作
        /*
         * isr1 是“字符流(Reader)”，它的 read(char[]) 只接收 char[]，不能用 byte[]。
         * 实际开发中读文本确实这样写；更省事可用 BufferedReader.readLine() 按行读。
         */
        char[] cBuffer = new char[1024];
        int len;
        while ((len = isr1.read(cBuffer)) != -1) {
            String str = new String(cBuffer, 0, len);
            /*
             * 换行不是 print 加的，而是来自文件内容里的换行符 \n。
             * 这些换行符被当成普通字符原样打印，所以能看到换行。
             */
            System.out.print(str);
        }

        // 关闭资源
        isr1.close();
    }

    /**
     * 故意用 GBK 解码集读取 UTF-8 文件，演示解码集不匹配导致乱码。
     * 读取到的数据出现了乱码。
     * 因为dbcp_utf-8.txt文件使用的是utf-8的字符集进行的编码，所以在读取此文件时使用的解码集必须也是utf-8，
     * 否则会出现乱码！
     */
    @Test
    public void test2() throws IOException {
        // 创建File对象
        File file1 = new File("/Users/Shared/java_learning/io/dbcp_utf-8.txt");

        // 创建流对象
        FileInputStream fis = new FileInputStream(file1);
        InputStreamReader isr1 = new InputStreamReader(fis, "GBK"); // 显式的使用GBK的字符集

        // 读入操作
        char[] cBuffer = new char[1024];
        int len;
        while ((len = isr1.read(cBuffer)) != -1) {
            String str = new String(cBuffer, 0, len);
            System.out.print(str);
        }

        // 关闭资源
        isr1.close();
    }

    /**
     * 用 GBK 解码集读取 GBK 文件并打印(不乱码)。
     */
    @Test
    public void test3() throws IOException {
        // 创建File对象
        File file1 = new File("/Users/Shared/java_learning/io/dbcp_gbk.txt");

        // 创建流对象
        FileInputStream fis = new FileInputStream(file1);
        InputStreamReader isr1 = new InputStreamReader(fis, "GBK"); // 显式的使用GBK的字符集

        // 读入操作
        char[] cBuffer = new char[1024];
        int len;
        while ((len = isr1.read(cBuffer)) != -1) {
            String str = new String(cBuffer, 0, len);
            System.out.print(str);
        }

        // 关闭资源
        isr1.close();
    }

    /**
     * 需求：将gbk格式的文件转换为utf-8格式的文件存储。
     * 读取 GBK 文件、再以 UTF-8 重新编码写出，实现文件字符集转换。
     */
    @Test
    public void test4() throws IOException {
        // 1. 造文件
        File file1 = new File("/Users/Shared/java_learning/io/dbcp_gbk.txt");
        File file2 = new File("/Users/Shared/java_learning/io/dbcp_gbk_to_utf8.txt");

        // 2. 造流
        FileInputStream fis = new FileInputStream(file1);
        InputStreamReader isr = new InputStreamReader(fis, "GBK");

        FileOutputStream fos = new FileOutputStream(file2);
        // 参数2指明内存中的字符存储到文件中的字节过程中使用的编码集。
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

        // 3. 读写过程
        char[] cBuffer = new char[1024];
        int len;
        while((len=isr.read(cBuffer))!=-1) {
            osw.write(cBuffer,0,len);
        }

        System.out.println("操作完成");

        // 4. 关闭资源
        osw.close();
        isr.close();
    }
}

/**
 * 案例
 * 把当前module下的《康师傅的话.txt》字符编码为GBK，复制到电脑桌面目录下的《寄语.txt》，
 * 字符编码为UTF-8。
 */
class InputStreamReaderDemo {
    @Test
    public void test() {
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        try {
            isr = new InputStreamReader(new FileInputStream("康师傅的话.txt"),"gbk");
            osw = new OutputStreamWriter(new FileOutputStream("/Users/Shared/java_learning/io/寄语.txt"),"utf-8");

            char[] cbuf = new char[1024];
            int len;
            while ((len = isr.read(cbuf)) != -1) {
                osw.write(cbuf, 0, len);
                osw.flush();
            }
            System.out.println("文件复制完成");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (osw != null)
                    osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
