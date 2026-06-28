package chapter06;

public class OOPEncapsulation {
    /*
    面向对象特征之一：封装性
    1. 为什么需要封装性？
    理论上：
      -`高内聚`：类的内部数据操作细节自己完成，不允许外部干涉；
      -`低耦合`：仅暴露少量的方法给外部使用，尽量方便外部调用。
    通俗的说：把该隐藏的隐藏起来，该暴露的暴露出来。这就是封装性的设计思想。
    2. 如何实现数据封装？
    2.1 权限修饰符
        Java规定了4种权限修饰，分别是：private、缺省、protected、public
    2.2 作用
        我们可以使用4种权限修饰来修饰类及类的内部成员。当这些成员被调用时，体现可见性的大小。
    2.3 实际案例：
    在题目中，我们给Animal的对象的legs属性赋值。在实际的常识中，我们知道legs不能赋值为负数的。但是如果
    直接调用属性legs，是不能加入判断逻辑的。那怎么办呢？
    > 将legs属性私有化(private)，禁止在Animal类的外部直接调用此属性
    > 提供给legs属性赋值的setLegs()方法，在此方法中加入legs赋值的判断逻辑if(legs >= 0 && legs % 2 ==0)
      将此方法暴露出去，使得在Animal类的外部调用此方法，对legs属性赋值。
    > 提供给legs属性获取的getLegs()方法，此方法对外暴露。使得在Animal类的外部还可以调用此属性的值。
    2.4 4种权限具体使用
    权限修饰符控制类或成员的可见性范围。具体访问范围如下：
    +------------+----------+--------+----------------+------------------+
    | 修饰符      | 本类内部  | 本包内  | 其他包的子类     | 其他包非子类      |
    +------------+----------+--------+----------------+------------------+
    | private    |    √     |   ×    |       ×        |        ×         |
    | 缺省        |    √     |   √    |       ×        |        ×         |
    | protected  |    √     |   √    |       √        |        ×         |
    | public     |    √     |   √    |       √        |        √         |
    +------------+----------+--------+----------------+------------------+
    > 类：只能使用public、缺省修饰
    > 类的内部成员：可以使用4种权限修饰进行修饰。
    2.5 开发中4种权限使用频率的情况：
       比较高：public、private
       比较低：缺省、protected
    3. 封装性的体现
    > 场景1：私有化(private)类的属性，提供公共(public)的get和set方法，对此属性进行获取或修改
    > 场景2：将类中不需要对外暴露的方法，设置为private
    > 场景3：单例模式中构造器private的了，避免在类的外部创建实例。（放到static关键字后讲）
     */
    public static void main(String[] args) {
        Animal ani = new Animal();
        ani.name = "Rabbit";
        // private int legs之后开始报错，出了class之外就不能调用
//        ani.legs = 4;
//        ani.legs = -4;
        //只能通过setLegs()，间接的对legs属性赋值
        // 0 来自 System.out.println(ani.getLegs());，因为 legs 从没被成功赋值，仍是 int 默认值 0。
//        ani.setLegs(-4); // Invalid legs   0
        ani.setLegs(4); // 4
//        System.out.println(ani.legs);
        System.out.println(ani.getLegs());
    }

}

class Animal {
    //属性
    String name;
    private int legs;

    //方法
    //设置legs的属性值
    public void setLegs(int l) {
        if (l >= 0 && l % 2 == 0) {
            legs = l;
        } else {
            System.out.println("Invalid legs"); // 无效
        }
    }

    //获取legs的属性值
    public int getLegs() {
        return legs;
    }

    public void eat() {
        System.out.println("Animal is eating");
    }

}

//报错
// 类：只能使用public、缺省修饰
//private class AA {
//
//}
