package chapter10;

public class AccountTest {
    /*
    银行有一个账户。
    有两个储户分别向同一个账户存3000元，每次存1000，存3次。每次存完打印账户余额。
    问题：该程序是否有安全问题，如果有，如何解决？
    【提示】
    1，明确哪些代码是多线程运行代码，须写入run()方法
    2，明确什么是共享数据。
    3，明确多线程运行代码中哪些语句是操作共享数据的。

     * 【整体讲解】这个文件模拟"两个人往同一个账户存钱"。
     * - AccountTest：程序入口（main 在这里），负责创建对象、启动线程。
     * - Account：账户类，持有余额 balance，提供存钱方法 deposit。
     * - Customer：储户类，继承 Thread，代表一个"会自己跑起来"的储户。
     * 运行流程：main 里创建 1 个共享账户 account，再创建 2 个储户线程，
     * 两个储户共用同一个 account，然后各自 start() 并发地往里存钱。
     */
    public static void main(String[] args) {
        // 只 new 一个 Account，这是"共享数据" —— 两个储户操作的是同一个账户对象
        Account account = new Account();

        // 两个储户都传入同一个 account，因此它们共享这份余额数据
        Customer customer1 = new Customer(account, "甲");
        Customer customer2 = new Customer(account, "乙");

        customer1.start();
        customer2.start();
    }
}

// 账户
// Account：账户类，封装余额 balance，对外只暴露 deposit 方法来修改余额
class Account {
    private double balance; // 余额

    /*
     * synchronized 用在实例方法上时，锁对象就是 this。
     * 这里的 this 指的就是"调用该方法的那个 Account 对象"，也就是 account。
     *
     * this 本题里唯一，因为全程只 new 了一个 account，
     *    两个线程调用的 deposit 里的 this 都是同一个对象，所以能锁住、互斥成功。
     *    如果 new 了多个 Account，各自的 this 就不同，就锁不到一起了。
     * 加 synchronized 的作用：保证同一时刻只有一个线程能进入 deposit，
     *    把"读余额→改余额→打印"变成不可被打断的整体（原子操作）。
     */
    public synchronized void deposit(double amount) {
        // 参数校验：金额为负直接抛异常，防止非法数据污染余额
        if (amount < 0) {
            throw new IllegalArgumentException();
        } else {
            this.balance += amount;
        }

        /*
         * sleep 是"故意"加的：放大线程切换的时间窗口，
         * 让不加 synchronized 时的安全问题更容易复现出来（否则可能很难碰到）。
         */
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "存钱1000块，余额为：" + balance);

        /*
         * 非法输入应当"报错让调用方知道"，而不是默默忽略；
         * 默默忽略会让 bug 藏起来，将来很难排查。
         * 所以"抛异常"比">=0"更健壮。
         * if (amount >= 0) {
         *     balance +=amount;
         * }
         */
    }
}

/*
 * 更推荐 class Customer implements Runnable。
 * 原因：Java 单继承，继承 Thread 会占掉唯一的继承名额；
 * 实现 Runnable 更灵活，也便于多个线程共享同一个 Runnable 对象。
 */
class Customer extends Thread {
    Account account;

    // 备用构造器：只传账户、不指定线程名字（本题 main 里其实没用到它）
    public Customer(Account account) {
        this.account = account;
    }

    public Customer(Account account, String name) {
        /*
         * super(name) 调用的是父类 Thread 的构造器，把 name 设置成"线程的名字"。
         * 它和 account 完全无关！name 存到了 Thread 内部的线程名字段里。
         * 好处：后面 Thread.currentThread().getName() 就能打印出"甲""乙"，
         * 输出更直观、方便区分是哪个储户在存钱。
         */
        super(name);
        this.account = account;
    }

    @Override
    public void run() {
        /*
         * run() 里的代码就是"这个线程要干的活"——存 3 次、每次 1000。
         * 这里操作的 account 是共享数据，所以线程安全问题就发生在这段调用里。
         */
        for (int i = 0; i < 3; i++) {
            account.deposit(1000);
        }
    }
}
