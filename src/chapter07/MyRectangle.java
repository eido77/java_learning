package chapter07;

public class MyRectangle extends GeometricObject{
    /*
    geometric-object-hierarchy类关系图.png
    案例：
    定义三个类，父类GeometricObject代表几何形状，子类Circle1代表圆形，MyRectangle代表矩形。
    定义一个测试类GeometricTest，
    编写equalsArea方法测试两个对象的面积是否相等（注意方法的参数类型），
    编写displayGeometricObject方法显示对象的面积（注意方法的参数类型）。
     */
    private double width; // 宽
    private double height; // 高

    public MyRectangle(String color, double weight, double width, double height) {
        super(color, weight);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double findArea() {
        return width * height;
    }
}
