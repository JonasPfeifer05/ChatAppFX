package at.pfeifer.chatapp.backend.server;

import at.pfeifer.chatapp.backend.ChatLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientAcceptor implements Runnable {
    private final ServerSocket socket;
    private boolean accept = true;
    private final Object hasStoppedNotifier = 0;
    private final ChatLobby lobby;
    private List<ClientHandler> handlers;

    public ClientAcceptor(ServerSocket socket, ChatLobby lobby) throws SocketException {
        handlers = new ArrayList<>();
        this.lobby = lobby;
        socket.setSoTimeout(100);
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Acceptor is now listening for connections");
        while (accept) {
            try {
                Socket client = socket.accept();

                handlers = new ArrayList<>(handlers.stream()
                        .filter(clientHandler -> !clientHandler.isClosed())
                        .toList());
                var handler = new ClientHandler(client, lobby);
                var thread = new Thread(handler);
                thread.start();
                handlers.add(handler);
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                System.err.println("Fatal error while waiting for connections: " + e.getMessage());
                break;
            }
        }
        accept = false;
        synchronized (hasStoppedNotifier) {
            hasStoppedNotifier.notifyAll();
        }
        System.out.println("Acceptor stopped listening for connections");
    }

    public void stop() {
        if (!accept) return;
        accept = false;
        try {
            synchronized (hasStoppedNotifier) {
                hasStoppedNotifier.wait();
            }
        } catch (InterruptedException e) {
            System.err.println("Got interrupted while waiting for ClientAcceptor to stop!");
        }
        handlers.forEach(clientHandler -> {
            try {
                clientHandler.stop();
            } catch (IOException ignored) {}
        });
    }
}
