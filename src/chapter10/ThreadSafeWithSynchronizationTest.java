package chapter10;

public class ThreadSafeWithSynchronizationTest {
    /*
    线程的安全问题与线程的同步机制
    1. 多线程卖票出现的问题：重票（同一张票卖多次）和错票（出现 0、-1 等非法票号）
    2. 原因：
        线程 A 操作 ticketCount 的过程尚未结束（比如刚判断完 >0 还没 --），
        CPU 就切换到线程 B，B 也进来读到了同样的值，于是同一张票被卖两次。
    3. 解决思路：保证线程 A 操作 ticketCount 期间，其它线程必须等待，
               直到 A 操作完释放锁，其它线程才能进来。即"把判断 + 售出"变成一个不可打断的整体（原子操作）。
    原子操作（Atomic Operation）指的是：一个操作要么全部完成，要么完全不执行，中间不会被其他线程打断。
    4. Java是如何解决线程的安全问题办法：线程同步机制
    方式1：同步代码块
    synchronized (同步监视器) {
        // 需要被同步的代码
    }
    说明：
        > 需要被同步的代码，即为操作共享数据的代码。
        > 共享数据：多个线程都要操作的数据。如：ticketCount
        > 需要被同步的代码，在被synchronized包裹以后，就使得一个线程在操作这些代码的过程中，其它线程必须等待。
        > 同步监视器（俗称"锁"）：谁抢到锁谁才能执行同步代码，执行完才释放。
        > 锁可以是任意类的对象，但【多个线程必须共用同一把锁】，否则等于没锁。
    注意：
        - 实现 Runnable 方式：因为多线程共用同一个 Runnable 对象，可用 this 当锁。
        - 继承 Thread 方式：this 指向不同的 Thread 对象（w1/w2/w3），不唯一，
        - 所以要慎用 this，可用"当前类.class"（如 Window.class，全程序唯一）。
    方式2：同步方法
    说明：
        - 若操作共享数据的代码正好完整地在一个方法里，可直接给方法加 synchronized。
        - 非静态同步方法：默认锁是 this。
        - 静态同步方法：默认锁是"当前类.class"。
    5. synchronized好处：解决了线程的安全问题。
                   弊端：同步部分变成串行执行，牺牲了并发性能。
     */
}

/*
 * 例题：开启三个窗口售票，总票数 100 张，分别用两种方式（同步代码块 / 同步方法）实现。
 */
/**
 * 使用实现Runnable接口的方式，实现卖票 ---> 存在线程安全问题的
 */
class SaleTicket implements Runnable {
    int ticketCount = 100;

    // 方式1：使用同步代码块解决上述卖票中的线程安全问题。
    Object lock = new Object();

