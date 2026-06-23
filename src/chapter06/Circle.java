package chapter06;

public class Circle {
    /*
    案例：
    利用面向对象的编程方法，设计类Circle计算圆的面积。
     */

    //属性
    double radius; // 半径

    //方法
    //文档注释Javadoc（/** */）在调用处悬停能弹出说明，其他文件调用处也能显示说明
    /**
     * 计算圆的面积。
     *
     * @return 面积
     */
    public double findArea() {
        return Math.PI * radius * radius;
    }
    //不推荐这个方法
    public void findArea2(double r){
        System.out.println("面积2为:" + 3.14 * r * r);
    }
    public void findArea3(){
        System.out.println("面积3为:" + 3.14 * radius * radius);
    }

    /*
    1. 定义一个Circle类，包含一个double型的radius1属性代表圆的半径，一个findArea1()方法返回圆的面积。
    2. 定义一个类PassObject，在类中定义一个方法printAreas()，该方法的定义如下：
         public void printAreas(Circle c, int time)。
    3. 在printAreas方法中打印输出1到time之间的每个整数半径值，以及对应的面积。
        例如，time为5，则输出半径1，2，3，4，5，以及对应的圆面积。
    4. 在main方法中调用printAreas()方法，调用完毕后输出当前半径值。程序运行结果如图所示。
     */

    double radius1;

    public double findArea1(){
        return Math.PI * radius1 * radius1;
    }

}
