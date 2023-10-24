package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.backend.client.Client;
import at.pfeifer.chatapp.services.exceptions.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

public class ClientService {
    private static Client client = null;
    private static boolean wasStarted = false;

    public static void startClient(String ip, int port, String password, String username) throws InvalidPortException, IOException, AlreadyStartedException, UsernameDeclinedException, WrongPasswordException {
        if (wasStarted) throw new AlreadyStartedException("Client already got started");
        if (port < 0) throw new InvalidPortException("Invalid port passed");
        Client clientInstance = new Client(ip, port, (ignored) -> {
        });
        try {
            String token = clientInstance.getDataInputStream().readUTF();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            String hash = new String(md.digest(token.getBytes()));
            clientInstance.sendMessage(hash);
            boolean succeeded = clientInstance.getDataInputStream().readBoolean();
            if (!succeeded) {
                throw new WrongPasswordException("Wrong password provided");
            }
            clientInstance.sendMessage(username);
            succeeded = clientInstance.getDataInputStream().readBoolean();
            if (!succeeded) {
                throw new UsernameDeclinedException("Server declined the username");
            }
        } catch (SocketTimeoutException ignored) {
            System.err.println("Server took to long to respond");
            return;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        clientInstance.startListening();
        client = clientInstance;
        wasStarted = true;
    }

    public static void setConsumer(Consumer<String> consumer) throws NotStartedException {
        if (client == null) throw new NotStartedException("Client needs to be started to set the consumer");
        client.setConsumer(consumer);
    }

    public static void stopClientIfPresent() {
        if (client != null) {
            try {
                client.stop();
                client = null;
            } catch (IOException e) {
                System.err.println("Fatal error encountered while stopping client");
            }
        }
    }

    public static void sendMessage(String message) throws IOException, NotStartedException {
        if (client == null) throw new NotStartedException("User didn't get started");
        client.sendMessage(message);
    }
}
