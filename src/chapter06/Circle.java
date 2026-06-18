package chapter06;

public class Circle {
    /*
    案例：
    利用面向对象的编程方法，设计类Circle计算圆的面积。
     */

    //属性
    double radius; // 半径

    //方法
    //文档注释Javadoc（/** */）在调用处悬停能弹出说明，其他文件调用处也能显示说明
    /**
     * 计算圆的面积。
     *
     * @return 面积
     */
    public double findArea() {
        return Math.PI * radius * radius;
    }
    //不推荐这个方法
    public void findArea2(double r){
        System.out.println("面积2为:" + 3.14 * r * r);
    }
    public void findArea3(){
        System.out.println("面积3为:" + 3.14 * radius * radius);
    }

}
