package chapter02;

public class SetValueTest {
    public static void main(String[] args) {
        /*
        赋值运算符
        = += -= *= /= %=
        “+= -= *= /= %=”这些操作不会改变变量本身的数据类型
         */

        /*
        =
        1.当“=”两侧数据类型不一致时，可以使用自动类型转换或使用强制类型转换原则进行处理。
        2.支持连续赋值。
        */
        //操作方式1
        int a1 = 10;
        int a2 = 10;
        //操作方式2：连续赋值
        int a3, a4; //或者是 int a3; int a4;
        a3 = a4 = 10;
        System.out.println("a3 = " + a3 + " a4 = " + a4);

        //操作方式3
        int a5 = 10;
        int a6 = 20;

        //操作方式4
        int aa5 = 10, aa6 = 20;
        System.out.println("aa5 = " + aa5 + " aa6 = " + aa6);

        // +=，不改变变量本身数据类型
        // n += X 在语法上就等价于 n = n + X
        int m1 = 10;
        m1 += 5; // 类似于m1 = m1 + 5;
        System.out.println(m1); // 15

        byte b1 = 10;
        b1 += 5; // b1 = b1 + 5的话不通过，应该为b1 = (byte)(b1 + 5);
        System.out.println(b1);

        int m2 = 1;
        m2 *= 0.1; // m2 = m2 * 0.1，编译报错
        System.out.println(m2); // 0

        //练习1：如何实现变量的值增加2
        //方式1
        int n1 = 10;
        n1 = n1 + 2;
        System.out.println(n1); // 12

        //方式2（推荐）
        int n2 = 10;
        n2 += 2;
        System.out.println(n2); // 12

        //练习2:如何实现变量的值增加1
        //上面两个方式都可以
        //方式3（推荐）
        int i3 = 10;
        i3++; // 或者++i3;
        System.out.println(i3); // 11

        //练习3
        int n = 10;
        n += (n++) + (++n); // n = n + (n++) + (++n)
        System.out.println(n); // 32


    }
}
