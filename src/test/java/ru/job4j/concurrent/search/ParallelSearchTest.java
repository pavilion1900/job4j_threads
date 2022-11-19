package ru.job4j.concurrent.search;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParallelSearchTest {

    @Test
    void whenSearchIndexAmongString() {
        String[] fruits = {"bananas", "apples", "peaches"};
        int index = ParallelSearch.runSearch(fruits, "apples");
        assertThat(index).isEqualTo(1);
    }

    @Test
    void whenSearchIndexAmongInt() {
        Integer[] numbers = {10, 500, 30, 70, 99, 55, 123, 48, 23, 30, 359, 801, 21};
        int index = ParallelSearch.runSearch(numbers, 359);
        assertThat(index).isEqualTo(10);
    }

    @Test
    void whenIndexNotFound() {
        Integer[] numbers = {10, 500, 30, 70, 99, 55, 123, 48, 23, 30, 359, 801, 21};
        assertThrows(IndexNotFoundException.class, () -> ParallelSearch.runSearch(numbers, 100));
    }
}
