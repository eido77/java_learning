package chapter08;

public class UserTest {
    /*
    （1）声明User类，
    - 包含属性：userName（String类型），password（String类型），registrationTime（long类型），私有化
    - 包含get/set方法，其中registrationTime没有set方法
    - 包含无参构造，
      - 输出“新用户注册”，
      - registrationTime赋值为当前系统时间，
      - userName就默认为当前系统时间值，
      - password默认为“123456”
    - 包含有参构造(String userName, String password)，
      - 输出“新用户注册”，
      - registrationTime赋值为当前系统时间，
      - username和password由参数赋值
    - 包含public String getInfo()方法，返回：“用户名：xx，密码：xx，注册时间：xx”
    （2）编写测试类，测试类main方法的代码
     */
    public static void main(String[] args) {
        User u1 = new User();
        System.out.println(u1.getInfo());

        User u2 = new User("Tom", "654321");
        System.out.println(u2.getInfo());
    }
}

class User {
    private String userName;
    private String password;
    private long registrationTime;

    public User() {
        System.out.println("新用户注册");
        // 获取系统当前时间（距离1970年1月1日 00:00:00到现在的毫秒数）
        registrationTime = System.currentTimeMillis();
        // currentTimeMillis() 返回 long，不能直接赋给 String。
        // 等价写法（更推荐、更易读）：String.valueOf(System.currentTimeMillis())
        // 这里直接用 registrationTime，顺带解决了调用两次 currentTimeMillis() 可能取到不同毫秒值的问题。
        // userName = String.valueOf(registrationTime);
        userName = System.currentTimeMillis() + "";
        password = "123456";
    }

    public User(String userName, String password) {
        System.out.println("新用户注册");
        registrationTime = System.currentTimeMillis();
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRegistrationTime() {
        return registrationTime;
    }

    public String getInfo() {
        return "用户名：" + userName + "，密码：" + password + "，注册时间：" + registrationTime;
    }
}
