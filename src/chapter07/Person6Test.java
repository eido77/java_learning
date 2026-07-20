package chapter07;

public class Person6Test {
    /*
    案例：
    1、在包中声明人Person、男人Man、女人Woman类
    （1）在Person类中，包含
    ①public void eat()：打印吃饭
    ②public void toilet()：打印上洗手间
    （2）在Man类中，包含
    ①重写上面的方法
    ②增加  public void smoke()：打印抽烟
    （3）在Woman类中，包含
    ①重写上面的方法
    ②增加 public void makeup()：打印化妆
    2、在包中声明测试类Person6Test
    1）public static void meeting(Person ...  ps)
    在该方法中，每一个人先吃饭，然后上洗手间，然后如果是男人，随后抽根烟；如果是女人，随后化个妆
    （2）public static void main(String[] args)
    在主方法中，创建多个男人和女人对象，并调用meeting()方法进行测试
     */
    public static void main(String[] args) {
        // meeting 是静态方法，属于类本身，同类中可直接调用，无需创建 Person6Test 对象
        // 若 meeting 未加 static（如视频中的写法），则必须先创建对象再调用：
//        Person6Test person6Test = new Person6Test();
        meeting(new Man1(), new Woman1(), new Man1());
        // man eating
        // man eating
        // man toilet
        // smoke
        // woman eating
        // woman toilet
        // makeup
        // man eating
        // man toilet
        // smoke
    }

    public static void meeting(Person6... ps) {
        // ps 是数组，数组本身没有 eat() 方法，必须通过 ps[i] 取出元素才能调用
//        ps.eat();
        // 下面这行是测试可变形参时的残留代码，会让第一个人多吃一次饭
        ps[0].eat();

        for (int i = 0; i < ps.length; i++) {
            ps[i].eat();
            ps[i].toilet();

            // 方法1：先强转赋值给变量，再调用（变量可复用）
            // if (ps[i] instanceof Man1) {
            //     Man1 man1 = (Man1) ps[i];
            //     man1.smoke();
            // } else if (ps[i] instanceof Woman1) {
            //     Woman1 woman1 = (Woman1) ps[i];
            //     woman1.makeup();
            // }

            // 方法2：一行内强转并调用（只调一次时简洁）
            // if (ps[i] instanceof Man1) {
            //     ((Man1) ps[i]).smoke();
            // } else if (ps[i] instanceof Woman1) {
            //     ((Woman1) ps[i]).makeup();
            // }

            // 方法3：instanceof 模式匹配（JDK 16+ 正式特性，推荐）
            // 这一行做了三件事：
            //   ① 判断 ps[i] 是不是 Man1 类型
            //   ② 如果是，编译器自动执行 Man1 man1 = (Man1) ps[i];（声明变量 + 强制类型转换 + 赋值）
            //      源码里看不到 (Man1) 圆括号，不代表没强转，只是编译器替你写了
            //   ③ 如果不是，man1 这个变量根本不会被创建，直接跳到 else if
            if (ps[i] instanceof Man1 man1) {
                // 进入这个大括号，说明上一行判断已经通过，man1 已被编译器创建并赋好值了
                // 所以不用再写 Man1 man1 = (Man1) ps[i]; 这行——那是方法1的活，模式匹配已经替你做完了
                // man1 的类型是 Man1（不是 Person6），所以能直接调用 Man1 独有的 smoke()
                man1.smoke();
            // 一个对象不可能同时是 Man1 和 Woman1，上面成立时这里就不必再判断，故用 else if
            // 这里的 woman1 同理：判断通过后自动创建，类型为 Woman1，可直接调用 makeup()
            } else if (ps[i] instanceof Woman1 woman1) {
                woman1.makeup();
            }
            // 补充：man1 和 woman1 的作用域仅限于各自的 {} 内部
            // 出了大括号它们就不存在了，编译器会报"找不到符号"
        }
    }
}
