package chapter07;

public class GeometricObject {
    /*
    geometric-object-hierarchy类关系图.png
    案例：
    定义三个类，父类GeometricObject代表几何形状，子类Circle1代表圆形，MyRectangle代表矩形。
    定义一个测试类GeometricTest，
    编写equalsArea方法测试两个对象的面积是否相等（注意方法的参数类型），
    编写displayGeometricObject方法显示对象的面积（注意方法的参数类型）。
     */
    protected String color;
    protected double weight;

    protected GeometricObject(String color, double weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double findArea() {
        return 0.0;
    }
}
