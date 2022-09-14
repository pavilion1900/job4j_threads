package ru.job4j.concurrent.net;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;
    private final int speed;
    private final String fileName;

    public Wget(String url, int speed, String fileName) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            int bytesWrite = 0;
            long start = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                bytesWrite += bytesRead;
                if (bytesWrite >= speed) {
                    long duration = System.currentTimeMillis() - start;
                    if (duration < 1000) {
                        Thread.sleep(1000 - duration);
                    }
                    bytesWrite = 0;
                    start = System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void validate(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        if (args[0] == null) {
            throw new IllegalArgumentException("Not enough arguments. Url is empty");
        }
        if (args[1] == null) {
            throw new IllegalArgumentException("Not enough arguments. Speed is empty");
        }
        if (args[2] == null) {
            throw new IllegalArgumentException("Not enough arguments. File name is empty");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validate(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String userName = args[2];
        Thread wget = new Thread(new Wget(url, speed, userName));
        wget.start();
        wget.join();
    }
}
