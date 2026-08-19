package project.优尚开发团队调度系统.domain;

// cmd + 1调出来Project之后上下移动选中要创建的文件夹，然后cmd + N可以快捷创建文件
// 不能加 static：static 方法属于接口本身、不需实现类实现
// static 方法自己必须自带方法体，否则它永远没有代码可执行 → 语法直接报错
public interface Equipment {
    // 抽象方法。默认等价于：public abstract String getDescription();
    // 没有方法体 {} 的接口方法就是抽象方法
    String getDescription();
}
