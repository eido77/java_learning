package chapter02;

public class LogicExer {
    public static void main(String[] args) {
        /*
        1.定义一个int类型变量a，变量b，都赋值为20
        2.定义boolean类型变量bo1，判断++a 是否被3整除，并且a++ 是否被7整除，将结果赋值给bo1
        3.输出a的值，bo1的值
        4.定义boolean类型变量bo2，判断b++ 是否被3整除，并且++b 是否被7整除，将结果赋值给bo2
        5.输出b的值，bo2的值
         */

        //int a = 20, b = 20;或者下面两行
        //int a = b = 20;这种情况不行，b没被声明，事先b被声明的情况是可以的
        int a, b;
        a = b = 20;
        // = 的优先级低，最后运行
        boolean bo1 = (++a % 3 == 0) && (a++ % 7 == 0);
        System.out.println("a = " + a); // 22
        System.out.println("b1 = " + bo1); // true

        boolean bo2 = (b++ % 3 == 0) && (b++ % 7 ==0);
        System.out.println("b = " + b); // 21
        System.out.println("bo2 = " + bo2); // false




    }
}
