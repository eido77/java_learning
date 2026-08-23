package chapter10;

public class ThreadMethodAndLifeCycleExer {
}

/**
 * 新年倒计时
 * 模拟新年倒计时，每隔1秒输出一个数字，依次输出10,9,8......1，最后输出：新年快乐！
 */
/*
 * 练习 Thread.sleep(long) 的用法：
 * 让【当前执行 sleep 的线程】暂停指定毫秒数，进入 TIMED_WAITING（计时等待）状态，
 * 时间到后自动回到就绪状态。sleep 是静态方法，作用对象永远是"当前线程"。
 */
class HappyNewYear {
    public static void main(String[] args) {
        // 从 10 递减到 0；i==0 时不打印数字，而是打印祝福语，所以循环到 >=0 而不是 >=1
        for (int i = 10; i >= 0; i--) {
            // 先睡 1 秒再打印，保证"每隔 1 秒才出现一个数字"的节奏；
            // InterruptedException 是受检异常，sleep 必须处理
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // i>0 时输出倒计时数字，i==0 时输出祝福语，用一个循环同时完成倒数和收尾
            if (i > 0) {
                System.out.println(i);
            } else {
                System.out.println("Happy New Year!");
            }
        }
    }
}

/**
 * 关于Thread.sleep()方法的一个面试题：如下的代码中sleep()执行后，到底是哪个线程进入阻塞状态了呢？
 */
/*
 * 阻塞的是【main 线程】，不是 t 线程！
 *
 * public static void sleep(long millis, int nanos)
 * 原因：sleep() 是 Thread 的【静态方法】，虽然写成 t.sleep(...)，
 * 但静态方法调用与对象无关，编译期实际等价于 Thread.sleep(...)，
 * 作用对象是"执行这行代码的当前线程"，也就是 main 线程。
 * 所以子线程 t 照常全速运行，而 main 线程睡 5 秒后才打印 hello World。
 * 结论：静态方法不要用"对象.方法()"的形式调用，容易产生误导。
 */
class ThreadTest {
    public static void main(String[] args) {
        // 创建并配置子线程，命名便于在输出中区分是哪个线程
        // 创建线程对象
        MyThread t = new MyThread();
        t.setName("线程1");
        t.start();

        // 调用sleep方法
        // 这种写法虽能编译，但极具误导性 —— 看似让 t 睡，实则让 main 睡。
        try {
            t.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 因为睡的是 main 线程，所以这句要等 5 秒后才执行
        System.out.println("hello World!");
    }
}

class MyThread extends Thread {
    // 子线程任务体：连续打印 0~9999，用来观察它与 main 线程"并发"执行、互不干扰
    public void run() {
        for (int i = 0; i < 10000; i++) {
            // Thread.currentThread().getName() 拿到的是"正在跑这段代码的线程名"，此处即"线程1"
            System.out.println(Thread.currentThread().getName() + "--->" + i);
        }
    }
}
