package chapter17;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

public class ReflectionApplicationTest {
    /*
    1. （掌握）反射的应用1：创建运行时类的对象
    1.1 如何实现？
    通过Class的实例调用newInstance()方法即可。
    1.2 要想创建对象成功，需要满足：
    条件1：要求运行时类中必须提供一个空参的构造器
    条件2：要求提供的空参的构造器的权限要足够
     * JavaBean 不是 JDK 自带的类，也不是语法，它是一种“约定/规范”（人为约定的写法习惯）：
     * 一个 public 类 + public 空参构造器 + 私有属性 + 对应的 getter/setter。
     * 之所以要这么约定，是为了让框架（Spring、MyBatis、JSON 工具等）能用反射统一地
     * “先 new 出空对象，再逐个 set 值”，不需要知道你这个类具体长什么样。
     * 所以它是“后加的、靠人遵守的规范”，编译器不会强制检查。
    1.3 回忆：JavaBean中要求给当前类提供一个公共的空参的构造器。有什么用？
    > 场景1：子类对象在实例化时，子类的构造器的首行默认调用父类空参的构造器。
    > 场景2：在反射中，经常用来创建运行时类的对象。那么我们要求各个运行时类都提供一个空参的构造器，
            便于编写通用的创建运行时类对象的代码。
    1.4 在jdk9中标识为过时，替换成什么结构？
    通过Constructor类调用newInstance(...)
     * 完整的替代写法：
     * clazz.getDeclaredConstructor().newInstance();
     * 为什么要替换？因为 Class.newInstance() 会把构造器里抛出的受检异常“偷偷”原样抛出，
     * 绕过了编译器的异常检查，设计上被认为有缺陷；Constructor.newInstance() 会把它
     * 包装成 InvocationTargetException，异常来源更清晰，而且还能调用带参构造器。
    2. 反射应用2：获取运行时类的内部结构
    2.1 （了解）获取运行时类的内部结构1：所有属性、所有方法、所有构造器
    2.2 （熟悉）获取运行时类的内部结构2：父类、接口们、包、带泛型的父类、父类的泛型等
    3. （掌握）反射的应用3：调用指定的结构：指定的属性、方法、构造器
    4. （了解）反射的应用4：通过反射获取注解的信息
    复习：自定义注解
    ① 参照@SuppressWarnings 进行创建即可。
    ② 注解要想通过反射的方式获取，必须声明元注解：@Retention(RetentionPolicy.RUNTIME)
     * 三个元注解的分工：
     * @Target        —— 规定这个注解“能贴在什么地方”（类、属性、方法、参数……）
     * @Retention     —— 规定这个注解“能活到什么时候”：SOURCE(只在源码) / CLASS(进class但运行期读不到，默认) / RUNTIME(运行期可反射读)
     * @Documented、@Inherited —— 是否进 javadoc、是否被子类继承
     * 只有 RUNTIME 的注解，getAnnotations() 才拿得到，这就是 MethodsTest.test2 能打印出注解的原因。
     */
}

/**
 * 反射的应用一：创建运行时类的对象
 */
class NewInstanceTest {
    @Test
    /*
     * InstantiationException：“造不出来” —— 目标是抽象类/接口/数组/基本类型，或者根本没有空参构造器。
     * IllegalAccessException：“造得出来但你没权限” —— 空参构造器存在，但它是 private/protected/缺省，
     * 而调用方不在允许访问的范围内。
     */
    public void test1() throws InstantiationException, IllegalAccessException {
        Class clazz = Person1.class;

        // 创建Person类的实例
        // newInstance() 作用 = 调用该类的空参构造器创建对象（等价于 new Person1()）
        Person1 per = (Person1) clazz.newInstance();
        /*
         * clazz.newInstance() 打印 Person()...
         * 因为 newInstance() 的本质就是去跑一次 Person1 的空参构造器，
         * 而构造器体里写了 System.out.println("Person()...")。
         * 构造器先执行完，对象才生成，所以 "Person()..." 必然排在 per 的 toString 之前。
         */

        // name 为 null 是因为空参构造器没给它赋值（引用类型默认 null），age 为 1 是因为声明处 age = 1
        System.out.println(per); // Person1{name='null', age=1}
    }
}

/**
 * 反射应用2：获取运行时类的内部结构
 */
