package chapter02;

public class CompareTest {
    public static void main(String[] args) {
        /*
        比较运算符
        == != > < >= <= instanceof(这个以后讲解)
        > < >= <=适用于除boolean类型以外的7种基本数据类型，运算的结果为boolean类型
        == != 适用于引用数据类型,boolean(结果为:boolean)
         */
        int m1 = 10;
        int m2 = 20;
        boolean compare1 = m1 > m2;
        System.out.println(compare1); // false

        int n1 = 10;
        int n2 = 20;
        System.out.println(n1 == n2); // false
        System.out.println(n1 = n2); // 20，把n2值赋值给n1，然后打印n1的值

        boolean b1 = false;
        boolean b2 = true;
        System.out.println(b1 == b2); //false
        System.out.println(b1 = b2); //true


    }
}
