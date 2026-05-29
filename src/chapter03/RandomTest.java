package chapter03;

public class RandomTest {
    public static void main(String[] args) {
        /*
        如何获取一个随机数？
        1.可以使用Java提供的api，Math类的random()
        2.random()调用后，会返回一个[0.0,1.0)范围的double型的随机数
         */
        double d1 = Math.random();
        System.out.println(d1);

        //3.需求1：获取一个[0,100]范围的随机参数
        int num1 = (int)(Math.random() * 101); // [0,100]
        System.out.println(num1);
        //需求2：获取一个[1,100]范围的随机参数
        int num2 = (int)(Math.random() * 100 + 1);
        //需求3：获取一个[a,b]范围的随机参数
        // num = a = b = 1; // 等价于 a = (b = 1);

        // (int)(Math.random() * (b - a + 1)) + a;



    }
}
