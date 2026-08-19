package project.优尚开发团队调度系统.domain;

import project.优尚开发团队调度系统.service.Status;

public class Programmer extends Employee {
    private int memberId; // 成员加入开发团队后在团队中的ID
    // option + enter（显示上下文操作 / 快速修复）
    // 创建了枚举，但是没被识别的情况
    // 赋默认值（初始化字段）：声明一个字段 status，类型是 Status，同时给它赋初始值 Status.FREE
    // 枚举里定义的每一个常量（如 FREE、BUSY）本质上都是 Status 类型的一个静态实例。
    // Status.FREE 就是访问 Status 这个枚举里名为 FREE 的那个实例
    // 枚举特有的，普通类不会自动帮你生成这些常量实例。
    private Status status = Status.FREE;
    private Equipment equipment;

    public Programmer() {
    }

    public Programmer(int id, String name, int age, double salary, Equipment equipment) {
        super(id, name, age, salary);
        this.equipment = equipment;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        // equipment 单独写，自动调用 equipment.toString()
        return getDetails() + "\t程序员\t" + status + "\t\t\t\t\t" + equipment.getDescription();
    }

    public String getDetailsForTeam() {
        return memberId + "\t" + getId() + "\t" + getName() + "\t" + getAge() + "\t" + getSalary() + "\t程序员";
    }
}
