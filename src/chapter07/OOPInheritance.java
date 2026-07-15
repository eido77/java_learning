package chapter07;

public class OOPInheritance {
    /*
    面向对象特征之二：继承性
    1. 继承性的理解
    > 自上而下：定义了一个类A，在定义另一个类B时，发现类B的功能与类A相似，考虑类B继承于类A
    > 自下而上：定义了类B，C，D等，发现B、C、D有类似的属性和方法，则可以考虑将相同的属性和方法进行抽取，
              封装到类A中，让类B、C、D继承于类A，同时，B、C、D中的相似的功能就可以删除了。
    2. 继承性的好处
    继承的出现减少了代犯冗余，提高了代码的复用性。
    继承的出现，更有利于功能的扩展。
    继承的出现让类与类之间产生了'is-a‘的关系，力多态的使用提供了前提。
    - 继承描述事物之间的所属关系，这种关系是：‘is-a’的关系。可见，父类更通用、更一般，子类更具体。
    3. 继承的格式：
    class A {
        // 属性、方法
    }

    class B extends A {

    }
    继承中的基本概念：
    类A：父类、superClass、超类、基类
    类B：子类、subClass、派生类
    4. 有了继承性以后：
    子类就获取到了父类中声明的所有属性和方法。
    > 由于封装性的影响，可能子类中不能直接调用父类中声明的属性或方法。
    子类继承父类之后，还可以拓展自己特有的功能（extends：延展、扩展、延伸）
    5. 默认的父类：
    java中声明的类，如果没有显式的声明其父类时，则默认继承于 java.lang.Object
    6. 补充说明：
    (1)支持多层继承，直接父类、间接父类
    class A {}
    class B extends A {}
    class C extends B {}
    (2)一个父类可以同时声明多个子类，一个子类只能有一个父类（java的单继承性）
     */

}

class Person2 {
    String name;
    int age;

    public void eat() {
        System.out.println("eating");
    }

    public void sleep() {
        System.out.println("sleeping");
    }
}

class Student extends Person2 {
//    String name;
//    int age;

    String school;

//    public void eat() {
//        System.out.println("eating");
//    }
//
//    public void sleep() {
//        System.out.println("sleeping");
//    }

    public void study() {
        System.out.println("studying");
    }
}
