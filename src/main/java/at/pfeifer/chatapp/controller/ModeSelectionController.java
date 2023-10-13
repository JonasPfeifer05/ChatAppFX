package at.pfeifer.chatapp.controller;

import at.pfeifer.chatapp.App;
import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.ServerService;
import at.pfeifer.chatapp.services.exceptions.AlreadyStartedException;
import at.pfeifer.chatapp.services.exceptions.InvalidPortException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModeSelectionController implements Initializable {
    private static final String HOST_PROMPT = "Enter port to host on: e.g. 8080";
    private static final String JOIN_PROMPT = "Enter host address to join: e.g. 127.0.0.1:8080";

    private static final String HOST_BUTTON_TEXT = "Host a lobby";
    private static final String JOIN_BUTTON_TEXT = "Join the lobby";


    @FXML
    private AnchorPane modeSelectionScene;

    @FXML
    private Button actionButton;

    @FXML
    private RadioButton hostSelector;
    @FXML
    private RadioButton joinSelector;

    @FXML
    private ToggleGroup mode;

    @FXML
    private TextField userInput;

    @FXML
    void performAction() {
        String userInput = this.userInput.getText();
        if (userInput.isEmpty()) return;

        try {
            if (hostSelector.isSelected()) {
                int port = Integer.parseInt(userInput);
                ServerService.startServer(port);
                ClientService.defineClient("localhost", port);
            } else {
                var parts = userInput.split(":");
                if (parts.length != 2) {
                    System.out.println("Invalid address passed");
                    return;
                }
                int port = Integer.parseInt(parts[1]);
                ClientService.defineClient(parts[0], port);
            }
        } catch (InvalidPortException e) {
            System.err.println("Invalid port was passed: " + e.getMessage());
            return;
        } catch (AlreadyStartedException e) {
            System.err.println("Couldn't start because it already was started: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Error while starting: " + e.getMessage());
            return;
        }

        Stage stage = (Stage) modeSelectionScene.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("chat-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 720, 500);
            stage.hide();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ignored) {}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hostSelector.selectedProperty().addListener((observed, oldValue, newValue) -> {
            if (newValue) {
                userInput.promptTextProperty().set(HOST_PROMPT);
                actionButton.setText(HOST_BUTTON_TEXT);
            } else {
                userInput.promptTextProperty().set(JOIN_PROMPT);
                actionButton.setText(JOIN_BUTTON_TEXT);
            }
        });

        hostSelector.setSelected(true);
    }
}
