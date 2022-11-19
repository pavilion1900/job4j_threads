package ru.job4j.concurrent.search;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final int from;
    private final int to;
    private final T value;

    public ParallelSearch(T[] array, int from, int to, T value) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.value = value;
    }

    @Override
    protected Integer compute() {
        if (to - from < 10) {
            return searchIndex(array, from, to, value);
        }
        int mid = (from + to) / 2;
        ParallelSearch<T> firstSearch = new ParallelSearch<>(array, from, mid, value);
        ParallelSearch<T> secondSearch = new ParallelSearch<>(array, mid + 1, to, value);
        firstSearch.fork();
        secondSearch.fork();
        int firstIndex = firstSearch.join();
        int secondIndex = secondSearch.join();
        return Math.max(firstIndex, secondIndex);
    }

    public static <T> int runSearch(T[] array, T value) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer index = forkJoinPool.invoke(
                new ParallelSearch<>(array, 0, array.length - 1, value));
        if (index == -1) {
            throw new IndexNotFoundException("Value " + value + " not found");
        }
        return index;
    }

    private int searchIndex(T[] array, int from, int to, T value) {
        int index = -1;
        for (int i = from; i <= to; i++) {
            if (Objects.equals(value, array[i])) {
                index = i;
                break;
            }
        }
        return index;
    }
}
