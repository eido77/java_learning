package chapter07;

public class SuperInterview01 {
    public static void main(String[] args) {
        // 判断运行结果
        new A(new B()); // B A AB
        // 如果注释的地方取消注释，并且注释掉class B
        // A B A AB
    }
}

class A {
    public A() {
        System.out.println("A");
    }

    public A(B b) {
        this();
        System.out.println("AB");
    }
}

class B {
    public B() {
        System.out.println("B");
    }
}

//class B extends A{
//    public B() {
//        System.out.println("B");
//    }
//}
