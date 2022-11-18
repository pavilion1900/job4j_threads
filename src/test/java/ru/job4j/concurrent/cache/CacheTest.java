package ru.job4j.concurrent.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CacheTest {

    private Cache cache;
    private Base firstModel;
    private Base secondModel;

    @BeforeEach
    void setUp() {
        cache = new Cache();
        firstModel = new Base(1, 0);
        secondModel = new Base(2, 0);
    }

    @Test
    void whenAddTwoElements() throws InterruptedException {
        Thread thread1 = new Thread(() -> cache.add(firstModel));
        Thread thread2 = new Thread(() -> cache.add(secondModel));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertThat(cache.size()).isEqualTo(2);
    }

    @Test
    void whenUpdateOneElement() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            cache.add(firstModel);
            cache.add(secondModel);
        });
        Base modelForUpdate = firstModel;
        modelForUpdate.setName("New name");
        Thread thread2 = new Thread(() -> {
            cache.update(modelForUpdate);
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Base expected = new Base(1, 1);
        expected.setName("New name");
        assertAll(
                () -> assertThat(cache.size()).isEqualTo(2),
                () -> assertThat(cache.get(1)).isEqualTo(expected)
        );
    }

    @Test
    void whenThrowExceptionByUpdateOneElement() {
        cache.add(firstModel);
        cache.add(secondModel);
        Base modelForUpdate = new Base(1, 1);
        modelForUpdate.setName("New name");
        assertThrows(OptimisticException.class, () -> cache.update(modelForUpdate));
    }

    @Test
    void whenDeleteTwoElements() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            cache.add(firstModel);
            cache.add(secondModel);
        });
        Thread thread2 = new Thread(() -> {
            cache.delete(firstModel);
            cache.delete(secondModel);
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertThat(cache.size()).isEqualTo(0);
    }
}
