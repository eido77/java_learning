package chapter10;

public class MultithreadingTest {
    /*
    1. 程序、进程和线程的区分：
    程序(program)：为完成特定任务，用某种语言编写的`一组指令的集合`。即指一段静态的代码。
    进程(process)：程序的一次执行过程，或是正在内存中运行的应用程序。程序是静态的，进程是动态的。
                  进程作为操作系统资源分配的最小单位（线程是 CPU 调度和执行的最小单位）
    线程(thread)：进程可进一步细化为线程，是程序内部的一条执行路径。
                 线程作为CPU调度和执行的最小单位
    补充：同一进程内的多个线程共享进程的内存资源（堆、方法区），
         但每个线程有自己独立的栈(virtual machine stack)和程序计数器(PC)。
         这也是为什么"多线程共享数据"会引发线程安全问题的根源。
    2. 线程调度策略
    分时调度：所有线程`轮流使用` CPU 的使用权，并且平均分配每个线程占用 CPU 的时间。
    抢占式调度：让`优先级高`的线程以`较大的概率`优先使用 CPU。如果线程的优先级相同，那么会随机选择一个(线程随机性)，Java使用的为抢占式调度。
    补充：优先级高只是"概率大"，不是"绝对优先"，所以不能靠优先级来控制执行顺序。
    3. 了解
    > 单核CPU与多核CPU
        单核CPU：同一时刻只能跑一个线程，靠"时间片轮转"快速切换，实现"并发"(假同时)
        多核CPU：多个核心同时工作，可实现真正的"并行"(真同时)
    > 并行与并发
        并发 (Concurrent)：多个任务在同一时间段内交替执行（宏观上"同时"，微观上是轮流切换）。单核也能并发。
        并行 (Parallel)：多个任务在同一时刻真正同时执行。必须多核才能真正并行。
        并发是"看起来同时"，并行是"真的同时"。单核只能并发，不能并行。
     */
}

/*
1. 线程的创建方式一：继承Thread类
1.1 步骤：
① 创建一个继承于Thread类的子类
② 重写Thread类的run() --->将此线程要执行的操作，声明在此方法体中
③ 创建当前Thread的子类的对象
④ 通过对象调用start(): 1.启动线程 2.调用当前线程的run()
1.2 例题：创建一个分线程1，用于遍历100以内的偶数
【拓展】 再创建一个分线程2，用于遍历100以内的偶数
 */

/**
 * 例题：创建一个分线程1，用于遍历100以内的偶数
 */
class EvenNumberTest {
    public static void main(String[] args) {
        // ③ 创建当前Thread的子类的对象
        PrimeNumber t1 = new PrimeNumber();
        // ④ 通过对象调用start(): 1.启动线程 2.调用当前线程的run()
        t1.start();

        // main()所在的线程执行的操作
        // t1 和 main 是两条独立执行路径，由 CPU 抢占式调度轮流分配时间片，
        // 谁抢到 CPU 谁就往下跑一段，所以两个线程的输出会交错(interleave)出现。
        // 每次抢占时机不同，故每次运行结果的交错顺序都不一样(线程随机性)。
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.print(i + "/");
            }
        }
    }
}

// ① 创建一个继承于Thread类的子类
class PrimeNumber extends Thread {
    // ② 重写Thread类的run() --->将此线程要执行的操作，声明在此方法体中
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
            }
        }
    }
}

/*
问题1：能否使用t1.run()替换t1.start()的调用，实现分线程的创建和调用? 不能！
① run() 只是一个普通方法。直接调用 t1.run()，是在【当前线程(main)】里同步执行这段代码，并不会开启新线程。
② 结果就是：整个程序始终只有一条执行路径(main)，run() 里的代码会先/顺序执行完，不存在"交错"，也谈不上并发。
③ 只有 start() 才会由 JVM 向操作系统申请创建一条新线程，新线程被调度后才自动回调 run()。所以"启动线程"和"调用run()"是两码事。

问题2：再提供一个分线程，用于100以内偶数的遍历。
不能对同一个对象 t1 再调用一次 t1.start()，因为一个 Thread 对象只对应一条线程，
start() 只能调用一次；再次调用会抛IllegalThreadStateException(线程状态不合法)。
正确做法：new 一个【新的】线程对象再 start()。示例(可直接运行)：
    PrimeNumber t2 = new PrimeNumber();  // 新对象，代表另一条线程
    t2.start();                          // 合法：这是 t2 的第一次 start
（注意：t1.start() 后不能再写 t1.start()，但 t2 是新对象所以没问题。）
 */

// 一个线程，就是程序的一条执行路径。
// 单线程：
//      main() 从头到尾只有一条执行链：main → method2 → method1，
//      整个过程没有调用任何 start() 开启新线程，所以自始至终只有一条
//      执行路径(即 main 线程)，故称单线程。方法调用只是压栈/出栈，
//      不会产生新的执行路径。
class SingleThread {
    public void method1(String str) {
        System.out.println(str);
    }

    public void method2(String str) {
        method1(str);
    }

    // main线程
    public static void main(String[] args) {
        SingleThread s = new SingleThread();
        s.method2("hello!");
    }
}

