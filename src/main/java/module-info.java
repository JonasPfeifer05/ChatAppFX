module at.pfeifer.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens at.pfeifer.chatapp to javafx.fxml;
    exports at.pfeifer.chatapp;
    exports at.pfeifer.chatapp.controller;
    opens at.pfeifer.chatapp.controller to javafx.fxml;
    exports at.pfeifer.chatapp.services;
    opens at.pfeifer.chatapp.services to javafx.fxml;

    exports at.pfeifer.chatapp.services.exceptions;
}
