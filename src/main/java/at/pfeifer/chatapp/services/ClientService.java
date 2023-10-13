package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.backend.client.Client;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;
import at.pfeifer.chatapp.services.exceptions.InvalidPortException;
import at.pfeifer.chatapp.services.exceptions.NotStartedException;

import java.io.IOException;
import java.util.function.Consumer;

public class ClientService {
    private static Client client = null;
    private static boolean wasStarted = false;

    public static void startClient(String ip, int port) throws InvalidPortException, IOException, AlreadyStartedException {
        if (wasStarted) throw new AlreadyStartedException("Client already got started");
        if (port < 0) throw new InvalidPortException("Invalid port passed");
        Client clientInstance = new Client(ip, port, (ignored) -> {
        });
        clientInstance.start();
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
