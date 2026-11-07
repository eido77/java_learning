## 进阶篇

### 七、JDBC扩展

#### 7.1 实体类和ORM

> - 在使用JDBC操作数据库时，我们会发现数据都是零散的，明明在数据库中是一行完整的数据，到了Java中变成了一个一个的变量，不利于维护和管理。而我们Java是面向对象的，一个表对应的是一个类，一行数据就对应的是Java中的一个对象，一个列对应的是对象的属性，所以我们要把数据存储在一个载体里，这个载体就是实体类！
> - 实体类示例：[Employee.java](jdbc-demo/src/main/java/com/example/advanced/pojo/Employee.java)
> - ORM（Object Relational Mapping）思想，【对象到关系数据库的映射】，作用是在编程中，把面向对象的概念跟数据库中表的概念对应起来，以面向对象的角度操作数据库中的数据，即一张表对应一个类，一行数据对应一个对象，一个列对应一个属性！
> - 当下JDBC中这种过程我们称其为手动ORM。后续我们也会学习ORM框架，比如MyBatis、JPA等。
> - 封装代码：手动 ORM 映射示例：[testJdbcAdvanced.java](jdbc-demo/src/test/java/com/example/advanced/testJdbcAdvanced.java)
