package chapter06;

public class PassObject {
        /*
        1. 定义一个Circle类，包含一个double型的radius1属性代表圆的半径，一个findArea1()方法返回圆的面积。
        2. 定义一个类PassObject，在类中定义一个方法printAreas()，该方法的定义如下：
             public void printAreas(Circle c, int time)。
        3. 在printAreas方法中打印输出1到time之间的每个整数半径值，以及对应的面积。
            例如，time为5，则输出半径1，2，3，4，5，以及对应的圆面积。
        4. 在main方法中调用printAreas()方法，调用完毕后输出当前半径值。程序运行结果如图所示。
         */

        //"这个方法需要别人传进来一个 Circle 类型的对象，我用 c 这个名字来接住它。"
        //它只是声明了一个引用变量 c，类型是 Circle，但没有 new，所以没有真的造出一个新对象。
        public static void main(String[] args) {
            PassObject p = new PassObject();
            Circle circle = new Circle();
            // c 是 printAreas 方法的形参名，circle 是 main 里的实参名
            p.printAreas(circle, 5);

            System.out.println("now radius1 is:" + circle.radius1); // 6.0
        }

        public void printAreas(Circle c, int time) {
            System.out.println("Radius\t\tArea");

            int i = 1;
            for (; i <= time; i++) {
                c.radius1 = i;
                System.out.println(c.radius1 + "\t\t\t" + c.findArea1());
            }

            //演示图要求显示6.0
            c.radius1 = i;
        }


}
