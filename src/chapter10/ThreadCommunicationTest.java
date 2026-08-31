package chapter10;

public class ThreadCommunicationTest {
    /*
    1. 线程间通信的理解
    当我们`需要多个线程`来共同完成一件任务，并且我们希望他们有规律的执行，那么多线程之间需要一些通信机制，
    可以协调它们的工作，以此实现多线程共同操作一份数据。
    2. 涉及到三个方法的使用：
    wait():线程一旦执行此方法，就进入等待状态。同时，会释放对同步监视器的调用
    notify():一旦执行此方法，就会唤醒被wait()的线程中优先级最高的那一个线程。（如果被wait()的多个线程的优先级相同，则
             随机唤醒一个）。被唤醒的线程从当初被wait的位置继续执行。
    notifyAll():一旦执行此方法，就会唤醒所有被wait的线程。
    3. 注意点：
    > 此三个方法的使用，必须是在同步代码块或同步方法中。
      (超纲：Lock需要配合Condition实现线程间的通信)
    > 此三个方法的调用者，必须是同步监视器。否则，会报IllegalMonitorStateException异常
    > 此三个方法声明在Object类中。
    4. 案例：
    案例1：使用两个线程打印 1-100。线程1, 线程2 交替打印
    案例2：生产者&消费者
    生产者(Productor)将产品交给店员(Clerk)，而消费者(Customer)从店员处取走产品，店员一次只能持有
    固定数量的产品(比如:20），如果生产者试图生产更多的产品，店员会叫生产者停一下，如果店中有空位放产品
    了再通知生产者继续生产；如果店中没有产品了，店员会告诉消费者等一下，如果店中有产品了再通知消费者来
    取走产品。
    5. wait() 和 sleep()的区别？
    相同点：一旦执行，当前线程都会进入阻塞状态
    不同点：
    > 声明的位置：wait():声明在Object类中
                sleep():声明在Thread类中，静态的
    > 使用的场景不同：wait():只能使用在同步代码块或同步方法中
                   sleep():可以在任何需要使用的场景
    > 使用在同步代码块或同步方法中：wait():一旦执行，会释放同步监视器
                              sleep():一旦执行，不会释放同步监视器
    > 结束阻塞的方式：wait(): 到达指定时间自动结束阻塞 或 通过被notify唤醒，结束阻塞
                   sleep(): 到达指定时间自动结束阻塞
     */
}

class PrintNumberTest2 {
    public static void main(String[] args) {
        PrintNumber2 p = new PrintNumber2();

        Thread t1 = new Thread(p, "线程1");
        Thread t2 = new Thread(p, "线程2");

        t1.start();
        t2.start();
    }
}

class PrintNumber2 implements Runnable {
    private int number = 1;

    Object lock = new Object();

    @Override
    public void run() {
        while (true) {
            /*
             * 【核心规则】wait()/notify() 的"调用者"必须和 synchronized 括号里的"那把锁"是同一个对象。
             *
             * 【为什么下面 synchronized(lock) 里直接写 notify() 会运行时报错？】
             *   因为 notify() 不写调用者时，默认是 this.notify()（省略的是 this）。
             *   而这里锁是 lock，不是 this，两者不一致 → 运行时抛 IllegalMonitorStateException。
             *   调用者必须是同步监视器，否则报 IllegalMonitorStateException。
             *
             * 【为什么编译不报错、运行才报错？】
             *   因为"锁和调用者是否一致"是运行期才知道的（monitor 是运行时状态），
             *   语法上 this.notify() 完全合法，所以编译期检查不出来。
             *
             * 【this 和 lock 哪个好？】
             *   两者都能用，只要"锁对象"和"调用者"保持一致即可。
             *   - 用 this：适合"当前对象就是天然的共享锁"的场景（本例 p 只有一个，this 就够用）。
             *   - 用 lock：更清晰、更规范，专门造一把锁，避免别人 synchronized(你的对象) 造成干扰。
             */
//            synchronized (this) {
            synchronized (lock) {
                /*
                 * notify() 的作用：唤醒另一个正卡在 wait() 上的线程，让两个线程能"交替"执行。
                 * 逻辑：本线程进来先叫醒对方 → 自己打印一个数 → 然后自己 wait() 交出锁并睡下，
                 *       如此往复，实现线程1、线程2 轮流打印。若不 notify，对方永远醒不来，会死锁。
                 * 位置：写在 if 外面是可以的；但更严谨的写法是先判断再唤醒，见下方 while 建议。
                 * 调用者：省略时默认 this.notify()。注意这与本块 synchronized(lock) 不一致（见上一条），
                 *        规范应写 lock.notify()。静态方法才用类名调用，notify 不是静态方法。
                 */
                lock.notify();

                if (number <= 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(Thread.currentThread().getName() + ":" + number);
                    number++;

                    /*
                     * wait() 加与不加：
                     * - 加 wait()：本线程打印完一个数后，主动释放锁并阻塞，让对方线程有机会拿到锁、
                     *   接着打印下一个数 → 实现"交替"打印。
                     * - 不加 wait()：当前线程会一直霸占锁，把 1-100 一口气全打完（几乎看不到交替），
                     *   另一个线程根本抢不到执行机会，就失去了"两个线程轮流"的效果。
                     */
                    try {
                        // 调用者省略时默认 this.wait()，与本块 synchronized(lock) 不一致，规范应写 lock.wait()。
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    break;
                }
            }
        }
    }
}

/**
 * 案例2：生产者&消费者
 *     生产者(Productor)将产品交给店员(Clerk)，而消费者(Customer)从店员处取走产品，店员一次只能持有
 *     固定数量的产品(比如:20），如果生产者试图生产更多的产品，店员会叫生产者停一下，如果店中有空位放产品
 *     了再通知生产者继续生产；如果店中没有产品了，店员会告诉消费者等一下，如果店中有产品了再通知消费者来
 *     取走产品。
 *
 * 共享数据就是本例的产品数量 productNum（存在 Clerk 里）。
 * 它是生产者和消费者都要读写的同一份数据，所以：
 *   - 谁是共享数据，谁就是需要用 synchronized 保护、需要用 wait/notify 协调的对象；
 *   - 它决定了"锁应该加在谁身上"——本例三个线程都通过同一个 clerk 操作，
 *     所以锁自然就是这个 clerk 对象（synchronized 方法锁的就是 clerk）。
 */
class ProducerConsumerTest {
    public static void main(String[] args) {
        /*
         * 关键点：这里只 new 了一个 clerk，然后把它同时传给生产者和消费者。
         * 正因为大家共用"同一个 clerk"，它内部的 productNum 才是被共享的那份数据，
         * 三个线程的 synchronized 才锁在同一把锁上，wait/notify 才能互相通信。
         * 如果每个线程各 new 一个 clerk，就成了各玩各的，同步和通信全部失效。
         */
        Clerk clerk = new Clerk();

        /*
         * clerk 为什么可以放进构造器括号里：
         * 因为 Producer/Consumer 定义了带 Clerk 参数的构造器（见下方 public Producer(Clerk clerk)）。
         * 这一步叫"依赖注入"：把外面创建好的同一个 clerk 交给每个线程持有，
         * 从而保证它们操作的是同一份共享数据。
         */
        Producer producer1 = new Producer(clerk);
        Consumer consumer1 = new Consumer(clerk);
        Consumer consumer2 = new Consumer(clerk);

        producer1.setName("生产者1");
        consumer1.setName("消费者1");
        consumer2.setName("消费者2");

        producer1.start();
        consumer1.start();
        consumer2.start();
    }
}

/**
 * 店员
 *
 * Clerk 是被共享的数据仓库（存 productNum，并提供加/减两个同步方法）。
 * Producer、Consumer 各自持有同一个 Clerk 的引用，通过调用它的 addProduct/minusProduct
 * 来操作同一份数据。可以理解为：一个柜台(Clerk)，多个人(线程)围着它存取货。
 */
class Clerk {
    private int productNum = 0;

