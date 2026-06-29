package chapter06;

public class Person2Test {
    /*
    案例：
    创建程序,在其中定义两个类：Person2和Person2Test类。定义如下：
    用setAge()设置人的合法年龄(0~130)，用getAge()返回人的年龄。
    在Person2Test类中实例化Person类的对象b，调用setAge()和getAge()方法，体会Java的封装性。
     */
    public static void main(String[] args) {
        //创建Person2的实例1
        Person2 p1 = new Person2();
//        p1.age = 10; // age has private access in chapter06.Person2
        p1.setAge(24);
        System.out.println(p1.getAge());
    }
}
