package chapter07;

import java.util.Random;

public class InterviewTest {
    // 面试题：多态是编译时行为还是运行时行为？运行时
    // key 由 new Random().nextInt(3) 随机产生，编译期无法确定它是 0、1 还是 2，
    // 因此编译器无法知道 getInstance(key) 究竟返回 Cat、Dog 还是 Sheep 的对象。
    // 编译时：animal 的编译类型是 Animal，编译器只检查 Animal 类中有没有 eat() 方法（有，通过）。
    // 运行时：JVM 看的是 animal 实际指向的对象的运行时类型，动态绑定到该子类重写的 eat()。
    //       所以每次运行输出可能不同——这正说明方法调用的具体版本是运行期才确定的，也就是俗称的"虚方法调用"或"动态绑定"。
    // 补充：只有重写(override)的方法是动态绑定；
    // 属性、static 方法、private/final 方法都是静态绑定，看编译类型。
    public static Animal getInstance(int key) {
        switch (key) {
            case 0:
                return new Cat();
            case 1:
                return new Dog();
            default:
                return new Sheep();
        }
    }

    public static void main(String[] args) {
        int key = new Random().nextInt(3); // 0, 1, 2
        System.out.println(key);

        Animal animal = getInstance(key);
        animal.eat();
    }
}

class Animal {
    protected void eat() {
        System.out.println("animal eat food");
    }
}

class Cat extends Animal {
    protected void eat() {
        System.out.println("cat eat fish");
    }
}

class Dog extends Animal {
    public void eat() {
        System.out.println("Dog eat bone");
    }
}

class Sheep extends Animal {
    public void eat() {
        System.out.println("Sheep eat grass");
    }
}