    @Override
    public void run() {
        /*
         * 不能放这里。
         * 三个 Thread 共用同一个 SaleTicket 对象，成员变量 ticketCount 本来就只有一份（这是对的）。
         * 但如果把 int ticketCount = 100 写成 run() 里的【局部变量】，
         * 那每个线程各跑一次 run()，各自栈里都有一份 100，三个线程互不相干，
         * 结果就是每个窗口都独立卖 100 张，总共卖 300 张 —— 共享就失效了。
         * 所以共享数据必须是成员变量（放在类里），不能是 run() 内的局部变量。
         */
//        int ticketCount = 100;

        /*
         * 若把整个 while 包进 synchronized，则第一个抢到锁的线程会一口气把 100 张全卖光才释放锁，
         * 另外两个窗口全程干等，等于"只有一个窗口在卖"，多线程没意义了。
         * 正确做法：synchronized 只包住 while 内部"判断+售票"这一小段，
         * 每卖一张就释放一次锁，让其它窗口有机会抢到。
         */
//        synchronized (lock) {
        while (true) {
            // option + cmd + T：包围代码（Surround With）
            /*
             * 锁必须唯一：
             * synchronized 的本质是"抢同一把锁"。只有争抢同一个对象，
             * 拿不到的线程才会被挡在外面等待。如果每个线程各拿各的锁，
             * 就没人跟谁竞争，等于锁形同虚设，安全问题依旧。
             * 这里 lock 是成员变量、三个线程共用一个 SaleTicket 对象，所以 lock 唯一。
             *
             * 为什么几乎总是窗口1：
             * 一个线程刚释放锁，因为它还在 CPU 上、切换有成本，
             * 往往下一瞬间又是它自己重新抢到锁（锁的"偏向/重入惯性"现象），
             * 所以看起来一直是窗口1。这属于正常调度现象，不是 bug，输出顺序本就不保证公平。
             *
             * Runnable 方式常用 this（够用且简洁）；
             * this 能直接用，是因为 Java 里每个对象天生自带一把内置锁，this 就是"当前对象"，直接可锁。
             * 在本例（Runnable 方式）中：三个线程共用同一个 SaleTicket 对象，所以指向唯一对象 —— 能正确当锁。
             *
             * 本例 this 就是那个唯一的 ticket 对象，所以能用。
             * 但如果你 new 了两个 SaleTicket 分给不同线程，this 就不唯一了，各锁各的，失效。
             * 规范/稳妥做法：要么保证共用一个对象再用 this；
             * 要么改用"全局唯一的锁"，比如 static 锁对象 或 类.class。
             */
            synchronized (lock) {
                if (ticketCount > 0) {
                    /*
                     * sleep 在这里【纯粹是为了放大线程安全问题、方便肉眼观察】，业务上并不需要它。
                     * 它人为拉长"判断完到 -- 之间"的时间窗口，让线程切换更容易发生，从而更容易暴露重票/错票。
                     * 注意：现在 sleep 在 synchronized 内部，锁没释放，所以【此时不会再出现重票/错票】——
                     * 你现在看到的"顺序错乱"只是打印顺序不固定（正常），不是数据错误。
                     * 若你把 sleep 放到 synchronized 外面，才会重新出现真正的重票/负数票。
                     */
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    /*
                     * ticketCount-- 能不能挪进 println 里写:
                     * 可以，写成 ... + ticketCount-- 是合法的（后缀--先取值再自减），效果一样。
                     * 但不推荐：把自减藏进打印语句里可读性差、容易看漏，分开写更清晰。
                     */
                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
                    ticketCount--;
                } else {
                    /*
                     * break 写在 else 里，是因为只有"票卖完了(ticketCount<=0)"才该停；
                     * 若不写 else 直接在 if 外写 break，则每卖一张都会立刻 break，只能卖一张。
                     * 所以必须放在 else 分支。若嫌 if/else 嵌套深，也可写成 if(ticketCount<=0) break;
                     */
                    break;
                }
            }

            // 方式2：使用同步方法解决实现Runnable接口的线程安全问题。
            /*
             * 严重问题：不该在这里又调用 show()！
             * 现在每次循环先在同步代码块里卖一张，出来又调用 show() 再卖一张，
             * 相当于一次循环卖两张、两套逻辑打架，这正是你"四种方式跑不通"的根源。
             * 建议：一个类只演示一种方式。这里应删掉 show() 调用。
             */
//            show();
        }
    }

    /*
     * 给方法加 synchronized 就是"同步方法"，作用同样是保证同一时刻只有一个线程能进这个方法，解决安全问题。
     * 非静态同步方法的锁【默认且固定就是 this】，不能改。本例 this 就是那个唯一的 ticket 对象，是唯一的，所以安全。
     */
    public synchronized void show() {
        if (ticketCount > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
            ticketCount--;
        } else {
            // 方法里没有循环，break 是语法错误。
//            break;
            return;
        }
    }
}

class WindowsTest {
    public static void main(String[] args) {
        SaleTicket ticket = new SaleTicket();

        Thread t1 = new Thread(ticket);
        Thread t2 = new Thread(ticket);
        Thread t3 = new Thread(ticket);

        t1.setName("窗口1");
        t2.setName("窗口2");
        t3.setName("窗口3");

        t1.start();
        t2.start();
        t3.start();
    }
}

/**
 * 使用继承Thread类的方式，实现卖票
 */
class Window extends Thread {
    /*
     * 为什么必须加 static:
     * 继承 Thread 方式是 new 了 w1/w2/w3 三个不同对象。
     * 若 ticketCount 是普通成员变量，则每个对象各有一份 100，三个窗口各卖各的 100 张，
     * 数据根本没共享 —— 所以会看到大量"重复"的票号（其实是各卖各的）。
     * 加 static 后，ticketCount 属于类、全程序只有一份，三个对象共享，才是真正的共享数据。
     */
//    int ticketCount = 100;
    static int ticketCount = 100;

