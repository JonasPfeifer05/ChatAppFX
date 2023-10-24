package at.pfeifer.chatapp.backend.client;

import at.pfeifer.chatapp.services.HashingService;
import at.pfeifer.chatapp.services.exceptions.UsernameDeclinedException;
import at.pfeifer.chatapp.services.exceptions.WrongPasswordException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private final Socket socket;
    private final ClientOutputHandler clientOutputHandler;
    private boolean alreadyStarted = false;
    private final DataOutputStream dataOutputStream;

    public Client(String ip, int port, String username, byte[] password, Consumer<String> consumer) throws IOException, WrongPasswordException, UsernameDeclinedException {
        socket = new Socket(ip, port);
        socket.setSoTimeout(100);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        clientOutputHandler = new ClientOutputHandler(consumer, dataInputStream);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] token = new byte[20];
        int ignored = dataInputStream.read(token);
        byte[] combinedHash = HashingService.hash(token, password);

        dataOutputStream.write(combinedHash);

        boolean succeeded = dataInputStream.readBoolean();
        if (!succeeded) {
            throw new WrongPasswordException("Wrong password provided");
        }

        dataOutputStream.writeUTF(username);
        succeeded = dataInputStream.readBoolean();
        if (!succeeded) {
            throw new UsernameDeclinedException("Server declined the username");
        }
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

    public void setConsumer(Consumer<String> consumer) {
        clientOutputHandler.setConsumer(consumer);
    }
}
