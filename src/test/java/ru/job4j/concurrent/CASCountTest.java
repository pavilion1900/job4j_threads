package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CASCountTest {

    @Test
    void whenIncrement5Times() throws InterruptedException {
        CASCount count = new CASCount();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                count.increment();
            }
        });
        thread.start();
        thread.join();
        assertThat(count.get()).isEqualTo(15);
    }
}
