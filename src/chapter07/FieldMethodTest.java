package chapter07;

public class FieldMethodTest {
    public static void main(String[] args) {
        // 方法可以被重写，有多态；属性不能被重写，只会被隐藏，没有多态。
        Sub s = new Sub();
        // s 的编译时类型 = 运行时类型 = Sub，属性和方法都找 Sub 的
        System.out.println(s.count); // 20：属性看编译时类型 Sub
        s.display(); // 20：方法看运行时类型 Sub
        Base b = s;
        System.out.println(b == s); // true：b 和 s 是同一个对象的两个引用，比较地址
        // 关键：b 的编译时类型是 Base，运行时类型是 Sub
        System.out.println(b.count); // 10：属性没有多态，编译期就按 Base 绑定
        b.display(); // 20：方法有多态，运行期动态绑定到 Sub 的重写方法

        Base b1 = new Base();
        System.out.println(b1.count); // 10
        b1.display(); // 10：Base 对象，没有子类介入

        System.out.println(((Base) s).count);  // 10：强转只改变编译时类型，属性就跟着变
        ((Base) s).display();                  // 20：强转对方法无效，运行时类型仍是 Sub
    }
}

class Base {
    int count = 10; // 这个字段和 Sub 的 count 同时存在于 Sub 对象内存中，互不覆盖

    public void display() {
        System.out.println(this.count); // Base 中的 this.count 只能看到 Base 的 count
    }
}

class Sub extends Base {
    int count = 20; // 不是"覆盖"，是"隐藏"（hiding）父类的同名字段

    public void display() { // 这是真正的重写（override）
        System.out.println(this.count); // Sub 中的 this.count 指向 Sub 的 count
    }
}
