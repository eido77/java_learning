package chapter07;

public class Circle1 extends GeometricObject {
    /*
    geometric-object-hierarchy类关系图.png
    案例：
    定义三个类，父类GeometricObject代表几何形状，子类Circle1代表圆形，MyRectangle代表矩形。
    定义一个测试类GeometricTest，
    编写equalsArea方法测试两个对象的面积是否相等（注意方法的参数类型），
    编写displayGeometricObject方法显示对象的面积（注意方法的参数类型）。
     */
    private double radius;

    public Circle1(String color, double weight, double radius) {
        super(color, weight);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double findArea() {
        return Math.PI * radius * radius;
    }
}
