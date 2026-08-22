package chapter10;

public class MultithreadingExer {
}

class Exer_1 {
    public static void main(String[] args) {
        // 下面演示的是"匿名子类 + Runnable 目标对象"同时存在时，run() 到底跑谁
        // 规则：new Thread(target){ run(){...} } 中，如果匿名子类重写了 run()，则以匿名子类的 run() 为准，
        //      target（这里的 b）会被忽略；只有当没重写 run() 时，Thread.run() 才会去调用 target.run()
        BB b = new BB();

        new Thread(b) {
            @Override
            public void run() {
                System.out.println("CC");
            }
        }.start();
        // 情况说明（针对上面那段）：
        // 情况① 解开匿名子类的 run()（打印 CC）：输出 CC。因为子类重写了 run()，target b 被忽略
        // 情况② 不解开匿名子类的 run()（空的 {}）：输出 BB。因为没重写 run()，Thread.run() 调用了 b 的 run()

        // 上面这行：匿名子类既没重写 run()，又没传 target，所以 Thread.run() 什么都不做，无任何输出
        new Thread() {
        }.start();
    }
}

class AA extends Thread {
    @Override
    public void run() {
        System.out.println("AA");
    }
}

class BB implements Runnable {
    @Override
    public void run() {
        System.out.println("BB");
    }
}

/**
 * 思考题：判断各自调用的是哪个run()？
 */
class Exer {
    public static void main(String[] args) {
        A a = new A();
        // ① 启动线程 ② 调用Thread类的run()
        // 更准确说：start() 会另起一条新线程，由 JVM 回调 a 重写的 run()，打印"线程A的run()..."
        a.start();

        B b = new B(a);
        // 两个 start为什么现在显示 A、B、A（3 个）？
        //   两个 start 确实是两条线程，但一条线程里可以打印不止一次：
        //   · a.start()  → 线程A 打印 1 次："线程A的run()..."（这是第 1 个 A）
        //   · b.start()  → 线程B 执行它自己的 run()：先打印"线程B的run()..."（这是 B）
        //                  然后 run() 里又写了 a.run()，这是"普通方法调用"，不是新线程，
        //                  它在线程B里同步执行 a 的 run()，再打印一次"线程A的run()..."（这是第 2 个 A）
        //   所以总共 3 行：A、B、A（顺序可能因线程调度略有变化，但 B 之后紧跟的 A 一定在 B 后面）
        b.start();
    }
}

//创建线程类A
class A extends Thread {
    @Override
    public void run() {
        System.out.println("线程A的run()...");
    }
}

//创建线程类B
class B extends Thread {
    private A a;

    // 构造器中，直接传入A类对象
    // 说明：此写法只是把 a 存起来，供 B 自己的 run() 里用 a.run() 普通调用；a 不会作为线程的 target
    public B(A a) {
        this.a = a;
    }

    // 下面这个被注释掉的构造器是"另一版"，用 super(a) 把 a 当作 Runnable target 传给 Thread
    // 前提：A 同时是 Thread 的子类，也就天然是一个 Runnable（Thread 实现了 Runnable）
    //
    // 组合1：用当前的 public B(A a){ this.a=a; } + B 重写了 run()
    //   b.start() 执行 B 的 run()：打印"线程B的run()..."，再 a.run() 打印"线程A的run()..."
    //   → 配合 a.start()，总输出：A、B、A
    //
    // 组合2：改用 public B(A a){ super(a); } 且 B 不重写 run()（把下面 run 也注释掉）
    //   b.start() 走 Thread.run()，因为传了 target=a，于是调用 a.run() 打印"线程A的run()..."
    //   → 配合 a.start()，总输出：A、A（这就是"演示显示 B"那类差异的来源之一，取决于 target 是谁）
    //
    // 组合3：用 super(a) 但 B 仍然重写了 run()（run 里有 a.run()）
    //   子类重写了 run()，target 被忽略，走 B 的 run()：打印"线程B的run()..."再"线程A的run()..."
    //   → 总输出：A、B、A（和组合1一样，因为重写 run 优先级更高，super(a) 白传了）
    //
    // 结论口诀：重写了 run() 就一定走重写的；没重写才看有没有 target

//    public B(A a){
//        super(a);
//    }

    @Override
    public void run() {
        System.out.println("线程B的run()...");

        // 这里 a.run() 是"普通方法调用"，会在当前线程(B)里同步执行，不会新开线程
        a.run();
    }
}
