package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                }
        );
        Thread second = new Thread(
                () -> {
                }
        );
        System.out.println(first.getName() + " state: " + first.getState());
        System.out.println(second.getName() + " state: " + second.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                && second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getName() + " state: " + first.getState());
            System.out.println(second.getName() + " state: " + second.getState());
        }
        System.out.println(first.getName() + " state: " + first.getState());
        System.out.println(second.getName() + " state: " + second.getState());
        System.out.println("Work is finished");
    }
}
