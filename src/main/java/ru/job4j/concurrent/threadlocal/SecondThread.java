package ru.job4j.concurrent.threadlocal;

public class SecondThread extends Thread {

    @Override
    public void run() {
        ThreadLocalDemo.getThreadLocal().set("Это поток 2");
        System.out.println(ThreadLocalDemo.getThreadLocal().get());
    }
}
