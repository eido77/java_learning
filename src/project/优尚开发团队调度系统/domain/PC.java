package project.优尚开发团队调度系统.domain;

// 类实现接口：用 implements
// 接口继承接口：用 extends
// 类继承类：用 extends
public class PC implements Equipment {
    private String model; // 机器的型号
    private String display; // 显示器名称

    public PC() {
    }

    public PC(String model, String display) {
        this.model = model;
        this.display = display;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String getDescription() {
        return model + "(" + display + ")";
    }
}
