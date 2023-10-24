package at.pfeifer.chatapp.services;

import at.pfeifer.chatapp.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class RoutingService {
    public static void toModeSelectionScene(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mode-selection-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.hide();
            stage.setScene(scene);
            stage.setMinHeight(300);
            stage.setMinWidth(350);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void toChatScene(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("chat-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 720, 500);
            stage.hide();
            stage.setScene(scene);
            stage.setMinHeight(720);
            stage.setMinWidth(500);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
