package ru.job4j.concurrent;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

    private static final int SIZE = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(SIZE);

    public ThreadPool() {
        for (int i = 0; i < SIZE; i++) {
            Thread thread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        tasks.poll().run();
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        Runnable job1 = () -> System.out.println(Thread.currentThread().getName());
        Runnable job2 = () -> System.out.println(Thread.currentThread().getName());
        Runnable job3 = () -> System.out.println(Thread.currentThread().getName());
        threadPool.work(job1);
        threadPool.work(job2);
        threadPool.work(job3);
        threadPool.shutdown();
    }
}
