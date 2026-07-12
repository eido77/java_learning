package chapter07;

public class Customer {
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
    private String firstName; // 名
    private String lastName; // 姓
    private Account account;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
