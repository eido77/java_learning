## 高级篇

### 九、JDBC优化及工具类封装

#### 9.1 现有问题

> 我们在使用JDBC的过程中，发现部分代码存在冗余的问题：
>
> - 创建连接池。
> - 获取连接。
> - 连接的回收。

#### 9.2 JDBC工具类封装V1.0

- 配置文件：[db.properties](jdbc-demo/src/main/resources/db.properties)
- 工具类代码：[JdbcUtil.java](jdbc-demo/src/main/java/com/example/senior/util/JdbcUtil.java)
- 测试代码：[JdbcUtilTest.java](jdbc-demo/src/test/java/com/example/senior/JdbcUtilTest.java)
- 注意：此种封装方式，无法保证单个请求线程多次操作数据库时使用同一个连接，因此无法保证事务！

#### 9.3 ThreadLocal

> JDK 1.2的版本中就提供java.lang.ThreadLocal，为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。通常用来在在多线程中管理共享数据库连接、Session等。
>
> ThreadLocal用于保存某个线程共享变量，原因是在Java中，每一个线程对象中都有一个ThreadLocalMap<ThreadLocal， Object>，其key就是一个ThreadLocal，而Object即为该线程的共享变量。
>
> 而这个map是通过ThreadLocal的set和get方法操作的。对于同一个static ThreadLocal，不同线程只能从中get，set，remove自己的变量，而不会影响其他线程的变量。
>
> - 在进行对象跨层传递的时候，使用ThreadLocal可以避免多次传递，打破层次间的约束。
> - 线程间数据隔离。
> - 进行事务操作，用于存储线程事务信息。
> - 数据库连接，`Session`会话管理。
>
> 1、ThreadLocal对象.get: 获取ThreadLocal中当前线程共享变量的值。
>
> 2、ThreadLocal对象.set: 设置ThreadLocal中当前线程共享变量的值。
>
> 3、ThreadLocal对象.remove: 移除ThreadLocal中当前线程共享变量的值。

![image-20240223104919548](JDBC.assets/image-20240223104919548.png)
