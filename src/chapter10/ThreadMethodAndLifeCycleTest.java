package chapter10;

public class ThreadMethodAndLifeCycleTest {
    /*
    一、线程的常用结构
    1. 线程中的构造器
    - public Thread() :分配一个新的线程对象。
    - public Thread(String name) :分配一个指定名字的新的线程对象。
    - public Thread(Runnable target) :指定创建线程的目标对象，它实现了Runnable接口中的run方法
    - public Thread(Runnable target,String name) :分配一个带有指定目标新的线程对象并指定名字。
    2.线程中的常用方法：
    > start():①启动线程 ②调用线程的run()
    > run():将线程要执行的操作，声明在run()中。
    > currentThread():获取当前执行代码对应的线程
    > getName(): 获取线程名
    > setName(): 设置线程名
    > sleep(long millis):静态方法，调用时，可以使得当前线程睡眠指定的毫秒数
    > yield():静态方法，一旦执行此方法，就释放CPU的执行权
    > join(): 在线程a中通过线程b调用join()，意味着线程a进入阻塞状态，直到线程b执行结束，线程a才结束阻塞状态，继续执行。
    > isAlive():判断当前线程是否存活
    过时方法：
    > stop():强行结束一个线程的执行，直接进入死亡状态。不建议使用
    > void suspend() / void resume() :可能造成死锁，所以也不建议使用
    3. 线程的优先级：
    getPriority():获取线程的优先级
    setPriority():设置线程的优先级。范围[1,10]

    Thread类内部声明的三个常量：
    - MAX_PRIORITY（10）：最高优先级
    - MIN_PRIORITY（1）：最低优先级
    - NORM_PRIORITY（5）：普通优先级，默认情况下main线程具有普通优先级。
    补充：优先级只是"概率提示"，高优先级线程被调度到的概率更大，
    但不保证一定先执行、更不保证执行顺序，最终由操作系统调度器决定。
    二、线程的生命周期
    线程的生命周期-jdk5之前.png（5状态）：新建 -> 就绪 -> 运行 -> 死亡，外加"阻塞"临时态。
    线程的生命周期-jdk5之后.png（即 Thread.State 枚举定义的 6 个状态，就是 public enum State，定义在 Thread 类内部）
       - NEW           新建：创建了线程对象，还没调 start()
       - RUNNABLE      可运行：合并了旧模型的"就绪+运行"两态（JVM 把它们合并了，交给 OS 调度）
       - BLOCKED       锁阻塞：抢 synchronized 锁失败，被挡在同步块外
       - WAITING       无限等待：wait()/join()/park()（无超时），须被唤醒
       - TIMED_WAITING 计时等待：sleep(n)/wait(n)/join(n)，时间到自己醒
       - TERMINATED    死亡：run正常结束 / 抛未捕获异常 / stop()
    记忆重点：旧"就绪+运行"=新 RUNNABLE；旧笼统的"阻塞"被拆成 BLOCKED / WAITING / TIMED_WAITING 三种。
            可用 thread.getState() 获取当前状态枚举值。
     */
}

class PrintNumber1 extends Thread {
    public PrintNumber1() {

    }

    public PrintNumber1(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            // 为什么sleep在run里面只能try- catch，不能throws？
            // 答：是【方法重写】的规则限制，不是sleep本身的问题。
            //   父类 Thread.run() 签名为 public void run()，没有 throws；
            //   重写规则要求：子类抛出的受检异常不能比父类更宽，父类没抛，子类一个受检异常都不能 throws。
            //   而 sleep 会抛受检异常InterruptedException，所以只能 try-catch。
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" +
                        Thread.currentThread().getPriority() + ":" + i);
            }

            // yield() 不一定切线程！它只是"提示"调度器我愿意让出CPU，调度器可以不理会，甚至立刻又切回当前线程。
            // 这与"运行main方法"无关：只要 t1 和主线程同时在跑就是并发，谁先谁后由调度器决定，每次运行结果都可能不同（多线程随机性）。
            if (i % 20 == 0) {
                Thread.yield();
            }
        }
    }
}

class EvenNumberTest3 {
    public static void main(String[] args) {
        PrintNumber1 t1 = new PrintNumber1("线程1");
        t1.setName("子线程1");
        t1.setPriority(Thread.MIN_PRIORITY);
        t1.start();

        // Thread.currentThread() = "正在执行这行代码的线程"。
        // 这几行在main里执行，执行main的就是主线程，所以改的是主线程。
        Thread.currentThread().setName("主线程");
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" +
                        Thread.currentThread().getPriority() + ":" + i);
            }

//            if (i == 20) {
//                try {
                    // join典型场景 = 主线程要等子线程算完拿到结果再往下走。
                    // 下面这段效果：主线程打印到20时卡住，等 t1 把1 ~100 全跑完，
                    // 主线程才继续打印 22 ~100（即让 t1 先跑完再接着跑主线程）
//                    t1.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        // 主线程用 Thread.currentThread().isAlive()，
        // 但在main里调它永远返回true（执行这行的主线程当然活着），
        // 所以对主线程调 isAlive 基本没意义。
//        System.out.println("子线程1是否存活？" + t1.isAlive());
    }
}
