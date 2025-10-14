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


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principalView.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 700);
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/images/logoMundoPC.png"), "null").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
      
    }

    public static void main(String[] args) {
        launch();
    }
}
