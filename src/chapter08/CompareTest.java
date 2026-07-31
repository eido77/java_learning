package chapter08;

public class CompareTest {
    /*
    定义一个接口用来实现两个对象的比较。
    interface CompareObject{
        //若返回值是 0 , 代表相等; 若为正数，代表当前对象大；负数代表当前对象小
        public int compareTo(Object o);
    }
    定义一个Circle类，声明radius属性，提供getter和setter方法
    定义一个ComparableCircle类，继承Circle类并且实现CompareObject接口。
    在ComparableCircle类中给出接口中方法compareTo的实现体，用来比较两个圆的半径大小。
    定义一个测试类InterfaceTest，创建两个ComparableCircle对象，调用compareTo方法比较两个类的半径大小。
    拓展：参照上述做法定义矩形类Rectangle和ComparableRectangle类，在ComparableRectangle类
    中给出compareTo方法的实现，比较两个矩形的面积大小。
     */
    public static void main(String[] args) {
        CompareCircle c1 = new CompareCircle(2.3);
        CompareCircle c2 = new CompareCircle(5.3);
        // 调用 compareTo 比较两个圆：返回值 >0 说明 c1 大，<0 说明 c2 大，=0 说明相等
        int compareValue = c1.compareTo(c2);
        if (compareValue > 0) {
            System.out.println("c1");
        } else if (compareValue < 0) {
            System.out.println("c2");
        } else {
            System.out.println("=");
        }

        // ------- 拓展：比较两个矩形的面积 -------
        ComparableRectangle r1 = new ComparableRectangle(3, 4);   // 面积 12
        ComparableRectangle r2 = new ComparableRectangle(2, 5);   // 面积 10

        int rectCompare = r1.compareTo(r2);
        if (rectCompare > 0) {
            System.out.println("r1");          // r1 面积更大
        } else if (rectCompare < 0) {
            System.out.println("r2");          // r2 面积更大
        } else {
            System.out.println("=");           // 面积相等
        }
    }
}

// 自定义比较接口：让实现它的类拥有“比较大小”的能力
interface CompareObject {
    //若返回值是 0 , 代表相等; 若为正数，代表当前对象大；负数代表当前对象小
    public int compareTo(Object o);
}

// 圆类：只负责封装 radius 属性和它的 getter / setter，不涉及比较逻辑
class Circle1 {
    private double radius;

    public Circle1() {
    }

    public Circle1(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle1{" +
                "radius=" + radius +
                '}';
    }
}

// 可比较的圆：继承 Circle1 拿到 radius，实现 CompareObject 获得比较能力
class CompareCircle extends Circle1 implements CompareObject {
    public CompareCircle() {
    }

    public CompareCircle(double radius) {
        super(radius);
    }

    // 根据对象的半径的大小，比较对象的大小
    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }

        // 先判断类型，避免向下转型时抛 ClassCastException
        if (o instanceof CompareCircle) {
            CompareCircle compareCircle = (CompareCircle) o;

            // option + enter 可以强转int
            /*
           【错误示范】：直接相减再强转 int，会丢失小数精度，
            比如 2.3 - 5.3 = -3.0 转成 int 恰好还对，但 2.3 - 2.8 = -0.5 转 int 变成 0，
            结果把“不相等”误判成“相等”，所以不能这么写
              */
//            return (int) (this.getRadius() - compareCircle.getRadius());

            // 正确写法1
//            if (this.getRadius() > compareCircle.getRadius()) {
//                return 1;
//            } else if (this.getRadius() < compareCircle.getRadius()) {
//                return -1;
//            } else {
//                return 0;
//            }
            // 正确写法2（推荐）：Double.compare 内部已处理好精度，直接返回 -1/0/1
            return Double.compare(this.getRadius(), compareCircle.getRadius());
        } else {
            // 传入类型不是 CompareCircle，无法比较，这里约定返回 2 表示“类型不匹配”
            return 2;
        }
    }
}

// ==================== 拓展：矩形比面积 ====================

// 矩形类：封装 length（长）和 width（宽）两个属性，只管数据，不管比较
class Rectangle {
    private double length;
    private double width;

    public Rectangle() {
    }

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    // 求面积：把“长 × 宽”封装成方法，比较时直接调用，避免重复写乘法
    public double getArea() {
        return length * width;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "length=" + length +
                ", width=" + width +
                '}';
    }
}

// 可比较的矩形：继承 Rectangle 拿到长宽和 getArea()，实现 CompareObject 获得比较能力
class ComparableRectangle extends Rectangle implements CompareObject {
    public ComparableRectangle() {
    }

    public ComparableRectangle(double length, double width) {
        super(length, width);   // 调用父类构造器给 length、width 赋值
    }

    // 按“面积”大小比较两个矩形（注意：圆比的是半径，这里比的是面积）
    @Override
    public int compareTo(Object o) {
        // 同一个对象直接判定相等
        if (this == o) {
            return 0;
        }

        // 先判断类型，避免向下转型时抛 ClassCastException
        if (o instanceof ComparableRectangle) {
            ComparableRectangle rect = (ComparableRectangle) o;
            // 同样别用相减强转 int，会丢精度，比如面积差 0.5 会被截成 0 误判相等
            // 直接用 getArea() 拿到两个面积，交给 Double.compare 处理，返回 -1/0/1
            return Double.compare(this.getArea(), rect.getArea());
        } else {
            // 类型不匹配，约定返回 2
            return 2;
        }
    }
}
