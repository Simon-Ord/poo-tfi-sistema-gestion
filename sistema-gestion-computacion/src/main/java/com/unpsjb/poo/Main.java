package com.unpsjb.poo;

import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene(loader.load(), 675.0, 406.0);
        stage.setScene(scene);
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
        getClass().getResource("/images/logoMundoPC.png"), "null").toExternalForm()));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
