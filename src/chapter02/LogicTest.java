package chapter02;

public class LogicTest {
    public static void main(String[] args) {
        /*
        逻辑运算符
        &、&&、|、||、!、^
        逻辑运算符针对的都是boolean类型变量进行的操作
        逻辑运算符运算的结果也是boolean类型
        常使用在条件判断结构、循环结构
         */

        //区分&和&&，推荐用&&
        //相同点：表达“且”的关系，只有当符号左右两边的类型值均为true时，结果才为true

        //执行过程：如果符号左边是true，则两个都会执行右边，
        //        如果符号左边是false，则&继续执行符号右边操作，&&不会继续执行。
        boolean b1 = true;
        b1 = false;
        int num1 = 10;
        if (b1 & (num1++ > 0)){
            System.out.println("123");
        }else{
            System.out.println("456"); // 456
        }
        System.out.println("num1 = " + num1); // 11


        boolean b2 = true;
        b2 = false;
        int num2 = 10;
        if (b2 && (num2++ > 0)){
            System.out.println("111");
        }else{
            System.out.println("222"); // 222
        }
        System.out.println("num2 = " + num2); // 10

        //区分|和||，推荐用||
        //相同点：表达“或”的关系，只要符号两边存在true，结果就为true

        //执行过程：如果符号左边是false，则两个都会执行右边，
        //        如果符号左边是true，则 | 继续执行符号右边操作，||不会继续执行。

        boolean b3 = false;
        b3 = true;
        int num3 = 10;
        if (b3 | (num3++ > 0)){
            System.out.println("444"); // 444
        }else{
            System.out.println("555");
        }
        System.out.println("num3 = " + num3); // 11


        boolean b4 = false;
        b4 = true;
        int num4 = 10;
        if (b4 || (num4++ > 0)){
            System.out.println("666"); // 666
        }else{
            System.out.println("777");
        }
        System.out.println("num4 = " + num4); // 10

        boolean x = true;
        boolean y = false;
        short z = 42;
        // = 是赋值，不是判断
        if ((z++ == 42) && (y = true)){
            z++;
        }
        if ((x = false) || (++z == 45)){
            z++;
        }
        System.out.println(z); // 46



    }
}
