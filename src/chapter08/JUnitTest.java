package chapter08;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

// 单元测试类
public class JUnitTest {
    /*
    JUnit单元测试的使用
    （重点关注）要想能正确的编写单元测试方法，需要满足：
    - 所在的类必须是public的，非抽象的，包含唯一的无参构造器。
    - @Test标记的方法本身必须是public，非抽象的，非静态的，void无返回值，()无参数的。
     */
    int num = 10;

    // 只有加了@Test注解的方法，方法左边才会出现JUnit的绿色运行小箭头，没加@Test的普通方法不会有这个箭头，也不能被单独当成测试来运行
    @Test
    // 单元测试方法
    public void test1() {
        System.out.println("hello"); // hello
    }

    // 各个@Test方法之间相互独立：运行test2时，不会自动执行test1，它们互不影响、可以单独运行。
    @Test
    public void test2() {
        System.out.println("hello1"); // hello1
        System.out.println(num); // 10

        /*
        对比传统main方法的写法：
            以前要在 main 里 new 对象才能调用：
                JUnitTest test = new JUnitTest();
                System.out.println(test.num);
                test.method();
            而在@Test方法里，因为本身就是实例方法，所以能直接调用同类的成员，无需new。
         */
        method(); // method

        int num1 = showInfo("Info");
        System.out.println(num1); // 先输出 Info（来自方法内部），再输出 10（返回值）
    }

    // 普通方法（没加@Test）：不会单独运行，只能被别的方法调用
    // 运行@Test的时候不显示外面的method()
    public void method() {
        System.out.println("method");
    }

    public int showInfo(String info) {
        System.out.println(info);
        return 10;
    }

    // 演示：默认情况下@Test方法里用Scanner键盘输入会“失效”，因为JUnit运行时没有连接控制台的输入。
    // 解决办法：不用JUnit运行，改成用普通main方法运行；或者在IDEA运行配置里做相应设置。
    @Test
    public void test3() {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入一个数值：");
        int num2 = input.nextInt();
        System.out.println(num2);
    }
}
