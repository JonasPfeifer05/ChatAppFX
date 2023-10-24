package at.pfeifer.chatapp;

import at.pfeifer.chatapp.services.ServerService;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;
import at.pfeifer.chatapp.services.exceptions.InvalidPortException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {
    public static void run() throws AlreadyStartedException, IOException {
        configureAndRunServer();
        System.out.println("Press enter to stop the server!");
        new BufferedReader(new InputStreamReader(System.in)).readLine();
        ServerService.stopServerIfPresent();
    }

    private static void configureAndRunServer() throws AlreadyStartedException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int port;
        System.out.print("Enter the port to start the server at: ");
        do {
            String portString = reader.readLine();
            try {
                port = Integer.parseInt(portString);
                System.out.print("Enter password for the lobby (or empty): ");
                String password = reader.readLine();
                ServerService.startServer(port, password);
                break;
            } catch (NumberFormatException | InvalidPortException e) {
                System.err.print("Invalid port! Try again: ");
            } catch (IOException e) {
                System.err.print("Fatal error while starting the server: " + e + " Try again: ");
            }
        } while (true);
    }
}