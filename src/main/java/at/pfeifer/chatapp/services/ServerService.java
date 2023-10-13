package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.backend.server.Server;

import java.io.IOException;
import java.util.Optional;

public class ServerService {
    public static Optional<Server> server = Optional.empty();

    public static void startServer(int port) throws IOException {
        if (server.isPresent()) throw new RuntimeException("Server was already started!");

        Server serverInstance = new Server(port);
        serverInstance.start();

        server = Optional.of(serverInstance);
    }

    public static void stopServerIfPresent() {
        server.ifPresent(server -> {
            try {
                server.stop();
            } catch (IOException e) {
                System.err.println("Fatal error encountered while stopping server");
            }
        });
    }
}
