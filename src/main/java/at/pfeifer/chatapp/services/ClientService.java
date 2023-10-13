package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.backend.client.Client;

import java.io.IOException;
import java.util.Optional;

public class ClientService {
    public static Optional<Client> client = Optional.empty();

    public static void startClient(String ip, int port) throws IOException {
        if (client.isPresent()) throw new RuntimeException("Client was already started!");

        Client clientInstance = new Client(ip, port, System.out::println);
        clientInstance.start();

        client = Optional.of(clientInstance);
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
}
