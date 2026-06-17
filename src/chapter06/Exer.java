package chapter06;

public class Exer {
    public static void main(String[] args) {
        /*
        有一个案例在Person里面
        案例2：
        1. 编写程序，声明一个method1方法，在方法中打印一个10*8 的*型矩形，在main方法中调用该方法。
        2. 编写程序，声明一个method2方法，除打印一个10*8的*型矩形外，
        再计算该矩形的面积，并将其作为方法返回值。在main方法中调用该方法，接收返回的面积值并打印。
        3. 编写程序，声明一个method3方法，在method3方法提供m和n两个参数，方法中打印一个m*n的*型矩形，
        并计算该矩形的面积，将其作为方法返回值。在main方法中调用该方法，接收返回的面积值并打印。
         */

        //method1是方法，定义在Exer类里面的方法
        Exer e = new Exer();
        e.method1(); // 10行每行8个*
        System.out.println("------------------");
        int area = e.method2();
        System.out.println(area); // 10行每行8个*，80
        int area3 = e.method3(2, 3);
        System.out.println(area3); // 6，2行每行3个*


    }
    public void method1() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public int method2() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
        return 10 * 8;
    }

    //括号里的叫形参（参数）。意思是「这个方法需要外界给它两个 int 值才能工作」。如果写在花括号里叫局部变量
    public int method3(int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
        return m * n;
    }



}
