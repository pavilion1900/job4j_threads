package ru.job4j.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private static final String SUBJECT_TEMPLATE = "Notification %s to email %s";
    private static final String BODY_TEMPLATE = "Add a new event to %s";
    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    public void emailTo(User user) {
        String subject = String.format(SUBJECT_TEMPLATE, user.getUsername(), user.getEmail());
        String body = String.format(BODY_TEMPLATE, user.getUsername());
        pool.submit(() -> send(subject, body, user.getEmail()));
    }

    public void send(String subject, String body, String email) {

    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
