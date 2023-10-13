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
    private final DataInputStream dataInputStream;

    public Client(String ip, int port, Consumer<String> consumer) throws IOException {
        socket = new Socket(ip, port);
        socket.setSoTimeout(100);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        clientOutputHandler = new ClientOutputHandler(consumer, dataInputStream);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    public void startListening() {
        if (alreadyStarted) throw new RuntimeException("Client already got started");

        System.out.println("Starting client");

        var thread = new Thread(clientOutputHandler);
        thread.start();
        alreadyStarted = true;
    }

    public void stop() throws IOException {
        clientOutputHandler.stop();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8080, System.out::println);
        client.startListening();

        for (int i = 0; i < 5; i++) {
            new Scanner(System.in).nextLine();
            client.sendMessage("Hallo " + i);
        }

        client.stop();
    }

    public void setConsumer(Consumer<String> consumer) {
        clientOutputHandler.setConsumer(consumer);
    }

// --Commented out by Inspection START (13.10.2023 18:59):
//    public DataOutputStream getDataOutputStream() {
//        return dataOutputStream;
//    }
// --Commented out by Inspection STOP (13.10.2023 18:59)

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }
}
