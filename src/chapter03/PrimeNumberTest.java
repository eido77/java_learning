package chapter03;

public class PrimeNumberTest {
    public static void main(String[] args) {
        /*
        题目：找出100以内所有的素数（质数）？100000以内的呢？
        质数：只能被1和它本身整除的自然数
          --> 从2开始到这个自然数-1为止，不存在此自然数的约数
         */
        /*
        方式1
        for (int i = 2; i <= 100; i++) {
            int number = 0; // 记录i的约数的个数，不能放到外面
            for (int j = 2; j <= i - 1; j ++) {
                // if (i % j != 0) { // 只要找到一个j不能整除i就输出
                if (i % j == 0) {
                    number++;
                }
            }
            if (number == 0) {
                System.out.println(i);
            }
        }
         */
        /*
        //方式2，这里把原本的100改成了100000
        // 遍历100000以内的所有质数，体会不同的算法实现，其性能的差别

        //获取系统当前时间
        //规范用long
        long start = System.currentTimeMillis();
        int count = 0; // 记录质数的个数
        for (int i = 2; i <= 100000; i++) {
            boolean isFlag = true; // 不能放到外面，
            for (int j = 2; j <= i - 1; j++) {
                if (i % j == 0) {
                    isFlag = false;
                }
            }
            if (isFlag) {
                //System.out.println(i);
                count++;
            }
        }
        //获取系统当前时间
        long end = System.currentTimeMillis();
        System.out.println("质数总个数为：" + count); // 9592
        System.out.println("花费的时间为：" + (end - start)); // 2402
         */

        //获取系统当前时间
        long start = System.currentTimeMillis();
        int count = 0; // 记录质数的个数
        for (int i = 2; i <= 100000; i++) {
            boolean isFlag = true; // 不能放到外面，
            //这些因数对里,总是一个比较小、一个比较大,而分界线正好是根号i
            // a * b = i，如果a、b都大于根号i的时候不成立
            for (int j = 2; j <= Math.sqrt(i); j++) { // j * j <= i
                if (i % j == 0) {
                    isFlag = false;
                    break; // 结束的只是这一个数的，针对非质数有效果
                }
            }
            if (isFlag) {
                //System.out.println(i);
                count++;
            }
        }
        //获取系统当前时间
        long end = System.currentTimeMillis();
        System.out.println("质数总个数为：" + count); // 9592
        System.out.println("花费的时间为：" + (end - start)); // break（248）根号i（3）












    }
}
