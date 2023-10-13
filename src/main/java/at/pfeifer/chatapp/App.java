package at.pfeifer.chatapp;

import at.pfeifer.chatapp.services.ClientService;
import at.pfeifer.chatapp.services.ServerService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mode-selection-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        stage.setOnCloseRequest(windowEvent -> {
            ClientService.stopClientIfPresent();
            ServerService.stopServerIfPresent();
            stage.close();
        });
        stage.setTitle("Chat App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}