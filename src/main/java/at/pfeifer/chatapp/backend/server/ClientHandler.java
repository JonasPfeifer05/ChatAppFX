package at.pfeifer.chatapp.backend.server;

import at.pfeifer.chatapp.backend.ChatLobby;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    private final Socket client;
    private final DataInputStream dataInputStream;
    private boolean handelInput = true;
    private final Object hasStoppedNotifier = 0;
    private final ChatLobby lobby;

    public ClientHandler(Socket client, ChatLobby lobby) throws IOException {
        this.lobby = lobby;
        this.client = client;
        client.setSoTimeout(10);
        dataInputStream = new DataInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        lobby.join(client);
        System.out.println("Now listening for client input");
        while (handelInput) {
            try {
                String input = dataInputStream.readUTF();
                lobby.sendMessage(client, input);
                System.out.println(input);
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                System.err.println("Fatal error while retrieving data from client: " + e.getMessage());
                break;
            }
        }
        lobby.leave(client);
        System.out.println("Stopped listening for client input");
    }

    public void stop() throws IOException {
        handelInput = false;
        try {
            synchronized (hasStoppedNotifier) {
                hasStoppedNotifier.wait();
            }
        } catch (InterruptedException e) {
            System.err.println("Got interrupted while waiting for client handler to stop!");
        }
        client.close();
    }
}
