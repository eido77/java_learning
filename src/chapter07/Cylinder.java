package chapter07;

public class Cylinder extends Circle{
    /*
    案例：
    根据下图实现相关的类。
    在CylinderTest类中创建Cylinder类的对象，设置圆柱的底面半径和高，并输出圆柱的体积。
    Circle-Cylinder.png
     */
    private double length;

    public Cylinder() {
        length = 1;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double findVolume() {
        return fineArea() * getLength();
    }

}
