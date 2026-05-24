package chapter02;
//整型、浮点型变量的使用
public class Variable2 {
    public static void main(String[] args) {

        //整型变量「byte、short、int、long」的使用
        /*
        1B=8b（1Byte/字节=8bit/位）
        byte 占用存储空间：1B=8b   范围：-128～127
        short 2字节=16bit    -2的十五次方～2的十五次方-1
        int 4字节      -2的三十一次方～2的三十一次方-1
        long 8字节     ·2的六十三次方～2的六十三次方-1
         */

        byte b1 = 127;

        short s1 = 128;

        //定义整型变量时，如果无特殊情况，通常声明为int类型
        int i1 = 129;

        //long类型变量时，后缀必须用L或者l结束
        //输出结果不带L
        long l1 = 130L;
        System.out.println(l1);

        /*
        浮点类型「float、double」，不适合在不容许舍入误差的金融计算领域。
        如果要精确数字计算或保留指定位数的精度，需要使用BigDecimal类。
        单精度float（尾数精确到7位有效数字）    占用4B=4字节
        双精度double（精度是float两倍，通常用此类型）   占用8B=8字节
         */
        //定义浮点型变量时，如果无特殊情况，通常声明为double类型
        double d1 = 131.1;

        //声明float变量时，后缀必须用F或f结束
        //输出结果不带F
        float f1 =131.2f;
        System.out.println(f1);

        //测试浮点型变量精度
        System.out.println(0.1 + 0.2); // 输出为0.30000000000000004

        float f11 = 213224214f;
        float f22 = f11 + 1;
        System.out.println(f11);
        System.out.println(f22);
        //浮点型变量精度不高，要提高精度，需要使用BigDecimal类替换
        System.out.println(f11 == f22); // 结果为true

    }
}
