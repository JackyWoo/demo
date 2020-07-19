package woo.demo.performance;

import java.util.Random;

/**
 * Created by wujianchao on 2020/7/19.
 */
public class CacheLine {

    public static void main(String[] args) {

        CacheLine test = new CacheLine();
        test.workWithNiceCacheLineHit();
        test.workWithoutNiceCacheLineHit();
    }

    private void workWithNiceCacheLineHit() {
        System.out.println("workWithNiceCacheLineHit");
        work(true);
    }

    private void workWithoutNiceCacheLineHit() {
        System.out.println("workWithoutNiceCacheLineHit");
        work(false);
    }

    private void work(boolean useCacheLine){
        int len = 10_000;
        int[][] matrix = new int[len][len];

        Random random = new Random();

        for(int i=0;i<len;i++){
            for(int j=0;j<len;j++){
                matrix[i][j] = random.nextInt(len);
            }
        }

        int a;

        long t1 = System.nanoTime();
        //是否利用CPU CacheLine
        if(useCacheLine){
            for(int i=0;i<len;i++){
                for(int j=0;j<len;j++){
                    a = matrix[i][j];
                }
            }
        }else{
            for(int i=0;i<len;i++){
                for(int j=0;j<len;j++){
                    a = matrix[j][i];
                }
            }
        }
        long t2 = System.nanoTime();
        System.out.println("\tTime cost: " + (t2-t1));
    }
}
