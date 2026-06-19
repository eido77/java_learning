package chapter06;

public class StudentTest {
    public static void main(String[] args) {
        /*
        案例：
        1）定义类Student，包含三个属性：学号number(int)，年级state(int)，成绩score(int)。
        2）创建20个学生对象，学号为1到20，年级和成绩都由随机数确定。
        问题一：打印出3年级(state值为3）的学生信息。
        问题二：使用冒泡排序按学生成绩排序，并遍历所有学生信息
        提示：
        1) 生成随机数：Math.random()，返回值类型double;
        2) 四舍五入取整：Math.round(double d)，返回值类型long。
        年级[1,6] : (int)(Math.random() * 6 + 1)
        分数[0,100] : (int)(Math.random() * 101)
         */

        //创建Student[]
        //创建长度20的对象数组,此时20个元素都是null
        Student[] students = new Student[20]; // String[]

        //使用循环给数组元素赋值
        for (int i = 0; i < students.length; i++) {
            //new 写在循环内：每次都创建新对象、产生新地址；若写在循环外，则 20 个元素共享同一个地址（同一个对象）
            //为每个元素 new 一个独立对象,格子从 null 变为指向真实对象
            students[i] = new Student();

            //给每一个对象的number, state, score 属性赋值
            //通过“对象.属性”赋值
            //后面有 () 就是调用方法,没 () 就是访问属性。
            students[i].number = i + 1;
            students[i].state = (int)(Math.random() * 6 + 1);
            students[i].score = (int)(Math.random() * 101);
        }

        //问题一：打印出3年级(state值为3）的学生信息。
        StudentUtil util = new StudentUtil();
        util.printStudentWithState(students, 3);
        /*
        for (int i = 0; i < students.length; i++) {
            if (students[i].state == 3) {
                //System.out.println("number = " + students[i].number + ", state = " + students[i].state + ", score = " + students[i].score);
                //报错，students 是数组(没有 show 方法),students[i] 才是一个 Student 对象(有 show 方法)。点号左边必须是「对象」
                //students.show();
                System.out.println(students[i].show());
            }
        }
         */

        //问题二：使用冒泡排序按学生成绩排序，并遍历所有学生信息

        //遍历，排序前
        util.printStudents(students);
        /*
        for (int i = 0; i < students.length; i++) {
            System.out.println(students[i].show());
        }
         */
        System.out.println("----------------");

        //排序
        util.sortStudents(students);
        /*
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - 1 - i; j++) {
                if (students[j].score > students[j + 1].score) {
                    //不满足实际需求
                    //int temp = students[j].score;
                    //students[j].score = students[j + 1].score;
                    //students[j + 1].score = temp;

                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;

                }
            }
        }
         */

        //遍历
        util.printStudents(students);
        /*
        for (int i = 0; i < students.length; i++) {
            System.out.println(students[i].show());
        }
         */



    }
}
