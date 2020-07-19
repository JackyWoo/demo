/**
 * Created by wujianchao on 2020/6/20.
 */
public class Test {

    public static void main(String[] args) {

        String [] input = {"bj", "sz", "bj", "sh", "xg", "cq", "cd", "cq", "sh", "xg", "tw", "sz"};

        String [] sorted1 = {"bj", "sz", "sh", "xg", "cq", "cd", "tw"};
        String [] sorted2 = {"bj", "cd", "cq", "sh", "sz", "tw", "xg"};

        printIndex(sorted1);
        printIndex(sorted2);

        printIndex(sorted2);
        printIndex(sorted1);

//        FloatVector

    }

    public static void printIndex(String[] input) {
        System.out.println((String.join(" ", input)));
    }
}
