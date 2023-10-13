package at.pfeifer.chatapp.backend.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;

public class ClientInputHandler implements Runnable {
    private final Consumer<String> consumer;
    private final DataInputStream dataInputStream;
    private boolean read = true;
    private final Object hasStoppedLock = 0;

    public ClientInputHandler(Consumer<String> consumer, DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        System.out.println("Now listening for input");
        while (read) {
            try {
                String input = dataInputStream.readUTF();
                consumer.accept(input);
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                throw new IllegalArgumentException("Fatal error while retrieving data from server: " + e.getMessage());
            }
        }
        System.out.println("Stopped listening for input");
        synchronized (hasStoppedLock) {
            hasStoppedLock.notifyAll();
        }
    }

    public void stop() {
        this.read = false;
        synchronized (hasStoppedLock) {
            try {
                hasStoppedLock.wait();
            } catch (InterruptedException e) {
                System.err.println("Got interrupted while waiting for ClientAcceptor to stop!");
            }
        }
    }
}