class FieldsTest {
    @Test
    public void test1() {
        Class clazz = Person1.class;
        // getFields():获取到运行时类本身及其所有的父类中声明为public权限的属性
        /*
         * Field 是 java.lang.reflect 包下的类，一个 Field 对象 = “一个属性的声明信息”
         * （修饰符、类型、名字、注解等），它不是属性的值。
         * getFields() 只返回 public 的（含父类继承来的 public），
         * 所以 Person1 的 name/info（private）拿不到，只能拿到 age 和父类 Creature 的 id。
         */
//        Field[] fields = clazz.getFields();
//        for (Field f : fields) {
//            System.out.println(f);
//        }

        // getDeclaredFields():获取当前运行时类中声明的所有属性
        /*
         * getFields()         = 本类 + 所有父类，但只要 public
         * getDeclaredFields() = 只有本类（不含父类），但权限不限（private 也给）
         * 所以这里能打印出 name、age、info 三个，但拿不到父类 Creature 的 gender/id。
         */
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            System.out.println(f);
        }
    }

    @Test
    public void test2() {
        Class clazz = Person1.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            // 1.权限修饰符
            /*
             * 0x是十六进制
             *   常量名          十六进制       十进制    二进制
             *   PUBLIC         0x00000001        1    0000 0001
             *   PRIVATE        0x00000002        2    0000 0010
             *   PROTECTED      0x00000004        4    0000 0100
             *   STATIC         0x00000008        8    0000 1000
             *   FINAL          0x00000010       16    0001 0000
             *   SYNCHRONIZED   0x00000020       32    0010 0000
             *   VOLATILE       0x00000040       64    0100 0000
             *   TRANSIENT      0x00000080      128    1000 0000
             *   NATIVE         0x00000100      256
             *   INTERFACE      0x00000200      512
             *   ABSTRACT       0x00000400     1024
             *   STRICT         0x00000800     2048
             * 组合方式是“按位或”：private static = 2 | 8 = 10，这就是 info 打印出 10 的原因。
             * 反过来判断某个修饰符在不在，用“按位与”：
             *   if ((modifier & Modifier.STATIC) != 0) { ... }  // 等价于 Modifier.isStatic(modifier)
             */
            /*
             * getModifiers() 定义在 java.lang.reflect.Member 接口里，
             * Field / Method / Constructor 都实现了 Member，所以三者都能调用它。
             * 它返回的就是上面那个“按位或组合出来的整数”，所以类型是 int。
             */
            int modifier = f.getModifiers();
            /*
             * 打印“修饰符的数字 : 修饰符的文字”，中间用冒号隔开，末尾一个制表符。
             * Person1 的三个属性依次是：
             *   name → 2:private
             *   age  → 1:public
             *   info → 10:private static
             */
            System.out.print(modifier + ":" + Modifier.toString(modifier) + "\t");

            // 2.数据类型
            /*
             * getType()
             * Field 的方法，返回这个属性“声明时的类型”，返回值是一个 Class 对象。
             * 例如 private String name → 返回 String.class；public int age → 返回 int.class。
             * 注意：它会擦掉泛型信息（List<String> 只会返回 List），要带泛型得用 getGenericType()。
             */
            Class type = f.getType();
            /*
             * getName() 是 Class 的方法，返回类的“全限定名”，如 java.lang.String。
             *   getName()       → java.lang.String / 数组是 [Ljava.lang.String;
             *   getSimpleName() → String（只要类名，更适合打印给人看）
             *   getTypeName()   → java.lang.String / 数组是 java.lang.String[]
             */
            System.out.print(type.getName() + "\t");

            // 3.变量名
            /*
             * 这里的 getName() 是 Field 的 getName()，返回属性名（如 name/age/info），
             * 和上面 Class 的 getName() 同名但不是一个方法，别搞混。
             */
            String fName = f.getName();
            System.out.println(fName);
            /*
            2:private	java.lang.String	name
            1:public	int	age
            10:private static	java.lang.String	info
             */
        }
    }
}

