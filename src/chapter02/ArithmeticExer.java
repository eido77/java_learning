package chapter02;

public class ArithmeticExer {
    public static void main(String[] args) {
        /*
        随意给出一个整数，打印显示它的个位数，十位数，百位数的值。
        格式如下：
        数宁xxx的情况如下：
        个位数：
        十位数：
        百位数：

        例如：
        数字153的情况如下：
        个位数：3
        十位数：5
        百位数：1
         */
        int num = 153;
        int num1 = 153 % 10; // 个位
        int num2 = 153 % 100 / 10; // 十位，或者int num2 = 153 / 10 % 10;
        int num3 = 153 / 100; // 百位

        System.out.println("个位数：" + num1);
        System.out.println("十位数：" + num2);
        System.out.println("百位数：" + num3);

        //1234，个十百千
        int a1 = 1234 % 10;
        int a2 = 1234 % 100 / 10; // 或者，int a2 = 1234 / 10 % 10;
        int a3 = 1234 / 100 % 10;
        int a4 = 1234 / 1000;

        System.out.println("个位数2：" + a1);
        System.out.println("十位数2：" + a2);
        System.out.println("百位数2：" + a3);
        System.out.println("千位数2：" + a4);

        //为抵抗洪水，战士连续作战89小时，编程计算共多少天零多少小时？
        int n1 = 89 % 24; // hour
        int n2 = 89 / 24; // day

        System.out.println("天：" + n2);
        System.out.println("小时：" + n1);

        System.out.println("= " + 5 + 5); // 55
        System.out.println("= " + (5 + 5)); // 10

        byte b1 = 127;
        b1++;
        System.out.println("b1 = " + b1); // -128

        int i1 = 5, j = 10;
        //初始i1 = 5，计算 i1++，i1 立刻自增，i1 = 6（计算使用之前的值），加法，i1 = 6，赋值：把 15 存进 i1，覆盖6
        // = 永远是最后执行的，它会覆盖之前的自增结果。
        i1 = i1++ + j;
        System.out.println(i1); // 输出 15，不是 16

        int i = 1;
        /*
        在 Java 里，规则是：
        优先级让 * 比 + 先组合，所以表达式被解析为：i++ + (++i * i++)
        但实际求值时，Java 严格从左到右计算操作数，不是先算乘法那部分！
        优先级只决定结构，不决定执行顺序。
        左到右取值，按优先级算账。
        i0 = 1 + (3 * 3)
         */
        int i0 = i++ + ++i * i++;
        System.out.println("i0 = " + i0); // 10

        //额外的练习4：
        int m = 2;
        // m++ 把旧值 2 传给 n；m 自己变 3，但这个 3 没传给 n
        int n = m++;
        System.out.println(n); // 2

        int m1 = 2;
        //（1）先取“2”放操作数栈（2）m1自增，m1=3（3）再把操作数栈中的“2”賦值给m1，m1=2，覆盖了3
        m1 = m1++;
        System.out.println(m1); // 2

        //++ 和 -- 单独成行最安全。一旦掺进复杂表达式，就拆开写。



    }
}
