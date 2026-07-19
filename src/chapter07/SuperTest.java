package chapter07;

public class SuperTest {
    public static void main(String[] args) {
         /*
        一、super关键字的使用
        1. 为什么需要super？
        举例1：子类继承父类以后，对父类的方法进行了重写，那么在子类中，是否还可以对父类中被重写的方法进行调用？
        可以！
        举例2：子类继承父类以后，发现子类和父类中定义了同名的属性，是否可以在子类中区分两个同名的属性？
        可以！
        如何调用？使用super关键字即可
        2. super的理解：父类的
        3. super可以调用的结构：属性、方法、构造器
        具体的：
        3.1 super调用属性、方法
        子类继承父类以后，我们就可以在子类的方法或构造器中，调用父类中声明的属性或方法。（满足封装性的前提下）
        如何调用呢？需要使用“super."的结构，表示调用父类的属性或方法。
        一般情况下，我们可以考虑省略“super.”的结构。但是，如果出现子类重写了父类的方法或子父类中出现了同名的属性时，
        则必须使用“super."的声明，显式的调用父类被重写的方法或父类中声明的同名的属性。
        3.2 super调用构造器
        ① 子类继承父类时，不会继承父类的构造器。只能通过“super（形参列表）”的方式调用父类指定的构造器。
        ② 规定：“super（形参列表）”，必须声明在构造器的首行。
        ③ 我们前面讲过，在构造器的首行可以使用"this（形参列表）”，调用本类中重载的构造器，结合②
           结论：在构造器的首行，"this（形参列表）"和“super（形参列表）"只能二选一。
        ④ 如果在子类构造器的首行既没有显示调用"this（形参列表）"，也没有显式调用"super（形参列表）"，则子类此构造器默认调用“super()"，即调用父类中空参的构造器。
        ⑤ 由③和④得到结论：子类的任何一个构造器中，要么会调用本类中重载的构造器，要么会调用父类的构造器。
        只能是这两种情况之一。
        ⑥ 由⑤得到：一个类中声明有n个构造器，最多有n-1个构造器中使用了"this（形参列表）”，则剩下的那个一定使用"super（形参列表）。
        —> 我们在通过子类的构造器创建对象时，一定在调用子类构造器的过程中，直接或间接的调用到父类的构造器。
           也正因为调用过父类的构造器，我们才会将父类中声明的属性或方法加载到内存中，供子类对象使用。
        开发中常见错误：
        如果子类构造器中既未显式调用父类或本类的构造器，且父类中又没有空参的构造器，则编译出错。
        二、子类对象实例化全过程
        代码举例：
        class Creature {
            // 声明属性、方法、构造器
        }

        class Animal extends Creature {

        }

        class Dog extends Animal {

        }

        class DogTest {
            psvm {
            Dog dog = new Dog();
            dog.xxx();
            dog.yyy = ...;
            }
        }
        1. 从结果的角度来看：体现为类的继承性
        当我们创建子类对象后，子类对象就获取了其父类中声明的所有的属性和方法，在权限允许的情况下，可以直接调用
        2. 从过程的角度来看：
        当我们通过子类的构造器创建对象时，子类的构造器一定会直接或间接的调用到其父类的构造器，而其父类的构造器同样会直接或间接的调用到其父类的父类的构造器，
        ...直到调用了Obiect类的构造器为止

        正因为我们调用过子类所有的父类的构造器，所以我们就会将父类中声明的属性、方法加载到内存中，供子类的对象使用
        问题：在创建子类对象的过程中，一定会调用父类中的构造器吗？对
        3. 问题：创建子类的对象时，内存中到底有几个对象？
                1个，当前new后面构造器对应的类的对象
         */
        Student1 s1 = new Student1();
        s1.eat(); // 吃有营养的食物
        s1.sleep(); // 睡够8小时

        s1.show(); // 吃有营养的食物 吃有营养的食物 eating
        s1.show2(); // 1002 1002 1001
        s1.show3(); // 都是null

        Student1 s2 = new Student1(); // Person3 Student1

        Student1 s3 = new Student1("Tom", 12); // Person3

        /*
        案例：
        修改方法重写的练习2中定义的类Kids中employeed()方法，在该方法中调用父类ManKind的employeed()方法，
        然后再输出"but Kids should study and no job."
        */
        Kids1 kids1 = new Kids1();
        kids1.employeed(); // no job  but Kids should study and no job
    }
}

class Person3 {
    String name;
    private int age;
    int id = 1001; // 身份证号

    public void eat() {
        System.out.println("eating");
    }

    public void sleep() {
        System.out.println("sleeping");
    }

    public void doSport() {
        System.out.println("doing sport");
    }

    public Person3() {
        System.out.println("Person3");
    }

    public Person3(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person3(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }
}

class Student1 extends Person3 {
    String school;
    int id = 1002; // 学号

    public void study() {
        System.out.println("studying");
    }

    @Override
    public void eat() {
        System.out.println("吃有营养的食物");
    }

    @Override
    public void sleep() {
        System.out.println("睡够8小时");
    }

    // 测试super调用方法和属性
    public void show() {
        eat(); // 省略this
        this.eat();

        super.eat();
    }

    public void show1() {
        doSport(); // 省略了 this.
        this.doSport(); // 先在 Student1 找，没找到，沿继承链向上找到 Person3 的
        super.doSport(); // 直接从 Person3 开始找
    }

    public void show2() {
        System.out.println(id); // 1002
        System.out.println(this.id); // 1002
        System.out.println(super.id); // 1001
    }

    public void show3() {
        System.out.println(name);
        System.out.println(this.name);
        System.out.println(super.name);
    }

    // 测试super调用父类的构造器
    public Student1() {
        super();
        System.out.println("Student1");
    }

    public Student1(String name, int age) {

    }

}

/*
案例：
修改方法重写的练习2中定义的类Kids中employeed()方法，在该方法中调用父类ManKind的employeed()方法，
然后再输出"but Kids should study and no job."
*/
class ManKind1 {
    private int salary;

    public void employeed() {
        if (salary == 0) {
            System.out.println("no job");
        } else {
            System.out.println("job");
        }
    }
}

class Kids1 extends ManKind1 {
    public void employeed() {
        super.employeed();
        System.out.println("but Kids should study and no job");
    }
}