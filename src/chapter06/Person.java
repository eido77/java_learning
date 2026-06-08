package chapter06;

public class Person {

    //属性
    String name; // 姓名
    int age; // 年龄
    char gender; // 性别


    //方法
    // eat 方法：没有参数，调用时直接 p1.eat()
    public void eat() {
        System.out.println("吃饭");
    }
    // sleep 方法：带一个 int 参数 hour（小时数）
    // 调用时必须传一个整数，例如 p1.sleep(8)，8 会被装进 hour
    public void sleep(int hour) {
        System.out.println("人至少保证每天" + hour + "小时的睡眠");
    }
    // interests 方法：带一个 String 参数 hobby（爱好）
    // 调用时必须传一个字符串，例如 p1.interests("打篮球")
    public void interests(String hobby) {
        System.out.println("我的爱好是" + hobby);
    }








}
