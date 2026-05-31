package chapter03;

import java.util.Scanner;

public class DoWhileTest {
    public static void main(String[] args) {
        /*
        do-while循环
        凡事循环结构一定有四要素：
            一.初始化条件
            二.循环条件(boolean类型)
            三.循环体
            四.迭代部分
        do-while格式：
        一
        do {
            三
            四
        } while (二);

        执行过程：一 --> 三 --> 四 --> 二 --> --> 三 --> 四 ... 二
        说明：
        （1）do-while循环至少执行一次循环体
        （2）for、while、do-while循环三者之间可以互相转换
        （3）do-while使用的较少
         */

        // 遍历1-100以内的偶数，并获取偶数的个数，获取所有的偶数的和
        int i = 1;
        int sum = 0;
        int count = 0;
        do {
            if (i % 2 == 0) {
                System.out.println(i);
                count++;
                sum += i;
            }
            i++;
        } while (i <= 100);
        System.out.println("偶数的个数：" + count);
        System.out.println("所有偶数的和：" + sum);

        //例子
        int num1 = 10;
        while (num1 > 10) {
            System.out.println("while:hello");
            num1--;
        }

        int num2 = 10;
        do {
            System.out.println("do-while:hello");
            num2--;
        } while (num2 > 10);

        //声明变量balance并初始化为0，用以表示银行账户的余额，下面通过ATM机程序实现存款，取款等功能。
        //===ATM===
        //1、存款
        //2、取款
        //3、显示余额
        //4、退出
        //请选择（1-4）：
        double balance = 0;
        Scanner scanner = new Scanner(System.in);
        boolean flag = true; // 控制循环的结束
        do {
            System.out.println("===ATM===" + "\n" + "1、存款" + "\n" + "2、取款" + "\n" + "3、显示余额" + "\n" + "4、退出" + "\n" + "请选择（1-4）：");
            int selection = scanner.nextInt();
            switch (selection) {
                case 1:
                    System.out.println("请输入存款金额：");
                    double money1 = scanner.nextDouble();
                    if (money1 > 0) {
                        balance += money1;
                    }
                    break;
                case 2:
                    System.out.println("请输入取款的金额：");
                    double money2 = scanner.nextDouble();
                    if (money2 > 0 && money2 <= balance) {
                        balance -= money2;
                    } else {
                        System.out.println("输入的数据有误或余额不足");
                    }
                    break;
                case 3:
                    System.out.println("您的余额为：" + balance);
                    break;
                case 4:
                    flag = false;
                    System.out.println("感谢使用，欢迎下次光临");
                    break;
                default:
                    System.out.println("输入错误");
                    break;
            }
        } while (flag);


        scanner.close();
    }
}
