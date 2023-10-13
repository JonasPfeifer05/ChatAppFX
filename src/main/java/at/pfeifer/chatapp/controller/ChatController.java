package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.exceptions.NotStartedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane chatScene;

    @FXML
    private TextField messageInput;

    @FXML
    private ListView<String> messages;

    @FXML
    private Button sendButton;

@FXML
private TextField signatureInput;

    @FXML
    void sendMessage() {
        String message = messageInput.getText();

        if (message.isEmpty()) return;
        String messageWithSignature= combineMessageWithSignature(message, signatureInput.getText());


        try {
            ClientService.sendMessage(messageWithSignature);
            displayMessage(messageWithSignature);
            messageInput.clear();
        } catch (NotStartedException e) {
            System.err.println("Cannot send message because the client wasn't started");
            return;
        } catch (IOException e) {
            System.err.println("Couldn't send message: " + e.getMessage());
            return;
        }

    }

    private void displayMessage(String message) {
        Platform.runLater(() -> {
            messages.getItems().add(message);
            messages.scrollTo(messages.getItems().size());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ClientService.setConsumer(this::displayMessage);
        } catch (NotStartedException e) {
            System.err.println("User wasn't started");
        }
    }


    public String combineMessageWithSignature(String message, String signature){
        if (signature.isEmpty()){
            return message;
        }
        return String.format("%s | %s", message, signature);
    }
}
