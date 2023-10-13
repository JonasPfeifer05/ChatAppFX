package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.services.AlertService;
import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.RoutingService;
import at.pfeifer.chatapp.services.exceptions.NotStartedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
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
            RoutingService.toModeSelectionScene(chatScene);
            AlertService.showAlert(Alert.AlertType.ERROR, "Redirecting to the connection screen because no user is started!");
        } catch (IOException e) {
            RoutingService.toModeSelectionScene(chatScene);
            AlertService.showAlert(Alert.AlertType.ERROR, "FATAL ERROR (Redirecting to connect screen): " + e);
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


    public String combineMessageWithSignature(String message, String signature) {
        if (signature.isEmpty()) {
            return message;
        }
        return String.format("%s | %s", message, signature);
    }
}
