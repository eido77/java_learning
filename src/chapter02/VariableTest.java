package chapter02;

public class VariableTest {
    public static void main(String[] args) {

        /*
        测试基本数据类型变量间的运算规则
        1.这里提到的可以做运算的基本数据类型有7种（不包含boolean类型）
        2.运算规则包括：
            （1）自动类型提升
            （2）强制类型转换（手动实现）
        3.此VariableTest.java用来测试自动类型提升
        规则：当容量小的变量与容量大的变量做运算时，结果自动转化为容量大的数据类型。

        byte、short、char ---> int ---> long ---> float ---> double
        特殊：byte、short、char类型的变量之间做运算，结果为int类型
        参与算术运算 --> 必须先变int，单纯赋值 --> 可以跳级，不用经过int

        说明：此时的容量小和大，并非指占用的内存空间大小，而是指表示数据的范围的大小。
             long占8字节，float占4字节，
         */

        int i1 = 10;
        int i2 = i1;

        //上面的3.
        long l1 = i1;
        float f1 = l1;
        byte b1 = 12;
        int i3 = b1 + i1;
        //byte b2 = b1 + i1;

        //特殊1：byte、short之间做运算
        byte b2 = 10;
        short s1 = 12;
        //编译不通过
        //short s2 = b1 + b2;
        int i4 = b2 + s1;

        byte b3 = 11;
        //编译不通过
        //byte b4 = b3 + b2;

        //特殊2：char
        char c1 = 'a';
        //编译不通过
        //char c2 = c1 + b3;
        int i5 = c1 + b3;


        //练习1
        long l2 = 123L;

        //***********见72行
        //整数不加后缀是int，有小数点或科学计数法不加后缀是double（只看数字）
        byte b6 = 10 + 1; //11是int，在byte的范围内，通过
        float f4 = 12; // 12是int，自动类型提升

        //理解为：自动类型提升，123是int，int --> long，“小变大”
        long l3 = 123;
        //理解为：int类型，因为超出了int范围，所以报错
        //long l4 = 123123213213213;

        long l5 = 123123213213213L; // 此时就是使用8字节存储的long类型的值

        //练习2
        float f2 = 12.3f;

        //12.3是double，double --> float，不能“大变小”，报错
        //float f3 = 12.3;

        //练习3**********见54行
        //当byte/short/char参与算术运算时，操作数会被自动提升为int，结果至少是int。
        //b4是byte变量，不是编译常量，b4提升为int，结果是int。
        byte b4 = 10;
        //byte b5 = b4 + 1;
        //整型常量规定为int
        int i6 = b4 + 1;

        long l4 = 10 + 1; // 自动提升，int11 --> long
        float f3 = 10 + 1; // 自动提升，int11 --> float
        //浮点型常量规定为double
        double d1 = 12.3 + b4;

        //变量名不能是数字开头
        //int 123L = 12;
        long l6 = 123L;


    }
}
