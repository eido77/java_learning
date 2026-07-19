package chapter07;

public class SuperInterview02 {
    public static void main(String[] args) {
        /**
         * 考点：属性（成员变量）不存在重写，方法存在重写（动态绑定）
         * 核心结论：
         * 1. 属性看"代码写在哪个类里"（编译期决定），方法看"对象实际是什么类型"（运行期决定）
         * 2. new Son() 的对象内部同时存在 Father.info 和 Son.info 两个字段
         */
        Father f = new Father();
        Son s = new Son();
        // Father 对象里只有一个 info = "atguigu"
        System.out.println(f.getInfo()); // atguigu

        // Son 没重写 getInfo()，调的就是 Father 的 getInfo()
        // Father.getInfo() 的代码写在 Father 里 → 读 Father.info
        System.out.println(s.getInfo()); // atguigu

        // this.getInfo() 和 super.getInfo() 指向的是同一个方法（Father.getInfo）
        // 因为 Son 根本没重写它，所以两行输出一样
        s.test(); // atguigu atguigu
        System.out.println("-----------------");
        // setInfo 也是 Father 的方法，代码写在 Father 里
        // this.info = info 改的是 Father.info，Son.info 纹丝不动
        s.setInfo("大硅谷");
        // f 和 s 是两个不同的对象，改 s 不影响 f
        System.out.println(f.getInfo()); // atguigu

        // 读的是 s 对象里的 Father.info，已被改成"大硅谷"
        System.out.println(s.getInfo()); // 大硅谷
        s.test(); // 大硅谷 大硅谷
        // 如果Son里面加上 public String getInfo()的话
        // atguigu
        // 尚硅谷
        // 尚硅谷
        // atguigu
        //-----------------
        // atguigu
        // 尚硅谷
        // 尚硅谷
        // 大硅谷
    }
}

class Father {
    private String info = "atguigu";

    // 这个方法写在 Father 里，方法体中的 info 永远指 Father.info
    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}

class Son extends Father {
    // 这不是重写！是"属性隐藏"，Son 对象里现在有两个 info 字段
    private String info = "尚硅谷";

    public void test() {
        // 关键：this.getInfo() 触发动态绑定，看 this 的运行时类型
        //   - Son 没重写 getInfo → 最终执行 Father.getInfo() → 返回 Father.info
        //   - Son 重写了 getInfo → 执行 Son.getInfo() → 返回 Son.info
        System.out.println(this.getInfo());

        // super.getInfo() 是静态绑定，强制调用父类版本，永远返回 Father.info
        System.out.println(super.getInfo());
    }

//    public String getInfo() {
//    这个方法写在 Son 里，方法体中的 info 指 Son.info
//        return info;
//    }
}

