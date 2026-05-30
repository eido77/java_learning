package chapter03;

public class ForTest {
    public static void main(String[] args) {
        /*
        循环结构：for循环
        3种循环结构：for、while、do-while
        凡事循环结构一定有四要素：
            一.初始化条件
            二.循环条件(boolean类型)
            三.循环体
            四.迭代部分
        for循环格式：两个分号不能少，但分号之间的三段内容可以为空。循环体也可以为空。
        for (一; 二; 四) {
            三
        }

        执行过程：一 --> 二 --> 三 --> 四 --> 二 --> 三 --> 四 ... --> 二

        可以在循环结构中使用break，一旦执行break，就跳出（结束）当前循环结构

        结束一个循环结构
            方式1：循环条件不满足（循环条件执行完以后是false）
            方式2：在循环体中执行了break
         */

        //输出5行Hello World
        for (int i = 1; i <= 5; i++) {
            System.out.println("Hello World");
        }

        //此时编译不通过，因为i已经出了其作用域范围，可以用多个i，因为作用域不同
        //System.out.println(i);

        //案例2，输出结果
        int num = 1;
        for (System.out.print("a"); num < 3; System.out.print("c"), num++) {
            System.out.print("b");
        } // abcbc

        //案例3：遍历1-100以内的偶数，并获取偶数的个数，获取所有的偶数的和
        int count = 0; // 记录偶数的个数
        int sum = 0; // 记录所以偶数的和

        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
                count++;
                sum += i;
            }
        }

        System.out.println("偶数的个数：" + count);
        System.out.println("所有偶数的和：" + sum);

        //输出所有的水仙花数，所谓水仙花数是指一个3位数，其各个位上数字立方和等于其本身。
        //例如：153 = 1*1*1 + 3*3*3 + 5*5*5
        for (int i = 100; i <= 999; i++) {

            //三位数各个位的值
            int a = i % 10;
            int b = i / 10 % 10; // int b = i % 100 / 10;
            int c = i / 100;

            //判断是否满足
            if (i == a * a * a + b * b * b + c * c * c) {
                System.out.println("水仙花数为：" +i);
            }

        }

        //说明：输入两个正整数m和n，求其最大公约数和最小公倍数。
        //比如：12和20的最大公约数是4，最小公倍数是60。
        int m = 12;
        int n = 20;

        //获取m、n中较小值
        int min = (m < n) ? m : n;

        //最大公约数
        //方式1
        int result = 1;
        for (int i = 1; i <= min; i++) {
            if (m % i == 0 && n % i == 0) {
                result = i;
            }
        }
        System.out.println(result);
        //方式2：推荐
        for (int i = min; i >= 1; i--) {
            if (m % i == 0 && n % i == 0) {
                System.out.println("最大公约数：" + i);
                break; // 一旦执行就跳出当前循环结构
            }
        }

        //最小公倍数
        int max = (m>n) ? m : n;
        for (int i = max; i <= m * n; i++) {
            if (i % m == 0 && i % n == 0) {
                System.out.println("最小公倍数：" + i);
                break;
            }

        }

    }
}
