package chapter09;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ThrowsTest {
    /*
    异常处理的方式2：throws
    1. 格式：在方法的声明除，使用"throws 异常类型1,异常类型2,..."
    2. 举例：
    public void test() throws 异常类型1,异常类型2,.. {
        //可能存在编译时异常
    }
    3. 是否真正处理了异常？
    > 从编译是否能通过的角度看，看成是给出了异常万一要是出现时候的解决方案。此方案就是，继续向上抛出(throws)。
    > 但是，此throws的方式，仅是将可能出现的异常抛给了此方法的调用者。此调用者仍然需要考虑如何处理相关异常。
      从这个角度来看，throws的方式不算是真正意义上处理了异常。
    4. 方法的重写的要求：(针对于编译时异常来说的)
       子类重写的方法抛出的异常类型可以与父类被重写的方法抛出的异常类型相同，或是父类被重写的方法抛出的异常类型的子类。
    5. 开发中，如何选择异常处理的两种方式？(重要、经验之谈)
    - 如果程序代码中，涉及到资源的调用（流、数据库连接、网络连接等），则必须考虑使用try-catch-finally来处理，保证不出现内存泄漏。
    - 如果父类被重写的方法没有throws异常类型，则子类重写的方法中如果出现异常，只能考虑使用try-catch-finally进行处理，不能throws。
    - 开发中，方法a中依次调用了方法b,c,d等方法，方法b,c,d之间是递进关系。此时，如果方法b,c,d中有异常，
      我们通常选择使用throws，而方法a中通常选择使用try-catch-finally。
     */

    // main方法在语法上是【可以】throws的（写 public static void main(String[] args) throws IOException 完全合法）。
    // 但main是整个程序的入口，它上面已经没有"调用者"了（调用者是JVM）。
    // 如果main往外throws，异常最终就抛给了JVM，JVM的处理方式就是：打印栈信息 + 程序终止。
    // 这相当于把异常直接"甩给用户看崩溃信息"，等于没做任何有意义的处理。
    // 所以规范做法是：在main（或其他顶层业务方法）里用try-catch-finally真正把异常"消化掉"，
    // 而不是继续throws甩出去。这也呼应了上面第5点的经验：越底层的方法越倾向throws，越顶层越倾向try-catch。
    public static void main(String[] args) {
        method3();

        // method2()、method1()声明了throws编译时异常(IOException等)，
        // 编译时异常"要么catch、要么throws"，二选一必须处理。
        // 而这里的main没有throws，也没有把它们放进try-catch，所以直接调用会编译报错。
        // 上面method3()能用，是因为method3内部已经用try-catch处理掉了。
//        method2();
//        method1();
    }

    // 方便不造对象，把下面方法静态了
    public static void method3() {
        try {
            method2();
            // catch的类型只要是抛出异常的父类(或本身)就能接住
            // 继承关系：FileNotFoundException 是 IOException 的子类，IOException 又是 Exception 的子类。
            // 【重要顺序规则】如果写多个catch，必须"子类异常在上、父类异常在下"，
            //    否则父类catch写在前面会把子类的都截胡，导致下面子类catch永远进不去 —— 编译直接报错。
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void method2() throws FileNotFoundException, IOException {
        // method2不处理的话会报错，throws只是"往上一层甩"，不是"消灭异常"
        // method1 throws IOException，意思是：method1把它自己内部可能出的异常，甩给了"调用method1的人"。
        // 现在调用method1的人就是method2，所以这个"甩过来的异常"落到了method2头上，
        // 变成了method2必须面对的问题：method2要么自己catch，要么自己接着throws往上甩。
        // 现在method2这一行本身也声明了throws，所以下面调用method1【是可以】不报错的。
        // 把method2的throws去掉、又不加try-catch时，才会报错。
        method1();
    }

    public static void method1() throws FileNotFoundException, IOException {
        File file = new File("D:\\hello.txt");

        FileInputStream fis = new FileInputStream(file); //可能报FileNotFoundException

        int data = fis.read(); //可能报IOException
        while (data != -1) {
            System.out.print((char) data);
            data = fis.read(); //可能报IOException
        }

        fis.close(); //可能报IOException
    }
}


// 4. 方法的重写的要求：(针对于编译时异常来说的)
// 子类重写的方法抛出的异常类型可以与父类被重写的方法抛出的异常类型相同，或是父类被重写的方法抛出的异常类型的子类。
// 多态调用时，编译器只认父类的声明
// Father f = new Son();
// f.method1();
// 编译器看到的是"父类Father的method1"，它只知道父类声明的 throws IOException，
// 于是调用方只按照"最多出IOException"来准备catch（比如只写了 catch(IOException)）。
// 但真正运行的是子类Son的method1。如果Java允许子类throws一个"更大的父类异常"(比如Exception)，
// 那子类实际就可能抛出Exception，而调用方只准备了接IOException，Exception根本接不住 —— 程序就崩了、约定被破坏。
// 所以规则必须反过来：子类抛出的异常只能"更小或相等"(子类异常/相同)，
// 这样父类声明的catch永远能兜得住子类实际抛出的，才安全。
// 一句话记忆：【子类重写方法抛出的异常，不能比父类的大】。
class OverrideTest1 {
    public static void main(String[] args) {
        Father f = new Son();
        try {
            f.method1();
            // 这里就是上面说的场景：编译器只认Father.method1的throws IOException，所以这里catch(IOException)就够了。
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 这是在演示"方法重写时，返回值类型也有类似的'只能更小'的规则"
        // 父类Father.method3()返回Number，子类Son.method3()返回Integer（Integer是Number的子类）。
        // 用父类接收 Number n = f.method3(); 实际拿到的是子类返回的Integer，
        // 因为Integer是Number，向上赋值天然安全，所以这行成立。
        // 这和异常规则是同一个思想：重写时，返回值/异常都可以"缩小(用子类)"，但不能"放大(用父类)"。
        Number n = f.method3();
    }
}

class Father {
    public void method1() throws IOException {
    }

    public void method2() {
    }

    public Number method3() {
        return null;
    }
}

class Son extends Father {
    @Override
    // 父类method1 throws IOException
    // 异常本质上也是一个类（Class），只不过它们继承了 Throwable，用来表示程序运行过程中出现的问题。
    // 因为异常也是类，所以异常之间存在继承关系：
    // Exception（父类异常，范围最大）
    //     ↓
    // IOException（父类异常，范围中等）
    //     ↓
    // FileNotFoundException（子类异常，范围更小、更具体）
    //  - 写 throws IOException             相同，允许
    //  - 写 throws FileNotFoundException   子类异常，允许（比IOException更小）
    //  - 写 throws Exception               父类异常，报错（比IOException更大，违反规则）
    //  - 什么都不throws                     也允许（相当于范围最小，一点都不抛）
//    public void method1() throws IOException {
    public void method1() throws FileNotFoundException {
    // 报错
//    public void method1() throws Exception {
    }

    // ① 编译时异常(如IOException)：父类method2没throws任何编译时异常，
    //    所以子类重写时也【不能】throws编译时异常（否则违反"不能比父类大"，父类是0，子类不能更大）——这是硬性"不能"，会编译报错。
    // ② 运行时异常(RuntimeException及其子类，如NullPointerException)：
    //    子类重写时 throws RuntimeException 是允许的，写了不报错。
    //    但因为运行时异常本来就"可以不声明、也不强制处理"，所以你写不写这个throws对编译、对调用者都没实质影响——即"写不写无所谓"。
    @Override
    public void method2() {
    }

    // ① 这仍然是重写。方法重写要求：方法名、参数列表必须完全相同；
    //    而返回值类型允许"父类返回引用类型时，子类返回它的子类"(叫协变返回类型)。
    //    Number是父类、Integer是它的子类，所以把返回类型改成Integer，依然算合法的重写。
    // ② 为什么能这么改：道理同上——重写时能"缩小"不能"放大"。返回Integer比返回Number更具体(更小)，安全。
    //    反过来若父类返回Integer、子类想返回Number(更大)就会报错。
    @Override
    public Integer method3() {
        return null;
    }
}
