package chapter10;

import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    /*
    除了使用synchronized同步机制处理线程安全问题之外，还可以使用jdk5.0提供的Lock锁的方式
    1. 步骤：
    步骤1. 创建Lock的实例，需要确保多个线程共用同一个Lock实例!需要考虑将此对象声明为static final
    步骤2. 执行lock()方法，锁定对共享资源的调用
    步骤3. unlock()的调用，释放对共享数据的锁定
    2. 面试题：
    synchronized同步的方式 与Lock的对比？
       1. 释放锁的方式不同：
          - synchronized 是"隐式锁"，出了同步代码块 {} 或同步方法就自动释放，
            即使发生异常，JVM 也会帮你自动释放锁，不需要手动管。
          - Lock 是"显式锁"，必须手动 lock() 加锁、手动 unlock() 解锁。
            正因为要手动解锁，才必须把 unlock() 放进 finally，否则一旦中间抛异常，
            锁永远不会被释放，其他线程全部卡死（死锁）。这是 Lock 最容易出 bug 的点。
       2. 灵活性不同：
          - synchronized 加锁/解锁的范围被 {} 绑死，不能跨方法。
          - Lock 的 lock()/unlock() 是两个独立方法，加锁和解锁可以在不同代码位置，
            更灵活。还支持 tryLock()（尝试加锁，拿不到就返回而不是死等）、
            lockInterruptibly()（可被中断的加锁）、公平锁等 synchronized 没有的能力。
          从 JDK 6 起对 synchronized 做了偏向锁、轻量级锁等大量优化，如今两者性能差距很小。
          需要 tryLock、超时、可中断、读写分离等高级能力时才用 Lock（如 ReentrantReadWriteLock）。
     */
    public static void main(String[] args) {
        Window2 w1 = new Window2();
        Window2 w2 = new Window2();
        Window2 w3 = new Window2();

        w1.setName("窗口1");
        w2.setName("窗口2");
        w3.setName("窗口3");

        w1.start();
        w2.start();
        w3.start();
    }
}

class Window2 extends Thread {
    static int ticket = 100;
    /*
     * 1. 创建Lock的实例，需要确保多个线程共用同一个Lock实例!需要考虑将此对象声明为static final
     *
     * private static final 不是语法必须，但强烈建议
     * - static：本例用 extends Thread，w1/w2/w3 是三个不同对象。如果 lock 不是 static，
     *   三个对象各有一把自己的锁，三个线程根本锁不到一起，同步失效。必须 static 才能保证"多个线程共用同一把锁"
     *   （如果改成 implements Runnable + 一个共享实例，就可以不用 static）
     * - final：锁对象一旦创建就不该被替换，加 final 防止中途被重新赋值成另一把锁而导致同步失效。
     *
     * lock 命名是否规范、要不要写成 LOCK：
     * 阿里/Google 等主流规范里，"常量"（static final 且值不可变的基本类型/字符串等）才用全大写 LOCK。
     * 而这里 lock 虽然是 static final 引用，但它指向的是一个"有状态、会被修改内部状态"的可变对象（锁会被加/解），
     * 语义上更接近"工具字段"而非"常量"，所以业界通常仍用小写驼峰 lock，你现在的写法是规范的，不用改成 LOCK。
     *
     * 【为什么说这是"创建 Lock 的实例"，明明 new 的是 ReentrantLock？】
     * 类型关系：
     *   Lock（接口）
     *     └── ReentrantLock（实现类，implements Lock）
     * 1. Lock 是接口，只规定"锁该有哪些方法"（lock()/unlock()/tryLock() 等），
     *    自己不写实现，接口不能被 new。
     * 2. ReentrantLock 是 Lock 的实现类，真正写出了加锁/解锁的具体逻辑。
     * 3. 因为 ReentrantLock implements Lock，根据多态，一个 ReentrantLock 对象
     *    既是 ReentrantLock 类型，也是 Lock 类型。
     *
     * 【建议：更推荐用 Lock 接口类型来接收（面向接口编程）】
     *   private static final Lock lock = new ReentrantLock();
     *                        ↑ 左边用接口类型
     * 好处：本例只用到了 Lock 接口自带的 lock()/unlock()，用 Lock 接收后，
     * 将来想换别的实现类，只需改 "= new XXX()" 这一行，下面代码一个字都不用动，耦合更低。
     *
     * 例外：如果要用 ReentrantLock 独有、Lock 接口里没有的方法
     * （如 getHoldCount()、isFair()、isLocked()），就必须用 ReentrantLock 类型接收，
     * 否则编译器不认识那些方法。本例没用到，所以用 Lock 接口接收更好。
     */
    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            /*
             * 【重要 bug 建议】原来的 try 是"包住 lock() 又包住业务逻辑"的，
             * 也就是 lock.lock() 写在了 try 里面。这在本例能跑，但是不规范、有隐患：
             * 如果 lock() 本身抛异常（极少见，但理论上存在），会进入 finally 执行 unlock()，
             * 而此时锁根本没加上，unlock() 会抛 IllegalMonitorStateException，掩盖真正的异常。
             * 标准写法是：lock() 放在 try 外面、紧挨着 try，try 里只放业务逻辑：
             *     lock.lock();
             *     try {
             *         // 业务逻辑
             *     } finally {
             *         lock.unlock();
             *     }
             */
            try {
                // 2. 执行lock()方法，锁定对共享资源的调用
                // 加锁：从这里开始到 unlock() 之间的代码，同一时刻只允许一个线程进入，
                // 保证"判断票数 + 卖票 + 减票"这三步是原子的，不会被别的线程插队导致超卖/重卖。
                lock.lock();

                if (ticket > 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticket);
                    ticket--;

                } else {
                    /*
                     * 票卖完，break 跳出 while 循环。
                     * 注意：break 之后仍会正常执行 finally 里的 unlock()，锁不会泄漏，这是对的。
                     */
                    break;
                }
            } finally {
                // 3. unlock()的调用，释放对共享数据的锁定
                // 解锁必须放在 finally：无论上面是正常执行、break、还是抛异常，
                // 都能保证锁一定被释放，避免因异常导致锁没释放而使其他线程永久阻塞（死锁）。
                lock.unlock();
            }
        }
    }
}
