package project.优尚开发团队调度系统.service;

import project.优尚开发团队调度系统.domain.Architect;
import project.优尚开发团队调度系统.domain.Designer;
import project.优尚开发团队调度系统.domain.Employee;
import project.优尚开发团队调度系统.domain.Programmer;

// 关于开发团队成员的管理：添加、删除等。
public class TeamService {
    /**
     * 为开发团队新增成员自动生成团队中的唯一ID，即memberId。（提示：应使用增1的方式）
     */
    private static int counter = 1;

    /**
     * 开发团队最大成员数
     */
    private final int MAX_MEMBER = 5;

    /**
     * 保存当前团队中的各成员对象
     */
    private Programmer[] team = new Programmer[MAX_MEMBER];

    /**
     * 记录团队成员的实际人数
     */
    private int total = 0;

    /**
     * 返回当前团队的所有对象
     *
     * @return 包含所有成员对象的数组，数组大小与成员人数一致
     */
    public Programmer[] getTeam() {
        // team 数组的长度固定为 MAX_MEMBER = 5，直接返回会把整个数组（包括 null 空位）都返回给调用方
//        return team;
        Programmer[] team = new Programmer[total];
        for (int i = 0; i < total; i++) {
            team[i] = this.team[i];
        }
        return team;
    }

    /**
     * 向团队中添加成员
     *
     * @param e 待添加成员的对象
     * @throws TeamException 添加失败， TeamException中包含了失败原因
     */
    public void addMember(Employee e) throws TeamException {
//        失败信息包含以下几种：
        if (total >= MAX_MEMBER) {
            throw new TeamException("成员已满，无法添加");
        }

        if (!(e instanceof Programmer)) {
            throw new TeamException("该成员不是开发人员，无法添加");
        }

        Programmer p = (Programmer) e;
        Status status = p.getStatus();
        switch (status) {
            case BUSY -> throw new TeamException("该员工已是某团队成员");

            case VOCATION -> throw new TeamException("该员工正在休假，无法添加");
        }

        boolean isExist = isExist(p);
        if (isExist) {
            throw new TeamException("该员工已在本开发团队中");
        }

        // 记录架构师、设计师、程序员的个数
        int architectNum = 0;
        int designerNum = 0;
        int programmerNum = 0;

        for (int i = 0; i < total; i++) {
            if (team[i] instanceof Architect) {
                architectNum++;
            } else if (team[i] instanceof Designer) {
                designerNum++;
            } else {
                programmerNum++;
            }
        }

        if (p instanceof Architect) {
            if (architectNum >= 1) {
                throw new TeamException("团队中至多只能有一名架构师");
            }
        } else if (p instanceof Designer) {
            if (designerNum >= 2) {
                throw new TeamException("团队中至多只能有两名设计师");
            }
        } else {
            if (programmerNum >= 3) {
                throw new TeamException("团队中至多只能有三名程序员");
            }
        }

        /*
        if (p instanceof Architect && architectNum >=1){
            throw new TeamException("团队中至多只能有一名架构师”);
        }else if (p instanceof Designer && designerNum >=2){
            throw new TeamException("团队中至多只能有两名设计师");
        }else if (p instanceof Programmer && programmerNum >= 3) {
            throw new TeamException("团队中至多只能有三名程序员");
        }
        如果只有两个设计师，p是架构师的情况
        - 不会执行if，跳到第一个else if里面
        - Architect是Designer的子类，p instanceof Designer是true
        - designerNum>=2，throw会执行，不符合题意
         */

        // 代码如果能执行到此位置，意味着p可以添加到team数组中
        team[total++] = p;
        p.setMemberId(counter++);
        p.setStatus(Status.BUSY);
    }

    /**
     * 判断p是否存在于当前开发团队中
     *
     * @param p
     * @return
     */
    private boolean isExist(Programmer p) {
        for (int i = 0; i < total; i++) {
            if (team[i].getId() == p.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从团队中删除成员
     *
     * @param memberId 待删除成员的memberId
     * @throws TeamException 找不到指定memberId的员工，删除失败
     */
    public void removeMember(int memberId) throws TeamException {
        boolean found = false;

        for (int i = 0; i < total; i++) {
            if (team[i].getMemberId() == memberId) {
                // 找到了这个成员，需要调整其相关属性
                team[i].setStatus(Status.FREE);
                // 员工memberId可以不修改，删除后ID给下一个添加的人

                // 调整数组
                for (int j = 0; j < total - 1; j++) {
                    team[j] = team[j + 1];
                }

//                team[total - 1] = null;
//                total--;
                // 假设：total = 5，team[4]是最后一个有效成员，所以要删除最后一个成员，就应该让 team[4] = null
                // 前--，先把 total 减 1，total：5 → 4，所以相当于：team[4] = null
                team[--total] = null;

                found = true;
                break;
            }
        }

        // 没找到成员
        if (!found) {
            throw new TeamException("找不到指定memberId的员工，删除失败");
        }
    }
}
