package chapter06;

public class CircleTest {
    public static void main(String[] args) {
        Circle c = new Circle();
        c.radius = 3;
        System.out.println(c.findArea()); // 28.274333882308138
        //不推荐
        c.findArea2(2.3); // 16.610599999999998
        c.radius = 2.4;
        c.findArea3(); // 18.086399999999998

    }
}
