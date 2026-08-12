package chapter09;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TryCatchFinallyTest {
    /*
    1. 方式一（抓抛模型）：try-catch-finally
    过程1：“抛”
        程序在执行的过程当中，一旦出现异常，就会在出现异常的代码处，生成对应异常类的对象，并将此对象抛出。
        一旦抛出，此程序就不执行其后的代码了。
    过程2：“抓”
        针对于过程1中抛出的异常对象，进行捕获处理。此捕获处理的过程，就称为抓。
        一旦将异常进行了处理，代码就可以继续执行。
    2. 基本结构：
    try{
        ......	//可能产生异常的代码
    }
    catch( 异常类型1 e ){
        ......	//当产生异常类型1型异常时的处置措施
    }
    catch( 异常类型2 e ){
        ...... 	//当产生异常类型2型异常时的处置措施
    }
    finally{
        ...... //无论是否发生异常，都无条件执行的语句
    }
    3. 使用细节：
    > 将可能出现异常的代码声明在try语句中。一旦代码出现异常，就会自动生成一个对应异常类的对象。并将此对象抛出。
    > 针对于try中抛出的异常类的对象，使用之后的catch语句进行匹配。一旦匹配上，就进入catch语句块进行处理。
      一旦处理接触，代码就可继续向下执行。
    > 如果声明了多个catch结构，不同的异常类型在不存在子父类关系的情况下，谁声明在上面，谁声明在下面都可以。
        如果多个异常类型满足子父类的关系，则必须将子类声明在父类结构的上面。否则，报错。
    > catch中异常处理的方式：
       ① 自己编写输出的语句。
       ② printStackTrace()：打印异常的详细信息。 （推荐）
       ③ getMessage()：获取发生异常的原因。
    > try中声明的变量，出了try结构之后，就不可以进行调用了。
    > try-catch结构是可以嵌套使用的。
    4. 开发体会：
       > 对于运行时异常：
            开发中，通常就不进行显示的处理了。
            一旦在程序执行中，出现了运行时异常，那么就根据异常的提示信息修改代码即可。
       > 对于编译时异常：
            一定要进行处理。否则编译不通过。
    5. finally的使用说明
    5.1 finally的理解
    > 我们将一定要被执行的代码声明在finally结构中。
    > 更深刻的理解：无论try中或catch中是否存在仍未被处理的异常，无论try中或catch中是否存在return语句等，finally
      中声明的语句都一定要被执行。
    > finally语句和catch语句是可选的，但finally不能单独使用。
        1. try + catch
        2. try + finally
        3. try + catch + finally
    5.2 什么样的代码我们一定要声明在finally中呢？
    > 我们在开发中，有一些资源（比如：输入流、输出流，数据库连接、Socket连接等资源），在使用完以后，必须显式的进行
    关闭操作，否则，GC不会自动的回收这些资源。进而导致内存的泄漏。
      为了保证这些资源在使用完以后，不管是否出现了未被处理的异常的情况下，这些资源能被关闭。我们必须将这些操作声明在finally中
    6.面试题
    final 、 finally 、finalize 的区别
    ★ final：修饰符。修饰类（不能被继承）、方法（不能被重写）、变量（变成常量，只能赋值一次）。
    ★ finally：异常处理结构的一部分。try-catch-finally中，无论是否发生异常都一定会执行的代码块（常用来释放资源）。
    ★ finalize：Object类的方法。对象被GC回收前会调用此方法。已过时（不推荐使用），不能保证被调用、调用时机也不确定。
     */
    // 运行时异常
    @Test
    public void test1() {
        try {
            Scanner input = new Scanner(System.in);
            int num = input.nextInt();
            System.out.println(num);

            // 为什么范围小的（子类）在上面、范围大的（父类）在下面？
            // 原因：catch是从上往下逐个匹配的，匹配到一个就进去处理、不再往下找。
            //      如果父类RuntimeException写在最上面，那么所有子类异常（InputMismatchException、
            //      NullPointerException）都会先被它匹配，后面的子类catch永远进不去 = 死代码，所以编译器直接报错。
            // 结论：子类在上、父类在下（父类兜底）。不存在子父类关系的异常，上下顺序随意。
//        } catch (RuntimeException e) { // 若打开这行并放最上面，下面两个子类catch会编译报错
//            System.out.println("出现了RuntimeException的异常");
        } catch (InputMismatchException e) {
            System.out.println("出现了InputMismatchException的异常");
        } catch (NullPointerException e) {
            System.out.println("出现了NullPointerException的异常");
        } catch (RuntimeException e) { // 父类写在最后，起“兜底”作用，前面没匹配到的运行时异常都归它处理
            System.out.println("出现了RuntimeException的异常");
        }
        System.out.println("异常处理结束，代码继续执行");
    }

    // 运行时异常
    @Test
    public void test2() {
        try {
            String str = "123";
            str = "abc";
            int i = Integer.parseInt(str);
            System.out.println(i);
        } catch (NumberFormatException e) {
            // 不处理 → 程序在异常处直接中断，后面的代码（比如"程序结束"）不会执行，且如果是main主线程，整个程序就崩了。
            // 处理  → 异常被“抓住”，程序不崩，catch执行完后能继续往下走（打印"程序结束"）。
            // 意义：让程序具有健壮性/容错性，出错也能优雅地继续或善后，而不是直接挂掉。
            e.printStackTrace();
            // 上面printStackTrace和下面getMessage能一起用，两者不冲突。
            // printStackTrace()打印完整堆栈（异常类型+原因+出错行号调用链），信息最全；
            // getMessage()只返回一句原因描述。实际开发常用printStackTrace()或日志框架，一般不必两个都写。
            System.out.println(e.getMessage()); // For input string: "abc"
        }
        System.out.println("程序结束");
        // str声明在try的{}内，是局部变量，作用域仅限于try块内部，出了大括号就被销毁、无法访问。
//        System.out.println(str);
    }

    // 编译时异常
    @Test
    public void test3() {
        try {
            File file = new File("D:\\hello.txt");

            FileInputStream fis = new FileInputStream(file); //可能报FileNotFoundException

            int data = fis.read(); //可能报IOException
            while (data != -1) {
                System.out.print((char) data);
                data = fis.read(); //可能报IOException
            }
            fis.close(); //可能报IOException
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // 为什么把上面那个FileNotFoundException的catch注释掉也可以？
            // FileNotFoundException是IOException的子类，父类catch能接住所有子类异常。
            // 只留IOException一个catch，就能把FileNotFoundException和其它IOException一并处理，
            // 所以编译能过。区别只是：分开写能针对不同异常做不同处理，合并写则统一处理。
            e.printStackTrace();
        }
        System.out.println("读取数据结束....");
    }

    // finally

    // test4和test5的区别？
    // 区别：test4没有finally，"程序结束"直接写在方法体里；test5把"程序结束"放进finally。
    // 共同点是：catch里都执行了 10/0（抛出ArithmeticException，且没人catch它）。
    // 结果差异：
    //      test4 → catch里10/0抛异常，异常向上抛出、方法中断，最后那句"程序结束"【不会执行】。
    //      test5 → catch里10/0同样抛异常，但finally是“一定执行”的，所以"程序结束"【会先执行】，然后异常再继续向上抛。
    // finally的核心价值：哪怕catch中又出了新异常/有return，finally里的代码照样执行。

    // finally在catch前面还是后面运行？顺序是什么？
    // 执行顺序：先执行try → 若try出异常则跳到匹配的catch → 最后无论如何都执行finally。
    //         所以finally总是在try/catch之后、方法真正结束（或异常抛出）之前执行，是“收尾”角色。
    @Test
    public void test4() {
        try {
            String str = "123";
            str = "abc";
            int i = Integer.parseInt(str);
            System.out.println(i);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // 在catch中存在异常
            System.out.println(10 / 0); // 这里抛ArithmeticException且无人处理，方法就此中断
        }
        System.out.println("程序结束"); // 因上一行抛异常且没finally保护，这句【执行不到】
    }

    @Test
    public void test5() {
        try {
            String str = "123";
            str = "abc";
            int i = Integer.parseInt(str);
            System.out.println(i);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // 在catch中存在异常
            System.out.println(10 / 0); // 这里抛ArithmeticException
        } finally {
            System.out.println("程序结束"); // 程序结束，即使catch中抛了异常，finally也【一定执行】，能打印出来
        }
    }

    // 实际开发中，finally的使用
    @Test
    public void test6() {
        // 为什么fis声明放外面、赋值放里面？有更好的办法吗？
        // 原因：finally里要调用fis.close()关闭资源，但finally访问不到try块内声明的局部变量，
        //      所以必须把fis声明提到try外面（方法级作用域），才能在finally里用到它。
        // 更好的办法：JDK7+的 try-with-resources 语法，会自动关闭资源，代码更简洁：
        //      try (FileInputStream fis = new FileInputStream(file)) { ... }
        //      不用写finally、不用手动close，编译器自动生成关闭逻辑。
        FileInputStream fis = null;

        try {
            File file = new File("D:\\hello.txt");
//            FileInputStream fis = new FileInputStream(file); //可能报FileNotFoundException
            fis = new FileInputStream(file);

            int data = fis.read(); //可能报IOException
            while (data != -1) {
                System.out.print((char) data);
                data = fis.read(); //可能报IOException
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //重点：将流资源的关闭操作声明在finally中
            // close()还可能抛IOException，所以要在finally里再包一层try-catch。
            try {
                // 这个if(fis != null)有必要吗？为什么加？
                // 有必要。fis上面初始化为null，如果 new FileInputStream(file) 这一步就抛异常
                //        （比如文件不存在），那fis还是null，从没成功创建。此时若直接fis.close()
                //        会触发空指针异常NullPointerException。所以先判空，非null（创建成功了）才关闭。
                if (fis != null) {
                    // fis有可能不会关闭。若不用finally，一旦read()等中途抛异常，程序就跳走了，close()根本执行不到，
                    //                 流资源就泄漏了。所以必须把close()放finally里，保证无论是否异常都会关。
                    fis.close(); //可能报IOException
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("读取数据结束....");
    }
}

// 下面4个类专门演示 finally 与 return 的“笔试题级”交互，务必理解执行顺序：
// 核心规则：try/catch里遇到return时，会先“暂存”返回值，然后【必须先去执行finally】，
//          finally执行完再真正返回那个暂存值；但如果finally里也有return，就会“覆盖”前面的返回值。
class FinallyTest1 {
    public static void main(String[] args) {
        int result = test("12"); // test结束
        System.out.println(result); // 1
    }

    public static int test(String str) {
        try {
            Integer.parseInt(str); // "12"能正常转换，不抛异常
            return 1; // 暂存返回值1，但先去执行finally
        } catch (NumberFormatException e) {
            return -1;
        } finally {
            System.out.println("test结束"); // finally执行（打印），无return，最终返回暂存的1
        }
    }
}

class FinallyTest2 {
    public static void main(String[] args) {
        int result = test("a"); // test结束
        System.out.println(result); // -1
    }

    public static int test(String str) {
        try {
            Integer.parseInt(str); // "a"无法转换，抛NumberFormatException，跳到catch
            return 1;
        } catch (NumberFormatException e) {
            return -1; // 暂存返回值-1，先去执行finally
        } finally {
            System.out.println("test结束"); // finally执行，无return，最终返回暂存的-1
        }
    }
}

class FinallyTest3 {
    public static void main(String[] args) {
        int result = test("a"); // test结束
        System.out.println(result); // 0
    }

    public static int test(String str) {
        try {
            Integer.parseInt(str); // 抛异常，跳到catch
            return 1;
        } catch (NumberFormatException e) {
            return -1; // 本想返回-1，但finally里有return，会被覆盖
        } finally {
            System.out.println("test结束");
            return 0; // finally里的return直接决定最终返回值 → 返回0（覆盖了catch的-1）
        }
    }
}

class FinallyTest4 {
    public static void main(String[] args) {
        int result = test(10); // test结束
        System.out.println(result); // 10
    }

    public static int test(int num) {
        try {
            return num; // 此刻num=10，返回值“10”被暂存下来（是当前值的副本，已定死）
        } catch (NumberFormatException e) {
            return num--;
        } finally {
            System.out.println("test结束");
//            return ++num; // 11，若打开这行：finally有return，会覆盖 → 返回11
            ++num; // 10，这里只是把num变成11，但没有return覆盖，返回的仍是之前暂存好的“10”，所以结果是10（关键易错点）
        }
    }
}
