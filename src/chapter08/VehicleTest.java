package chapter08;

public class VehicleTest {
    /*
    Vehicle类和接口关系图.png Developer类图.png

    阿里的一个工程师Developer,结构见图。
    其中，有一个乘坐交通工具的方法takingVehicle()，在此方法中调用交通工具的run()。
    为了出行方便，他买了一辆捷安特自行车、一辆雅迪电动车和一辆奔驰轿车。这里涉及到的相关类及接口关系如图。
    其中，电动车增加动力的方式是充电，轿车增加动力的方式是加油。在具体交通工具的run()中调用其所在类
    的相关属性信息。
    请编写相关代码，并测试。
    提示：创建Vehicle[]数组，保存阿里工程师的三辆交通工具，并分别在工程师的takingVehicle()中调用。
     */
    public static void main(String[] args) {
        Developer developer = new Developer();

        // 用父类 Vehicle 数组统一保存不同的子类对象，这就是多态（父类引用指向子类对象）
        // 创建3个交通工具，保存在数组中
        Vehicle[] vehicles = new Vehicle[3];
        // 编辑器里可用 cmd + p 查看构造器需要哪些参数
        vehicles[0] = new Bicycle("捷安特", "Red");
        vehicles[1] = new ElectricVehicle("雅迪", "Black");
        vehicles[2] = new Car("奔驰", "White", "京A");

        for (int i = 0; i < vehicles.length; i++) {
            // 传入的是父类类型，实际执行的是各子类重写的 run()，体现"动态绑定"
            developer.takingVehicle(vehicles[i]);

            // 自行车没有实现 IPower（不需要充电/加油），所以要先用 instanceof 判断
            // 只有实现了 IPower 的对象才向下转型并调用 power()，否则强转会抛 ClassCastException
            if (vehicles[i] instanceof IPower) {
                ((IPower) vehicles[i]).power();
            }
        }
        /*
        人脚蹬
        电机驱动
        电力提供动力
        内燃机驱动
        汽油提供动力
         */
    }
}

class Developer {
    private String name;
    private int age;

    public Developer() {
    }

    public Developer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 形参是父类 Vehicle 类型，可接收任意子类对象，方法内调用其 run()（多态调用）
    public void takingVehicle(Vehicle vehicle) {
        vehicle.run();
    }
}

abstract class Vehicle {
    private String brand;
    private String color;

    public Vehicle() {
    }

    public Vehicle(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // 抽象方法：没有方法体，强制每个具体子类必须重写，实现各自不同的行驶方式
    public abstract void run();
}

interface IPower {
    // 接口方法默认就是 public abstract，下面是省略写法；完整写法：public abstract void power();
    void power();
}

class Bicycle extends Vehicle {
    public Bicycle() {
    }

    public Bicycle(String brand, String color) {
        super(brand, color);
    }

    @Override
    public void run() {
        System.out.println("人脚蹬");
    }
}

class ElectricVehicle extends Vehicle implements IPower {
    public ElectricVehicle() {
    }

    public ElectricVehicle(String brand, String color) {
        super(brand, color);
    }

    @Override
    public void run() {
        System.out.println("电机驱动");
    }

    @Override
    public void power() {
        System.out.println("电力提供动力");
    }
}

class Car extends Vehicle implements IPower {
    private String carNumber;

    public Car() {
    }

    public Car(String brand, String color, String carNumber) {
        super(brand, color);
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public void run() {
        System.out.println("内燃机驱动");
    }

    @Override
    public void power() {
        System.out.println("汽油提供动力");
    }
}