    /*
     * lock 为什么也要 static:
     * 同理，锁必须唯一。普通成员 lock 会随 w1/w2/w3 各生成一个，三把不同的锁 = 没锁住。
     * 加 static 后 lock 全程序唯一，三个对象共用一把锁，才有效。
     * 结论：继承 Thread 方式里，共享数据 和 锁 都得是 static（或用 类.class 当锁）。
     */
    //    Object lock = new Object();
    static Object lock = new Object();

    @Override
    public void run() {
        while (true) {
            // 方式1：使用同步代码块的方式解决线程安全问题。
            /*
             * this 为什么是 w1/w2/w3:
             * this 永远指"当前正在执行的对象"。这里三个线程分别是 w1/w2/w3 三个不同对象，
             * 各自的 this 各不相同，等于三把不同的锁 —— 锁不住，所以继承 Thread 方式【不能用 this】。
             */
//            synchronized (this) {

//            synchronized (lock) {

            /*
             * Window.class 是"Window 类对应的那个 Class 对象"，每个类被加载后在 JVM 中只有唯一一个 Class 对象，
             * 天然全局唯一，非常适合当锁。它不是某个 .class 文件，而是运行时代表这个类的对象。
             * Class clz = Window.class; 只是把它赋给变量，本质一样。直接写 synchronized(Window.class) 就够规范。
             * 继承 Thread 方式推荐用它，因为不用额外声明 static 锁字段，且与静态同步方法的锁一致。
             */
            synchronized (Window.class) {
                if (ticketCount > 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
                    ticketCount--;
                } else {
                    break;
                }
            }

            // 方式2：使用同步方法解决继承Thread类中的线程安全问题。
            // 同上：不该在这里又调用 show()，一次循环会卖两张。
//            show();
        }
    }

    /*
     * 1) 不加 synchronized → 线程不安全（重票错票）。
     * 2) 只加 synchronized（非静态同步方法）→ 锁默认是 this，
     *    而本例 this 有三个（w1/w2/w3），三把锁锁不住 → 仍然线程不安全。
     * 3) 方式1(同步代码块) 和 方式2(同步方法) 本质相同，都是加锁；
     *    区别是同步方法把"整个方法"当作同步范围、锁固定（this 或 类.class），
     *    同步代码块能自定义锁、范围更灵活（只锁一小段）。
     * 4) 正确解法：把 show() 改成【静态同步方法】public static synchronized void show()，
     *    此时锁是 Window.class（全局唯一）→ 线程安全。见下方修复。
     */

    public static synchronized void show() {
        if (ticketCount > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
            ticketCount--;
        } else {
            // break; // 错误：方法内无循环，不能 break
            return;
        }
    }
}

class WindowTest2 {
    public static void main(String[] args) {
        Window w1 = new Window();
        Window w2 = new Window();
        Window w3 = new Window();

        w1.setName("窗口1");
        w2.setName("窗口2");
        w3.setName("窗口3");

        w1.start();
        w2.start();
        w3.start();
    }
}

/*
 * ============================================================
 * 【四种方式各自独立】的清晰示范。
 * 每个类只演示一种组合，互不干扰，都能独立跑通、都线程安全。
 *
 *   方式A：Runnable + 同步代码块
 *   方式B：Runnable + 同步方法
 *   方式C：Thread   + 同步代码块（锁用 类.class）
 *   方式D：Thread   + 同步方法（静态同步方法，锁 = 类.class）
 *
 * 【四种方式的优缺点与实际选型】
 *       方式         	锁	     优点                      	缺点
 * A Runnable+同步代码块	this	 灵活（可只锁一小段）、共享天然	需自己控制锁范围
 * B Runnable+同步方法	this	 写法简洁                    锁整个方法、粒度粗
 * C Thread+同步代码块	类.class  灵活	                    数据/锁都得 static，不够优雅
 * D Thread+同步方法	    类.class  简洁                      	必须 static，粒度粗
 *
 * Runnable 方式整体优于继承 Thread 方式，这跟同步无关，而是设计原则：Runnable 把"任务"和"线程"解耦，天然共享同一份数据，还能实现多接口、避免单继承限制。所以 A/B 优于 C/D。
 * 同步代码块 vs 同步方法：同步代码块粒度更细（只锁真正需要保护的几行），并发性能更好；同步方法写法简洁但锁住整个方法。实际中 优先同步代码块。
 * 综合最推荐：方式 A（Runnable + 同步代码块），其中锁如果追求严谨用 private final Object lock = new Object(); 而非 this（防止外部误用 synchronized(ticketObj) 干扰）。
 * 真实工程里，这几种"手写 synchronized"都很少直接用。实际会用：
 *      java.util.concurrent.atomic.AtomicInteger（本例这种计数场景最合适，无锁、高性能）；
 *      ReentrantLock（需要更灵活的加锁/超时/公平锁时）；
 *      线程池 ExecutorService 管理线程，而不是自己 new Thread。
 *  synchronized 依然常用，但通常用在"锁一小段业务逻辑"上，且现代 JVM 对它做了大量优化，性能已不差。
 * ============================================================
 */

