package at.pfeifer.chatapp;

public class MainCLI {
    public static void main(String[] args) {
        try {
            CLI.run();
        } catch (Exception e) {
            System.err.println("Error while running the cli: " + e);
        }
    }
}
