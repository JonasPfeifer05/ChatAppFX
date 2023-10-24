package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoutingService {
    public static void toModeSelectionScene(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mode-selection-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setMinWidth(300);
            stage.setMinHeight(250);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void toChatScene(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("chat-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.hide();
            stage.setScene(scene);
            stage.setMinWidth(720);
            stage.setMinHeight(500);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
