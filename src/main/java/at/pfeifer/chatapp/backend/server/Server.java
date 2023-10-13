package at.pfeifer.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final ServerSocket socket;
    private final ClientAcceptor clientAcceptor;
    private boolean alreadyStarted = false;

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
        System.out.println("Created server socket");

        clientAcceptor = new ClientAcceptor(socket);
        System.out.println("Created client acceptor");
    }

    public void start() {
        if (alreadyStarted) throw new RuntimeException("Server already got started");

        new Thread(clientAcceptor).start();
        alreadyStarted = true;
    }

    public void stop() throws IOException {
        clientAcceptor.stop();
        socket.close();
    }
}
