package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.backend.server.Server;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;
import at.pfeifer.chatapp.services.exceptions.InvalidPortException;

import java.io.IOException;

public class ServerService {
    private static Server server = null;

    public static void startServer(int port, String password) throws IOException, AlreadyStartedException, InvalidPortException {
        if (server != null) throw new AlreadyStartedException("Server was already started!");
        if (port < 0) throw new InvalidPortException("Invalid port passed!");

        Server serverInstance = new Server(port, HashingService.hash(password.getBytes()));
        serverInstance.start();

        server = serverInstance;
    }

    public static void stopServerIfPresent() {
        if (server != null) {
            try {
                server.stop();
                server = null;
            } catch (IOException e) {
                System.err.println("Fatal error encountered while stopping server");
            }
        }
    }
}
