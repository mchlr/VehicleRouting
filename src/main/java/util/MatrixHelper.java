package util;

import java.util.Arrays;

public class MatrixHelper {

    private static MatrixHelper me = new MatrixHelper();

    private MatrixHelper() {
    }

    public static MatrixHelper getInstance() {
        if (me == null) {
            me = new MatrixHelper();
        }
        return me;
    }

    public static void prettyprintmatrix(double[][] Matrix) {
        Arrays.stream(Matrix).forEach((row) -> {
            System.out.print("[");
            Arrays.stream(row).forEach((el) -> System.out.print(" " + el + " "));
            System.out.println("]");
        });
    }

    public static void fuckYouPrintMatrix(double[][] Matrix) {
        for(int i = 0; i < Matrix.length; i++) {

            System.out.println("Spalte = " + i);
            for(int j = 0; j < Matrix[0].length; j++) {
                System.out.println("Zeile " + j + " = " + Matrix[i][j]);   
            }
            System.out.println("\n");
        }
    }
}