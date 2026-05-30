package chapter03;
import java.util.Scanner;

public class SwitchCaseTest2 {
    public static void main(String[] args) {
        //使用switch-case实现：对学生成绩大于60分的，输出“合格”。低于60分的，输出“不合格”。
        //方式1:一个一个列
        //方式2：体会case穿透
        int score = 59;
        switch (score / 10) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: // 59 / 10 = 5（int里面向下取整）
                System.out.println("不合格");
                break;
            case 6: // 60 / 10 = 6
            case 7:
            case 8:
            case 9:
            case 10:
                System.out.println("合格");
                break;
            default:
                System.out.println("成绩输入错误");
                break;
        }
        //方式3:
        switch (score / 60) {
            case 0: // 59 / 60 小于1，int里面向下取整
                System.out.println("不合格");
                break;
            case 1: // 60 / 60 = 1
                System.out.println("合格");
                break;
            default:
                System.out.println("成绩输入错误");
                break;
        }

        //编写程序：从键盘上输入2023年的“month”和“day”，要求通过程序输出输入的日期为2023年的第几天。
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入2023年的月份：");
        int month = scanner.nextInt(); // 阻塞式方法

        System.out.println("请输入日期：");
        int day = scanner.nextInt();
        //假设输入的是合法的，后期在开发中，使用正则表达式进行校验
        int days = 0;
        /*
        方式1（不推荐）,存在数据的冗余
        switch (month) {
            case 1:
                days = day;
                break;
            case 2:
                days = 31 + day;
                break;
            case 3:
                days = 31 + 28 + day;
                break;
            case 4:
                days = 31 + 28 + 31 + day;
                break;
            case 5:
                days = 31 + 28 + 31 + 30 + day;
                break;
            case 6:
                days = 31 + 28 + 31 + 30 + 31 + day;
                break;
            case 7:
                days = 31 + 28 + 31 + 30 + 31 + 30 + day;
                break;
            case 8:
                days = 31 + 28 + 31 + 30 + 31 + 30 + 31 + day;
                break;
            case 9:
                days = 1 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + day;
                break;
            case 10:
                days = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + day;
                break;
            case 11:
                days = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + day;
                break;
            case 12:
                days = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + day;
                break;
            default:
                System.out.println("输入错误");
                break;
        }
        */
        //方式2
        switch (month) {
            case 12:
                days += 30;
            case 11:
                days += 31;
            case 10:
                days += 30;
            case 9:
                days += 31;
            case 8:
                days += 31;
            case 7:
                days += 30;
            case 6:
                days += 31;
            case 5:
                days += 30;
            case 4:
                days += 31;
            case 3:
                days += 28; // 2月份天数
            case 2:
                days += 31; // 1月份天数
            case 1:
                days += day;
                break;
            default:
                System.out.println("输入错误");
                break;
        }

        System.out.println("2023年" + month + "月" + "日是第" + days + "天");

        scanner.close(); // 防止内存泄漏


    }
}
