package chapter08;

public class AbstractTest {
    /*
    抽象类与抽象方法
    1. 案例引入
    举例1：GeometricObject-Circle-Rectangle
    abstract class GeometricObject{  //几何图形（父类）
        //求面积 （只能考虑提供方法的声明，而没有办法提供方法体。所以，此方法适合声明为抽象方法）
        //求周长（只能考虑提供方法的声明，而没有办法提供方法体。所以，此方法适合声明为抽象方法）
    }

    class Circle extends GeometricObject{
        //求面积 （必须重写（或实现）父类中的抽象方法）
        //求周长（必须重写（或实现）父类中的抽象方法）
    }

    class Rectangle extends GeometricObject{
        //求面积 （必须重写（或实现）父类中的抽象方法）
        //求周长（必须重写（或实现）父类中的抽象方法）
    }

    举例2：Account-SavingAccount-CheckAcount
    abstract class Account{
        double balance;//余额
        //取钱 （声明为抽象方法）
        //存钱 （声明为抽象方法）
    }

    class SavingAccount extends Account{ //储蓄卡
        //取钱 （需要重写父类中的抽象方法）
        //存钱（需要重写父类中的抽象方法）
    }

    class CheckAccount extends Account{ //信用卡
        //取钱（需要重写父类中的抽象方法）
        //存钱（需要重写父类中的抽象方法）
    }
    2. abstract的概念：抽象的
    3. abstract可以用来修饰：类、方法
    4. 具体的使用：
    4.1 abstract修饰类：
        > 此类称为抽象类
        > 抽象类不能实例化。（不能 new 对象）
        > 抽象类中是包含构造器的，因为子类对象实例化时，需要通过super()直接或间接的调用到父类的构造器。
        > 抽象类中可以没有抽象方法。反之，抽象方法所在的类，一定是抽象类。
    4.2 abstract修饰方法：
        > 此方法即为抽象方法
        > 抽象方法只有方法的声明，没有方法体。（连 {} 都没有，直接以分号结尾）
        > 抽象方法其功能是确定的（通过方法的声明即可确定），只是不知道如何具体实现（没有方法体）
        > 子类必须重写父类中的所有的抽象方法之后，方可实例化。只要还有抽象方法没重写，该子类本身也必须声明为抽象类
    5. abstract不能使用的场景：
    5.1 abstract 不能修饰哪些结构？
    属性、构造器、代码块等。
    5.2 abstract 不能与哪些关键字共用？
    不能用abstract修饰私有方法、静态方法、final的方法、final的类。
        > private（私有）：私有方法不能被子类重写，而抽象方法就是要子类重写，矛盾
        > static（静态）：静态方法可通过"类名.方法"调用，但抽象方法没有实现，不能被调用，矛盾
        > final（方法）：final 方法不能被重写，与抽象方法必须被重写矛盾
        > final（类）：final 类不能有子类，而抽象类必须靠子类实现，矛盾
     */
    public static void main(String[] args) {
        // Person1 is abstract; cannot be instantiated
        // 抽象类 Person1 不能实例化，下面两行会编译报错
//        Person1 p1 = new Person1();
//        p1.eat();

        // 通过子类对象来使用（多态：Person1 p = new Student(); 也可以）
        Student s1 = new Student();
        s1.eat();
    }
}

//class Person1 { // 普通类写法（对比用）
abstract class Person1 { // 抽象类（完整写法：public abstract class Person1）
    String name;
    int age;

    // 抽象类有构造器，供子类通过 super() 调用
    public Person1() {
    }

    public Person1(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 抽象方法：只有声明，没有方法体，以分号结尾
//    public void eat() { // 普通方法写法（对比用）
    public abstract void eat();
    // abstract methods cannot have a body
    // 抽象方法不能有方法体，下面写法是错误的
//    {    System.out.println("eating");
//    }

    public abstract void sleep(); // 抽象方法
}

// 子类 Student：重写了父类【所有】抽象方法，因此是普通类，可以实例化
class Student extends Person1 {
    String school;

    public Student() {
    }

    public Student(String name, int age, String school) {
        super(name, age); // 调用父类（抽象类）的构造器
        this.school = school;
    }

    @Override
    public void eat() {
        System.out.println("student eating");
    }

    @Override
    public void sleep() {
        System.out.println("student sleeping");
    }
}

// 子类 Worker：同样重写了所有抽象方法
class Worker extends Person1 {
    @Override
    public void eat() {
    }

    @Override
    public void sleep() {
    }
}
