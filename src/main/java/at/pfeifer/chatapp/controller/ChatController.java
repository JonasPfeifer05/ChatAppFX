package com.example.chatapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController {

    @FXML
    private AnchorPane chatScene;

    @FXML
    private TextField messageInput;

    @FXML
    private ListView<String> messages;

    @FXML
    private Button sendButton;

    @FXML
    void sendMessage(ActionEvent event) {
        String message = messageInput.getText();
        if (message.isEmpty()) return;

        messages.getItems().add(message);
        messages.scrollTo(messages.getItems().size());
    }
}
