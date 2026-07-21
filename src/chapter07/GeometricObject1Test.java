package chapter07;

import java.util.Objects;

public class GeometricObject1Test {
    /*
    案例：
    定义两个类，父类GeometricObject1代表几何形状，子类Circle2代表圆形。
    写一个测试类，创建两个Circle2对象，判断其颜色是否相等；利用equals方法判断其半径是否相等；
    利用toString()方法输出其半径。
     */
    public static void main(String[] args) {
        Circle2 c1 = new Circle2(2.3);
        Circle2 c2 = new Circle2("red", 2.0, 3.4);
        System.out.println(c1.getColor().equals(c2.getColor())); // false
        System.out.println(c1.equals(c2)); // false
        System.out.println(c1); // Circle2{radius=2.3}
        System.out.println(c1.toString()); // Circle2{radius=2.3}
    }
}

class GeometricObject1 {
    protected String color;
    protected double weight;

    public GeometricObject1() {
        color = "white";
        weight = 1.0;
    }

    public GeometricObject1(String color, double weight) {
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
}

class Circle2 extends GeometricObject1 {
    private double radius;

    public Circle2() {
        // 隐式调用 super()，父类空参构造器已把 color 置为 "white"、weight 置为 1.0
//        color = "white";
//        weight = 1.0;
        radius = 1.0;
    }

    public Circle2(double radius) {
        // 同上：隐式 super() 已完成 color、weight 的默认初始化
//        color = "white";
//        weight = 1.0;
        this.radius = radius;
    }


    public Circle2(String color, double weight, double radius) {
        super(color, weight);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double findArea() {
        return radius * radius * Math.PI;
    }

    // 重写equals()
    // 手写
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (obj instanceof Circle2) {
//            Circle2 circle = (Circle2) obj;
//            return this.radius == circle.radius;
//        }
//        return false;
//    }
    // 自动
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Circle2 circle2 = (Circle2) o;
        return Double.compare(radius, circle2.radius) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(radius);
    }

    // 重写toString()
    // 手动
//    @Override
//    public String toString() {
//        return "Circle2{radius = " + radius + "}";
//    }
    // 自动
    @Override
    public String toString() {
        return "Circle2{" +
                "radius=" + radius +
                '}';
    }
}
