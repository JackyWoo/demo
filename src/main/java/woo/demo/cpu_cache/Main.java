package woo.demo.cpu_cache;

/**
 * Created by wujianchao on 2020/3/18.
 */
public class Main {

    public static void main(String[] args) {
        
        long[][] arr = new long[10_000][];
        
        for (int i = 0; i < 10_000; i++) {
            arr[i] = new long[10_000];
            for (int j = 0; j < 10_000; j++) {
                arr[i][j] = j;
            }
        }


        long element = 0L;
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            for (int j = 0; j < 10_000; j++) {
                element = arr[i][j];
            }
        }
        System.out.println("traverse cache affinity array times:" + (System.currentTimeMillis() - time) + "ms");


        time = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            for (int j = 0; j < 10_000; j++) {
                element = arr[j][i];
            }
        }
        System.out.println("traverse non cache affinity array times:" + (System.currentTimeMillis() - time) + "ms");
    }
}
