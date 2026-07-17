package chapter07;

public class OOPOverride {
    /*
    方法的重写
    1. 为什么需要方法的重写？
    子类在继承父类以后，就获取了父类中声明的所有的方法。
    但是，父类中的方法可能不太适用于子类，换句话说，子类需要对父类中继承过来的方法进行覆盖、覆写的操作。
    2. 何为方法的重写？
    子类对父类继承过来的方法进行的覆盖、覆写的操作
    3. 方法重写应遵循的规则
    [复习]方法声明的格式：权限修饰符 返回值类型 方法名(形参列表) [throws 异常类型] { // 方法体 }
    具体规则：
    ① 父类被重写的方法与子类重写的方法的方法名和形参列表必须相同。
    ② 子类重写的方法的权限修饰符不小于父类被重写的方法的权限修饰符
        > 子类不能重写父类中声明为private权限修饰的方法。
    ③ 关于返回值类型：
    > 父类被重写的方法的返回值类型是void，则子类重写的方法的返回值类型必须是void
    > 父类被重写的方法的返回值类型是基本数据类型，则子类重写的方法的返回值类型必须与被重写的方法的返回值类型相同
    > 父类被重写的方法的返回值类型是引用数据类型（比如类），则子类重写的方法的返回值类型可以与被重写的方法的返回值类型相同或是被重写的方法的返回值类型的子类
    ④（超纲）子类重写的方法抛出的异常类型可以与父类被重写的方法抛出的异常类型相同，或是父类被重写的方法抛出的异常类型的子类
    补充说明：方法体：没有要求，但是子类重写的方法的方法体必然与父类被重写的方法不同
    4. 面试题：区分方法的重载(overload)与重写(override / overwrite)
    重载：“两同一不同” 方法名相同、所在类相同，参数列表不同
    重写：继承以后，子类覆盖父类中同名同参数的方法
    [类比]相同类型的面试题：
    throws / throw
    final / finally / finalize
    Collection / Collections
    String / StringBuffer / StringBuilder
    ArrayList / LinkedList
    HashMap / LinkedHashMap / Hashtable
    同步 / 异步
    == / equals()
    ......
     */


    /*
    修改继承内容的练习中定义的类Cylinder，在Cylinder中重写父类方法findArea()，用于计算圆柱的表面积
     */
    public static void main(String[] args) {
        Cylinder1 cylinder = new Cylinder1();
        cylinder.setRadius(5);
        cylinder.setLength(2);
        // 表面积
        System.out.println(cylinder.fineArea()); // 219.9114857512855
    }
}

// 举例（银行账户）：
class Account1 {
    double balance;

    // 取钱
    public void withdraw(double amt) {
        // 判断balance余额是否够amt取钱的额度
    }
}

class CheckAccount extends Account1 {
    double protectedBy; // 透支额度

    @Override
    public void withdraw(double amt) {
        // 判断balance余额是否够amt取钱的额度
        // 如果不够，还可以考虑从protectedBy透支额度里取
    }
}

class Account1Test {
    public static void main(String[] args) {
        CheckAccount acct = new CheckAccount();
        // 假如余额不够，但可从透支额度补 —— 执行的是子类重写的方法
        acct.withdraw(100); // 执行的是子类重写父类的方法
    }
}

/*
修改继承内容的练习中定义的类Cylinder，在Cylinder中重写父类方法findArea()，用于计算圆柱的表面积
 */
class Cylinder1 extends Cylinder {
    @Override
    public double fineArea() {
        return Math.PI * getRadius() * getRadius() * 2 + Math.PI * getRadius() * 2 * getLength();
    }
}