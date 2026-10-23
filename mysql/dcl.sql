-- DCL英文全称是Data Control Language(数据控制语言)，用来管理数据库用户、控制数据库的访问权限。

-- 查询用户
SELECT *
FROM mysql.user;
/*
 * Host代表当前用户访问的主机, 如果为localhost, 仅代表只能够在当前本机访问，是不可以远程访问的。
 * User代表的是访问该数据库的用户名。在MySQL中需要通过Host和User来唯一标识一个用户。
 */
/*
 * 创建用户
 * CREATE USER '用户名'@'主机名' IDENTIFIED BY '密码';
 * 修改用户密码
 * ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '新密码' ;
 * 删除用户
 * DROP USER '用户名'@'主机名' ;

 * 主机名可以使用 % 通配。
 * 'itcast'@'localhost' 和 'itcast'@'%' 是两个不同的账号，可以有不同的密码和权限。
 */
-- 案例：
-- A. 创建用户itcast, 只能够在当前主机localhost访问, 密码123456;
CREATE USER 'itcast'@'localhost' IDENTIFIED BY '123456';

-- B. 创建用户heima, 可以在任意主机访问该数据库, 密码123456;
/*
 * mysql 是 MySQL 的命令行客户端程序。这条命令要在系统终端里运行，不能放在 SQL 编辑器里执行。
 *   -u itcast：用 itcast 这个用户名登录（u 是 user）；
 *   -p       ：登录时提示输入密码（p 是 password）；
 *   不写 -h  ：默认连接本机，因此会匹配到 'itcast'@'localhost'。
 *
 * 在终端执行 mysql -u itcast -p，按回车；
 * 出现 Enter password: 后输入 123456。输入时屏幕不会显示任何字符，这是正常的保护措施，输完后按回车；
 * 登录后执行 SHOW DATABASES;，只能看到 information_schema 和 performance_schema；
 */
-- 说明：% 指的是“可以从任意主机连接 MySQL 服务器”，不是“可以访问任意数据库”。能访问哪些库由 GRANT 授权决定。
CREATE USER 'heima'@'%' IDENTIFIED BY '123456';

-- C. 修改用户heima的访问密码为1234;
# ALTER USER 'heima'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
/*
 * 区别在于 WITH 后面指定的“认证插件”，也就是密码的存储和校验方式：
 *   mysql_native_password：旧方式，基于 SHA1，安全性较弱；
 *   caching_sha2_password：MySQL 8.0 起的默认方式，基于 SHA-256，更安全。
 * 从 MySQL 8.4 开始，mysql_native_password 已被弃用，并且默认不加载。
 * 因此执行注释掉的那句，通常会报错：Plugin 'mysql_native_password' is not loaded。
 */
ALTER USER 'heima'@'%' IDENTIFIED BY '1234';

/*
 * 执行 SHOW DATABASES; 仍然只能看到两个系统库，因为 heima 现在还没有任何数据库权限；
 * % 只说明“从哪里可以连接”，不说明“连接后能做什么”。

 * heima 虽然能登录，但还没有被授予任何数据库权限。
 * MySQL 访问控制分两步：
 *   第一步是“连接验证”（认证）：根据用户名、来源主机和密码，判断能不能登录。'heima'@'%' 只管这一步；
 *   第二步是“权限检查”（授权）：登录后，每执行一条语句都检查是否有对应权限。这一步由 GRANT 决定。

 * 主机名部分（% 或 localhost）规定“允许从哪台机器连接”，相当于门禁规定从哪扇门可以进；
 * 权限（GRANT）规定“连接后能操作哪些库、哪些表，以及能执行什么操作”，相当于进门后能开哪些房间。
 */

-- D. 删除 itcast@localhost 用户
-- 删除用户时，该用户的权限也会一起删除。
DROP USER 'itcast'@'localhost';


-- 权限控制
/*
 * MySQL 常用权限
 *
 * ALL 或 ALL PRIVILEGES：表示指定授权层级支持的全部权限，
 *   不包含 GRANT OPTION（向其他账号转授权）和 PROXY（账号代理权限）。
 *   “全部权限”受授权范围限制，不一定是整个服务器的全部权限。
 *
 * SELECT：查询数据。
 * INSERT：插入新的数据行。
 * UPDATE：修改已有数据行中的字段值。
 * DELETE：删除数据行，保留表本身。
 * ALTER ：修改表结构，例如增加字段、修改字段类型。
 * DROP  ：删除数据库、表或视图。
 * CREATE：创建数据库或表。
 *
 * 常见授权范围：
 *   *.*           ：全局范围。
 *   数据库名.*     ：指定数据库范围。
 *   数据库名.表名   ：指定表范围。
 *
 * 上述说明是各权限的主要用途；具体操作可能还需要其他权限配合。
 * 授予权限使用 GRANT，撤销权限使用 REVOKE。
 */

/*
 * 查询权限
 * SHOW GRANTS FOR '用户名'@'主机名' ;
 * 授予权限
 * GRANT 权限列表 ON 数据库名.表名 TO '用户名'@'主机名';
 * 撤销权限
 * REVOKE 权限列表 ON 数据库名.表名 FROM '用户名'@'主机名';

 * 多个权限之间，使用逗号分隔
 */
-- 案例:
-- A. 查询 'heima'@'%' 用户的权限
/*
 * 输出 GRANT USAGE ON *.* TO `heima`@`%`
 * 意思是：heima 在全局范围内“没有任何权限”。
 *   USAGE：特殊的“无权限”标记，只表示这个账号存在并且可以登录；
 *   ON *.*：作用于全局范围；
 *   反引号：MySQL 显示标识符时的写法，和单引号写法表示同一个账号。
 * 每个账号都会有这一行。新建账号还没授权时，就只有这一行。
 */
SHOW GRANTS FOR 'heima'@'%';

-- B. 授予 'heima'@'%' 用户sql_study数据库所有表的所有操作权限
-- 即使 sql_study 库还不存在，MySQL 也允许授权；之后创建这个库时，权限会自动生效。
GRANT ALL ON sql_study.* TO 'heima'@'%';
/*
 * GRANT USAGE ON *.* TO `heima`@`%`
 * GRANT ALL PRIVILEGES ON `sql_study`.* TO `heima`@`%`

 * SHOW GRANTS 会按授权范围分别列出权限，每个范围显示一行：
 *   第 1 行是全局范围（*.*）：仍然只有 USAGE，也就是全局没有权限。这一行始终存在；
 *   第 2 行是数据库范围（sql_study.*）：拥有全部权限。
 *     ALL 和 ALL PRIVILEGES 是同义词，MySQL 显示时统一写成 ALL PRIVILEGES。
 * heima 只能完整操作 sql_study 库，其他库仍然没有权限。
 */

-- C. 撤销 'heima'@'%' 用户的sql_study数据库的所有权限
REVOKE ALL ON sql_study.* FROM 'heima'@'%';
-- 输出：GRANT USAGE ON *.* TO `heima`@`%`
-- REVOKE 只撤销 sql_study 库的权限，不会删除账号，所以还剩下表示“账号存在、没有权限”的 USAGE 这一行。

DROP USER 'heima'@'%';
