package chapter07;

public class CylinderTest {
    public static void main(String[] args) {
         /*
        案例：
        根据下图实现相关的类。
        在CylinderTest类中创建Cylinder类的对象，设置圆柱的底面半径和高，并输出圆柱的体积。
        Circle-Cylinder.png
         */
        Cylinder cylinder = new Cylinder();
        cylinder.setRadius(3.3);
        cylinder.setLength(1.4);
        System.out.println(cylinder.fineArea()); // 34.21194399759285
        System.out.println(cylinder.findVolume()); // 47.89672159662998

    }
}