class MethodsTest {
    @Test
    public void test1() {
        Class clazz = Person1.class;
        // getMethods():获取到运行时类本身及其所有的父类中声明为public权限的方法
        /*
         * 一个 Method 对象 = “一个方法的声明信息”（修饰符、返回值、方法名、形参列表、throws、注解）
         * 用 getMethods() 时，除了本类 public 方法，还会带上从 Object 继承来的
         * toString/equals/hashCode/wait/notify 等一堆，输出会很长
         */
//        Method[] methods = clazz.getMethods();
//        for (Method m : methods) {
//            System.out.println(m);
//        }

        // getDeclaredMethods():获取当前运行时类中声明的所有方法
        /*
         * 只拿本类声明的方法（private 也拿得到），不含父类继承的。
         * 输出里会多出一个没写过的
         *   public int chapter17.Person1.compareTo(java.lang.Object)
         * 这是编译器为泛型接口 Comparable<Person> 自动生成的“桥方法（bridge method）”，
         * 用来做类型擦除后的转发，属于正常现象。
         * 想过滤掉可以判断 m.isBridge() / m.isSynthetic()。
         */
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m : declaredMethods) {
            System.out.println(m);
        }
    }

    // 注解信息
    // 权限修饰符 返回值类型 方法名(形参类型1 参数1,形参类型2 参数2,...) throws 异常类型1,...{}
    @Test
    public void test2() {
        Class clazz = Person1.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m : declaredMethods) {
            // 1.获取方法声明的注解
            /*
             * getAnnotations() 返回“这个方法上所有保留到运行期的注解”。
             * 元素类型是 java.lang.annotation.Annotation
             * （所有注解的公共父接口，自定义的 @MyAnnotation 本质上就是一个继承 Annotation 的接口）。
             */
            Annotation[] annos = m.getAnnotations();
            for (Annotation a : annos) {
                System.out.println(a);
            }

            // 2.权限修饰符
            /*
             * Modifier 是 java.lang.reflect.Modifier，一个“工具类”：
             * getModifiers() 是 Member 接口的方法（Field/Method/Constructor 都有），
             * 返回按位或组合的 int，含义见 FieldsTest.test2 里那张表。
             */
            System.out.print(Modifier.toString(m.getModifiers()) + "\t");

            // 3.返回值类型
            /*
             * getReturnType() 是 Method 的方法，返回“该方法返回值类型对应的 Class 对象”。
             * getReturnType() 负责“拿到类型”，getName() 负责“把这个类型变成字符串”。
             * void 方法会返回 void.class，getName() 打印出 "void"。
             */
            System.out.print(m.getReturnType().getName() + "\t");

            // 4.方法名
            /*
             * 这里的 getName() 是 Method 的 getName()，返回方法名字符串（如 showNation）。
             * 和上一行 Class 的 getName() 不是同一个方法，只是名字相同。
             */
            System.out.print(m.getName());
            System.out.print("(");

            // 5.形参列表
            /*
             * getParameterTypes() 返回该方法“形参类型”的列表，按声明顺序排列。
             * 例如 showNation(String nation, int age) → [String.class, int.class]。
             */
            Class[] parameterTypes = m.getParameterTypes();
            /*
             * 如果形参列表不是空的，就进去遍历打印
             * parameterTypes == null（数组引用为空）
             * && parameterTypes.length == 0（数组长度为 0）
             * 外面再套一个 !（取反）
             * if (!(parameterTypes == null && parameterTypes.length == 0)) {
             *
             * 这里有逻辑瑕疵：&& 要求“既是 null 又长度为 0”，而一个 null 数组去取 .length 会 NPE，
             * 所以这两个条件永远不可能同时成立 → 整个 && 恒为 false → !false 恒为 true，
             * 也就是说这个 if 其实永远都会进去。
             * 它之所以没出错，是因为 getParameterTypes() 规定“无参方法返回长度为 0 的数组、绝不返回 null”，
             * 空数组进 for 循环也不会执行，结果碰巧正确。
             * 更清晰的写法（语义等价且不绕）：
             *   if (parameterTypes.length > 0) { ... }
             * 若真想防 null，正确写法是用 ||：
             */
            if (!(parameterTypes == null || parameterTypes.length == 0)) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i == parameterTypes.length - 1) {
                        System.out.print(parameterTypes[i].getName() + " args_" + i);
                        break;
                    }
                    System.out.print(parameterTypes[i].getName() + " args_" + i + ",");
                }
            }
            System.out.print(")");

            // 6.抛出的异常
            /*
             * getExceptionTypes() 返回方法声明上 throws 后面列出的异常类型。
             * 它只反映“声明的 throws 列表”，不代表方法运行时真的会抛这些异常；
             * 方法体里直接 throw 的未声明运行时异常，这里是看不到的。
             * 没有 throws 时返回长度为 0 的数组（不是 null），所以下面 length>0 的判断是安全的。
             */
            Class[] exceptionTypes = m.getExceptionTypes();
            if (exceptionTypes.length > 0) {
                System.out.print("throws ");
                for (int i = 0; i < exceptionTypes.length; i++) {
                    if (i == exceptionTypes.length - 1) {
                        System.out.print(exceptionTypes[i].getName());
                        break;
                    }
                    System.out.print(exceptionTypes[i].getName() + ",");
                }
            }
        }
    }
}


