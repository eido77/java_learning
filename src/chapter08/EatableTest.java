package chapter08;

public class EatableTest {
    /*
    1、声明接口Eatable，包含抽象方法public abstract void eat();
    2、声明实现类中国人Chinese，重写抽象方法，打印用筷子吃饭
    3、声明实现类美国人American，重写抽象方法，打印用刀叉吃饭
    4、声明实现类印度人Indian，重写抽象方法，打印用手抓饭
    5、声明测试类EatableTest，创建Eatable数组，存储各国人对象，并遍历数组，调用eat()方法
     */
    public static void main(String[] args) {
        // 接口不能直接创建对象（不能 new 接口），但可以创建接口类型的数组来存放实现类对象
        Eatable[] eatables = new Eatable[3];
        // 用父接口引用指向子类对象，这就是多态：编译看左边(Eatable)，运行看右边(具体实现类)
        eatables[0] = new Chinese1();
        eatables[1] = new American();
        eatables[2] = new Indian1();

        // 遍历数组，统一调用 eat()。虽然都是 Eatable 类型，但会各自执行子类重写后的方法（动态绑定）
        for (int i = 0; i < eatables.length; i++) {
            eatables[i].eat(); // 筷子 刀叉 手
        }
    }
}

// 声明接口 Eatable：接口中的方法默认就是 public abstract，可以省略这两个关键字
interface Eatable {
    void eat(); // 抽象方法，只有声明没有方法体，交给实现类去重写
}

// 实现类：中国人，implements 表示实现接口，必须重写接口里所有的抽象方法
class Chinese1 implements Eatable {
    @Override // 表示这是重写接口的方法，加上后编译器会帮忙检查方法名/参数是否写对
    public void eat() {
        System.out.println("筷子");
    }
}

class American implements Eatable {
    @Override
    public void eat() {
        System.out.println("刀叉");
    }
}

class Indian1 implements Eatable {
    @Override
    public void eat() {
        System.out.println("手");
    }
}
