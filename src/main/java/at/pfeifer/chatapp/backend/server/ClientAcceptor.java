package at.pfeifer.chatapp.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientAcceptor implements Runnable {
    private final ServerSocket socket;
    private boolean accept = true;

    public ClientAcceptor(ServerSocket socket) throws SocketException {
        socket.setSoTimeout(10);
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Acceptor is now listening for connections");
        while (accept) {
            try {
                Socket client = socket.accept();
                System.out.println(client);
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                throw new RuntimeException("Fatal error while waiting for connections: " + e.getMessage());
            }
        }
        this.notifyAll();
        System.out.println("Acceptor stopped listening for connections");
    }

    public void stop() {
        this.accept = false;
        try {
            this.wait();
        } catch (InterruptedException e) {
            System.err.println("Got interrupted while waiting for ClientAcceptor to stop!");
        }
    }
}
