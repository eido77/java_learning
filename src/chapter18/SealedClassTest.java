package chapter18;

public class SealedClassTest {
    /*
    密封类的使用
    JDK15的预览特性
    JDK16二次预览特性
    JDK17转正特性
     */
    /*
     * 【密封类】
     * 在密封类出现之前，Java 对“继承”只有两种极端态度：
     *   1) 普通类（不写 final）：任何人、任何地方都能继承它，无法控制类型体系；
     *   2) final 类：彻底禁止继承，一个子类都不许有。
     * 密封类（sealed class）提供了第三种中间状态：“可以被继承，但只能被我点名的那几个类继承”。
     * 也就是把“继承权”从“开放给所有人”变成“定向授权”。
     *
     * sealed class 父类 permits 子类A, 子类B { }
     * permits 后面必须列出“全部直接子类”，少写一个那个子类就编译不过。
     *
     * permits 里列出的类必须真的 extends（或 implements）这个密封类，
     * 光写在 permits 里但不继承，也是编译错误（两边必须互相“认领”）。
     *
     * 密封类的每一个直接子类，必须显式是 final / sealed / non-sealed 之一。
     *
     * 位置限制：子类必须和密封类在同一个模块（module）里；
     * 如果没有用模块（未命名模块），则必须在同一个包里。所以不能跨 jar 乱继承。
     *
     * 如果所有子类都写在同一个源文件（像本文件这样），permits 子句可以省略，编译器自动推导；
     * 但显式写出来可读性更好，是推荐做法。
     *
     * sealed 不只能用于 class，也能用于 interface 和 abstract class：
     *
     * record 和 enum 是隐式 final 的，所以它们作为密封类型的子类时不用再写 final。
     *
     * 【容易出错的点】
     * 1. sealed、permits、non-sealed 都是“上下文关键字”，不是真正的保留字，
     *    所以 int sealed = 1; 这种代码依然合法，不会和老代码冲突。
     * 2. non-sealed 只能用在“直接父类/父接口是密封的”类上；随便给一个普通类加 non-sealed 会编译报错。
     * 4. 密封类不能被匿名内部类实现，也不能被 lambda 实现（因为匿名类无法声明 final/sealed/non-sealed，
     *    而且它“不在 permits 名单里”）；局部类同样不能继承密封类。
     * 5. 最大的误区：“密封 = 子类型总数有限”。并不是！
     *    封闭的只是“直接子类”这一层。本文件里 Worker 是 non-sealed，WatchWorker 又继承了 Worker，
     *    所以 Person 的“全部子类型”其实是无限的；只有“Person 的直接子类”是固定的三个。
     *    switch 的穷尽性检查也只依赖直接子类这一层（WatchWorker 会落入 case Worker 分支）。
     * 6. 反射支持（JDK17 起）：Class#isSealed() 判断是否密封，
     *    Class#getPermittedSubclasses() 取出 permits 名单。字节码中对应 PermittedSubclasses 属性。
     * 7. 和 enum 的区别：enum 限制的是“实例个数”，sealed 限制的是“子类个数”。
     */
}

// Person是一个密封类，可以被指定的子类所继承。非指定的类不能继承Person类
sealed class Person permits Student, Teacher, Worker {
}

// 要求指定的子类必须是final、sealed、non-sealed三者之一
// Student类不能被继承了
/*
 * final      ：彻底封死，自己不能再被继承，是继承树的“叶子节点”。
 * sealed     ：自己继续当密封类，再用 permits 指定下一层允许的子类（受限地继续开放）。
 * non-sealed ：主动放弃限制，宣布“我这一支对所有人开放”，谁都可以继承我（破开封闭边界）。
 *
 * 能否再被继承：final 不能；sealed 只能被 permits 名单里的类继承；non-sealed 任何类都能继承。
 * 对穷尽性检查的影响：final / sealed 分支是封闭的，可以继续往下细分 case；
 * non-sealed 分支到此为止，switch 只能匹配到这一层（子类归入父类分支）。
 *
 * abstract 子类不能写 final（抽象类必须被继承，二者矛盾）；
 * record / enum 隐式 final，所以作为子类时不用也不能再写 final；
 * non-sealed 必须有连字符，且只有在父类/父接口是 sealed 时才合法。
 */
final class Student extends Person {
}

// Teacher类只能被指定的子类继承
/*
 * 形成多层密封体系：Person → Teacher → SeniorTeacher，
 * 每一层的直接子类都在编译期完全确定；别的类想 extends Teacher 同样会编译报错。
 * 注意：Teacher 既是 Person 的子类（必须表态 final/sealed/non-sealed，它选了 sealed），
 * 又是 SeniorTeacher 的密封父类，这两个身份不冲突。
 */
sealed class Teacher extends Person permits SeniorTeacher {
}

/*
 * 从这里开始不再受限，任何类都可以继承 SeniorTeacher
 * 换句话说，它是密封体系里“主动开的一个口子”。
 * 如果写成 final，就表示 Teacher 这一支到 SeniorTeacher 彻底结束。
 */
non-sealed class SeniorTeacher extends Teacher {
}

// Worker类在继承时，没有任何限制
non-sealed class Worker extends Person {
}

class WatchWorker extends Worker {
}

//class Farmer extends Person {
//}
