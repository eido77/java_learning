package chapter03;

import java.util.Scanner;

public class ForWhileTest {
    public static void main(String[] args) {
        /*
        “无限”循环结构
        格式：while (true)或for (;;)
        开发中，有时并不确定需要循环多少次，需要根据循环体内部某些条件，来控制循环的结束（使用break）。
        如果此循环结构不能终止，则构成了死循环！开发中要避免出现死循环。

         */

        /*
        while (true) {
            System.out.println("Hello World");
        }
        */
        //死循环的后面不能有执行语句
        //System.out.println("end"); // 报错

        /*
        for (;;) {
            System.out.println("Hello World");
        }
         */

        //从键盘读入个数不确定的整数，并判断读入的正数和负数的个数，输入为0时结束程序。
        Scanner scanner = new Scanner(System.in);
        int positiveCount = 0; // 正数
        int negativeCount = 0; // 负数
        while (true) {
            System.out.print("请输入一个整数（为0时结束程序）：");
            int num = scanner.nextInt();
            if (num > 0) {
                positiveCount++;
            } else if (num < 0) {
                negativeCount++;
            } else {
                System.out.println("程序结束");
                break; // break 直接终止整个 while (true) 循环
            }
        }
        System.out.println("正数个数为：" + positiveCount);
        System.out.println("负数个数为：" + negativeCount);

        scanner.close();
    }
}
