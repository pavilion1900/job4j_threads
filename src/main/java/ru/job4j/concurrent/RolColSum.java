package ru.job4j.concurrent;

import java.util.concurrent.CompletableFuture;

public class RolColSum {

    public static class Sums {

        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            sums[i] = new Sums(getRowSum(matrix, i), getColumnSum(matrix, i));
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < matrix.length; i++) {
                sums[i] = new Sums(getRowSum(matrix, i), getColumnSum(matrix, i));
            }
        });
        return sums;
    }

    private static int getRowSum(int[][] data, int row) {
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[row][i];
        }
        return sum;
    }

    private static int getColumnSum(int[][] data, int column) {
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i][column];
        }
        return sum;
    }
}
