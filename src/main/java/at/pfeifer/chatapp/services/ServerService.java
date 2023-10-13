package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.backend.server.Server;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;

import java.io.IOException;

public class ServerService {
    private static Server server = null;

    public static void startServer(int port) throws IOException, AlreadyStartedException {
        if (server != null) throw new AlreadyStartedException("Server was already started!");

        Server serverInstance = new Server(port);
        serverInstance.start();

        server = serverInstance;
    }

    public static void stopServerIfPresent() {
        if (server != null) {
            try {
                server.stop();
            } catch (IOException e) {
                System.err.println("Fatal error encountered while stopping server");
            }
        }
    }
}
