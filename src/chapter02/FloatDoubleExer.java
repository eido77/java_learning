package chapter02;

public class FloatDoubleExer {
    public static void main(String[] args) {

        //案例1 定义圆周率并赋值为3.14，现有3个圆的半径分别为1.2、2.5、6，求它们的面积。
        //先定义圆周率，浮点型通常用double
        double pi = 3.14;

        //然后定义三个圆半径
        double radius1 = 1.2;
        double radius2 = 2.5;
        double radius3 = 6;

        //计算面积
        //如果是高次方可以用Math.pow(), "Math.pow(2, 10"2的十次方
        double area1 = pi * radius1 * radius1;
        double area2 = pi * radius2 * radius2;
        double area3 = pi * radius3 * radius3;

        //输出
        System.out.println("圆1的半径为：" + radius1 + "面积为：" + area1);
        System.out.println("圆2的半径为：" + radius2 + "面积为：" + area2);
        System.out.println("圆3的半径为：" + radius3 + "面积为：" + area3);

        /*
        案例2：小明要到美国旅游，可是那里的温度是以华氏度为单位记录的。
        它需要一个程序将华氏温度（80度）转换为摄氏度，并以华氏度和摄氏度为单位分别显示该温度。
        ℃ = (℉ - 32) / 1.8
         */

        double tempF = 80;

        double tempC = (tempF - 32) / 1.8;

        System.out.println("华氏度为" + tempF + "°F 对应的摄氏度为" + tempC + "°C");


    }
}
