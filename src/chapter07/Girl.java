package chapter07;

public class Girl {
    /*
    案例：
    根据图示，添加必要的构造器，综合应用构造器的重载，this关键字。
    BoyGirl.png
     */

    private String name;
    private int age;

    public Girl(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void marry(Boy boy) {
        System.out.println("我想嫁给" + boy.getName());
        boy.marry(this);
    }

    // 比较两个Girl对象的大小
    // 正数：当前对象大  负数：当前对象小（或形参girl大）  0:相等
    public int compare(Girl girl) {
        if (this.age > girl.age) {
            return 1;
        } else if (this.age < girl.age) {
            return -1;
        } else {
            return 0;
        }
    }

}
