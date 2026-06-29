package chapter06;

public class Person2 {
    /*
    案例：
    创建程序,在其中定义两个类：Person2和Person2Test类。定义如下：
    用setAge()设置人的合法年龄(0~130)，用getAge()返回人的年龄。
    在Person2Test类中实例化Person类的对象b，调用setAge()和getAge()方法，体会Java的封装性。
     */

    private int age; // ← 成员变量 age（属于对象，这就是 this.age 指的那个）

    // 设置age属性
    // 带 this. 的 → 成员变量（对象的属性）
    // 不带 this. 的 → 离它最近的那个，也就是参数 age（因为方法内部参数优先，遵循「就近原则」）。
    // set 方法：负责「写入」，接收参数、无返回值（标准写法返回 void）
    public void setAge(int age) { // ← 这个括号里的 age 是「参数」，调用时传进来的值
        if (age >= 0 && age <= 130) {
            this.age = age;
            //  ↑成员变量   ↑参数
            //  this.age 指对象自己的 age（上面 private int age）
            //  右边的 age 指参数 age（括号里 int age）
            //  含义：把「传进来的参数 age」赋给「对象的成员 age」
        } else {
            System.out.println("输入的数据非法");
        }
    }

    // 获取age属性
    // get 方法：负责「读取」，无参数、返回属性值
    public int getAge() {
        return age; // 没有同名参数，age 直接指成员变量（等价于 this.age）
    }

    // 错误的
//    public int doAge(int a) {
//        if (a >= 0 && a <= 130) {
//            this.age = a;
//            return this.age;
//        } else {
//            System.out.println("输入的数据非法");
//            return -1;
//        }
//    }


}