/**
 * 方式A：实现 Runnable + 同步代码块。
 * 锁用 this（三个线程共用同一个对象，this 唯一，安全）。
 */
class SaleA implements Runnable {
    private int ticketCount = 100;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (ticketCount > 0) {
                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
                    ticketCount--;
                } else {
                    break;
                }
            }
        }
    }
}

/**
 * 方式B：实现 Runnable + 同步方法。
 * 把"判断+售票"完整放进一个非静态同步方法里，锁默认 this（唯一，安全）。
 * 注意：方法里没有 while，循环放在 run() 里；方法返回 boolean 告诉外层是否还有票。
 */
class SaleB implements Runnable {
    private int ticketCount = 100;

    @Override
    public void run() {
        while (sale()) {
            // 循环调用同步方法，直到卖完
        }
    }

    private synchronized boolean sale() {
        if (ticketCount > 0) {
            System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
            ticketCount--;
            return true;
        } else {
            return false;
        }
    }
}

/**
 * 方式C：继承 Thread + 同步代码块。
 * 共享数据用 static；锁用 Window 对应的 类.class（全局唯一，安全）。
 */
class WindowC extends Thread {
    private static int ticketCount = 100;

    @Override
    public void run() {
        while (true) {
            synchronized (WindowC.class) {
                if (ticketCount > 0) {
                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
                    ticketCount--;
                } else {
                    break;
                }
            }
        }
    }
}

/**
 * 方式D：继承 Thread + 同步方法。
 * 必须用【静态同步方法】，锁才是 类.class（唯一）；若用非静态，锁是各自的 this，锁不住。
 */
class WindowD extends Thread {
    private static int ticketCount = 100;

    @Override
    public void run() {
        while (sale()) {
            // 循环调用静态同步方法，直到卖完
        }
    }

    private static synchronized boolean sale() {
        if (ticketCount > 0) {
            System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticketCount);
            ticketCount--;
            return true;
        } else {
            return false;
        }
    }
}

/**
 * 统一测试类：四种方式可分别取消注释单独运行观察。
 * （建议一次只跑一种，避免四组输出混在一起看不清。）
 */
class AllWaysTest {
    public static void main(String[] args) {
        // ---- 方式A：Runnable + 同步代码块 ----
        SaleA a = new SaleA();
        new Thread(a, "A窗口1").start();
        new Thread(a, "A窗口2").start();
        new Thread(a, "A窗口3").start();

        // ---- 方式B：Runnable + 同步方法 ----
//        SaleB b = new SaleB();
//        new Thread(b, "B窗口1").start();
//        new Thread(b, "B窗口2").start();
//        new Thread(b, "B窗口3").start();

        // ---- 方式C：Thread + 同步代码块 ----
//        WindowC c1 = new WindowC(); c1.setName("C窗口1");
//        WindowC c2 = new WindowC(); c2.setName("C窗口2");
//        WindowC c3 = new WindowC(); c3.setName("C窗口3");
//        c1.start(); c2.start(); c3.start();

        // ---- 方式D：Thread + 同步方法 ----
//        WindowD d1 = new WindowD(); d1.setName("D窗口1");
//        WindowD d2 = new WindowD(); d2.setName("D窗口2");
//        WindowD d3 = new WindowD(); d3.setName("D窗口3");
//        d1.start(); d2.start(); d3.start();
    }
}
