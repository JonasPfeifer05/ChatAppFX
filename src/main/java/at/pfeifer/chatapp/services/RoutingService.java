package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RoutingService {
    public static void toModeSelectionScene(AnchorPane anchor) {
        Stage stage = (Stage) anchor.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mode-selection-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);
            stage.hide();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ignored) {}
    }

    public static void toChatScene(AnchorPane anchor) {
        Stage stage = (Stage) anchor.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("chat-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 720, 500);
            stage.hide();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ignored) {}
    }
}
