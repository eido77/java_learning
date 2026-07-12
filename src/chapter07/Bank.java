package chapter07;

public class Bank {
    /*
   1、按照UML类图，创建Account类，提供必要的结构。
   类图Account
   - 在提款方法withdraw()中，需要判断用户余额是否能够满足提款数额的要求，如果不能，应给出提示。
   - deposit()方法表示存款。
   2、按照UML类图，创建Customer类，提供必要的结构。
   类图Customer
   3、按照UML类图，创建Bank类，提供必要的结构。
   类图Bank
   - addCustomer 方法必须依照参数（姓，名）构造一个新的 Customer对象，然后把它放到 customer 数组中。
     还必须把 numberOfCustomer 属性的值加 1。
   - getNumOfCustomers 方法返回 numberofCustomers 属性值。
   - getCustomer方法返回与给出的index参数相关的客户。
   4、创建BankTest类，进行测试。
    */
    private Customer[] customers; // 用于保存多个客户
    // 不等于数组长度，数组可能有空
    private int numberOfCustomer; // 用于记录存储的客户的个数

    public Bank() {
        customers = new Customer[10];
    }

    // 将指定姓名的客户保存在银行的客户列表中
    public void addCustomer(String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName);
        // 一定要后++
        customers[numberOfCustomer++] = customer;
    }

    // 获取客户列表中存储的客户个数
    public int getNumberOfCustomer() {
        return numberOfCustomer;
    }

    // 获取指定索引位置上的客户
    public Customer getCustomer(int index) {
        // 无效范围
        if (index < 0 || index >= numberOfCustomer) {
            return null;
        }
        // 有效范围
//        } else {
//            return customers[index];
//        }
        return customers[index];
    }

}
