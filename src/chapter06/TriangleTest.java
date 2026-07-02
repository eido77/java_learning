package chapter06;

public class TriangleTest {
    public static void main(String[] args) {
        /*
        案例：
        编写两个类，TriAngle和TriAngleTest，其中TriAngle类中声明私有的底边长base和高height，
        同时声明公共方法访问私有变量。此外，提供类必要的构造器。另一个类中使用这些公共方法，计算三角形的面积。
         */
        // 创建TriAngle实例1
        TriAngle t1 = new TriAngle();
        t1.setBase(3.4);
        t1.setHeight(2.3);
        System.out.println(t1.findArea()); // 3.9099999999999997

        // 创建TriAngle实例2
        TriAngle t2 = new TriAngle(2.4, 4.5);
        System.out.println(t2.getBase()); // 2.4
        System.out.println(t2.getHeight()); // 4.5
        System.out.println(t2.findArea()); // 5.3999999999999995

    }
}
