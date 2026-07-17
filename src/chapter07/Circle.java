package chapter07;

public class Circle {
    /*
    案例：
    根据下图实现相关的类。
    在CylinderTest类中创建Cylinder类的对象，设置圆柱的底面半径和高，并输出圆柱的体积。
    Circle-Cylinder.png
     */
    private double radius;

    public Circle() {
        radius = 1;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double fineArea() {
        return Math.PI * radius * radius;
    }

}
