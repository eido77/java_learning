package chapter09;

public class ThrowTest {
    /*
    1. 为什么需要手动抛出异常对象？
    当出现"语法没错、但业务逻辑不符合要求"的情况时（例如给id赋一个负数），
       编译器和JVM不会自动报错，此时需要我们主动 throw 一个异常来中断并提示。
    2. 如何理解"自动 vs 手动"抛出异常对象？
    过程1：“抛”
        "自动抛"：程序执行中一旦出现异常(如1/0)，会在出错处自动 new 出对应异常对象并抛出。
        "手动抛"：不满足指定条件时，我们主动用 "throw + 异常类的对象" 抛出异常对象。
    过程2：“抓”
        狭义上讲：try-catch 捕获异常，并处理。
        广义上讲：把“抓”理解为“处理”。对应异常处理的两种方式：① try-catch-finally ② throws
    3. 如何实现手动抛出异常？
    在方法内部，当满足指定(异常)条件时，使用 "throw 异常类的对象" 抛出。
       注意：
         - throw 后面跟的是异常"对象"(new 出来的)，不是异常"类"。
         - 若抛出的是"编译时异常"(如 Exception)，则该方法必须用 throws 声明，
           或在方法内用 try-catch 处理，否则编译不通过。
         - 若抛出的是"运行时异常"(如 RuntimeException)，则可以不做任何声明，编译通过。
    4. 注意点：throw 语句一旦执行，本方法后面的代码不再执行(相当于return的中断效果)；
       另外，紧跟在 throw 同一层的、后面的代码若"永远无法到达"，编译不通过。
    5. 面试题：throw 和 throws 的区别？ “上游排污，下游治污”
       ┌────────┬───────────────────────┬────────────────────────────┐
       │        │ throw                 │ throws                     │
       ├────────┼───────────────────────┼────────────────────────────┤
       │ 位置    │ 方法体内部             │ 方法声明处(参数列表右侧)       │
       │ 后跟    │ 一个异常"对象"         │ 一个或多个异常"类"(逗号隔开)    │
       │ 作用    │ 真正地"抛出"一个异常    │ "声明"本方法可能抛异常，       │
       │        │ (制造/排污)            │  把处理责任交给调用者(治污)    │
       └────────┴───────────────────────┴────────────────────────────┘
     */
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.register2(10);
        System.out.println(s1); // Student{id=10}
        s1.register2(-10); // RuntimeException: 输入的id非法
        // "输入的id非法" 只是我们自己写在异常对象里的提示文字(message)，不是Java关键字。
        // 这行不会执行!因为上一行 register2(-10) 抛异常后直接跳到 catch
        System.out.println(s1);

        // register1 → 演示编译时异常（Exception + throws，调用者必须 try-catch）
        // register2 → 演示运行时异常（RuntimeException，方法不用 throws，调用者可以不 try-catch）

        // ============ register1：抛 Exception(编译时异常) + 用 throws 声明 ============
        // 因为 register1 用 throws 抛出了编译时异常，所以调用它【必须】用 try-catch(或继续throws)。
        try {
            s1.register1(10);
            System.out.println(s1); // Student{id=10}
            s1.register1(-10); // Exception: 输入的id非法
            // 这行不会执行!因为上一行 register1(-10) 抛异常后直接跳到 catch
            System.out.println(s1);

            // 这里写Exception是因为throws还是throw?
            // 是因为方法内 throw 了一个 Exception 对象；throws 只是"声明"让编译通过，
            // 真正被 catch 抓到的是 throw 出来的那个对象。
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息:java.lang.Exception:输入的id非法
        }

        // 下面这行会【编译报错】,因为 register1 声明了 throws Exception(编译时异常)却没被处理
        // s1.register1(1); // 直接这样裸调用编译不通过,必须放进 try 里

        // ============ register2 的问题(重点) ============
        // register2 抛运行时异常,调用时可以不套 try-catch(编译能过)
        s1.register2(-10); // RuntimeException: 输入的id非法
        // 套 try-catch 也完全可以(合法,只是非强制)
        /*
        catch 的规则是：catch 括号里的类型，只要是抛出异常对象的"本类或父类"，就能抓住它。
        Throwable
           └── Exception
                 ├── RuntimeException  ← 运行时异常(NullPointerException等都在这下面)
                 │
                 └── IOException 等    ← 其他编译时异常
         */
        // try {
        //     s1.register2(-10);
        // } catch(RuntimeException e) {
        //     e.printStackTrace();
        // }
    }
}

class Student {
    int id;

    // register1:抛"编译时异常"Exception,所以方法【必须】用 throws Exception 声明
    public void register1(int id) throws Exception {
        if (id > 0) {
            this.id = id;
        } else {
            // 必须 throws Exception。因为 Exception 是编译时异常,方法抛出它就必须"声明抛出(throws)"
            //   或者"在本方法内 try-catch 掉",否则编译不通过。

            // 手动抛出异常类的对象
            // 为什么圆括号里能写东西?
            // 因为异常类有构造器 RuntimeException(String message),
            // 括号里的字符串是"异常的提示信息",printStackTrace()/getMessage() 时会显示它。
            // Exception 同理,也有 Exception(String message) 构造器。
            throw new Exception("输入的id非法");
        }
    }

    // register2:抛"运行时异常"RuntimeException → 方法不需要 throws,调用者也不强制 try-catch
    // (与 register1 对比:register1 是编译时异常必须处理,register2 是运行时异常可不处理)
    public void register2(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            // 手动抛出异常类的对象
            throw new RuntimeException("输入的id非法");
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                '}';
    }
}
