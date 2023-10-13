package at.pfeifer.chatapp.backend;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatLobby {
    private List<Socket> clients;

    public ChatLobby() {
        clients = new ArrayList<>();
    }

    public synchronized void join(Socket socket) {
        clients.add(socket);
    }

    public synchronized void leave(Socket socket) {
        clients.remove(socket);
    }

    public synchronized void sendMessage(Socket from, String message) {
        clients = new ArrayList<>(clients.stream()
                .filter(socket -> !socket.isClosed())
                .toList());

        clients.stream()
                .filter(socket -> !socket.equals(from))
                .forEach(socket -> writeMessageToSocket(socket, message));
    }

    private static void writeMessageToSocket(Socket socket, String message) {
        try {
            new DataOutputStream(socket.getOutputStream()).writeUTF(message);
        } catch (IOException ignored) {}
    }
}