class OtherTest {
    // 1. 获取运行时类的父类
    @Test
    public void test1() throws ClassNotFoundException {
        /*
         * Class.forName(String) 是“根据类的名字字符串，在运行期加载这个类并返回它的 Class 对象”。
         * 括号里必须是“全限定类名”（包名 + 类名，如 chapter17.Person1），不能只写 Person1，
         * 否则抛 ClassNotFoundException（这也是方法签名要 throws 它的原因）。
         * JVM 靠全名唯一定位类，import 只是编译期的语法糖，运行期不存在。
         *
         * 对比三种获取 Class 的方式：
         *   Person1.class           —— 编译期就确定，最安全
         *   对象.getClass()          —— 运行期从实例拿
         *   Class.forName("字符串")  —— 字符串可来自配置文件，最灵活（JDBC/Spring 就靠它），但没有编译检查
         * 内部类要用 $，如 chapter17.Outer$Inner。
         */
        Class clazz = Class.forName("chapter17.Person1");
        /*
         * getSuperclass() 返回“直接父类”的 Class 对象（只往上一层，不是一直到 Object）。
         * Object.class.getSuperclass() 返回 null；接口调用它也返回 null。
         * 它会丢掉泛型信息（只给 Creature），要带泛型得用 getGenericSuperclass()（见 test4）
         */
        Class superClass = clazz.getSuperclass();
        System.out.println(superClass); // class chapter17.Creature
    }

    // 2. 获取运行时类实现的接口
    @Test
    public void test2() throws ClassNotFoundException {
        Class clazz = Class.forName("chapter17.Person1");

        /*
         * getInterfaces() 返回“本类直接实现的所有接口”。
         * 它只看本类 implements 后面写的，不包含父类实现的接口，也不包含接口的父接口；
         * 顺序和 implements 书写顺序一致，所以这里先打印 Comparable 再打印 MyInterface；
         * 打印出来前缀是 interface 而不是 class，是 Class.toString() 的格式。
         */
        Class[] interfaces = clazz.getInterfaces();
        for (Class c : interfaces) {
            System.out.println(c);
            /*
            interface java.lang.Comparable
            interface chapter17.MyInterface
             */
        }
    }

    // 3. 获取运行时类所在的包
    @Test
    public void test3() throws ClassNotFoundException {
        Class clazz = Class.forName("chapter17.Person1");

        /*
         * getPackage() 返回“这个类所在的包”的描述对象，类型是 java.lang.Package，
         * 在某些类加载情况下可能返回 null（比如默认包/未加载过该包），
         * 新代码推荐用 getPackageName()（JDK 9+），它直接返回字符串且不会是 null。
         */
        Package pack = clazz.getPackage();
        System.out.println(pack); // package chapter17
    }

    // 4. 获取运行时类的带泛型的父类
    @Test
    public void test4() throws ClassNotFoundException {
        Class clazz = Class.forName("chapter17.Person1");
        /*
         * getGenericSuperclass() 返回“带泛型信息的父类”，比如 Creature<java.lang.String>。
         * 父类不带泛型时（如 extends Creature），这里返回的就是一个 Class，
         * 强转ParameterizedType 会抛 ClassCastException，这是最容易踩的坑。
         *
         * 这些泛型信息能被读到，是因为编译器把它们写进了 class 文件的 Signature 属性里，
         * 并不违背“泛型擦除”（擦除的是运行期的类型检查，不是声明处的元数据）。
         */
        Type superclass = clazz.getGenericSuperclass();
        System.out.println(superclass); // chapter17.Creature<java.lang.String>
    }

