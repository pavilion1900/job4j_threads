package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int count;

    public SimpleBlockingQueue(int count) {
        this.count = count;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() >= count) {
            wait();
        }
        queue.offer(value);
        notifyAll();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T takenElement = queue.poll();
        notifyAll();
        return takenElement;
    }

    public synchronized int size() {
        return queue.size();
    }
}
