package chapter07;

public class OrderTest {
    /*
    编写Order1类，有int型的orderId，String型的orderName，相应的getter()和setter()方法，两个参数的构造器，
    重写父类的equals()方法：public boolean equals(Object obj)，并判断测试类中创建的两个对象是否相等。
     */
    public static void main(String[] args) {
        Order order1 = new Order(1011, "orderAA");
        Order order2 = new Order(1011, "orderAA");
        System.out.println(order1.equals(order2)); // true

        Order order3 = new Order(1022, new String("orderBB"));
        Order order4 = new Order(1022, new String("orderBB"));
        System.out.println(order3.equals(order4)); // true

        String str1 = "AA";
        String str2 = "AA";
        System.out.println(str1 == str2); // true
    }
}

class Order {
    private int orderID;
    private String orderName;

    public Order() {
    }

    public Order(int orderID, String orderName) {
        this.orderID = orderID;
        this.orderName = orderName;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    // 手动重写equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Order) {
            Order order = (Order) obj;
            return this.orderID == order.orderID && this.orderName.equals(order.orderName);
        }
        return false;
    }
}
