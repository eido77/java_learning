package chapter03;

public class IfElseTest {
    public static void main(String[] args) {
        /*
        分支结构1：if-else条件判断结构
            格式1：if (条件表达式) {
                  语块句
                  }
            格式2：“二选一”
                  if (条件表达式) {
                    语块句1;
                  } else {
                    语块句2;
                  }
            格式3：“多选一”
                  if (条件表达式1) {
                    语块句1;
                  } else if (条件表达式2) {
                    语块句2;
                  }
                  ...
                  } else if (条件表达式n) {
                    语块句n;
                  } else {
                    语块句n+1;
                  }

         */

        //案例1：成年人心率的正常范围是每分钟60-100次。体检时，如果心率不在此范围内，则提示需要做进一步的检查。
        int heartRateMin = 60;
        int heartRateMax = 100;
        int heartRate = 110;
        if ((heartRateMin <= heartRate) && (heartRateMax >= heartRate) ) {
            System.out.println("心率正常");
        } else {
            System.out.println("心率不正常");
        } // 心率不正常

        heartRate = 40;
        if (heartRate < 60 || heartRate > 100) {
            System.out.println("需要做进一步检查");
        } // 需要做进一步检查
        System.out.println("体检结束"); // 体检结束

        //案例2:定义一个整数，判定是偶数还是奇数
        int num = 13;
        if (num % 2 == 0) {
            System.out.println(num + "为偶数");
        } else {
            System.out.println(num + "为奇数"); // 13为奇数
        }

        /*
        案例3
        岳小鹏参加Java考试，他和父亲岳不群达成承诺：
        如果：
        成绩为100分时，奖励一辆跑车；
        成绩为（80，99］时，奖励一辆山地自行车：
        当成绩为［60，80］时，奖励环球影城一日游；
        其它时，胖揍一顿。
        说明：默认成绩是在［0,100］范围内
         */
        int result1 = 60;
        if (result1 == 100) { // 不能用=，要用==
            System.out.println("奖励一辆跑车");
        } else if (result1 > 80) {
            System.out.println("奖励一辆山地自行车");
        } else if (result1 >= 60) {
            System.out.println("奖励环球影城一日游");
        //结尾用 else 还是 else if 都是合法的，甚至整条链只有一个 if 也行。
        // else 兜底：它会把所有剩下的值（包括 110、-5 这种非法值）都算进最后一类。
        // else if：可能会不输出，如果分数超出范围时，不会报错但不输出，可以在最开始加上超出范围的时候输出“成绩无效”
        //非法检查必须排在「任何可能也对它成立的分支」之前，建议放最开始
        //处理非法值时，光靠 else 或 else if 结尾都不够，else 会把 110、-5 全误判进最后一类，else if (<60) 对 110 静默、对 -5 仍会误判。正确做法是在最前面单独加一个范围检查（if (result1 < 0 || result1 > 100) → 成绩无效）
        // } else if (result1 < 60) {
        } else {
            System.out.println("胖揍一顿");
        }



    }
}
