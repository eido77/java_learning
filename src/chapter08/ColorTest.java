package chapter08;

public class ColorTest {
    /*
    案例拓展：颜色枚举类(使用enum声明)
    （1）声明颜色枚举类Color：
    - 声明final修饰的int类型的属性red,green,blue
    - 声明final修饰的String类型的属性description
    - 声明有参构造器Color(int red, int green, int blue,String description)
    - 创建7个常量对象：红、橙、黄、绿、青、蓝、紫，
    - 重写toString方法，例如：RED(255,0,0)->红色
    （2）在测试类中，使用枚举类，获取绿色对象，并打印对象。
    提示：
    - 7个常量对象的RGB值如下：
    红：(255,0,0)
    橙：(255,128,0)
    黄：(255,255,0)
    绿：(0,255,0)
    青：(0,255,255)
    蓝：(0,0,255)
    紫：(128,0,255)
    7个常量对象名如下：
    RED, ORANGE, YELLOW, GREEN, CYAN, BLUE,PURPLE
     */
    public static void main(String[] args) {
        // 直接通过“枚举类名.常量名”获取枚举对象，因为枚举常量本质是 public static final 的对象
        // println会自动调用对象的toString()，所以这里会打印重写后的格式
        System.out.println(Color.GREEN);
    }
}

// enum是定义枚举类的关键字；枚举类默认隐式继承java.lang.Enum类，因此不能再继承其他类
// 枚举 = 一组固定的、有限的常量对象（比如颜色、季节、星期），比用 public static final 常量更规范
enum Color {
    // 下面这7行就是枚举的“常量对象”，必须写在枚举体的最前面
    // 每个常量后面的括号，就是在调用下面那个有参构造器来创建这个对象
    RED(255, 0, 0, "RED"),
    ORANGE(255, 128, 0, "ORANGE"),
    YELLOW(255, 255, 0, "YELLOW"),
    GREEN(0, 255, 0, "GREEN"),
    CYAN(0, 255, 255, "CYAN"),
    BLUE(0, 0, 255, "BLUE"),
    PURPLE(128, 0, 255, "PURPLE");

    // final修饰：这些属性一旦在构造器中赋值就不能再改，保证每个颜色常量的RGB值和描述是不可变的
    private final int red;
    private final int green;
    private final int blue;
    private final String description;

    // 枚举的构造器天生就是private的（写不写private都一样，但绝不能写public/protected，否则编译报错）
    // 原因：不允许外部 new Color(...) 来创建对象，对象只能是上面定义好的那7个常量
    // 不需要空参构造器：因为7个常量都用了这个有参构造器，且没有任何地方用到空参，编译器也不会自动生成
    Color(int red, int green, int blue, String description) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.description = description;
    }

    // 提供get方法对外只读访问属性（因为属性是private的，外部拿不到，只能通过get）
    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        // super.toString() 调用的是父类Enum的toString()，它默认返回的就是常量名（如"GREEN"）
        // 所以效果等同于下面注释掉的 name() 写法，两种都可以
        return super.toString() + "(" + red + ", " + green + ", " + blue + ")->" + description;
//        return name() + "(" + red + ", " + green + ", " + blue + ")->" + description;
    }
}
