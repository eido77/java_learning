package chapter06;

public class ValueTransferTest {
    public static void main(String[] args) {
        //1.基本数据类型的局部变量
        int m = 10;
        //传递的是数据值
        //变量中直接存储数据值本身，赋值时是把"值"复制一份给对方
        int n = m; // 把 m 的值(10)复制给 n，此后两者互不影响
        System.out.println(m); // 10
        System.out.println(n); // 10

        m++;
        System.out.println(m); // 11
        System.out.println(n); // 10

        //2.引用数据类型的局部变量
        //2.1 数组类型
        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        //传递的是地址值
        //变量中存储的是对象的"地址值"，赋值时复制的是"地址"，两者指向同一个对象
        int[] arr2 = arr1; // 把 arr1 的地址复制给 arr2，两者指向同一个数组
        arr2[0] = 10; // 通过 arr2 改动，arr1 看到的也是改后的结果
        System.out.println(arr1[0]); // 10
        //2，2 对象类型
        Order order1 = new Order();
        order1.orderId = 1001;

        //传递的是地址值，order2 和 order1 指向同一个 Order 对象
        Order order2 = order1; // 复制地址，order1 和 order2 指向同一个 Order 对象
        order2.orderId = 1002; // 通过 order2 改动，order1 看到的也变了
        System.out.println(order1.orderId); // 1002



        //规则：实参给形参赋值的过程
        ValueTransferTest test = new ValueTransferTest();
        //1.基本数据类型的变量
        int m1 = 10;
        //把 m1 的值(10)复制给形参 m，方法内的修改不影响 m1
        test.method1(m1);
        System.out.println(m1); // 10

        //2.引用数据类型的变量
        Person11 p = new Person11();
        p.age = 10;
        test.method2(p);
        // method2 改的是同一个对象，p 也看到了变化
        System.out.println(p.age); // 11
    }

    //形参 m1 是 main 里 m1 的值副本，与原变量无关
    public void method1(int m) {
        //这里的 m 是形参（method1 的局部变量），和 main 里的 m1 是两个独立变量
        //改的是副本，从 10 变成 11，不影响 main 里的 m1
        m++;
    }

    //形参是引用类型，传进来的是对象的地址
    public void method2(Person11 p) { // 方法的形参，永远是"类型 + 变量名"
        //形参 p 和 main 里的 p 存的是同一个地址，改的是同一个对象
        p.age++;
    }
}

class Order {
    int orderId;
}

class Person11 {
    int age;
}
