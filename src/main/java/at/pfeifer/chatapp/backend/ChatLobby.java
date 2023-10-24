package at.pfeifer.chatapp.backend;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatLobby {
    private final Map<Socket, String> clients;
    private final byte[] password;

    public ChatLobby(byte[] password) {
        clients = new HashMap<>();
        this.password = Objects.requireNonNullElse(password, new byte[]{});
    }

    public byte[] getPassword() {
        return password;
    }

    public synchronized void join(Socket socket, String username) {
        sendToEveryone(username + " joined the lobby!");
        clients.put(socket, username);
    }

    public synchronized void leave(Socket socket) {
        String username = clients.get(socket);
        if (username == null) return;
        clients.remove(socket);
        sendToEveryone(username + " left the lobby!");
    }

    public synchronized void sendMessageFrom(Socket from, String message) {
        String name = clients.get(from) + ":";

        for (Socket socket : clients.keySet()) {
            if (socket.isClosed()) clients.remove(socket);
        }

        sendToAllExcept(from, name + " " + message);
    }

    public synchronized void sendToEveryone(String message) {
        clients.keySet()
                .forEach(socket -> writeMessageToSocket(socket, message));
    }

    private synchronized void sendToAllExcept(Socket except, String message) {
        clients.keySet().stream()
                .filter(socket -> !socket.equals(except))
                .forEach(socket -> writeMessageToSocket(socket, message));
    }

    private static void writeMessageToSocket(Socket socket, String message) {
        try {
            new DataOutputStream(socket.getOutputStream()).writeUTF(message);
        } catch (IOException ignored) {
        }
    }

    public boolean usernameInUse(String username) {
        return clients.containsValue(username);
    }
}