// 练习：创建两个分线程，其中一个线程遍历100以内的偶数，另一个线程遍历100以内的奇数。
class PrintNumberTest {
    public static void main(String[] args) {
        // 方式1：定义具名子类(见下方 EvenNumberPrint / OddNumberPrint)，可复用、可读性好
        EvenNumberPrint t1 = new EvenNumberPrint();
        OddNumberPrint t2 = new OddNumberPrint();

        t1.start();
        t2.start();

        // 方式2：创建Thread类的匿名子类的匿名对象
        //   适用场景：只用一次的一次性小任务、临时启动一个线程时。
        //   因为它没有类名，无法被再次 new，"想复用第二次"时不方便，此时就该改用具名类(方式1)。
        // 实际开发：早期确实常见；但现在多用 Lambda / 线程池(ExecutorService)，很少直接 new Thread。匿名类主要出现在老代码或需要重写多个方法的场景。
        /**
         * 用于打印偶数
         */
        new Thread() {
            @Override
            public void run() {
                for (int i = 1; i < 100; i++) {
                    if (i % 2 == 0) {
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                    }
                }
            }
        }.start();

        /**
         * 用于打印奇数
         */
        new Thread() {
            @Override
            public void run() {
                for (int i = 1; i < 100; i++) {
                    if (i % 2 != 0) {
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                    }
                }
            }
        }.start();

        // 方式3：使用实现Runnable接口的方式（提供了Runnable接口匿名实现类的匿名对象）
        // 函数式接口的定义是：只包含一个抽象方法的接口。
        // Lambda 其实就是把匿名内部类中那些"编译器能推断出来的废话"全部省略了：
        //      省略的部分	                为什么能省
        //      new Runnable()	            Thread 构造器参数类型就是 Runnable，编译器知道要创建的是它
        //      @Override public void run	Runnable 只有一个抽象方法，不会有歧义，必然是 run
        // 注意事项：
        // 只有函数式接口才能用 Lambda。如果接口有两个或以上抽象方法，编译器无法推断你实现的是哪个，就不能用 Lambda，只能用匿名内部类。
        // @FunctionalInterface 注解不是必须的，但加上后编译器会帮你检查该接口是否真的只有一个抽象方法，防止误改。
        // 两种写法运行效果完全一样，Lambda 只是语法糖，让代码更简洁。
        // 建议：Runnable 是函数式接口，方式3可用 Lambda 简化，更简洁：
        //   new Thread(() -> {
        //       for (int i = 1; i < 100; i++)
        //           if (i % 2 == 0)
        //               System.out.println(Thread.currentThread().getName() + ":" + i);
        //   }).start();
        // 偶数
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 100; i++) {
                    if (i % 2 == 0) {
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                    }
                }
            }
        }).start();

        // 奇数
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 100; i++) {
                    if (i % 2 != 0) {
                        System.out.println(Thread.currentThread().getName() + ":" + i);
                    }
                }
            }
        }).start();
    }
}

/**
 * 用于打印偶数
 */
class EvenNumberPrint extends Thread {
    @Override
    public void run() {
        for (int i = 1; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }
}

/**
 * 用于打印奇数
 */
class OddNumberPrint extends Thread {
    @Override
    public void run() {
        for (int i = 1; i < 100; i++) {
            if (i % 2 != 0) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }
}


//============================================================
/*
2. 线程的创建方式二：实现Runnable接口
2.1 步骤：
① 创建一个实现Runnable接口的类
② 实现接口中的run() -->将此线程要执行的操作，声明在此方法体中
③ 创建当前实现类的对象
④ 将此对象作为参数传递到Thread类的构造器中，创建Thread类的实例
⑤ Thread类的实例调用start():1.启动线程 2.调用当前线程的run()
2.2 例题：创建分线程遍历100以内的偶数
 */
class EvenNumberTest2 {
    public static void main(String[] args) {
        // ③ 创建当前实现类的对象
        EvenNumberPrint2 p = new EvenNumberPrint2();
        // ④ 将此对象作为参数传递到Thread类的构造器中，创建Thread类的实例
        // Thread 的构造器形参类型是 Runnable(接口)，而这里实际传入的是它的实现类对象 EvenNumberPrint2，
        // 即"父类型(接口)引用指向子类型(实现类)对象"——这就是多态。
        // Thread 内部只按 Runnable 接口调用 run()，运行时才真正执行到 EvenNumberPrint2 的 run()(动态绑定)。
        Thread t1 = new Thread(p);
        // ⑤ Thread类的实例调用start():1.启动线程 2.调用当前线程的run()
        t1.start();

        // main()对应的主线程
        for (int i = 1; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }

        // 拓展：再创建一个线程，用于遍历100以内的偶数
        // 这里"不能再 start 的限制"是针对 Thread 对象，不是 Runnable 对象。
        // p 只是被共享的"任务(要执行的代码逻辑)"，本身不代表线程；
        // 真正代表线程的是 t1、t2 这两个【不同的】Thread 对象。
        // 所以同一个 p 可以交给多个 Thread 复用，这也正是 Runnable"更适合处理共享数据"的体现。
        Thread t2 = new Thread(p);
        t2.start();
    }
}

// ① 创建一个实现Runnable接口的类
class EvenNumberPrint2 implements Runnable {
    // ② 实现接口中的run() -->将此线程要执行的操作，声明在此方法体中
    @Override
    public void run() {
        for (int i = 1; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }
}

/*
3. 对比两种方式？
   共同点：① 启动线程，使用的都是Thread类中定义的start()
         ② 创建的线程对象，都是Thread类或其子类的实例。
   不同点：一个是类的继承，一个是接口的实现。
   建议：建议使用实现Runnable接口的方式。
        Runnable方式的好处：① 实现的方式，避免的类的单继承的局限性
                          ② 更适合处理有共享数据的问题
                          ③ 实现了代码和数据的分离
   联系：public class Thread implements Runnable （代理模式）
   补充：更现代的做法是用线程池(ExecutorService) + Runnable/Callable，
        避免频繁手动 new Thread，能复用线程、便于统一管理，实际开发首选。
 */
