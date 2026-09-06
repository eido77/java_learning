package chapter11;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class OtherAPITest {
    /*
    1. System类
    > 属性：out、in、err
    > 方法：currentTimeMillis() / gc() / exit(int status) / getProperty(String property)
    2. Runtime类
    > 对应着Java进程的内存使用的运行时环境，是单例的
    3. Math类
    > 凡是与数学运算相关的操作，大家可以在此类中找相关的方法即可
    4. BigInteger类和BigDecimal类
        BigInteger:可以表示任意长度的整数
        BigDecimal:可以表示任意精度的浮点数
    5. Random类
    > 获取指定范围的随机整数：nextInt(int bound)
     */

    /*
     * System 类 —— getProperty(String key)
     * 通过键名读取 JVM / 操作系统 / 当前用户的系统属性。
     * 这些键都是 Java 规定好的固定字符串，返回值全是 String。
     * 常用键速查：
     *   java.version  -> JDK 版本
     *   java.home     -> JDK 安装目录
     *   os.name       -> 操作系统名称
     *   os.version    -> 操作系统版本
     *   user.name     -> 当前登录用户名
     *   user.home     -> 用户主目录
     *   user.dir      -> 当前工程的工作目录（项目根路径），调试路径问题时很有用
     */
    @Test
    public void test1() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("java的version:" + javaVersion);

        String javaHome = System.getProperty("java.home");
        System.out.println("java的home:" + javaHome);

        String osName = System.getProperty("os.name");
        System.out.println("os的name:" + osName);

        String osVersion = System.getProperty("os.version");
        System.out.println("os的version:" + osVersion);

        String userName = System.getProperty("user.name");
        System.out.println("user的name:" + userName);

        String userHome = System.getProperty("user.home");
        System.out.println("user的home:" + userHome);

        String userDir = System.getProperty("user.dir");
        System.out.println("user的dir:" + userDir);
    }

    /*
     * Runtime 类 —— 查看 JVM 内存使用情况
     * Runtime 是单例，只能通过 getRuntime() 获取，代表当前 Java 进程的运行时环境。
     * 三个内存方法（单位都是字节 byte）：
     *   totalMemory() -> 当前已从系统申请到的堆总量（会随程序运行动态增长）
     *   maxMemory()   -> JVM 能申请到的堆上限（对应 -Xmx 参数）
     *   freeMemory()  -> 当前堆中还空闲、未被对象占用的量
     * 所以“已用内存 = totalMemory - freeMemory”。
     */
    @Test
    public void test2() {
        Runtime runtime = Runtime.getRuntime();

        long initialMemory = runtime.totalMemory(); // 获取虚拟机“当前已申请到的堆总量”，会随运行增长
        long maxMemory = runtime.maxMemory(); // 获取虚拟机最大堆内存总量：JVM 可申请的堆最大值（-Xmx），不是“当前”值
        String str = "";

        // 模拟占用内存
        for (int i = 0; i < 10000; i++) {
            str += i;
        }

        long freeMemory = runtime.freeMemory(); // 获取空闲堆内存总量：当前堆里还没被占用的空闲量

        System.out.println("已申请总内存(total)：" + initialMemory / 1024 / 1024 + "MB");
        System.out.println("最大内存(max)：" + maxMemory / 1024 / 1024 + "MB");
        System.out.println("空闲内存：" + freeMemory / 1024 / 1024 + "MB");
        System.out.println("已用内存：" + (initialMemory - freeMemory) / 1024 / 1024 + "MB");
    }

    @Test
    public void test3() {
        /*
         * Math 类 —— round(double a) 四舍五入
         * 关键规则：Math.round(x) == Math.floor(x + 0.5)，即“加 0.5 后向下取整”。
         *   -12.5 + 0.5 = -12.0 -> floor(-12.0) = -12  （所以 -12.5 结果是 -12，不是 -13）
         *   -12.6 + 0.5 = -12.1 -> floor(-12.1) = -13
         *    12.5 + 0.5 = 13.0  -> floor(13.0)  = 13
         * 和 double 有没有精度误差无关，是 round 的定义本身如此。
         */
        System.out.println(Math.round(12.3)); // 12
        System.out.println(Math.round(12.5)); // 13
        System.out.println(Math.round(-12.3)); // -12
        System.out.println(Math.round(-12.5)); // -12
        System.out.println(Math.round(-12.6)); // -13
    }

    /**
     * BigInteger —— 任意长度的整数运算（超过 long 范围也不会溢出）
     * 核心用法：数字必须放进构造器的字符串里，运算不能用 + - * /
     * 而要调用方法 add/subtract/multiply/divide/remainder，返回新的 BigInteger（不可变）。
     */
    @Test
    public void test4() {
        // 数字太大，超过 long 最大值，直接写会编译报错
//        long bigNum = 123456789123456789123456789L;

        BigInteger b1 = new BigInteger("12345678912345678912345678");
        BigInteger b2 = new BigInteger("78923456789123456789123456789");

        // 错误的，无法直接使用+进行求和
//        System.out.println("和：" + (b1 + b2));

        System.out.println("和：" + b1.add(b2));
        System.out.println("减：" + b1.subtract(b2));
        System.out.println("乘：" + b1.multiply(b2));
        System.out.println("除：" + b2.divide(b1));
        System.out.println("余：" + b2.remainder(b1));
    }

    /**
     * BigDecimal —— 任意精度的小数运算
     */
    @Test
    public void test5() {
        BigInteger bi = new BigInteger("12433241123");
        BigDecimal bd = new BigDecimal("12435.351");
        BigDecimal bd2 = new BigDecimal("11");

        /*
         * 这行不能直接用：当除不尽（无限循环小数）时，divide 会抛 ArithmeticException，
         * 所以必须像下面那样指定“舍入模式”或“保留位数”。
         */
//        System.out.println(bd.divide(bd2));

        /*
         * 只给舍入模式、不指定位数：结果小数位数按被除数 bd 的位数走，四舍五入。
         * 补充：ROUND_HALF_UP 是老式常量，已过时；新写法推荐 RoundingMode.HALF_UP。
         */
        System.out.println(bd.divide(bd2, BigDecimal.ROUND_HALF_UP));

        // 第 2 个参数 15 = 保留 15 位小数，第 3 个参数 = 四舍五入模式；这是最常用、最稳妥的写法
        System.out.println(bd.divide(bd2, 15, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * Random —— 生成伪随机数
     * nextInt()      -> 全 int 范围随机数
     * nextInt(bound) -> [0, bound) 的随机整数
     */
    @Test
    public void test6() {
        Random random = new Random();

        // 无参 nextInt() 范围是整个 int：[-2147483648, 2147483647]，正负都可能
        int i = random.nextInt();
        System.out.println(i);

        int j = random.nextInt(10); // 随机获取[0,10)范围的整数
        System.out.println(j);
    }
}
