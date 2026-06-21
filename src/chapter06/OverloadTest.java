package chapter06;

public class OverloadTest {
    public static void main(String[] args) {
        OverloadTest test = new OverloadTest();
        test.add(1, 2, 3);
        test.add(10, 20); // 111
        test.add(10, 20.0); // 333
        test.add((double) 10, 20); // 333


        //面试题
        int[] arr = new int[]{1,2,3};
        System.out.println(arr);// 地址值，[I@3fee733d

        //char[] 有专属重载 println(char[] x),会遍历数组逐个打印字符，所以唯独 char[] 能打印出"内容",其余数组都打印地址值
        char[] arr1 = new char[]{'a','b','c','d','e'};
        System.out.println(arr1);// abcde

        boolean[] arr2 = new boolean[]{false,true,true};
        System.out.println(arr2);// 地址值，[Z@5acf9800
    }

    public void add(int i, int j) {
        System.out.println("111");
    }
    /*
    报错，和上面的重复定义了，参数列表必须不同
    public void add(int m, int n) {
        System.out.println("222");
    }
    报错，返回值类型不构成重载的判断依据
    Java 判断两个方法是不是同一个,只看方法名 + 参数列表
    public int add(int m, int n) {
        return m + n;
    }
     */
    //test.add(10, 20);
    //如果上面的注释掉就会输出333，不注释掉就是111
    public void add(double d1, double d2) {
        System.out.println("333");
    }

    public void add(int i, int j, int k) {

    }

    public void add(String s1, String s2) {

    }

    public void add(int i, String s) {

    }

    public void add(String s, int i) {

    }

    /*
    方法重载练习
    练习1：判断与 void show(int a, char b, double c){} 构成重载的有哪些
    重载规则：方法名相同，参数列表不同（参数个数、类型或顺序不同），与返回值类型、形参名称无关。
    原始方法：void show(int a, char b, double c){}
    a) void show(int x, char y, double z){}     // no，参数列表完全相同（仅形参名不同），属于重复定义，编译报错
    b) int show(int a, double c, char b){}      // yes，参数顺序为 int,double,char，与原方法不同
    c) void show(int a, double c, char b){}     // yes，参数顺序不同
    d) boolean show(int c, char b){}            // yes，参数个数不同（2个）
    e) void show(double c){}                    // yes，参数个数不同（1个）
    f) double show(int x, char y, double z){}   // no，参数列表与原方法相同，仅返回值不同，编译报错
    g) void shows(){double c}                   // no，方法名是 shows，不是 show
    */

    /*
    练习2：
    编写程序，定义三个重载方法并调用。方法名为mOL。
    三个方法分别接收一个int参数、两个int参数、一个字符串参数。
    分别执行平方运算并输出结果，相乘并输出結果，输出字符串信息。
    */
    public void mOL(int num) {
        System.out.println(num * num);
    }

    public void mOL(int num1, int num2) {
        System.out.println(num1 * num2);
    }

    public void mOL(String message) {
        System.out.println(message);
    }

    /*
    练习3：
    定义三个重载方法max（）：
    第一个方法求两个int值中的最大值，
    第二个方法求两个double值中的最大值，
    第三个方法求三个double值中的最大值，并分别调用三个方法。
     */
    public int max(int i, int j) {
        return (i >= j) ? i : j;
    }

    public double max(double d1, double d2) {
        return (d1 >= d2) ? d1 : d2;
    }

    public double max(double d1, double d2, double d3) {
        double tempMax = max(d1, d2);
        return max(tempMax, d3);
        // return (tempMax >= d3) ? tempMax : d3;
        // return ((max(d1, d2) >= d3) ? max(d1, d2) : d3;
    }


}
