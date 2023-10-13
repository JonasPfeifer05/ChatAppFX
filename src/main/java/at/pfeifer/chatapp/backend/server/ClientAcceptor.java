package at.pfeifer.chatapp.backend.server;

import at.pfeifer.chatapp.backend.ChatLobby;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientAcceptor implements Runnable {
    private final ServerSocket socket;
    private boolean accept = true;
    private final Object hasStoppedNotifier = 0;
    private final ChatLobby lobby;

    public ClientAcceptor(ServerSocket socket, ChatLobby lobby) throws SocketException {
        this.lobby = lobby;
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
                new Thread(new ClientHandler(client, lobby)).start();
            } catch (SocketTimeoutException ignored) {
            } catch (Exception e) {
                System.err.println("Fatal error while waiting for connections: " + e.getMessage());
                break;
            }
        }
        synchronized (hasStoppedNotifier) {
            hasStoppedNotifier.notifyAll();
        }
        System.out.println("Acceptor stopped listening for connections");
    }

    public void stop() {
        this.accept = false;
        try {
            synchronized (hasStoppedNotifier) {
                hasStoppedNotifier.wait();
            }
        } catch (InterruptedException e) {
            System.err.println("Got interrupted while waiting for ClientAcceptor to stop!");
        }
    }
}
