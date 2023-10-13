package at.pfeifer.chatapp.services;

import javafx.scene.control.Alert;

public class AlertService {
    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setResizable(true);
        alert.show();
    }
}
