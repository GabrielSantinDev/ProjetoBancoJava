package com.projetodb.projetobancojava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Principal extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Principal.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Image icone = new Image(getClass().getResourceAsStream("/images/bankIcon.png"));

        stage.getIcons().add(icone);
        stage.setTitle("Gerenciamento de Banco");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}