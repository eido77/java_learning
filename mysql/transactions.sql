-- 事务
-- 事务 是一组操作的集合，它是一个不可分割的工作单位，事务会把所有的操作作为一个整体
-- 一起向系统提交或撤销操作请求，即这些操作要么同时成功，要么同时失败。
-- 注意：默认MySQL的事务是自动提交的，也就是说，当执行完一条DML语句时，
-- MySQL会立即隐式的提交事务。

-- 数据准备：
DROP TABLE IF EXISTS account;

CREATE TABLE account
(
    id    INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    name  VARCHAR(10) COMMENT '姓名',
    -- 金额通常使用定点数 DECIMAL
    money DECIMAL(10, 2) COMMENT '余额'
) COMMENT '账户表';

INSERT INTO account (name, money)
VALUES ('张三', 2000.00),
       ('李四', 2000.00);

-- 未控制事务
-- 测试正常情况
-- 1. 查询张三余额
SELECT *
FROM account
WHERE name = '张三';

-- 2. 张三的余额减少1000
UPDATE account
-- MySQL 的 UPDATE ... SET 不支持 -=、+= 这类复合赋值写法，写 money -= 1000 会报语法错误。
SET money = money - 1000
WHERE name = '张三';

-- 3. 李四的余额增加1000
UPDATE account
SET money = money + 1000
WHERE name = '李四';

-- 测试异常情况
-- 1. 查询张三余额
SELECT *
FROM account
WHERE name = '张三';

-- 2. 张三的余额减少1000
UPDATE account
SET money = money - 1000
WHERE name = '张三';

程序抛出异常
-- 3. 李四的余额增加1000
UPDATE account
SET money = money + 1000
WHERE name = '李四';


-- 控制事务一
-- 1). 查看/设置事务提交方式
SELECT @@autocommit;
/*
 * autocommit 是 MySQL 内置的系统变量，一直存在，默认值是 1，表示开启自动提交。
 * 执行 SET @@autocommit = 0 后，只会影响当前会话，也就是当前这一个数据库连接。
 * 它不是针对某张表或某个数据库设置的。其他连接、新打开的连接仍然是 1；断开当前连接后，这个设置也会失效。
 * 设置为 0 后，当前连接执行的 DML 会一直处于同一个未提交事务中，直到执行 COMMIT 或 ROLLBACK。

 * 如果事务中已有未提交的修改，执行 SET @@autocommit = 1 会自动提交当前事务，不会回滚。
 * 连接断开时，未提交的修改会被回滚。
 */
SET @@autocommit = 0;

-- 2). 提交事务
COMMIT;

-- 3). 回滚事务
ROLLBACK;

-- 控制事务二
-- 1). 开启事务
/*
 * SET @@autocommit = 0 是一直有效的开关。之后当前连接中的所有 DML 都需要手动提交；
 *    COMMIT 后会自动开始下一个事务，直到把 autocommit 改回 1。
 * START TRANSACTION（或 BEGIN）只开启这一次事务，autocommit 仍然保持 1。
 *    执行 COMMIT 或 ROLLBACK 后，这次事务结束，之后又恢复为每条语句自动提交。
 */
START TRANSACTION;
-- 或
BEGIN;

-- 2). 提交事务
COMMIT;

-- 3). 回滚事务
ROLLBACK;

-- 转账案例：
-- 开启事务1
SELECT @@autocommit;
SET @@autocommit = 0;

-- 恢复自动提交
SET @@autocommit = 1;

-- 开启事务2
SELECT @@autocommit;

START TRANSACTION;

-- 1. 查询张三余额
SELECT *
FROM account
WHERE name = '张三';

-- 2. 张三的余额减少1000
UPDATE account
SET money = money - 1000
WHERE name = '张三';

程序抛出异常
-- 3. 李四的余额增加1000
UPDATE account
SET money = money + 1000
WHERE name = '李四';

-- 如果正常执行完毕, 则提交事务
COMMIT;

-- 如果执行过程中报错, 则回滚事务
ROLLBACK;


-- 事务四大特性
/*
 原子性（Atomicity）：事务是不可分割的最小操作单元，要么全部成功，要么全部失败。
 一致性（Consistency）：事务完成时，必须使所有的数据都保持一致状态。
 隔离性（Isolation）：数据库系统提供的隔离机制，保证事务在不受外部并发操作影响的独立环境下运行。
 持久性（Durability）：事务提交后，修改会被永久保存，即使数据库宕机也不会丢失。
 上述就是事务的四大特性，简称ACID。
 */

-- 并发事务问题
-- 1). 脏读：一个事务读到另外一个事务还没有提交的数据。
-- 2). 不可重复读：一个事务先后读取同一条记录，但两次读取的数据不同，称之为不可重复读。
-- 3). 幻读：一个事务按照条件查询数据时，没有对应的数据行，但是在插入数据时，
--          又发现这行数据已经存在，好像出现了 "幻影"。

-- 事务隔离级别
-- 为了解决并发事务所引发的问题，在数据库中引入了事务隔离级别。主要有以下几种：
/*
 * 隔离级别                        脏读   不可重复读   幻读
 * -------------------------------------------------------
 * READ UNCOMMITTED（读未提交）      √        √         √
 * READ COMMITTED（读已提交）        ×        √         √
 * REPEATABLE READ（可重复读）       ×        ×         √
 * SERIALIZABLE（可串行化）          ×        ×         ×
 *
 * 默认使用 REPEATABLE READ，但可以修改配置。
 * 注意：事务隔离级别越高，数据越安全，但是性能越低。
 */
-- 查看事务隔离级别
SELECT @@transaction_isolation;

-- 设置事务隔离级别
/*
 * 方括号 [ ] 表示可以省略，竖线 | 表示多选一，花括号 { } 表示必须从中选择一个。
 * SESSION：只对当前连接生效，断开后失效，最常用。
 * GLOBAL：对之后新建立的连接生效，不影响已经建立的连接，并且需要相应权限。
 * 两者都不写时，只对当前会话中的下一个事务生效，生效一次后恢复原设置。
 *
 * SET [SESSION | GLOBAL] TRANSACTION ISOLATION LEVEL
 *     { READ UNCOMMITTED
 *     | READ COMMITTED
 *     | REPEATABLE READ
 *     | SERIALIZABLE };
 */
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
