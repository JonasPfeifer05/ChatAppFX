package at.pfeifer.chatapp;

import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.RoutingService;
import at.pfeifer.chatapp.services.ServerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Chat App");
        stage.setOnCloseRequest(windowEvent -> {
            ClientService.stopClientIfPresent();
            ServerService.stopServerIfPresent();
            stage.close();
        });
        RoutingService.toModeSelectionScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}