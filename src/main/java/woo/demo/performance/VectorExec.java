package woo.demo.performance;

import java.util.Random;

/**
 * Created by wujianchao on 2020/7/19.
 */
public class VectorExec {

    public static void main(String[] args) {

        VectorExec test = new VectorExec();
        long t1 = System.nanoTime();
        test.vectorExec();
        long t2 = System.nanoTime();
        test.scalarExec();
        long t3 = System.nanoTime();

        System.out.println("vector exec time cost: " + (t2-t1));
        System.out.println("scalar exec time cost: " + (t3-t2));

    }

    int len = 100_000;
    int batchSize = 1000;

    private void vectorExec() {
        for(int i=0;i<len/batchSize;i+=batchSize){
            vectorTransform(vectorScan(i));
        }
    }
    private int[] vectorScan(int pre){
        int[] result = new int[batchSize];
        for(int i=0;i<batchSize;i++){
            result[i] = ++pre;
        }
        return result;
    }
    private int[] vectorTransform(int[] a){
        int[] result = new int[a.length];
        for(int i=0;i<a.length;i++){
            result[i] += 1;
        }
        return result;
    }

    private void scalarExec() {
        for(int i=0;i<len;i++){
            transform(scan(i));
        }
    }
    private int scan(int pre){
        return ++pre;
    }

    private int transform(int a){
        return a + 1;
    }
}
