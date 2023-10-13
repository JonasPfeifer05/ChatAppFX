package at.pfeifer.chatapp.backend.server;

import at.pfeifer.chatapp.backend.ChatLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private final ServerSocket socket;
    private final ClientAcceptor clientAcceptor;
    private final ChatLobby lobby;
    private boolean alreadyStarted = false;

    public Server(int port) throws IOException {
        lobby = new ChatLobby();

        socket = new ServerSocket(port);
        System.out.println("Created server socket");

        clientAcceptor = new ClientAcceptor(socket, lobby);
        System.out.println("Created client acceptor");
    }

    public void start() {
        if (alreadyStarted) throw new RuntimeException("Server already got started");

        System.out.println("Starting server on port: " + socket.getLocalPort());
        new Thread(clientAcceptor).start();
        alreadyStarted = true;
    }

    public void stop() throws IOException {
        clientAcceptor.stop();
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(8080);
        server.start();

        new Scanner(System.in).nextLine();

        server.stop();
    }
}
