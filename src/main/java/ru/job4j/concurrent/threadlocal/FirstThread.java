package ru.job4j.concurrent.threadlocal;

public class FirstThread extends Thread {

    @Override
    public void run() {
        ThreadLocalDemo.getThreadLocal().set("Это поток 1");
        System.out.println(ThreadLocalDemo.getThreadLocal().get());
    }
}
