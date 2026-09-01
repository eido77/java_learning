package chapter10;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

public class CallablePoolTest {
    /*
    1. 创建多线程的方式三：实现Callable （jdk5.0新增的）
    与之前的方式的对比：与Runnable方式的对比的好处
    > call()可以有返回值，更灵活
    > call()可以使用throws的方式处理异常，更灵活
    > Callable使用了泛型参数，可以指明具体的call()的返回值类型，更灵活
    缺点：如果在主线程中需要获取分线程call()的返回值，则此时的主线程是阻塞状态的。
    2. 创建多线程的方式四：使用线程池（提前创建好一批线程放在"池子"里循环复用的容器）
    此方式的好处：
    > 提高了程序执行的效率。（因为线程已经提前创建好了）
    > 提高了资源的复用率。（因为执行完的线程并未销毁，而是可以继续执行其他的任务）
    > 可以设置相关的参数，对线程池中的线程的使用进行管理

     * 【方式三：Callable vs Runnable 对比】
     * 1) 返回值：
     *    - Runnable 的 run() 返回 void，线程干完活没法直接告诉你结果。
     *    - Callable 的 call() 有返回值，能把计算结果带回来。
     * 2) 异常处理：
     *    - Runnable 的 run() 方法签名里没有 throws，所以你在 run() 里
     *      只能用 try-catch 处理受检异常(checked exception)，不能往外 throws。
     *      （因为重写方法抛出的异常范围不能比父类声明的更大，父类没声明，你就不能加。）
     *    - Callable 的 call() 方法本身在接口里就声明了 throws Exception，
     *      所以你重写时可以直接 throws Exception，把异常抛给调用者（比如 get() 处）处理。
     *      下面 NumThread 的 call() 能直接 throws Exception 而 run() 不行。
     * 3) 泛型：
     *    - Callable<V> 是带泛型的，可以指定 call() 的返回值类型，类型安全。
     * 4) 缺点：
     *    - 通过 FutureTask.get() 取结果时，主线程会阻塞，直到 call() 执行完毕返回。
     */
}

/**
 * 创建多线程的方式三：实现Callable （jdk5.0新增的）
 */
// 1.创建一个实现Callable的实现类
// 建议：Callable 是泛型接口，最好写成 Callable<Integer> 明确返回类型，
// 这样 call() 就能直接返回 int/Integer，get() 拿到的也是 Integer 而不是 Object，无需强转。
// implements Callable（裸类型）会丢失泛型信息，编译器会给 unchecked 警告。
class NumThread implements Callable {
    // 2.实现call方法，将此线程需要执行的操作声明在call()中
    @Override
    public Object call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
                sum += i;
            }
            Thread.sleep(1000);
        }
        /*
         * 这里和 run() 有本质区别。
         * run() 返回 void，写不了 return sum；call() 有返回值，可以 return，
         * 这个返回值最终会被 FutureTask 缓存，供主线程通过 get() 取出。
         */
        return sum;
    }
}

class CallableTest {
    public static void main(String[] args) {
        // 3.创建Callable接口实现类的对象
        NumThread numThread = new NumThread();

        // 4.将此Callable接口实现类的对象作为传递到FutureTask构造器中，创建FutureTask的对象
        // 说明：FutureTask 既实现了 Runnable，又能持有 Callable 的返回结果，
        // 相当于"中间人"——它能被 Thread 执行，又能通过 get() 把 call() 的结果拿回来。
        // 建议：加泛型 FutureTask<Integer>，配合 Callable<Integer> 使用，get() 直接返回 Integer。
        FutureTask futureTask = new FutureTask(numThread);

        // 5.将FutureTask的对象作为参数传递到Thread类的构造器中，创建Thread对象，并调用start()
        Thread t1 = new Thread(futureTask);
        t1.start();

        System.out.println("main()线程");

        /*
         * get() 会让 main 线程阻塞，直到 t1 的 call() 执行完并返回结果。
         * 上面"main()线程"这句一般会先打印，而"总和为"要等 5000 次 sleep 后才出来。
         */
        try {
            // 6.获取Callable中call方法的返回值
            // get()返回值即为FutureTask构造器参数Callable实现类重写的call()的返回值。
            Object sum = futureTask.get();
            System.out.println("总和为：" + sum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 创建并使用多线程的第四种方法：使用线程池
 */
/*
 * 不是必须Runnable。
 *   实现 Runnable 用 service.execute(...) 或 service.submit(...) 提交；
 *   实现 Callable 只能用 service.submit(...) 提交（因为要拿返回值）。
 * 这里用 Runnable 只是因为这个任务不需要返回值。
 *
 * 没有 new Thread：这正是线程池的核心好处。
 * 线程由线程池内部提前创建并复用，你只需要把"任务(Runnable/Callable)"交给池子，
 * 池子会自动分配空闲线程去跑，所以不需要你手动 new Thread、手动 start()。
 */
class NumberThread implements Runnable{
    @Override
    public void run() {
        for(int i = 0;i <= 100;i++){
            if(i % 2 == 0){
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }
    }
}

class NumberThread1 implements Runnable{
    @Override
    public void run() {
        for(int i = 0;i <= 100;i++){
            if(i % 2 != 0){
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }
    }
}

class ThreadPool {
    public static void main(String[] args) {
        // 1.提供指定线程数量的线程池
        // newFixedThreadPool(10) 创建一个固定 10 个线程的线程池。
        // 返回类型是 ExecutorService（接口），拿到的实际对象其实是 ThreadPoolExecutor。
        // 不推荐用 Executors 工厂方法（队列/线程数可能无界，易 OOM），
        // 生产环境建议直接 new ThreadPoolExecutor(...) 显式指定各项参数。
        ExecutorService service = Executors.newFixedThreadPool(10);

        // 向下强转成实现类 ThreadPoolExecutor，才能调用 setMaximumPoolSize 等设置方法，
        // 因为 ExecutorService 接口本身没有这些配置方法。
        ThreadPoolExecutor service1 = (ThreadPoolExecutor) service;

        // 设置线程池的属性
//        System.out.println(service.getClass()); // ThreadPoolExecutor
        service1.setMaximumPoolSize(50); // 设置线程池中线程数的上限


        // 2.执行指定的线程的操作。需要提供实现Runnable接口或Callable接口实现类的对象
        // execute() 只能提交 Runnable，没有返回值；submit() 既能提交 Runnable 也能提交 Callable，
        // 且会返回一个 Future，可用于获取结果或取消任务。
        service.execute(new NumberThread()); // 适合适用于Runnable
        service.execute(new NumberThread1()); // 适合适用于Runnable

//        service.submit(Callable callable); // 适合使用于Callable

        // 3.关闭连接池
        /*
         * 说明：shutdown() 是"温和关闭"——不再接收新任务，但会把已提交的任务执行完再关。
         * 注意原注释写的是"关闭连接池"，这里其实是"关闭线程池"，措辞纠正一下。
         */
        service.shutdown();
    }
}
