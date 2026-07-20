package chapter07;

public class GeometricTest {
    /*
    geometric-object-hierarchy类关系图.png
    案例：
    定义三个类，父类GeometricObject代表几何形状，子类Circle1代表圆形，MyRectangle代表矩形。
    定义一个测试类GeometricTest，
    编写equalsArea方法测试两个对象的面积是否相等（注意方法的参数类型），
    编写displayGeometricObject方法显示对象的面积（注意方法的参数类型）。
     */
    public static void main(String[] args) {
        GeometricTest geometricTest = new GeometricTest();
        Circle1 circle1 = new Circle1("Red", 1.0, 2.3);
        Circle1 circle2 = new Circle1("Blue", 1.0, 3.3);
        geometricTest.displayGeometricObject(circle1); // 几何图形的面积为：16.619025137490002
        geometricTest.displayGeometricObject(circle2); // 几何图形的面积为：34.21194399759285

        boolean isEquals = geometricTest.equalsArea(circle1, circle2);
        if (isEquals) {
            System.out.println("equal");
        } else {
            System.out.println("not equal"); // not equal
        }

        // 使用匿名对象
        // 匿名对象就是创建出来之后没有赋给任何变量的对象。这个对象只用一次。
        geometricTest.displayGeometricObject(new MyRectangle("Blue", 1.0, 2.3, 4.5)); // 几何图形的面积为：10.35
    }

    /**
     * 比较两个几何图形的面积是否相等
     *
     * @param g1
     * @param g2
     * @return true：面积相等 false：面积不相等
     */
    public boolean equalsArea(GeometricObject g1, GeometricObject g2) {
        return g1.findArea() == g2.findArea();
    }

    /**
     * 显示几何图形的面积
     *
     * @param g
     */
    public void displayGeometricObject(GeometricObject g) {
        System.out.println("几何图形的面积为：" + g.findArea());
    }
}
