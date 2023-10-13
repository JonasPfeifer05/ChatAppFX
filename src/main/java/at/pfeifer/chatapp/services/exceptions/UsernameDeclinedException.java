package at.pfeifer.chatapp.services.exceptions;

public class UsernameDeclinedException extends Exception {
    public UsernameDeclinedException(String message) {
        super(message);
    }
}