    // 5. 获取运行时类的父类的泛型 (难)
    @Test
    public void test5() throws ClassNotFoundException {
        Class clazz = Class.forName("chapter17.Person1");
        // 获取带泛型的父类（Type是一个接口，Class实现了此接口）
        Type superclass = clazz.getGenericSuperclass();
        // 如果父类是带泛型的，则可以强转为ParameterizedType
        /*
         * getActualTypeArguments() 这个方法只定义在 ParameterizedType 接口里，父接口 Type 里没有
         * 风险提示：如果父类没写泛型（extends Creature）或者没有父类，运行期会抛 ClassCastException，
         * 稳妥写法是先判断：
         *   if (superclass instanceof ParameterizedType) { ... }
         */
        ParameterizedType paramType = (ParameterizedType) superclass;
        // 调用getActualTypeArguments()获取泛型的参数，结果是一个数组，因为可能有多个泛型参数。
        /*
         * getActualTypeArguments() 取的是“泛型的实际参数（实参）”，
         * 即 Creature<String> 里尖括号中的 String，而不是声明时的 T。
         */
        Type[] arguments = paramType.getActualTypeArguments();
        // 获取泛型参数的名称
        /*
         * 打印的是“父类泛型实参的全限定类名”，结果是 java.lang.String。
         *   1) arguments            —— 上一行得到的 Type[]，这里内容是 [java.lang.String]
         *   2) arguments[0]         —— 取第 0 个泛型实参；因为 Creature<T> 只有一个泛型，
         *                              所以只有下标 0 可用，写 arguments[1] 会数组越界
         *   3) (Class) arguments[0] —— 把 Type 强转成 Class。能转成功是因为 String 就是个普通类，
         *                              Class 实现了 Type 接口；但如果实参是 List<String> 或 T，
         *                              这里就会 ClassCastException
         *   4) .getName()           —— Class 的方法，返回全限定名 java.lang.String
         *                              （getSimpleName() 则只返回 String）
         * 不强转也能打印：System.out.println(arguments[0].getTypeName()); 更安全。
         */
        System.out.println(((Class) arguments[0]).getName()); // java.lang.String
    }
}

/**
 * 反射的应用3-1：调用指定的属性
 */
class ReflectTest {
    // public int age = 1
    @Test
    public void test1() throws Exception {
        Class clazz = Person1.class;

        /*
         * newInstance() = 调用空参构造器创建对象。
         * 等价于 new Person1()，所以 Person1 的空参构造器会被执行（因此会打印 Person()...）
         */
        Person1 per = (Person1) clazz.newInstance();

        // 1. 获取运行时类指定名的属性
        /*
         * getField(String name) = 按名字找一个“public 的属性”（本类或父类的 public 都能找到）。
         * 这里能用 getField("age") 是因为 age 是 public；
         * 换成 name（private）会抛 NoSuchFieldException，
         * 那种情况必须用 getDeclaredField + setAccessible(true)。
         */
        Field ageField = clazz.getField("age");

        // 2. 获取或设置此属性的值
        /*
         * set 是 Field 的方法，作用 = “给某个对象的这个属性赋值”，相当于 per.age = 2。
         * 它的签名是 void set(Object obj, Object value)，返回类型是 void
         *
         * 两个参数的含义（必须都写，形参个数固定，不能空着）：
         *   第1个 per   —— “改哪个对象的属性”。因为 age 是实例属性，每个对象各有一份，
         *                  不指定对象 JVM 不知道该改谁
         *   第2个 2     —— “改成什么值”。这里发生了自动装箱 int 2 → Integer，
         *                  再由反射拆箱写回 int 属性
         * 如果属性是 static，第1个参数可以传 null（见 test3）
         */
        ageField.set(per, 2);
        /*
         * get “读取”，只需要知道“读哪个对象的属性”，不需要值
         * 它的签名是 Object get(Object obj)，只有一个参数，返回读到的值。
         * 返回类型是 Object，int 属性读出来会被装箱成 Integer；
         * 想直接拿原始类型可以用 ageField.getInt(per)。
         */
        System.out.println(ageField.get(per));
    }

