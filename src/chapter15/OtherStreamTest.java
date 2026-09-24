package chapter15;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherStreamTest {
    /*
    1. 标准输入、输出流
    System.in: 标准的输入流，默认从键盘输入
    System.out:标准的输出流，默认从显示器输出（理解为控制台输出）
    通过调用如下的方法，修改输入流和输出流的位置
      setIn(InputStream is)：System 类的静态方法，用于“重定向”标准输入流的来源，
     * 比如让程序不再从键盘读，而是从某个文件/网络流读取数据。
      setOut(PrintStream ps)：System 类的静态方法，用于“重定向”标准输出流的目的地，
     * 比如把原本打印到控制台的内容改为写入文件（本文件 test2/test3/Logger 都用到了它）。
     * 注意：这两个方法都是静态的，调用后会影响整个 JVM 运行期内的 System.in / System.out。
    2. 打印流
    3. apache-common包的使用
     */

    /**
     * 从键盘输入字符串，要求将读取到的整行字符串转成大写输出。
     * 然后继续进行输入操作，直至当输入“e”或者“exit”时，退出程序。
     * 关键机制：
     * 1) System.in 是“字节流(InputStream)”，键盘输入的是字符，所以要转换。
     * 2) InputStreamReader 是“字节转字符”的桥梁流，把字节流按编码解码成字符流。
     * 3) BufferedReader 是缓冲字符流，提供 readLine() 方法可以“按行”读取。
     * 这就是 System.in -> InputStreamReader -> BufferedReader 三层包装的原因。
     */
    @Test
    public void test1() {
        System.out.println("请输入信息(退出输入e或exit):");
        // 把"标准"输入流(键盘输入)这个字节流包装成字符流,再包装成缓冲流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try {
            // 读取用户输入的一行数据 --> 阻塞程序
            // readLine()会一直等待用户按回车,没输入时线程阻塞;返回null通常表示流结束
            while ((s = br.readLine()) != null) {
                if ("e".equalsIgnoreCase(s) || "exit".equalsIgnoreCase(s)) {
                    System.out.println("安全退出!!");
                    break;
                }
                // 将读取到的整行字符串转成大写输出
                System.out.println("-->:" + s.toUpperCase());
                System.out.println("继续输入信息");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    // 关闭过滤流时,会自动关闭它包装的底层节点流
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() throws FileNotFoundException {
        /*
         * PrintStream 带“文件路径”的构造器在创建流对象时,如果目标文件不存在会自动新建;
         * 如果文件已存在,则会清空原内容后重新写入。
         * PrintStream 是“打印流”,专门用于方便地输出各种数据类型:
         * println/print 可以直接打印 String、int、double、char、boolean 等,内部会自动转成字符串。
         */
        PrintStream ps = new PrintStream("/Users/Shared/java_learning/io/io.txt");
        ps.println("hello"); // hello
        ps.println(1); // 1
        ps.println(1.5); // 1.5

        /*
         * 上面是“直接用 ps 对象”把数据写进文件 io.txt。
         * 而 System.setOut(ps) 是把“标准输出流”重定向到 ps,
         * 之后再写 System.out.println(...) 时,内容也会写进 io.txt,而不是打印到控制台。
         * 上面是显式用 ps 写文件;下面是把 System.out 换成了 ps,让默认输出也进文件。
         */
        System.setOut(ps);
        System.out.println("你好,atguigu"); // 你好,atguigu

        ps.close();
    }

    @Test
    public void test3(){
        PrintStream ps = null;
        try {
            /*
             * 路径是否区分大小写,取决于“操作系统 + 文件系统”,和 Java 无关:
             * 1) Windows(NTFS/FAT):默认不区分大小写(a.txt 和 A.txt 视为同一个文件)。
             * 2) macOS(默认 APFS/HFS+):默认也不区分大小写,但“保留”你写的大小写。
             * 3) Linux(ext4 等):区分大小写(a.txt 和 A.txt 是两个不同文件)。
             * 另外:路径分隔符 Windows 用 \ 、类 Unix 用 / ;为跨平台建议用 File.separator。
             */
            FileOutputStream fos = new FileOutputStream(new File("/Users/Shared/java_learning/io/text.txt"));
            // 创建打印输出流,设置为自动刷新模式(写入换行符或字节 '\n' 时都会刷新输出缓冲区)
            ps = new PrintStream(fos, true);
            // 把标准输出流(控制台输出)改成文件
            if (ps != null) {
                System.setOut(ps);
            }
            // 输出ASCII字符
            for (int i = 0; i <= 255; i++) {
                System.out.print((char) i);
                if (i % 50 == 0) { // 每50个数据一行
                    System.out.println(); // 换行
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    // 测试自定义的日志类
    /*
     * 调用自定义 Logger.log(...) 把日志追加写入 log.txt。
     * Logger 是本文件下方自定义的一个工具类,log 是它的静态方法,可直接“类名.方法名”调用。
     */
    @Test
    public void test4(){
        // 测试工具类
        Logger.log("调用了System类的gc()方法，建议启动垃圾回收");
        Logger.log("调用了TeamView的addMember()方法");
        Logger.log("用户尝试进行登录，验证失败");
    }

    /*
     * 用 Apache commons-io 提供的 FileUtils.copyFile 把一张图片复制成另一份。
     * 一行代码搞定文件复制,不用自己写字节流循环读写,是第三方工具类的典型价值。
     */
    @Test
    public void test5() throws IOException {
        // 赋值一个图片
        // “定义指向图片的 File 对象”,File 只是路径的抽象,并不代表已读入图片内容
        File srcFile = new File("/Users/Shared/java_learning/io/playgirl.jpg");
        File destFile = new File("/Users/Shared/java_learning/io/playgirl_copy2.jpg");

        // 把包放到lib里面，右键 jar -> Add as Library
        FileUtils.copyFile(srcFile,destFile);

        System.out.println("复制成功");
    }
}

/**
 * 记录日志的方法。
 */
class Logger {
    public static void log(String msg) {
        try {
            // 指向一个日志文件
            PrintStream out = new PrintStream(new FileOutputStream("/Users/Shared/java_learning/io/log.txt", true));
            // 改变输出方向
            System.setOut(out);
            // 日期当前时间
            /*
             * java.util.Date:代表“日期 + 时间(年月日时分秒毫秒)”,一般业务里用的就是它。
             * java.sql.Date:是 util.Date 的子类,专门配合数据库使用,只表示“日期(年月日)”,
             *   把时分秒都当作 0,常用于向数据库存/取 DATE 类型字段。
             * 补充:Java 8 以后更推荐用 java.time 包(LocalDate/LocalDateTime),更安全好用。
             */
            Date nowTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            String strTime = sdf.format(nowTime);

            System.out.println(strTime + ": " + msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
