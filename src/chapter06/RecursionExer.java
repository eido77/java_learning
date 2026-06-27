package chapter06;

public class RecursionExer {
    public static void main(String[] args) {
        //不死神兔演示
        RecursionExer r = new RecursionExer();
        System.out.println(r.getRabbitNumber(5)); // 5
        System.out.println(r.getRabbitNumber(6)); // 8
        System.out.println(r.getRabbitNumber(7)); // 13

    }

    /*
    练习1：
    已知一个数列：f(20) = 1,f(21) = 4,f(n+2) = 2*f(n+1)+f(n),
    其中n是大于0的整数，求f(10)的值。
    */
    public int f(int n) {
        if (n == 20) {
            return 1;
        } else if (n == 21) {
            return 4;
        } else {
            //如果不是确定的求f(10)的情况，可以用if-else来区分
            //正确
            return f(n + 2) - 2 * f(n + 1);
            //错误的，越走越小，走不到20和21
            // n+2看作n
            //return 2 * f(n -1) + f(n - 2);
        }
    }

    /*
    练习2：
    已知有一个数列：f(0) = 1,f(1) = 4,
    f(n+2)=2*f(n+1) + f(n),其中n是大于0的整数，求f(10)的值。
    */
    public int f1(int n) {
        if (n == 0) {
            return 1;
        } else if (n == 1) {
            return 4;
        } else {
            //错误，越走越大，走不到0和1
            //return f1(n + 2) - 2 * f1(n + 1);
            //正确
            // n+2看作n
            return 2 * f1(n - 1) + f1(n - 2);
        }
    }

    /*
    案例：不死神兔
    用递归实现不死神兔：故事得从西元1202年说起，话说有一位意大利青年，名叫斐波那契(Fibonacci)。
    在他的一部著作中提出了一个有趣的问题：假设一对刚出生的小兔一个月后就能长成大兔，
    再过一个月就能生下一对小兔，并且此后每个月都生一对小兔，没有发生死亡，
    问：现有一对刚出生的兔子2年后(24个月)会有多少对兔子?
    月份       1    2   3    4    5
    兔子对数    1    1   2    3    5
    拓展：走台阶问题
    假如有10阶楼梯，小朋友每次只能向上走1阶或者2阶，请问一共有多少种不同的走法呢？
    阶数  1   2   3   4   。。。
    走法  1   2   3   5   。。。
    从n为3开始：
    f(n) = f(n - 1) + f(n - 2)
    【奇妙的属性】随着数列的增加，斐波那契数列前一个数与后一个数的比值越来越逼近黄金分割的数值0.618。
     */
    public int getRabbitNumber(int month) {
        if (month == 1) {
            return 1;
        } else if (month == 2) {
            return 1;
        } else {
            return getRabbitNumber(month - 1) + getRabbitNumber(month - 2);
        }
    }


}
