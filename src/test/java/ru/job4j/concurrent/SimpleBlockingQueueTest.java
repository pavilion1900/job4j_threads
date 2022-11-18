package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {

    @Test
    void whenAdd3AndTake2Elements() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(10);
                queue.offer(30);
                queue.offer(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
                queue.poll();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(queue.size()).isEqualTo(1);
    }

    @Test
    void whenAdd4AndTake4Elements() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(10);
                queue.offer(20);
                queue.offer(30);
                queue.offer(40);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
                queue.poll();
                queue.poll();
                queue.poll();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(queue.size()).isEqualTo(0);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(element -> {
                                try {
                                    queue.offer(element);
                                } catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                    );
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).isEqualTo(Arrays.asList(0, 1, 2, 3, 4));
    }
}
