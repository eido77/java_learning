package chapter10;

public class DeadLockTest {
    /*
    线程的同步机制带来的问题：死锁（Dead Lock）
    1. 什么是死锁？
    不同线程分别占用对方需要的同步资源（锁）且都不释放，同时又都在等待
    对方释放自己需要的资源，于是所有线程互相僵持、永久阻塞，这就是死锁。
    死锁不会抛异常、不会给提示，程序只是“卡住”，因此编码时要主动避免。
    2. 死锁产生的四个必要条件（同时满足才会发生）：
        - 互斥条件：资源同一时刻只能被一个线程占用。
        - 占用且等待：线程持有部分资源的同时，又去申请其他被占用的资源。
        - 不可抢夺：线程已获得的资源，别人不能强行夺走，只能由其自己释放。
        - 循环等待：存在一个线程—资源的环形等待链。
    3. 如何避免死锁？（逐条破坏上面的条件）
        - 条件1（互斥）：基本无法破坏，因为互斥本就是解决线程安全的手段。
        - 条件2（占用且等待）：一次性申请全部所需资源，避免“边持有边等待”。
        - 条件3（不可抢夺）：申请不到新资源时，主动释放已占有的资源。
        - 条件4（循环等待）：给资源编号，所有线程都按序号从小到大申请，破坏环路。
     */
    /*
     * 情况一：保留 sleep(100)（大概率复现死锁）
     *   线程1 先拿到 s1 锁，然后 sleep；线程2 先拿到 s2 锁，然后 sleep。
     *   sleep 期间【不释放已持有的锁】，睡醒后线程1 想拿 s2、线程2 想拿 s1，
     *   彼此都拿不到 → 互相等待 → 死锁。此时【不会有任何输出】，程序卡死不退出。
     *   之所以说“大概率”而非“一定”：如果某个线程还没启动，另一个线程可能已经
     *   跑完拿到两把锁，但由于两处 sleep 让“各拿一把锁”几乎必然同时发生，
     *   所以实际运行几乎每次都死锁。
     *
     * 情况二：注释掉 sleep（不一定死锁，输出也不确定）
     *   没有 sleep 时，先抢到第一把锁的线程很可能一口气把第二把锁也拿到并执行完，
     *   于是不发生死锁。但输出内容【不确定】，取决于两个线程的调度顺序：
     *     - 若线程1 先完整执行完，再线程2 执行完，可能输出 ab / 12 后再 abcd / 1234；
     *     - 若线程2 先执行，则先输出 cd 相关内容，再是另一线程的结果。
     *   而且偶尔仍可能死锁（若两线程恰好各抢到一把锁）。
     *   结论：注释掉 sleep 后【既不保证不死锁，也不保证输出内容】，只是概率上更常见到正常输出。
     */
    public static void main(String[] args) {
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        // 线程1：先锁 s1 再锁 s2
        new Thread() {
            @Override
            public void run() {
                // 持有 s1 锁；此时线程2 若持有 s2，双方就埋下了死锁的种子
                synchronized (s1) {
                    s1.append("a");
                    s2.append(1);

                    try {
                        /*
                         * 关键点：sleep 只让当前线程暂停，【不会释放已持有的 s1 锁】。
                         * 这段停顿给了线程2 充分时间去拿到 s2 锁，从而稳定复现死锁。
                         */
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // 已持有 s1，此处再申请 s2；若线程2 正持有 s2，则在此永久阻塞
                    synchronized (s2) {
                        s1.append("b");
                        s2.append(2);

                        System.out.println(s1);
                        System.out.println(s2);
                    }
                }
            }
        }.start();

        /*
         * 线程2：先锁 s2 再锁 s1，与线程1 顺序【相反】。
         * 这正是触发“循环等待”的根源。
         *
         * 【避免死锁的改进建议（破坏“循环等待”条件，不改动请忽略）】
         * 让两个线程使用【相同的加锁顺序】，例如都先锁 s1 再锁 s2，即把本线程改为：
         *     synchronized (s1) {
         *         synchronized (s2) { ... }
         *     }
         * 这样按固定序号申请资源，就不会形成环，死锁自然消失。
         */
        new Thread() {
            @Override
            public void run() {
                synchronized (s2) {
                    s1.append("c");
                    s2.append(3);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // 已持有 s2，此处再申请 s1；若线程1 正持有 s1，则在此永久阻塞 → 死锁
                    synchronized (s1) {
                        s1.append("d");
                        s2.append(4);

                        System.out.println(s1);
                        System.out.println(s2);
                    }
                }
            }
        }.start();
    }
}

/*
 * 下面 A2 / B2 / DeadLock 是【另一种死锁演示】：不是锁两个 StringBuilder，
 * 而是利用【synchronized 方法】隐式地锁住对象本身（this）来制造死锁。
 * 与上面 main 里的例子本质相同（互相持锁、循环等待），只是换了一种更贴近业务的写法。
 *
 * A2 类：它的 synchronized 方法锁的是【A2 实例（this）】。
 * foo() 里先持有 A2 锁，睡一会儿后再去调用 B2 实例的 last()，
 * 而 last() 又需要 B2 锁，于是与 B2.bar() 形成交叉等待。
 */
class A2 {
    // synchronized 作用在方法上 == 锁 this（当前 A2 实例）
    public synchronized void foo(B2 b) {
        System.out.println("当前线程名: " + Thread.currentThread().getName()
                + " 进入了A实例的foo方法"); // ①
        try {
            // 持有 A2 锁期间 sleep，故意拖延，好让副线程也进入 bar 拿到 B2 锁
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("当前线程名: " + Thread.currentThread().getName()
                + " 企图调用B实例的last方法"); // ③
        // 已持有 A2 锁，这里还要去拿 B2 锁（b.last 需要 B2 锁）→ 若副线程正持有 B2，则死锁
        b.last();
    }

    // 需要 A2 锁才能进入；死锁时永远等不到调用
    public synchronized void last() {
        System.out.println("进入了A类的last方法内部");
    }
}

/*
 * B2 类：与 A2 对称。它的 synchronized 方法锁的是【B2 实例（this）】。
 * bar() 里先持有 B2 锁，睡一会儿后再去调用 A2 实例的 last()（需 A2 锁），
 * 从而与 A2.foo() 构成循环等待。
 */
class B2 {
    // synchronized 作用在方法上 == 锁 this（当前 B2 实例）
    public synchronized void bar(A2 a) {
        System.out.println("当前线程名: " + Thread.currentThread().getName()
                + " 进入了B实例的bar方法"); // ②
        try {
            // 持有 B2 锁期间 sleep，拖延以便主线程进入 foo 拿到 A2 锁
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("当前线程名: " + Thread.currentThread().getName()
                + " 企图调用A实例的last方法"); // ④
        // 已持有 B2 锁，这里还要去拿 A2 锁（a.last 需要 A2 锁）→ 若主线程正持有 A2，则死锁
        a.last();
    }

    // 需要 B2 锁才能进入；死锁时永远等不到调用
    public synchronized void last() {
        System.out.println("进入了B类的last方法内部");
    }
}

/*
 * DeadLock：驱动上面 A2/B2 死锁的示例。
 * - 主线程执行 init() → 调用 a.foo(b)，先拿 A2 锁。
 * - 副线程执行 run()  → 调用 b.bar(a)，先拿 B2 锁。
 * 两线程各持一把锁并 sleep，睡醒后互相索取对方的锁 → 死锁。
 * 现象：先打印 ①②，再打印 ③④，然后卡死；
 *      “进入了主线程之后 / 进入了副线程之后”以及两个 last 方法【都不会执行】。
 */
class DeadLock implements Runnable {
    A2 a = new A2();
    B2 b = new B2();

    public void init() {
        // 由 main 中“主线程”调用 init()，这里显式给主线程改名，便于观察输出
        Thread.currentThread().setName("主线程");
        // 调用a对象的foo方法
        // 主线程在此拿到 A2 锁，随后企图拿 B2 锁
        a.foo(b);
        // 该行位于 foo() 之后；一旦死锁，foo 永不返回，本行【不会被执行】
        System.out.println("进入了主线程之后");
    }

    public void run() {
        // 由 new Thread(dl).start() 启动的子线程执行，命名为“副线程”
        Thread.currentThread().setName("副线程");
        // 调用b对象的bar方法
        // 副线程在此拿到 B2 锁，随后企图拿 A2 锁
        b.bar(a);
        // 死锁时 bar 永不返回，本行同样【不会被执行】
        System.out.println("进入了副线程之后");
    }

    public static void main(String[] args) {
        DeadLock dl = new DeadLock();
        /*
         * 先启动副线程执行 run()（拿 B2 锁），再由 main（主线程）执行 init()（拿 A2 锁）。
         * 两者交叉持锁，制造死锁。
         */
        new Thread(dl).start();
        dl.init();
    }
}
