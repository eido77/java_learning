package chapter07;

public class Boy {
    /*
    案例：
    根据图示，添加必要的构造器，综合应用构造器的重载，this关键字。
    BoyGirl.png
     */

    private String name;
    private int age;

    // cmd + N直接选中就能出来，然后选Getter 和 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 和上面一样操作，选择构造函数，之后选最上面的圆括号里面就是空的
    public Boy() {
    }

    // 选下面两个的先后顺序不一样，圆括号里面的顺序也不一样
    public Boy(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Boy(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void marry(Girl girl) {
        System.out.println("我想娶" + girl.getName());
    }

    public void shout() {
        if (age >= 22) {
            System.out.println("结婚");
        } else {
            System.out.println("不能结婚");
        }
    }
}
