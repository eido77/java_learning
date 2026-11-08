## 进阶篇

### 七、JDBC扩展

#### 7.1 实体类和ORM

> - 在使用JDBC操作数据库时，我们会发现数据都是零散的，明明在数据库中是一行完整的数据，到了Java中变成了一个一个的变量，不利于维护和管理。而我们Java是面向对象的，一个表对应的是一个类，一行数据就对应的是Java中的一个对象，一个列对应的是对象的属性，所以我们要把数据存储在一个载体里，这个载体就是实体类！
> - 实体类示例：[Employee.java](jdbc-demo/src/main/java/com/example/advanced/pojo/Employee.java)
> - ORM（Object Relational Mapping）思想，【对象到关系数据库的映射】，作用是在编程中，把面向对象的概念跟数据库中表的概念对应起来，以面向对象的角度操作数据库中的数据，即一张表对应一个类，一行数据对应一个对象，一个列对应一个属性！
> - 当下JDBC中这种过程我们称其为手动ORM。后续我们也会学习ORM框架，比如MyBatis、JPA等。
> - 封装代码：手动 ORM 映射示例：[testOrm()和testOrmList()](jdbc-demo/src/test/java/com/example/advanced/testJdbcAdvanced.java)

#### 7.2 主键回显

- 在数据中，执行新增操作时，主键列为自动增长，可以在表中直观的看到，但是在Java程序中，我们执行完新增后，只能得到受影响行数，无法得知当前新增数据的主键值。在Java程序中获取数据库中插入新数据后的主键值，并赋值给Java对象，此操作为主键回显。

- 代码实现：[testReturnPK()](jdbc-demo/src/test/java/com/example/advanced/testJdbcAdvanced.java)

#### 7.3 批量操作

- 插入多条数据时，一条一条发送给数据库执行，效率低下！

- 通过批量操作，可以提升多次操作效率！

- 代码实现：
-
    - 普通插入：[testMoresInsert()](jdbc-demo/src/test/java/com/example/advanced/testJdbcAdvanced.java)
-
    - 批量插入：[testBatch()](jdbc-demo/src/test/java/com/example/advanced/testJdbcAdvanced.java)
