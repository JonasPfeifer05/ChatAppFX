package at.pfeifer.chatapp.services.exceptions;

public class AlreadyStartedException extends Exception {
    public AlreadyStartedException(String message) {
        super(message);
    }
}
