package ru.job4j.concurrent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            sums[i] = new Sums(getRowSum(matrix, i), getColumnSum(matrix, i));
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] sums = new Sums[matrix.length];
        List<CompletableFuture<Sums>> futures = IntStream.range(0, matrix.length)
                .mapToObj(index -> CompletableFuture.supplyAsync(() ->
                        new Sums(getRowSum(matrix, index), getColumnSum(matrix, index))))
                .collect(toList());
        for (int i = 0; i < futures.size(); i++) {
            sums[i] = futures.get(i).get();
        }
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
