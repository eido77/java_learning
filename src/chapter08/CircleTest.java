package chapter08;

public class CircleTest {
    public static void main(String[] args) {
        Circle c1 = new Circle();
        System.out.println(c1); // Circle{radius=0.0, id=1001}

        Circle c2 = new Circle();
        System.out.println(c2); // Circle{radius=0.0, id=1002}

        Circle c3 = new Circle();
        System.out.println(c3); // Circle{radius=0.0, id=1003}

        System.out.println(Circle.total); // 3

        Circle c4 = new Circle(2.3);
        System.out.println(c4); // Circle{radius=2.3, id=1004}

        System.out.println(Circle.total); // 4
    }
}

class Circle {
    private double radius;
    private int id;

    static int total; // 创建的Circle实例的个数
    private static int init = 1001; // 自动给id赋值的基数

    public Circle() {
        this.id = init;
        init++;
        total++;
    }

    public Circle(double radius) {
        // this(): 构造器互调，调用本类的无参构造器 Circle()
        // 作用：复用无参构造器中的 id 分配与计数逻辑，避免重复代码
        this();
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "radius=" + radius +
                ", id=" + id +
                '}';
    }
}
