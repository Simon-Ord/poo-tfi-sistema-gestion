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

        // ------------------------------------------
        // LOGIN 
        // ------------------------------------------
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene(loader.load(), 675, 406);
        // Cargar CSS
        scene.getStylesheets().add(getClass().getResource("/view/styleLogin.css").toExternalForm());
        // Contenidos de la ventana Stage
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/images/logoMundoPC.png"), "null").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        

        // ------------------------------------------
        //  VENTANA DE USUARIOS (comentado temporalmente)
        // -----------------------------------------
        // lo puse porque asi entra ahi n usuario 
        //pero hay que terminal el principal.....lo digo en whasaapp
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/usuariosView.fxml"));
        Scene sceneUsuarios = new Scene(loader.load(), 750, 450);

        // Cargar CSS para usuarios
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
        
    
    
     // ------------------------------------------
        // Vista Principal
        // ------------------------------------------
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principalView.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 700);
        // Cargar CSS
        //scene.getStylesheets().add(getClass().getResource("/view/styleLogin.css").toExternalForm());
        // Contenidos de la ventana Stage
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResource("/images/logoMundoPC.png"), "null").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        */
    }

    public static void main(String[] args) {
        launch();
    }
}
