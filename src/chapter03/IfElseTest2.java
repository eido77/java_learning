package chapter03;

public class IfElseTest2 {
    public static void main(String[] args) {
        //if-else执行语句块中只有一条执行语句时，大括号可以不写，但是，不建议省略
        //案例4：（由键盘）输入三个整数分别存入变量num1、num2、num3，对它们进行排序（使用 if-else if-else），并且从小到大输出。
        int num1 = 30;
        int num2 = 21;
        int num3 = 44;
        if (num1 >= num2) {
            if (num1 <= num3) {
                System.out.println("num2 < num1 < num3");
            } else if (num2 >= num3) {
                System.out.println("num3 < num2 < num1");
            } else {
                System.out.println("num2 < num3 < num1");
            }
        } else { // num1 < num2
            if (num3 >= num2) {
                System.out.println("num1 < num2 < num3");
            } else if (num1 >= num3) {
                System.out.println("num3 < num1 < num2");
            } else {
                System.out.println("num1 < num3 < num2");
            }
        }



    }
}
