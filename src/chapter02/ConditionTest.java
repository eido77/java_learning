package chapter02;

public class ConditionTest {
    public static void main(String[] args) {
        /*
        条件运算符
        (条件表达式) ? 表达式1 : 表达式2;
        条件表达式的结果是boolean类型
        如果条件表达式结果是true，则执行表达式1，否则执行表达式2。
        表达式1、2需要是相同或可兼容的类型，实务上强烈建议保持一致或类型相近，避免可读性问题和隐式转换的坑。
         */
        //开发中凡是可以使用条件运算符的位置，都可以改成if else结构。
        //反之，能使用if else结构，不一定能改写为条件运算符
        //在二者都能用的情况，推荐用条件运算符，运行效率稍高

        //用什么类型接收，取决于表达式1、2的类型
        String info = (2 > 1) ? "111" : "222";
        System.out.println(info); // 111

        double d = (2 > 1) ? 1 : 2.0;
        System.out.println(d); // 1.0

        //练习1：获取两个整数的较大值
        int m = 10;
        int n = 20;
        int mn = (m > n) ? m : n;
        System.out.println(mn); // 20

        //练习2：获取三个整数的最大值
        int a = 20;
        int b = 30;
        int c = 23;
        int tempMax = (a > b) ? a : b;
        int finalMax = (tempMax > c) ? tempMax : c;
        System.out.println("最大值为：" + finalMax);

        //今天是周4，10天以后是周几？
        int week = 4;
        week += 10;
        week %= 7;
        System.out.println("今天是周4，10天以后是周" + ((week > 0) ? week : "日"));


    }
}
