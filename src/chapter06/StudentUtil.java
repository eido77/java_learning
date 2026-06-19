package chapter06;

public class StudentUtil {
    /**
     * 问题一：打印指定年级state的学生信息
     */
    public void printStudentWithState(Student[] students, int state) {
        for (int i = 0; i < students.length; i++) {
            if (students[i].state == state) {
                System.out.println(students[i].show());
            }
        }
    }

    /**
     * 遍历指定的学生数组
     * @param students
     */
    public void printStudents(Student[] students) {
        for (int i = 0; i < students.length; i++) {
            System.out.println(students[i].show());
        }
    }

    /**
     * 针对学生数组按照成绩属性从低到高排列
     * @param students
     */
    public void sortStudents(Student[] students) {
        for (int i = 0; i < students.length - 1; i++) {
            for (int j = 0; j < students.length - 1 - i; j++) {
                if (students[j].score > students[j + 1].score) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }



}
