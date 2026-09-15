package chapter13;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

public class DAOTest {
    @Test
    public void test1() {
        CustomerDAO dao1 = new CustomerDAO();
        /*
         * CustomerDAO 在继承时写的是 extends DAO<Customer>，
         * 相当于把父类 DAO<T> 里所有的 T 都"固定替换"成了 Customer。
         */
        dao1.insert(new Customer());
        Customer customer = dao1.queryForInstance(1);
    }

    @Test
    public void test2() {
        Order2DAO dao2 = new Order2DAO();
        /*
         * Order2DAO extends DAO<Order2>，T 被固定成了 Order2，
         * 所以 insert(T) 变成了 insert(Order2)，只接受 Order2 类型的对象。
         */
        dao2.insert(new Order2());

        /*
         * 父类方法声明是 List<T> queryForList(int id)，
         * 在 Order2DAO 里 T = Order2，所以 List<T> 就变成了 List<Order2>。
         * 也就是"返回一个装满 Order2 对象的集合"。
         */
        List<Order2> list = dao2.queryForList(1);
    }
}

/**
 * DAO:Data(Base) Access Object。内部封装了操作数据库相关表的增删改查操作。(CRUD)
 * 它的职责是"把对数据库某张表的增(insert)、删(delete)、改(update)、查(query) 操作封装成 Java 方法"。
 */
/*
 * 用泛型是为了让这一个 DAO<T> 类能"复用"给所有的表/实体类。
 * 把"具体是哪种实体"这件事延迟到子类继承时再确定
 *
 * 实际项目里通常是"一张表(一个实体类)对应一个 DAO"。命名习惯是：实体名 + DAO
 * 例如 Customer -> CustomerDAO，Order -> OrderDAO。
 *
 * DAO<T> 是"公共父类"，把所有实体都通用的 CRUD 方法写在里面。
 * 然后 CustomerDAO / Order2DAO 只需 extends DAO<Customer> / DAO<Order2>，
 * 就自动拥有了针对各自实体的、类型安全的增删改查方法，不用再重复写。
 * 这就是"泛型 + 继承"配合起来减少重复代码的经典写法。
 *
 * 现实开发中很少手写这种 JDBC 版 DAO，更多用框架：MyBatis、
 * Spring Data JPA、Hibernate 等，它们把 DAO 的重复工作自动化了。
 */
class DAO<T> {
    // 增
    /*
     * T 是"参数类型"。insert(T bean) 表示"要插入的对象类型是 T"。
     * T 是在类名 DAO<T> 上声明的类型占位符，子类继承时把它替换成具体类型，
     * 于是 CustomerDAO 里就变成 insert(Customer)，Order2DAO 里变成 insert(Order2)。
     */
    public void insert(T bean) {
        // 通过相应的sql语句，将bean对象的属性值写入到数据表中。
    }

    // 删
    /*
     * 返回 T 通常表示"返回被删除的那条记录对应的对象"（方便调用方拿到刚删掉的数据）。
     * T 的作用还是那个"类型占位符"：CustomerDAO 里它就是 Customer，返回 Customer。
     */
    public T deleteById(int id) {
        // 略
        return null;
    }

    // 改
    public void update(int id, T bean) {
        // 略
    }

    // 查
    // 查询一条记录
    /*
     * 返回类型是 T（单个对象），而不是 List<T>（一堆对象），
     * 一个 T 只能装一条记录，所以判断它是"查一条"。
     * 参数是单个 id（按主键定位唯一一条），以及方法名 queryForInstance
     * （Instance = 一个实例/一个对象），三者共同说明它查的是一条记录。
     */
    public T queryForInstance(int id) {
        // 略
        return null;
    }

    // 查询多条记录构成的集合
    /*
     *   queryForInstance 返回 T        -> 一个对象 -> 查一条；
     *   queryForList     返回 List<T>  -> 一个装很多 T 的集合 -> 查多条。
     */
    public List<T> queryForList(int id) {
        return null;
    }

    // 定义泛型方法
    // 比如：查询表中的记录数。（E：Long类型）
    // 比如：查询表中最大的生日。（E:Date类型）
    /*
     * 【建议】当前 getValue 只接收一个 String sql 参数，直接把原始 SQL 传进来，
     * 现实中这样容易被 SQL 注入攻击，也不好传参数。
     * 更规范的做法是用 PreparedStatement + 占位符，例如：
     *   public <E> E getValue(String sql, Object... args) { ... }
     */
    public <E> E getValue(String sql) {
        return null;
    }
}

/**
 * ORM思想(Object Relational Mapping)（对象-关系映射）
 * 数据库中的一个表 与 Java中的一个类对应
 * 表中的一条记录 与 Java类的一个对象对应
 * 表中的一个字段（或列） 与 Java类的一个属性（或字段）对应
 * 真实项目里一般不手写映射，而是用成熟的 ORM 框架，
 * 比如 MyBatis（半自动）、Hibernate / JPA（全自动）。它们底层正是基于这套 ORM 思想。
 * 现在这个 Customer 类，就是"ORM 里和 customer 表对应的那个类"的示例。
 */
class Customer {
    int id;
    String name;
    String email;
    Date birth;
}

class CustomerDAO extends DAO<Customer> {
}

class Order2 {
    int orderId;
    String orderName;
    Date orderDate;
}

class Order2DAO extends DAO<Order2> {
}
