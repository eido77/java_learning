package chapter02;

public class CharBoolean {
    public static void main(String[] args) {

        /*
        字符类型char，占用两个字节
        Java中所有字符都使用Unicode编码，一个字符可以存储一个字母、一个汉字、一个数字符
         */
        //表示形式1：使用''单引号来表示，有且仅有一个字符，不可以没有
        char c1 = 'a';
        char c2 = '一';
        char c3 = '1';
        char c4 = '℃';
        char c5 = 'Ɣ';

        //编译不通过
        //char c6 = '';
        //char c7 = 'ab';

        //表示形式2：之间用Unicode来表示字符型常量（区分大小写），https://www.compart.com/en/unicode/
        char c8 = '\u0043'; // 输出为C
        System.out.println(c8);

        //表示形式3：使用转义字符“\”
        char c9 = '\n';
        char c10 = '\t';
        System.out.println("Hello" + c9 + "World");
        System.out.println("Hello" + c10 + "World");

        //表示形式4：具体字符对应的数值，ASCII码(a是97，A是65，0是48，1是49...)
        char c11 = 97; // a
        System.out.println(c11);

        char c12 = '1'; // 数字1
        char c13 = 1; // ASCII码上的1


        /*
        布尔类型boolean：数据类型只有两个（true、false）
        不谈占用的空间大小，但真正在内存中分配的话，使用的是4字节
         */
        boolean bo1 = true;
        boolean bo2 = false;

        //常使用在流程控制语句中（条件判断、循环结构等）
        boolean isMarried = true;
        //boolean isMarried = false;
        if (isMarried) {
            System.out.println("很遗憾不能参见单身派对了");
        } else {
            System.out.println("可以多谈几个男/女朋友");
        }

    }

}
