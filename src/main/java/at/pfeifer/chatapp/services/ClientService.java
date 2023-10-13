package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.backend.client.Client;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;
import at.pfeifer.chatapp.services.exceptions.InvalidPortException;
import at.pfeifer.chatapp.services.exceptions.NotStartedException;
import at.pfeifer.chatapp.services.exceptions.UndefinedClientException;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class ClientService {
    private static Optional<Client> client = Optional.empty();
    private static String ip = null;
    private static int port = -1;
    private static boolean wasDefined = false;

    public static void defineClient(String ip, int port) throws InvalidPortException, IOException {
        if (port < 0) throw new InvalidPortException("Invalid port passed");
        wasDefined = true;
        ClientService.ip = ip;
        ClientService.port = port;
        Client clientInstance = new Client(ip, port, (ignored) -> {});
        clientInstance.start();
        client = Optional.of(clientInstance);
    }

    public static void setConsumer(Consumer<String> consumer) throws NotStartedException {
        if (client.isEmpty()) throw new NotStartedException("Client needs to be started to set the consumer");
        client.get().setConsumer(consumer);
    }

    public static void stopServerIfPresent() {
        client.ifPresent(client -> {
            try {
                client.stop();
            } catch (IOException e) {
                System.err.println("Fatal error encountered while stopping client");
            }
        });
    }

    public static void sendMessage(String message) throws IOException, NotStartedException {
        if (client.isEmpty()) throw new NotStartedException("User didn't get started");
        client.get().sendMessage(message);
    }
}
