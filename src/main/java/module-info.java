module com.example.chatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.pfeifer.chatapp to javafx.fxml;
    exports at.pfeifer.chatapp;
    exports at.pfeifer.chatapp.controller;
    opens at.pfeifer.chatapp.controller to javafx.fxml;
    exports at.pfeifer.chatapp.services;
    opens at.pfeifer.chatapp.services to javafx.fxml;
}