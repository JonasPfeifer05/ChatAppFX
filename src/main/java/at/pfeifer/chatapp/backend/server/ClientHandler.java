package at.pfeifer.chatapp.backend.server;

import at.pfeifer.chatapp.backend.ChatLobby;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ClientHandler implements Runnable {
    private final Socket client;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private boolean handelInput = true;
    private final Object hasStoppedNotifier = 0;
    private final ChatLobby lobby;

    public ClientHandler(Socket client, ChatLobby lobby) throws IOException {
        this.lobby = lobby;
        this.client = client;
        client.setSoTimeout(100);
        dataInputStream = new DataInputStream(client.getInputStream());
        dataOutputStream = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            client.setSoTimeout(1000);
            byte[] randomBytes = new byte[20];
            new Random().nextBytes(randomBytes);
            String randomString = new String(randomBytes);

            dataOutputStream.writeUTF(randomString);
            String passwordHash = dataInputStream.readUTF();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(lobby.getPassword().getBytes());
            String correctHash = new String(md.digest(randomString.getBytes()));
            if (!passwordHash.equals(correctHash)) {
                System.out.println("Invalid password passed");
                handelInput = false;
                dataOutputStream.writeBoolean(false);
                return;
            }
            dataOutputStream.writeBoolean(true);

            String username = dataInputStream.readUTF();
            if (lobby.usernameInUse(username)) {
                dataOutputStream.writeBoolean(false);
                handelInput = false;
                client.close();
                return;
            }
            dataOutputStream.writeBoolean(true);
            client.setSoTimeout(100);
            lobby.join(client, username);
        } catch (IOException e) {
            System.err.println("Failed to communicate with client");
            try {
                dataOutputStream.writeBoolean(false);
                client.close();
                return;
            } catch (IOException ignored) {
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Now listening for client input");
        while (handelInput) {
            try {
                String input = dataInputStream.readUTF();
                lobby.sendMessageFrom(client, input);
                System.out.println(input);
            } catch (EOFException e) {
                System.err.println("Can no longer read data from client: " + e.getMessage());
                break;
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                System.err.println("Fatal error while waiting for client input: " + e.getMessage());
                break;
            }
        }
        lobby.leave(client);
        handelInput = false;
        synchronized (hasStoppedNotifier) {
            hasStoppedNotifier.notifyAll();
        }
        System.out.println("Stopped listening for client input");
    }

    public void stop() throws IOException {
        if (!handelInput) return;
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

    public boolean isClosed() {
        return client.isClosed();
    }
}
