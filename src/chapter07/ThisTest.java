package chapter07;

public class ThisTest {
    /*
    this关键字的使用
    1. 目前可能出现的问题？及解决方案？
    我们在声明一个属性对应的setXxx方法时，通过形参给对应的属性赋值。如果形参名和属性名同名了，那么该如何在方法内区分这两个变量呢？
    解决方案：使用this。
    具体来讲：使用this修饰的变量表示属性，没有用this修饰的表示的是形参。

    2. this可以调用的结构：
    成员变量、方法、构造器。

    3. this的理解：
    当前对象 或 当前正在创建的对象

    4.1 this调用属性和方法
    针对于方法内的使用情况：（准确的说是非static修饰的方法）

    针对于构造器内的使用情况：

    4.2 this调用构造器
     */
}

class Person1 {
    String name;
    int age;

    public Person1(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge1(int age) {
        this.age = age;
    }

    public int getAge1() {
        // return this.age; // this可加可不加
        return age;
    }
}

