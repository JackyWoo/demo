/**
 * Created by wujianchao on 2020/7/18.
 */
public class MatrixPrint {

    public static void main(String[] args) {
        int[][] matrix = new int[3][];
        int[] a = new int[]{1,2,3};
        int[] b = new int[]{4,5,6};
        int[] c = new int[]{7,8,9};

        matrix[0] = a;
        matrix[1] = b;
        matrix[2] = c;

        MatrixPrint printer = new MatrixPrint();
        printer.print(matrix);

    }

    public void print(int[][] matrix){
        if(matrix == null){
            return;
        }

        int len1 = matrix.length;
        if(len1 == 0){
            return;
        }
        int len2 = matrix[0].length;

        StringBuffer sb = new StringBuffer();
        for(int j=0;j<len2;j++){
            if(j%2==0) {
                for (int i = 0; i < len1; i++) {
                    sb.append(matrix[i][j] + ",");
                }
            }else{
                for (int i = len1-1; i >=0; i--) {
                    sb.append(matrix[i][j] + ",");
                }
            }
        }

        System.out.println(sb.subSequence(0, sb.length() -1));
    }
}
