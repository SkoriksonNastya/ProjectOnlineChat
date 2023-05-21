package client;

import logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver extends Thread {

    private static final Logger LOG = Logger.getLog();
    private final BufferedReader in;

    public Receiver (BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (! Thread.currentThread().isInterrupted()) {
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
