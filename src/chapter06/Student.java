package chapter06;

public class Student {
    /*
    对象数组
    1. 何为对象数组？如何理解？
    数组的元素可以是基本数据类型，也可以是引用数据类型。当元素是引用类型中的类时，我们称为对象数组。
    2. 举例：
    String[],Person[],Student[],Customer[]等


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
    int number; // 学号
    int state; //年级
    int score; // 成绩

    //声明一个方法，显示学生的属性信息
    public String show() {
        return "number = " + number + ", state = " + state + ", score = " + score;
    }


}
