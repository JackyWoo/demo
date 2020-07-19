package woo.demo.performance;

import java.util.Arrays;
import java.util.Random;

/**
 *
 *
 * Created by wujianchao on 2020/7/19.
 */
public class BranchPrediction {

    public static void main(String[] args) {

        BranchPrediction test = new BranchPrediction();
        test.workWithNiceBranchPrediction();
        test.workWithoutNiceBranchPrediction();
    }

    public void workWithNiceBranchPrediction(){
        System.out.println("workWithNiceBranchPrediction:");
        work(true);
    }

    public void workWithoutNiceBranchPrediction(){
        System.out.println("workWithoutNiceBranchPrediction:");
        work(false);
    }

    private void work(boolean sort){
        int len = 100_000;
        int[] array = new int[len];

        Random random = new Random();

        for(int i=0;i<len;i++){
            array[i] = random.nextInt(len);
        }

        if(sort) {
            Arrays.sort(array);
        }

        // Test
        long t1 = System.nanoTime();
        int sum = 0;
        for(int i=0;i<len;i++){
            if(array[i]>len/2){
                sum += array[i];
            }
        }
        long t2 = System.nanoTime();
        System.out.println("\tTime cost: " + (t2 - t1));
    }


}
