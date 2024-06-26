package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.services.AlertService;
import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.RoutingService;
import at.pfeifer.chatapp.services.exceptions.NotStartedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane chatScene;

    @FXML
    private Button saveButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageInput;

    @FXML
    private ListView<String> messages;

    @FXML
    private TextField signatureInput;

    @FXML
    void saveMessage() {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        );
        var file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            saveMessagesToFile(getMessagesFormatted(), file);
        }
    }

    @FXML
    void sendMessage() {
        String message = messageInput.getText();

        if (message.isEmpty()) return;
        String messageWithSignature = combineMessageWithSignature(message, signatureInput.getText());

        try {
            ClientService.sendMessage(messageWithSignature);
            displayMessage("You: " + messageWithSignature);
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

    public String getMessagesFormatted() {
        return String.join("\n", messages
            .getItems());
    }

    private void saveMessagesToFile(String content, File file) {
        try (var writer = new PrintWriter(file)) {
            writer.println(content);
        } catch (IOException ioException) {
            throw new RuntimeException("could not save messages to file", ioException);
        }
    }
}
