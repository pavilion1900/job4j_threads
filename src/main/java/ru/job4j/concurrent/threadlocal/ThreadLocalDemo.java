package ru.job4j.concurrent.threadlocal;

public class ThreadLocalDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static ThreadLocal<String> getThreadLocal() {
        return threadLocal;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread first = new FirstThread();
        Thread second = new SecondThread();
        threadLocal.set("Это поток main");
        System.out.println(threadLocal.get());
        first.start();
        second.start();
        first.join();
        second.join();
    }
}