    // private String name
    @Test
    public void test2() throws Exception {
        Class clazz = Person.class;

        Person per = (Person) clazz.newInstance();

        // 1.通过Class实例调用getDeclaredField(String fieldName)，获取运行时类指定名的属性
        /*
         * getDeclaredField(String name) = 在“本类声明的属性”里按名字找，
         * 权限不限（private 也能找到），但不会去父类里找。
         *   getField(name)         → 本类 + 父类，但只认 public
         *   getDeclaredField(name) → 只本类，但 private/protected/缺省都认
         */
        Field nameField = clazz.getDeclaredField("name");

        // 2. setAccessible(true)：确保此属性是可以访问的
        /*
         * 从 JDK 9 模块化开始，跨模块对 JDK 内部类调用它可能抛
         * InaccessibleObjectException，需要 --add-opens；对自己写的类不受影响。
         */
        nameField.setAccessible(true);

        // 3. 通过Filed类的实例调用get(Object obj) （获取的操作）
        // 或 set(Object obj,Object value) （设置的操作）进行操作。
        nameField.set(per, "Tom");
        System.out.println(nameField.get(per));
    }

    // private static String info
    @Test
    public void test3() throws Exception {
        Class clazz = Person1.class;
        /*
         * 这个测试没有创建对象，因为 info 是静态属性（类变量），
         * 它属于类本身、随类加载而存在，不依赖任何实例，所以不需要 newInstance()。
         */

        // 1.通过Class实例调用getDeclaredField(String fieldName)，获取运行时类指定名的属性
        Field infoField = clazz.getDeclaredField("info");

        // 2. setAccessible(true)：确保此属性是可以访问的
        infoField.setAccessible(true);

        // 3. 通过Filed类的实例调用get(Object obj) （获取的操作）
        // 或 set(Object obj,Object value) （设置的操作）进行操作。
//        infoField.set(Person1.class,"我是一个人");
//        System.out.println(infoField.get(Person1.class));
        /*
         * 判断标准只有一条：看属性是不是 static。
         * 实例属性（没有 static）—— 第1个参数必须传一个真实对象，传 null 会抛 NullPointerException，
         *                        因为每个对象各有一份值，不给对象就不知道读/写谁的
         * 静态属性（有 static） —— 整个类只有一份值，存在类的静态区里，与对象无关，
         *                        所以第1个参数被忽略，写 null 最合适
         *
         * 写 null 是因为 info 是静态属性，“哪个对象”这个信息无意义，用 null 占位。
         * null 不是“省略”，而是“明确地告诉它不需要对象”。
         */
        infoField.set(null, "我是一个人");
        System.out.println(infoField.get(null));
    }

    /**
     * 反射的应用3-2：调用指定的方法
     */
    // private String showNation(String nation,int age)
    @Test
    public void test4() throws Exception {
        Class clazz = Person1.class;

        Person1 per = (Person1) clazz.newInstance();

        // 1.通过Class的实例调用getDeclaredMethod(String methodName,Class ... args),获取指定的方法
        /*
         * 反射是“按方法签名精确匹配”的，int 和 Integer 在 JVM 里是两种完全不同的类型：
         * int.class 代表基本类型 int，Integer.class 代表包装类 java.lang.Integer。
         * int.class 也可以写成 Integer.TYPE，两者完全等价。
         */
        /*
         * getDeclaredMethod(String name, Class<?>... parameterTypes)
         *   在“本类声明的方法”里，按【方法名 + 形参类型列表】精确找一个方法，private 也能找到。
         * String.class, int.class —— 形参类型，个数和顺序必须和方法声明完全一致
         * getMethod 只找 public（含父类的），getDeclaredMethod 只找本类（权限不限）
         */
        Method showNationMethod = clazz.getDeclaredMethod("showNation", String.class, int.class);

        // 2. setAccessible(true)：确保此方法是可访问的
        showNationMethod.setAccessible(true);

        // 3.通过Method实例调用invoke(Object obj,Object ... objs),即为对Method对应的方法的调用。
        // invoke()的返回值即为Method对应的方法的返回值
        // 特别的：如果Method对应的方法的返回值类型为void，则invoke()返回值为null
        /*
         * invoke 的作用 = “真正去执行这个方法”，本行等价于 per.showNation("CHN", 10)。
         * 签名：Object invoke(Object obj, Object... args)
         * 括号里的内容：
         *   第1个 per            —— 用哪个对象去调用（实例方法必须给对象；静态方法可传 null，见 test5）
         *   第2个起 "CHN", 10    —— 实参，个数/顺序/类型要和形参对应
         *
         * 如果被调用的方法体内部抛了异常，invoke 不会原样抛出，
         * 而是统一包装成 InvocationTargetException，真正的异常要用 getCause() 取出来。
         */
        Object returnValue = showNationMethod.invoke(per, "CHN", 10);
        /*
         * 这里会打印两行：
         *   先是方法体内部的 "showNation..."（invoke 执行方法时打印的，属于间接输出）
         *   再是本行的 "我的国籍是：CHN，生活了10年"（方法的返回值）
         */
        System.out.println(returnValue);
    }

