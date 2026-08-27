package chapter10;

public class SingletonThreadSafeTest {
    /*
    解决单例模式中的懒汉式的线程安全问题
    > 饿汉式：不存在线程安全问题。
    > 懒汉式：存在线程安全问题，（需要使用同步机制来处理）

     * 饿汉式：类一加载就创建好实例（static Bank instance = new Bank()）。
     *         类加载由 JVM 保证只执行一次且线程安全，所以天生没有并发问题。
     *
     * 懒汉式：等第一次调用 getInstance() 时才 new。
     *         若 A、B 两个线程同时判断 instance == null 都成立，
     *         就会各自 new 一个对象，导致单例被破坏（出现两个实例）。
     *         因此必须用同步机制（synchronized）来保证"只 new 一次"。
     */
}

class BankTest {
    /*
     * b1、b2 是两个静态变量，分别用来"接住"两个线程各自拿到的单例对象。
     * 用 static 的原因：下面匿名内部类（Thread 的 run 方法）要访问它们并赋值，
     * static 变量属于类，任何地方都能直接读写，最省事。
     */
    static Bank b1 = null;
    static Bank b2 = null;

    public static void main(String[] args) {
        /*
         * t1、t2 是两个线程，让它们"同时"去调用 Bank.getInstance()。
         * 目的：模拟高并发场景，看看两个线程拿到的是不是同一个对象。
         * 如果单例写得线程安全，b1 == b2 应该恒为 true。
         */
        Thread t1 = new Thread() {
            @Override
            public void run() {
                b1 = Bank.getInstance();
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                b2 = Bank.getInstance();
            }
        };

        t1.start();
        t2.start();

        /*
         * 【异常处理用什么】
         * e.printStackTrace() 只适合自己学习、调试时用，它只是把错误打到控制台，
         * 正式项目里不规范。生产环境更推荐的做法：
         *   1. 用日志框架记录：log.error("线程被中断", e);
         *   2. 或者重新抛出 / 包装成运行时异常：throw new RuntimeException(e);
         *   3. 对于 InterruptedException，规范做法通常还会恢复中断状态：
         *      Thread.currentThread().interrupt();
         */
        try {
            /*
             * join() 的作用：让"当前线程（这里是 main）"等待 t1 执行完再继续往下走。
             * 通俗说：main 在这里停住，直到 t1 跑完。
             */
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * 【加 join 之前和之后的区别】
         * 不加 join：main 线程 start() 完两个线程后会立刻往下执行打印。
         *           此时 t1、t2 可能还没跑完，b1、b2 很可能还是 null，
         *           打印结果就会出现 null，b1 == b2 的结果也不可靠。
         * 加了 join：main 会等 t1、t2 都执行完毕，b1、b2 都被正确赋值后，
         *           再打印，结果才稳定可信（能真正验证单例是否唯一）。
         */
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b1 == b2);
    }
}

class Bank {
    // 私有构造器：把 new 的权限锁死在类内部，外面无法直接 new Bank()，这是单例模式的关键前提。
    private Bank() {
    }

    /*
     * instance 是全局唯一的那个实例。
     * 建议：为了配合下面"方式3（双重检查锁）"的正确性，这里应加 volatile：
     *   private static volatile Bank instance = null;
     * 原因见下面 getInstance 处对 volatile 的详细说明。
     */
    private static Bank instance = null;

    // 实现线程安全的方式1
    // 同步监视器默认为Bank.class
    /*
     * synchronized
     * 方式1（加在方法上）：写法最简单，但锁的范围是"整个方法"。
     *          意味着每次调用 getInstance() 都要抢锁、排队，
     *          即使实例早就创建好了、本可以直接返回，也还是要同步，效率偏低。
     * 方式2/3（加代码块）：可以把锁的范围缩到最小，只锁"真正需要同步的那几行"，
     *          性能更好。方式3（双重检查）是这三种里综合最优的。
     * 结论：学习理解用方式1最直观；追求性能用方式3。
     */
//    public static synchronized Bank getInstance() {
//        if (instance == null) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            instance = new Bank();
//        }
//        return instance;
//    }

    // 实现线程安全的方式2
    /*
     * this 代表"当前对象"（某个具体实例）。但 getInstance() 是 static 静态方法，
     * 静态方法属于"类"，此时根本没有对象存在，也就没有 this 可用，编译会报错。
     * 所以静态方法里做同步，只能锁"类对象"，也就是 Bank.class。
     * this 一般用在"非静态（实例）方法"里，用来指代调用该方法的那个对象。
     *
     * Bank.class 表示 Bank 这个类在 JVM 中唯一对应的 Class 对象。
     * 一个类无论被加载多少次，它的 Class 对象全局只有一个，
     * 所以拿它当"锁"，能保证所有线程抢的是同一把锁，达到同步效果。
     * 写法就是固定的：类名.class。
     */
//    public static Bank getInstance() {
//        synchronized (Bank.class) {
//            if (instance == null) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                instance = new Bank();
//            }

    /*
     * 【return 放同步块里面 还是外面 更规范？】
     * 两种都能正确运行（因为 instance 已经赋好值）。区别：
     *   放里面：还在持有锁的状态下 return，锁会稍晚一点释放。
     *   放外面：先跳出同步块、释放锁，再 return，锁占用时间更短、更推荐。
     * 结论：推荐放"外面"，尽量缩短持锁时间。所以保留外面那个 return 即可，
     *       里面这个 return 可以删掉。
     */
//            // return是放外面还是这里的里面，哪个更规范呢？放这里还是外面？
    //            return instance;
//        }
//        return instance;
//    }

    // 实现线程安全的方式3
    public static Bank getInstance() {
        /*
         * 【为什么要有两个一模一样的 if (instance == null) 判断？】（双重检查锁 DCL）
         * 第一个 if（在 synchronized 外面）：性能优化。
         *     一旦实例已经创建好，后续调用直接命中这个 if 为 false，
         *     根本不用进 synchronized 抢锁，避免了每次都同步的开销。
         * 第二个 if（在 synchronized 里面）：保证正确性。
         *     可能有多个线程同时通过了第一个 if（那时实例还没建），
         *     它们会排队进入同步块。第一个线程 new 完后，
         *     后面线程进来必须再判断一次，发现已经不为 null，就不会重复 new。
         * 少了任何一个 if 都不行：只留外层→不安全；只留内层→退化成方式2、失去性能优化。
         *
         * volatile 加在字段上，即上面那行改成：
         *     private static volatile Bank instance = null;
         * 作用：解决双重检查锁的"指令重排序"隐患。
         * instance = new Bank() 这行其实分三步：
         *     ① 分配内存 ② 调用构造器初始化 ③ 把 instance 指向这块内存。
         * JVM 可能把 ②③ 重排成 ①③②。若线程 A 刚做完 ①③（instance 已非 null，
         * 但对象还没初始化完），此时线程 B 在外层 if 看到 instance != null，
         * 直接拿去用，就会拿到一个"半成品"对象，出错。
         * 加了 volatile 能禁止这种重排序，并保证可见性，DCL 才真正安全。
         * 所以：方式3 必须给 instance 加 volatile 才算完整正确。
         */
        if (instance == null) {
            synchronized (Bank.class) {
                if (instance == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    instance = new Bank();
                }
            }
        }
        return instance;
    }
}
