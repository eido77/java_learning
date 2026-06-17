package chapter06;

public class Person {
    /*
    案例：
    (1)创建Person类的对象，设置该对象的name、age和gender属性，
    调用study方法，输出字符串“studying”;
    调用showAge()方法，返回age值;
    调用addAge(int addAge)方法给对象的age属性值增加addAge岁。比如：2岁。
    (2)创建第二个对象，执行上述操作，体会同一个类的不同对象之间的关系。
     */

    //属性
    String name; // 姓名
    int age; // 年龄
    char gender; // 性别

    //案例
    //调用study方法，输出字符串“studying”;
    public void study() {
        System.out.println("studying");
    }
    //调用showAge()方法，返回age值;
    public int showAge() {
        return age;
    }
    //调用addAge(int addAge)方法给对象的age属性值增加addAge岁。比如：2岁。
    public void addAge(int addAge) {
        age += addAge;
    }


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