    // public static void showInfo()
    @Test
    public void test5() throws Exception {
        Class clazz = Person1.class;

        // 1.通过Class的实例调用getDeclaredMethod(String methodName,Class ... args),获取指定的方法
        /*
         * 括号里只写了一个东西：字符串 "showInfo"，也就是方法名。
         * 后面不用再写形参类型，是因为 showInfo() 是无参方法，
         * 而 getDeclaredMethod 的第二个参数是可变参数 Class... ，可以一个都不传。
         */
        Method showInfoMethod = clazz.getDeclaredMethod("showInfo");

        // 2. setAccessible(true)：确保此方法是可访问的
        /*
         * showInfo() 本身是 public 的，这一行其实可以不写，不会报错；
         * 写上只是形成统一的习惯（而且能略微提升反射调用性能）。
         */
        showInfoMethod.setAccessible(true);

        // 3.通过Method实例调用invoke(Object obj,Object ... objs),即为对Method对应的方法的调用。
        // invoke()的返回值即为Method对应的方法的返回值
        // 特别的：如果Method对应的方法的返回值类型为void，则invoke()返回值为null
        /*
         * 这里第1个参数传 null，是因为 showInfo() 是 static 方法 —— 静态方法属于类，
         * 不需要对象，所以 obj 参数被忽略，传 null 最清晰（传 Person1 对象也能跑通）。
         *
         * 本行会先打印方法体里的 "我是一个人"（间接输出），
         * 然后下一行打印 null —— 因为 showInfo() 返回值类型是 void，invoke 固定返回 null。
         */
        Object returnValue = showInfoMethod.invoke(null);
        System.out.println(returnValue);
    }

    /**
     * 反射的应用3-3：调用指定的构造器
     */
    // private Person(String name, int age)
    @Test
    public void test6() throws Exception {
        Class clazz = Person1.class;

        // 1.通过Class的实例调用getDeclaredConstructor(Class ... args)，获取指定参数类型的构造器
        /*
         * getDeclaredConstructor(Class<?>... parameterTypes)
         *   按“形参类型列表”在本类里找一个构造器，private 的也能找到。
         *   括号里只写形参类型，不需要写名字
         * 和 getConstructor 的区别：后者只能拿 public 构造器。
         */
        Constructor constructor = clazz.getDeclaredConstructor(String.class, int.class);

        // 2.setAccessible(true)：确保此构造器是可以访问的
        constructor.setAccessible(true);

        // 3.通过Constructor实例调用newInstance(Object ... objs),返回一个运行时类的实例。
        /*
         * Constructor.newInstance(Object... initargs) = 用这个构造器创建对象，
         * 本行等价于 new Person1("Tom", 12)。
         * 括号里写的是“传给构造器的实参”："Tom" 对应 String name，12 对应 int age，
         * 顺序和类型必须和第1步声明的形参类型一致。
         * 空参构造器就写 newInstance()（见 test7）。
         */
        Person1 per = (Person1) constructor.newInstance("Tom", 12);
        System.out.println(per);
    }

