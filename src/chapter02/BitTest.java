package chapter02;

public class BitTest {
    public static void main(String[] args) {
        /*
        位运算符
        << >> >>> & | ^ ~
        针对数值类型的变量或常量进行运算，运算结果也是数值

         */

        //左移 <<：一定范围内，每向左移动一位，结果就在原有基础上*2（正负都适用）
        //左移 n 位等价于乘以 2 的 n 次方
        int num1 = 7;
        System.out.println("num1 << 1 ：" + (num1 << 1)); // 14
        System.out.println("num1 << 2 ：" + (num1 << 2)); // 28
        System.out.println("num1 << 3 ：" + (num1 << 3)); // 56
        System.out.println("num1 << 28 ：" + (num1 << 28)); // 1879048192
        System.out.println("num1 << 29 ：" + (num1 << 29)); // -536870912

        int num2 = -7;
        System.out.println("num2 << 1 ：" + (num2 << 1)); // -14
        System.out.println("num2 << 2 ：" + (num2 << 2)); // -28
        System.out.println("num2 << 3 ：" + (num2 << 3)); // -56

        //高效的方式计算2*8
        //2 << 3;或者8 << 1;

        //右移 >>：一定范围内，每向右移动一位，结果就在原有基础上/2（正负都适用）
        //右移 n 位等价于除以 2 的 n 次方（向下取整，3.9取3）
        //正数右移：最终趋向 0，负数右移（算术右移）：最终趋向 -1，不会变成正数

        //无符号右移，逻辑右移 >>>
        //正数：结果和 >> 完全一样（最终趋向 0）
        //负数：会立刻变成一个很大的正数，然后继续右移也趋向 0
        int i1 = -1;
        int i2 = -1 >>> 1;
        System.out.println(i2); // 2147483647

        //按位与 &：对应位都是1才是1（真值表 1&1=1，其余=0）
        //判断奇偶：x & 1，结果为 1 是奇数，0 是偶数（比 x % 2 快，且对负数更安全）

        //按位或 |：对应位有1就是1（真值表 0|0=0，其余=1）
        //常用：设置位为1、合并flag

        //按位异或 ^：对应位不同为1，相同为0
        //交换律：a ^ b = b ^ a
        //结合律：(a ^ b) ^ c = a ^ (b ^ c)
        //自反性：a ^ a = 0，以及 a ^ 0 = a
        //判断两数符号是否相同：(a ^ b) >= 0 为真则同号（异号时最高位会变成 1，结果为负）

        //按位取反 ~：单目，0变1、1变0
        //等价：~x = -x - 1
        //     -x = ~x + 1
        //常用：配合&清零某些位

        //如何交换两个int型变量的值？Spring呢？
        int m = 10;
        int n = 20;
        System.out.println("m = " + m + " n = " + n);
        //方式1：声明一个临时变量（推荐），交换变量的值
        int temp = m;
        // int 是用来"声明"一个新变量的，不是用来"使用"变量的。一个变量只需要声明一次。
        // int m = n; // 会报错，m 已经存在了，不能重复声明。
        // int xx = 1; int xx = 2; // 这样也会报错，int只是声明的
        m = n;
        n = temp;
        System.out.println("m = " + m + " n = " + n);

        //方式2,不需要定义临时变量，难，适用性差（不适用于非数值类型），可能超出int范围
        int mm = 10;
        int nn = 20;
        mm = mm + nn; // nn没变， mm为全部
        nn = mm - nn; // 全部 - nn，现在是原本的mm放到nn，但是mm此时还是全部，没改变值
        mm = mm - nn; //原本的全部 - 现在的nn（现在nn已经是最开始的mm了）
        System.out.println("mm = " + mm + " nn = " + nn);

        //方式3，不需要定义临时变量，更难，适用性差（不适用于非数值类型）
        //交换律：a ^ b = b ^ a
        //结合律：(a ^ b) ^ c = a ^ (b ^ c)
        //自反性：a ^ a = 0，以及 a ^ 0 = a
        //x = k ^ y = (x ^ y ) ^ y
        //x ^ y ^ x = x ^ x ^ y = 0 ^ y = y
        int x = 10;
        int y = 20;
        x = x ^ y;
        y = x ^ y;
        x = x ^ y;
        System.out.println("x = " + x + " y = " + y);




    }
}
