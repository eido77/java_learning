package chapter06;

public class ValueTransferTest1 {
    public static void main(String[] args) {
        int m = 10;
        int n = 20;
        System.out.println("m = " + m + " n = " + n); // m = 10 n = 20
        // swap 是实例方法，需通过对象调用
        ValueTransferTest1 t = new ValueTransferTest1();
        Data data = new Data();

        //参数传递机制举例1.png
        //交换两个变量的值
//        int temp = m;
//        m = n;
//        n = temp;
        //调用方法
        t.swap(m, n); // m = 10 n = 20

        data.m1 = 10;
        data.n1 = 20;
        System.out.println("m1 = " + data.m1 + " n1 = " + data.n1); // m1 = 10 n1 = 20

        //参数传递机制举例2.png
//        int temp1 = data.m1;
//        data.m1 = data.n1;
//        data.n1 = temp1;
        t.swap1(data);
        System.out.println("m1 = " + data.m1 + " n1 = " + data.n1); // m1 = 20 n1 = 10

    }

    public void swap(int m, int n) {
        int temp = m;
        m = n;
        n = temp;
    }

    public void swap1(Data data) {
        int temp = data.m1;
        data.m1 = data.n1;
        data.n1 = temp;
    }
}

class Data {
    int m1;
    int n1;
}