    // 使用Constructor替换原有的使用Class调用newInstance()的方式创建对象
    /*
     * JDK 9 之后用来替代过时的 clazz.newInstance() 的标准写法，
     * 即 clazz.getDeclaredConstructor().newInstance()。
     */
    @Test
    public void test7() throws Exception {
        Class clazz = Person1.class;

        // 1.通过Class的实例调用getDeclaredConstructor(Class ... args)，获取指定参数类型的构造器
        /*
         * 括号空着表示“要找形参列表为空的构造器”，也就是空参构造器 Person()。
         * 因为参数是可变参数 Class... ，传 0 个合法，含义就是“没有形参”。
         * 空着不等于“随便找一个构造器”，如果类里没有空参构造器，照样抛 NoSuchMethodException。
         */
        Constructor constructor = clazz.getDeclaredConstructor();

        // 2.setAccessible(true)：确保此构造器是可以访问的
        constructor.setAccessible(true);

        // 3.通过Constructor实例调用newInstance(Object ... objs),返回一个运行时类的实例。
        // 括号空着表示“不传实参”，因为上一步拿到的是空参构造器，本行等价于 new Person()。
        Person1 per = (Person1) constructor.newInstance();
        System.out.println(per);
    }
}

class Creature<T> {
    boolean gender;
    public int id;

    public void breath() {
        System.out.println("呼吸");
    }

    private void info() {
        System.out.println("我是一个生物");
    }
}

/**
 * 自定义注解
 */
/*
 * 定义一个自己的注解 @MyAnnotation，并给它配置两条“元注解”。
 * 第1行 @Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
 *       —— 元注解，规定 @MyAnnotation 允许被贴在哪些位置：类/接口、属性、方法、方法参数、构造器、局部变量。
 *          如果贴到没列出的位置（比如包声明），编译直接报错。
 * 第2行 @Retention(RetentionPolicy.RUNTIME)
 *       —— 元注解，规定它的生命周期能保留到运行期，这是反射能读到它的前提条件。
 * 第3行 @interface MyAnnotation {
 *       —— 声明注解类型本身（不是 class，不是 interface，是 @interface）。
 * 第4行 String value();
 *       —— 注解的“成员/属性”。
 *
 * Java 规定“注解的使用”一律以 @ 开头，用来和普通的类型名区分；
 * 贴在注解定义上的注解就叫“元注解（meta-annotation）”，即“注解的注解”。
 *
 * 注解后面的圆括号是“给注解的成员赋值”，语法类似方法调用，但语义是“配置参数”。
 * @Target 的成员类型是 ElementType[]（数组），所以：
 *   ① 赋多个值要用大括号 {} 括起来，中间逗号分隔 —— 这就是 {TYPE, FIELD, ...} 的来历；
 *   ② 只有一个值时大括号可以省略，写成 @Target(TYPE) 也合法。
 * @Retention 的成员类型是单个 RetentionPolicy 枚举值，所以直接写 RetentionPolicy.RUNTIME，不用大括号。
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    // 注解成员的类型只能是：8 种基本类型、String、Class、枚举、注解，以及这些类型的一维数组。
    String value();
}

interface MyInterface {
    void method();
}

/*
 * @MyAnnotation("t_persons")
 * 把刚才自定义的注解贴到 Person1 这个类上。
 * “使用注解”就得写 @；能贴在类上是因为 @Target 里包含了 TYPE。
 * 圆括号里的 "t_persons" 是给成员 value 赋的值，完整写法是 @MyAnnotation(value = "t_persons")，
 * 因为成员名正好叫 value，所以可以省略 "value="。
 * 注解不会改变类的行为，不读它就等于不存在。
 */
@MyAnnotation("t_persons")
class Person1 extends Creature<String> implements Comparable<Person1>, MyInterface {
    private String name;
    public int age = 1;

    @MyAnnotation("info")
    private static String info;

    public Person1() {
        System.out.println("Person()...");
    }

    protected Person1(int age) {
        this.age = age;
    }

    private Person1(String name, int age) {
        this.name = name;
        this.age = age;

    }

    public void show() throws RuntimeException, ClassNotFoundException {
        System.out.println("你好，我是一个Person");
    }

    // @MyAnnotation(value = "show_nation") —— 这里写了完整的 value=，和类上省略写法等价
    @MyAnnotation(value = "show_nation")
    private String showNation(String nation, int age) {
        System.out.println("showNation...");
        return "我的国籍是：" + nation + "，生活了" + age + "年";
    }

    @Override
    public String toString() {
        return "Person1{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public int compareTo(Person1 o) {
        return 0;
    }

    @Override
    public void method() {
    }

    public static void showInfo() {
        System.out.println("我是一个人");
    }
}
