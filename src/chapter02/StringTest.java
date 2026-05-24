package chapter02;

public class StringTest {
    public static void main(String[] args) {

        /*
        基本数据类型与String的运算
        1.String类，属于引用数据类型，俗称字符串
        2.String类型的变量可以使用一对""的方式进行赋值
         */
        String str1 = "Hello World";
        System.out.println("str1");
        System.out.println(str1);

        //3.String声明的字符串内部，可以包含0个、1个或多个字符
        String str2 = "";
        String str3 = "a";

        /*
        String与基本数据类型变量间的运算
        这里的基本数据类型包括所有的8种
         */

        //String与基本数据类型变量间，只能用 + 做连接运算
        //测试连接运算
        int num1 = 10;
        boolean b1 = true;
        String str4 = "Hello";
        //编译出错，+ 想做连接，必须让String先到场。
        //只要从左到右算的过程中，String出现得够早，boolean、int等都可以进到字符串里
        //System.out.println(b1 + num1 + str4);
        System.out.println(b1 + str4 + num1);

        String str5 = str4 + b1 + num1;
        System.out.println(str5);

        //能将String类型的变量转换为基本数据类型吗？
        int num2 = 10;
        String str6 = num2 + ""; // 10
        System.out.println(str6);
        String str7 = "abc"; // 不能考虑转化为基本数据类型
        //int num3 = (int) str6; // 编译不通过

        //如何实现？使用Integer类（以后学）
        int num3 = Integer.parseInt(str6);
        System.out.println(num3 + 1);
    }
}
