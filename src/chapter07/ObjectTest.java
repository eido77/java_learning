package chapter07;

public class ObjectTest {
    /*
    1. Object类的说明
    > 明确：java.lang.Object
    > 任何一个Java类（除Object类）都直接或间接的继承于Object类
        > 数组也是对象，同样继承Object。比如 int[]、String[] 都可以调用 toString()、equals() 等方法
        > 接口不继承Object
    > Object类称为java类的根父类
    > Object类中声明的结构（属性、方法等）就具有通用性。
        > Object类中没有声明属性
         （正因如此，new Object() 几乎不占额外空间，常被用作纯粹的锁对象：
           private final Object lock = new Object(); ）
        > Object类提供了一个空参的构造器
        > 重点关注：Object类中声明的方法
    2. 常用方法
       重点方法：equals() / toString()
       熟悉方法：clone() / finalize()
       目前不需要关注：getClass() / hashCode() / notify() / notifyAll() / wait() / wait(xx) / wait(xx, yy)
    3. == 与 equals() 的区别
        == ：运算符
            > 基本数据类型：比较值是否相等（类型不同但可自动转换时也能比较，如 int 和 double）
            > 引用数据类型：比较地址值，即是否指向同一个对象
            > == 两边的类型必须兼容，否则编译不通过
   equals() ：方法，只能用于引用数据类型
            > 未重写时等价于 ==（比较地址）
            > 重写后一般比较实体内容
    4. clone()
    > 作用：复制一个对象，得到一个内容相同但地址不同的新对象
    > 使用前提：所在类必须实现 Cloneable 接口，否则调用时抛 CloneNotSupportedException
      （Cloneable 是标记接口，内部没有任何方法，只起"允许克隆"的标识作用）
    > 默认是浅拷贝：只复制基本类型的值和引用变量的地址值，
      被引用的对象本身并没有被复制，新旧对象仍指向同一个成员对象
    > 需要深拷贝时，要在clone()中手动对引用类型成员再次clone

    5. finalize()
    > 曾用于：对象被垃圾回收前，由GC自动调用，做资源释放等收尾工作
    > 现状：从 Java 9 起已被 @Deprecated，Java 18 起标记为待移除
    > 结论：了解即可，实际开发中不要使用。释放资源应使用 try-with-resources 或显式的 close()
     */
}
