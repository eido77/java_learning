package chapter02;

/*
定义好变量后，可以通过变量名的方式对变量进行调用、运算
变量只在它的作用域有效。
变量理解：内存中的存储区域，该区域的数据可以在同一类型范围内不断变化
变量构成因素：数据类型、变量名、存储的值
Java中变量声明格式：数据类型 变量名 = 变量值
Java变量按数据类型分类
    1.基本数据类型（8种）
    整型（整数）「byte、short、int、long」，浮点型（小数类型）「float、double、」，字符型「char」，布尔型「boolean」
    2.引用数据类型
    类（class）、数组（array）、接口（interface）、枚举（enum）、注解（annotation）、记录（record）
定义变量时，变量名遵循标识符命名规则和规范。
变量值在赋值时，必须满足变量的数据类型，并在数据类型有效的范围内变化。
 */
public class Variable {
    public static void main(StringTest[] args) {

        //定义变量方式1
        int age = 10;  //声明与初始化合并

        //定义变量方式2
        //gender只在最内层的两个大括号内有效
        //char 只能存放一个字符，字面量只能用单引号 ' '（\n和\t算一个字符）
        char gender;  //变量的声明
        gender = '男';  //变量赋值（或初始化）

        //同一个作用域内，不能声明两个同名的变量
        //char gender = '女';
        gender = '女';

        //number前没声明类型，没有提前定义，所以编译不通过
        //number = 10;

        byte b1 = 127;
        //b1超出了byte范围，编译不通过
        //b1 = 128;

        //不加""括起来age（如果加上就是字符串字面量，会原样输出）
        System.out.println(age);
        //+是连接符
        System.out.println("age = " + age);

        System.out.println(gender);
    }

    public static void main1(StringTest[] args) {
        //char 只能存放一个字符，字面量只能用单引号 ' '（\n、\t算一个字符，Unicode转译也算一个字符）
        char gender = '女';
    }
}