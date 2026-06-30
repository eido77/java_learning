package chapter06;

public class Order2 {
    // 测试几种常见的权限修饰符。

    // 声明不同权限的属性
    private int orderPrivate;
    int orderDefault;
    public int orderPublic;

    // 声明不同权限的方法
    private void methodPrivate() {

    }

    void methodDefault() {

    }

    public void methodPublic() {

    }

    public void test() {
        orderPublic = 1;
        orderPrivate = 2;
        orderDefault = 3;

        methodPublic();
        methodPrivate();
        methodDefault();
    }
}
