package chapter05;

import java.util.Scanner;

public class ArrayExer {
    public static void main(String[] args) {
        /*
        案例："破解"房东电话
        升景坊单间短期出租4个月，550元/月（水电煤公摊，网费35元/月），空调、卫生间、厨房齐全。屋内均是IT行业人士，喜欢安静。
        所以要求来租者最好是同行或者刚毕业的年轻人，爱干净、安静。
         */
        int[] arr = new int[]{8, 2, 1, 0, 3};
        int[] index = new int[]{2, 0, 3, 2, 4, 0, 1, 3, 2, 3, 3};

        String tel = "";

        for (int i = 0; i < index.length; i++) {
            int value = index[i];
            tel += arr[value];
        }
        System.out.println("联系方式：" + tel); // 18013820100

        /*
        案例：输出英文星期几
        用一个数组，保存星期一到星期天的7个英语单词，从键盘输入1-7，显示对应的单词
        {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"}
         */

        //定义包含7个单词的数组
        String[] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        //从键盘获取指定的数值，使用Scanner
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入数值（1-7）：");
        int day = sc.nextInt();
        //针对获取的数据判断
        if (day < 1 || day > 7) {
            System.out.println("输入错误");
        } else {
            System.out.println(weeks[day - 1]);
        }
        /*
        案例：学生考试等级划分
        从键盘读入学生成绩，找出最高分，并输出学生成绩等级。
            成绩>=最高分-10    等级为’A’
            成绩>=最高分-20    等级为’B’
            成绩>=最高分-30    等级为’C’
            其余              等级为’D’
        提示：先读入学生人数，根据人数创建int数组，存放学生成绩。

        效果演示
        请输入学生人数：5
        请输入5个成绩：56 74 89 41 89 最高分是：89
        student 0 score is 56 grade is D
        student 1 score is 74 grade is B
        student 2 score is 89 grade is A
        student 3 score is 41 grade is D
        student 4 score is 89 grade is A
         */
        //1.从键盘输入学生人数，根据人数创建数组
        System.out.println("输入学生人数：");
        int student = sc.nextInt();
        int[] scores = new int[student];

        //2.根据提示，依次输入学生成绩，并将成绩保存在数组元素中
        System.out.println("请输入" + student + "个成绩");
        for (int i = 0; i < scores.length; i++) {
            //int[] scores 就是声明，它创建了一个叫 scores 的数组变量。声明只需要做一次,做完之后 scores 这个名字就存在了。
            //后面的 scores[i] = sc.nextInt(); 是在使用这个已有的数组,往它的第 i 个位置放值,直接写名字 scores 就行,不能再加 int。
            scores[i] = sc.nextInt();
            System.out.print(scores[i] + " ");
        }

        //3.获取学生成绩的最大值
        // int scoreMax = 0;
        //用 scores[0] 初始化时，把 for初始条件改成 int i = 1 是更规范的写法（避免重复比较）
        // scores[0] 是第一个数据，对应的是 int i 的 0，不是 1。
        // scores[i] 里中括号 [ ] 里的 i 就是下标。
        int scoreMax = scores[0];
        for (int i = 1; i < scores.length; i++) {
            if (scoreMax < scores[i]) {
                scoreMax = scores[i];
            }
        }
        System.out.println("最高分为：" + scoreMax);
        /*
        2和3优化
        int scoreMax = scores[0];
        System.out.println("请输入" + student + "个成绩");
        for (int i = 0; i < scores.length; i++) {
             scores[i] = sc.nextInt();
             if (scoreMax < scores[i]) {
                scoreMax = scores[i];
             }
         */

        //4.遍历数组元素，根据学生成绩与最高分差值，来得到每个学生的等级，并输出成绩和等级
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] >= scoreMax - 10) {
                System.out.println("student " + i + " score is " + scores[i] + " grade is A");
            } else if (scores[i] >= scoreMax - 20) {
                System.out.println("student " + i + " score is " + scores[i] + " grade is B");
            } else if (scores[i] >= scoreMax - 30) {
                System.out.println("student " + i + " score is " + scores[i] + " grade is C");
            } else {
                System.out.println("student " + i + " score is " + scores[i] + " grade is D");
            }
        }
        /*
        4优化
        这个= 0 也能去掉，因为在输出前一定给他赋值了，有else
        char grade = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] >= scoreMax - 10) {
                grade = 'A';
            } else if (scores[i] >= scoreMax - 20) {
                grade = 'B';
            } else if (scores[i] >= scoreMax - 30) {
                grade = 'C';
            } else {
                grade = 'D';
            }
            System.out.println("student " + i + " score is " + scores[i] + " grade is " + grade);
        }
         */


        sc.close();

    }
}
