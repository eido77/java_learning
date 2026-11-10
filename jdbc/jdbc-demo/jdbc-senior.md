## 高级篇

### 九、JDBC优化及工具类封装

#### 9.1 现有问题

> 我们在使用JDBC的过程中，发现部分代码存在冗余的问题：
>
> - 创建连接池。
> - 获取连接。
> - 连接的回收。

#### 9.2 JDBC工具类封装V1.0

- 配置文件：[db.properties](src/main/resources/db.properties)
- 工具类代码：[JdbcUtil.java](src/main/java/com/example/senior/util/JdbcUtil.java)
- 测试代码：[JdbcUtilTest.java](src/test/java/com/example/senior/JdbcUtilTest.java)
- 注意：此种封装方式，无法保证单个请求线程多次操作数据库时使用同一个连接，因此无法保证事务！
