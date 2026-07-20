package chapter07;

public class OOPPolymorphism {
    /*
    面向对象特征之三：多态性
    1. 如何理解多态性？
    理解：一个事物的多种形态
    2. Java中多态性的体现:
       子类对象的多态性：父类的引用指向子类的对象 / 子类的对象赋给父类的引用
       比如：Person p2 = new Man();
    3. 多态性的应用：虚拟方法调用
        在多态的场景下，调用方法时。
            编译时，认为方法是左边声明的父类的类型的方法（即被重写的方法）
            执行时，实际执行的是子类重写父类的方法。
        简称为：编译看左边（声明类型），运行看右边（实际对象）
    4. 多态性的使用前提：① 要有类的继承关系 ② 要有方法的重写
    5. 多态的适用性：适用于方法，不适用于属性
    6. 多态的好处与弊端
------------------------------------------------------------------------------
多态：形参用父类，实参传子类，一个方法应对所有子类，避免重载爆炸
public class AnimalTest {
    public static void main(String[] args) {
        AnimalTest test = new AnimalTest();
        // 传 Dog，形参 Animal animal = new Dog() —— 就是多态
        test.adopt(new Dog());
        test.adopt(new Cat());  // 换任何子类都不用改 adopt()
    }
    多态的核心价值：形参声明为父类 Animal，可接收所有子类对象。
    方法内调用 eat()/jump()，编译期按 Animal 校验，运行期执行子类重写的版本。
    若不用多态，就得为每个子类各写一个重载方法（见下方注释），
    每新增一个子类都要改代码，扩展性极差。
    public void adopt(Animal animal) {
        animal.eat();
        animal.jump();
    }

    // public void adopt(Dog dog){ ... }
    // public void adopt(Cat cat){ ... }   // 冗余，被多态取代
}
------------------------------------------------------------------------------
    6.1 弊端：
    在多态的场景下，我们创建了子类的对象，也加载了子类特有的属性和方法。
    但是由于声明为父类的引用，导致我们没有办法直接调用子类特有的属性和方法。
    问题：Person4 p2 = new Man();
         针对于创建的对象，在内存中是否加载了Man类中声明的特有的属性和方法？
         加载了
    问题：能不能直接调用Man类中加载的特有的属性和方法？
         不能
    6.2 好处：
    极大的减少了代码的冗余，不需要定义多个重载的方法
    举例：
    class Account {
        public void withdraw() {}
    }
    class CheckAccount extends Account { // 信用卡
        // 存在方法的重写
        public void withdraw() {}
    }
    class SavingAccount extends Account { // 储蓄卡
        // 存在方法的重写
    }

    class Customer {
        Account account;
        public void setAccount(Account account) {
            this.account = account;
        }
        public Account getAccount() {}
    }

    class CustomerTest {
        psvm {
            Customer customer = new Customer();
            customer.setAccount(new CheckAccount());
            customer.getAccount().withdraw();
        }
    }
    7. instanceof的使用
              ┌─────────────────────────┐
              │    父类（如：Person）     │
              └─────────────────────────┘
                 ↓                    ↑
              向下转型             向上转型：多态
        （需用 instanceof 判断）  （自动完成，安全）
                 ↓                    ↑
              ┌─────────────────────────┐
              │    子类（如：Student）    │
              └─────────────────────────┘
        7.1 向上转型：编译期只能看到父类的成员，子类特有的属性和方法无法直接调用。
        7.2 向下转型：把父类引用强转回子类，才能调用子类特有成员。
        Student s = (Student) p;  // 手动强转
        风险：若 p 实际指向的不是 Student，运行时抛 ClassCastException。
        7.3 instanceof：转型前的安全检查，返回 boolean，本身不改变任何类型。
    建议在向下转型之前，使用instanceof进行判断，避免出现类型转换异常
    格式：a instanceof A：判断对象a是否是类A的实例
    如果a instanceof A返回true，则
        a instanceof superA 返回也是true。其中，A是superA的子类
        反之不成立：a instanceof superA 为 true 时，a instanceof A 未必为 true
     */
    public static void main(String[] args) {
        // 多态性之前的场景：声明类型 = 实际类型，各管各的
        Person4 p1 = new Person4();
        Man man = new Man();
        // 多态性：子类对象的多态性
        // 父类引用指向子类对象（前提：Man extends Person4）
        Person4 p2 = new Man();

        /*
        多态性的应用：虚拟方法调用
        在多态的场景下，调用方法时。
            编译时，认为方法是左边声明的父类的类型的方法（即被重写的方法）
            执行时，实际执行的是子类重写父类的方法。
        简称为：编译看左边，运行看右边
         */
        p2.eat(); // man eating
        p2.sleep(); // man sleeping

        // 测试属性是否满足多态性？不满足
        System.out.println(p2.id); // 1001

        // 7. instanceof的使用
        Person4 p3 = new Man();
        // 不能直接调用子类特有的结构
//        p3.earnMoney();
//        System.out.println(p3.isSmoking);

        // 向下转型：使用强转符
        Man m1 = (Man) p3;
        m1.earnMoney(); // earn money
        System.out.println(m1.isSmoking); // false

        // p3和m1指向堆空间中的同一个对象
        System.out.println(p3 == m1); // true

        // 向下转型可能出现：类型转换异常（ClassCastException）
        Person4 p4 = new Woman();
//        Man m2 = (Man) p4; // ClassCastException
//        m2.earnMoney();

        /*
        建议在向下转型之前，使用instanceof进行判断，避免出现类型转换异常
        格式：a instanceof A：判断对象a是否是类A的实例
        如果a instanceof A返回true，则
            a instanceof superA 返回也是true。其中，A是superA的子类
            反之不成立：a instanceof superA 为 true 时，a instanceof A 未必为 true
         */

        // p4 实际是 Woman，与 Man 是兄弟关系，此判断恒为 false，块内代码不会执行
        // 但因为有 instanceof 兜底，程序不会抛 ClassCastException（对比上面被注释掉的强转）
        if (p4 instanceof Man) {
            Man m2 = (Man) p4;
            m2.earnMoney();
        }

        if (p4 instanceof Woman) {
            System.out.println("Woman"); // Woman
        }

        if (p4 instanceof Person4) {
            System.out.println("Person4"); // Person4
        }

        if (p4 instanceof Object) {
            System.out.println("Object"); // Object
        }
    }
}

class Person4 {
    String name;
    int age;

    int id = 1001;

    public void eat() {
        System.out.println("eating");
    }

    public void sleep() {
        System.out.println("sleeping");
    }
}

class Man extends Person4 {
    boolean isSmoking;

    int id = 1002;

    @Override
    public void eat() {
        System.out.println("man eating");
    }

    @Override
    public void sleep() {
        System.out.println("man sleeping");
    }

    public void earnMoney() {
        System.out.println("earn money");
    }
}

class Woman extends Person4 {
    boolean isBeauty;

    @Override
    public void eat() {
        System.out.println("woman eating");
    }

    @Override
    public void sleep() {
        System.out.println("woman sleeping");
    }

    public void goShopping() {
        System.out.println("go shopping");
    }
}