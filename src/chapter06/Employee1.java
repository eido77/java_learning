package chapter06;

public class Employee1 {
    /*
    案例：普通员工类
    （1）声明员工类Employee1
    - 包含属性：姓名、性别、年龄、电话，属性私有化
    - 提供get/set方法
    - 提供String getInfo()方法
    （2）在测试类的main中创建员工数组，并从键盘输入员工对象信息，最后遍历输出
     */

    // 属性
    private String name;
    private char gender;
    private int age;
    // 电话号码必须用 String，不能用 int，电话号码开头的 0 会被 int 吃掉。电话号码太长，int 装不下。电话号码根本不是「数」，是「一串符号」
    private String phoneNumber;

    // 提供get和set方法
    public void setName(String name) {
        this.name = name;
    }

    // getName() 括号是空的，没有参数，方法里没有别的 name 来抢，所以 name 只可能指成员变量。不需要 this.
    public String getName() {
        return name;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public char getGender() {
        return gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getInfo() {
        return name + "\t" + gender  + "\t" + age + "\t" + phoneNumber;
//        return getName() + "\t" + getGender() + "\t" + getAge() + "\t" + getPhoneNumber();
    }


}