    // 增加产品数量的方法
    /*
     * 加 synchronized 是因为 wait 在里面吗：
     * 1) wait/notify 规定必须在同步环境里调用，所以有 wait 就必须有 synchronized；
     * 2) 更本质的是 productNum 是共享数据，多线程同时改会有线程安全问题，本来就该加锁。
     * synchronized 方法锁的是"当前对象"，即调用它的那个 clerk。
     */
    public synchronized void addProduct() {
        if (productNum >= 20) {
            // 等待
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        productNum++;
        System.out.println(Thread.currentThread().getName() + "生产了第" + productNum + "个产品");

        // 唤醒
        /*
         * notify vs notifyAll：
         *   - notify 只随机叫醒一个等待线程；notifyAll 叫醒全部等待线程。
         *   - 本例有多个消费者，用 notify 可能叫醒"不该被叫醒"的同类线程（如生产者叫醒了另一个生产者），
         *     导致该干活的没被叫醒而卡住。所以多生产者/多消费者场景推荐用 notifyAll 更安全。
         */
//        notify();
        notifyAll();
    }

    // 减少产品数量的方法
    public synchronized void minusProduct() {
        if (productNum <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*
         * 【为什么 -- 要放打印之后？为什么和 addProduct 的 ++ 位置相反？】
         *    这其实是"打印内容想显示哪个编号"的问题，不是语法强制：
         *    - 生产：先 ++ 再打印，是想显示"生产了第几个"（新增后的编号）；
         *    - 消费：先打印再 --，是想显示"消费了第几个"（消费的是当前这个编号）。
         *    如果想两个方法风格统一，可以都"先算数值、再打印/或反过来"，
         *    但要保证打印出来的编号符合你的语义（生产显示增加后的、消费显示被拿走的）。
         *    ——所以不是"必须放下面"，而是为了让打印的编号看起来正确。
         */
//        productNum--;
        System.out.println(Thread.currentThread().getName() + "消费了第" + productNum + "个产品");
        productNum--;

        // 唤醒
//        notify();
        notifyAll();
    }
}

/**
 * 生产者
 *
 * 继承 Thread（或实现 Runnable）是让这个类具备"成为一条线程运行"的能力，
 * 从而可以重写 run()、调用 start() 独立运行。
 * 本例继承 Thread 是写法之一；也可以改成 implements Runnable（更推荐，避免单继承限制），
 * 两种都行，此处继承 Thread 只是为了写起来直观。
 */
class Producer extends Thread {
    /*
     * Clerk clerk 字段作用：
     * "引用/成员变量"，用来持有外部传进来的那个共享 clerk，
     * 好让 run() 里能通过 clerk.addProduct() 去操作共享数据。
     */
    private Clerk clerk;

    /*
     * 这是构造器，作用是把外部 new 出来的 clerk 赋值给本对象的 clerk 字段（this.clerk = clerk）。
     * 这样每个 Producer 一创建就"记住"了要操作哪个店员。
     * 左边 this.clerk 是本对象的字段，右边 clerk 是构造器参数，两者靠 this. 区分。
     */
    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("生产者开始生产产品...");

            try {
                Thread.sleep(50);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            clerk.addProduct();
        }
    }
}

/**
 * 消费者
 */
class Consumer extends Thread {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("消费者开始消费产品...");

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            clerk.minusProduct();
        }
    }
}
