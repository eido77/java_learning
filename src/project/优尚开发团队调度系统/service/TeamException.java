package project.优尚开发团队调度系统.service;

public class TeamException extends Exception {
    // option + enter 显示当前代码的问题和快速修复
    static final long serialVersionUID = -8416184348686331694L;

    public TeamException() {
    }

    public TeamException(String message) {
        super(message);
    }
}
