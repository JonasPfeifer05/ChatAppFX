package at.pfeifer.chatapp.backend.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class Client {
    private final Socket socket;
    private final ClientOutputHandler clientOutputHandler;
    private boolean alreadyStarted = false;
    private final DataOutputStream dataOutputStream;

    public Client(String ip, int port, Consumer<String> consumer) throws IOException {
        socket = new Socket(ip, port);
        socket.setSoTimeout(10);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        clientOutputHandler = new ClientOutputHandler(consumer, dataInputStream);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    public void start() {
        if (alreadyStarted) throw new RuntimeException("Client already got started");

        System.out.println("Starting client");
        new Thread(clientOutputHandler).start();
        alreadyStarted = true;
    }

    public void stop() throws IOException {
        clientOutputHandler.stop();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8080, System.out::println);
        client.start();

        for (int i = 0; i < 5; i++) {
            new Scanner(System.in).nextLine();
            client.sendMessage("Hallo " + i);
        }

        client.stop();
    }

    public void setConsumer(Consumer<String> consumer) {
        clientOutputHandler.setConsumer(consumer);
    }
}
