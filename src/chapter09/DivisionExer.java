package chapter09;

public class DivisionExer {
    /*
    编写应用程序DivisionExer.java，接收命令行的两个参数，要求不能输入负数，计算两数相除。
        对数据类型不一致(NumberFormatException)、缺少命令行参数(ArrayIndexOutOfBoundsException、
        除0(ArithmeticException)及输入负数(BelowZeroException1 自定义的异常)进行异常处理。
    提示：
        (1)在主类(DivisionExer)中定义异常方法(divide)完成两数相除功能。
        (2)在main()方法中调用divide方法，使用异常处理语句进行异常处理。
        (3)在程序中，自定义对应输入负数的异常类(BelowZeroException1)。
        (4)运行时接受参数 java DivisionDemo 20 10   //args[0] = "20" args[1] = "10"
        (5)Integer类的static方法parseInt(String s)将s转换成对应的int值。
            如：int a = Integer.parseInt("314");	// a = 314;
     */
    public static void main(String[] args) {
        // ========== 关于 Integer.parseInt（包装类）==========
        // int 是基本数据类型，Integer 是它对应的“包装类”(wrapper class)
        // 八种基本类型都有各自的包装类：
        //   byte→Byte  short→Short  int→Integer  long→Long
        //   float→Float  double→Double  char→Character  boolean→Boolean
        // 包装类的重要用途：
        //   1. 提供把字符串转成数字的静态方法，如 Integer.parseInt("314") → 314
        //   2. 提供常量和工具方法，如 Integer.MAX_VALUE、Integer.toBinaryString()
        //   3. 集合（ArrayList、HashMap）只能存对象，不能存 int，所以要用 Integer
        //   4. 自动装箱/拆箱：Integer x = 10;（装箱）  int y = x;（拆箱）
        // 注意：parseInt 如果传入的字符串不是合法数字（如 "abc"），
        //       就会抛出 NumberFormatException，这正是下面要 catch 的异常之一。

        // Option + Command + T：环绕代码快捷键（Surround With）
        // 快速生成 try/catch、if、for 等代码模板
        try {
            // 把 String 转换成 int 基本数据类型
            // Integer 在这里只是调用方法的类名，并不是创建了一个 Integer 对象，只有 String → int
            int m = Integer.parseInt(args[0]);
            int n = Integer.parseInt(args[1]);

            int result = divide(m, n);
            System.out.println(result);
            // ========== 关于“怎么测试 / 怎么传参数”==========
            // 【怎么给命令行参数？】
            //   IDEA 里：Run → Edit Configurations... → Program arguments 里填入，
            //   例如填： 20 10
            // 命令行靠空格来切分多个参数的，多个空格会被当成一个分隔符；
            // 如果某个参数本身含空格，要用引号括起来，如 "hello world"。
        } catch (BelowZeroException1 e) {
            // 自定义异常：负数时进这里，getMessage() 拿到我们抛出时写的提示文字
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            // 参数不是合法数字时进这里，如输入 "abc"
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            // 没传够参数（如只填了 1 个），访问 args[1] 越界时进这里
            e.printStackTrace();
        } catch (ArithmeticException e) {
            // 除数为 0（如 20/0）时进这里
            e.printStackTrace();
        }
    }

    public static int divide(int m, int n) throws BelowZeroException1 {
        if (m < 0 || n < 0) {
            throw new BelowZeroException1("输入负数了");
        }
        // 若 n==0，这里 JVM 自动抛 ArithmeticException
        return m / n;
    }
}

class BelowZeroException1 extends Exception {
    static final long serialVersionUID = 229372485598164692L;

    public BelowZeroException1() {
    }

    public BelowZeroException1(String message) {
        super(message);
    }
}
