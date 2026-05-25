package chapter02;

public class BinaryTest {
    public static void main(String[] args) {
        /*
        测试常用的进制：

        十进制（decimal）
        数字组成：0-9
        进位规则：满十进一

        二进制（binary）
        数字组成：0-1
        进位规则：满二进一，以0b或0B开头

        八进制（octal）：很少使用
        数字组成：0-7
        进位规则：满八进一，以数字0开头表示

        十六进制
        数字组成：0-9，a-f
        进位规则：满十六进一，以Ox 或 OX 开头表示。此处的 a-f 不区分大小写
         */

        //十进制
        int num1 = 103;
        System.out.println(num1); // 103
        //二进制
        int num2 = 0b10;
        System.out.println(num2); // 2
        //int num3 = 0b20; //报错
        //八进制
        int num3 = 023;
        System.out.println(num3); // 19
        //十六进制
        int num4 = 0x23;
        System.out.println(num4); // 35


    }
}
