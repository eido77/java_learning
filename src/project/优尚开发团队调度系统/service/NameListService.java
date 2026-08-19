package project.优尚开发团队调度系统.service;

import project.优尚开发团队调度系统.domain.Architect;
import project.优尚开发团队调度系统.domain.Designer;
import project.优尚开发团队调度系统.domain.Employee;
import project.优尚开发团队调度系统.domain.Equipment;
import project.优尚开发团队调度系统.domain.NoteBook;
import project.优尚开发团队调度系统.domain.PC;
import project.优尚开发团队调度系统.domain.Printer;
import project.优尚开发团队调度系统.domain.Programmer;

/**
 * 负责将Data中的数据封装到Employee[]数组中，同时提供相关操作Employee[]的方法。
 */
public class NameListService {
    private Employee[] employees; // 保存公司所有员工对象

    public NameListService() {
        // 根据项目提供的Data类构建相应大小的employees数组
        employees = new Employee[Data.EMPLOYEES.length];

        for (int i = 0; i < employees.length; i++) {
            int type = Integer.parseInt(Data.EMPLOYEES[i][0]); // 员工类型

            // 获取通用的属性
            int id = Integer.parseInt(Data.EMPLOYEES[i][1]);
            String name = Data.EMPLOYEES[i][2];
            int age = Integer.parseInt(Data.EMPLOYEES[i][3]);
            double salary = Double.parseDouble(Data.EMPLOYEES[i][4]);

            // 再根据Data类中的数据构建不同的对象，包括Employee、Programmer、Designer和Architect对象，以及相关联的Equipment子类的对象
            // 将对象存于数组中
            switch (type) {
                // 写法一：箭头（->）—— 新式写法（Java 14+）
                // 箭头写法：只执行 -> 右边的代码块，执行完自动跳出 switch
                // 【不会】穿透(fall-through)到下一个 case，所以不需要 break
                // 如果只有一行，甚至可以省略大括号：
                // case Data.EMPLOYEE -> employees[i] = new Employee();
                case Data.EMPLOYEE -> {
                    employees[i] = new Employee(id, name, age, salary);
                }
                // 写法二：冒号（:）—— 传统写法（老写法）
                // 冒号写法：从匹配的 case 开始往下执行
                // 【会】穿透！如果不写 break，会继续执行下一个 case 的代码，直到遇到 break 或 switch 结束
                // break 必须写，否则会“漏”到下面的 case 继续执行
//                case Data.EMPLOYEE:
//                    employees[i] = new Employee();
//                    break;

                // 箭头写法和冒号写法一个很重要的区别——变量作用域（scope）
                // 旧的冒号写法，两个 case 里的 equipment 变量名会冲突报错
                //                          箭头 ->        冒号 :（不加 {}）
                //每个 case 作用域         	独立         	共享同一个
                //不同 case 变量能否重名	    可以	            不行，会报错
                //解决重名的办法	           不用管         	改名 或 手动加 { }

                case Data.PROGRAMMER -> {
                    Equipment equipment = createEquipment(i);
                    employees[i] = new Programmer(id, name, age, salary, equipment);
                }

                // 如果用旧方法的情况，是不是equipment的声明的名字不能和PROGRAMMER里面的一样啊？
                case Data.DESIGNER -> {
                    Equipment equipment = createEquipment(i);
                    double bonus = Double.parseDouble(Data.EMPLOYEES[i][5]);
                    employees[i] = new Designer(id, name, age, salary, equipment, bonus);
                }

                case Data.ARCHITECT -> {
                    Equipment equipment = createEquipment(i);
                    double bonus = Double.parseDouble(Data.EMPLOYEES[i][5]);
                    int stock = Integer.parseInt(Data.EMPLOYEES[i][6]);
                    employees[i] = new Architect(id, name, age, salary, equipment, bonus, stock);
                }
            }
        }
    }

    private Equipment createEquipment(int index) {
        int equipementType = Integer.parseInt(Data.EQUIPMENTS[index][0]);

        String modelOrName = Data.EQUIPMENTS[index][1];
        String priceOrDisplayOrType = Data.EQUIPMENTS[index][2];

        // switch 的工作方式是：看一下圆括号里的值是多少，然后跳到对应的 case 去执行。
        switch (equipementType) {
            case Data.PC -> {
                return new PC(modelOrName, priceOrDisplayOrType);
            }

            case Data.NOTEBOOK ->  {
                double price = Double.parseDouble(priceOrDisplayOrType);
                return new NoteBook(modelOrName, price);
            }

            case Data.PRINTER -> {
                return new Printer(modelOrName, priceOrDisplayOrType);
            }
        }
        return null;
        // 或者return switch (equipmentType) {
        // 单行：箭头右边直接是要返回的值，不用写 return，不用大括号
        // case Data.PC -> new PC(Data.EQUIPMENTS[index][1], Data.EQUIPMENTS[index][2]);
        // 多行：需要计算的用 { }，最后用 yield 把值“交出去”（相当于返回给 switch）
//        case Data.NOTEBOOK -> {
//            int price = Integer.parseInt(Data.EQUIPMENTS[index][2]);
//            yield new NoteBook(Data.EQUIPMENTS[index][1], price);  // yield：把值返回给 switch 表达式
//        }
        // 表达式写法必须处理“所有情况”，所以要有 default 兜底
//        default -> null;
    }

    /**
     * 获取当前所有员工。
     * @return 包含所有员工对象的数组
     */
    public Employee[] getAllEmployees() {
        return employees;
    }

    /**
     * 获取指定ID的员工对象。
     * 参数：指定员工的ID
     * 异常：找不到指定的员工
     * （在service子包下提供自定义异常类：TeamException）
     * @param id
     * @return 指定员工对象
     */
    public Employee getEmployee(int id) throws TeamException {
        for (int i = 0; i < employees.length; i++) {
            if (employees[i].getId() == id) {
                // return 一旦执行，方法立刻结束并"跳出去"，后面的代码（包括没跑完的循环）通通不再执行。
                // return 是"立即退出"：不光是跳出循环，而是跳出整个方法（break 只跳出循环，return 跳出整个方法，这俩不一样）
                // return 在这段代码里"最多执行一次"
                return employees[i];
            }
        }
        // 如果执行到此位置，就意味着没找到
        // 【关键】只有当 for 循环把所有员工都检查完一遍，
        // 都没有 return（也就是没找到）时，才会执行到这一行
        // 此时抛出异常，表示"没找到指定员工"
        throw new TeamException("找不到指定员工");
    }
}
