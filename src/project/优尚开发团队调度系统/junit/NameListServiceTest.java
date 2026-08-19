package project.优尚开发团队调度系统.junit;

import org.junit.jupiter.api.Test;
import project.优尚开发团队调度系统.domain.Employee;
import project.优尚开发团队调度系统.service.NameListService;
import project.优尚开发团队调度系统.service.TeamException;

public class NameListServiceTest {
    @Test
    public void testGetAllEmployees() {
        NameListService nameListService = new NameListService();
        Employee[] employees = nameListService.getAllEmployees();

        for (int i = 0; i < employees.length; i++) {
            System.out.println(employees[i]);
        }
    }
    
    @Test
    public void testGetEmployee() {
        // option + cmd + T：Surround With（环绕代码）
        try {
            NameListService nameListService = new NameListService();
            int id = 3; // 3	李彦宏	23	7000.0	程序员	FREE					戴尔(NEC17寸)
            id = 13; // 找不到指定员工
            Employee employee = nameListService.getEmployee(id);
            System.out.println(employee);
        } catch (TeamException e) {
            System.out.println(e.getMessage());
        }
    }
}
