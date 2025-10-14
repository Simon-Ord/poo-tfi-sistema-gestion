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

        // -----------------------------------------------------
        // 🚀 ABRIR DIRECTAMENTE EL FORMULARIO DE USUARIO
        // -----------------------------------------------------


        // -----------------------------------------------------
        // 💤 Comentado temporalmente (otras pantallas)
        // -----------------------------------------------------
        
        /*
        // LOGIN
        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Scene sceneLogin = new Scene(loaderLogin.load(), 675, 406);
        sceneLogin.getStylesheets().add(getClass().getResource("/view/styleLogin.css").toExternalForm());
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/images/logoMundoPC.png"), "null").toExternalForm()));
        stage.setScene(sceneLogin);
        stage.setResizable(false);
        stage.show();
         */

        // GESTIÓN DE USUARIOS
        FXMLLoader loaderUsuarios = new FXMLLoader(getClass().getResource("/view/usuariosView.fxml"));
        Scene sceneUsuarios = new Scene(loaderUsuarios.load(), 750, 450);
        sceneUsuarios.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/view/styleusuariosView.css"),
                "No se encontró el archivo CSS de usuarios").toExternalForm()
        );
        stage.setTitle("Gestión de Usuarios - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/images/logoMundoPC.png"), "No se encontró el icono").toExternalForm()));
        stage.setScene(sceneUsuarios);
        stage.setResizable(false);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch();
    }
}
