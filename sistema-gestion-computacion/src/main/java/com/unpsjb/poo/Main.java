package com.unpsjb.poo;

import java.util.Objects;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
// La clase Main tiene que extender application para hacer que funcione javaFX 
public class Main extends Application {
    // Estage es el marco principal donde se agregan los componentes principales (parecido a un Jframe)
    // Start tiene como argumento el escenario. Es quien inicializa la interfaz grafica
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml")); // Cargar la direccion de la interfaz 
        Scene scene = new Scene(loader.load(), 675, 406);
        // Cargar CSS
        scene.getStylesheets().add(getClass().getResource("/view/styleLogin.css").toExternalForm());
        // Contenidos de la ventana Stage
        stage.setTitle("Login - MundoPC");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/logoMundoPC.png"),"null").toExternalForm()));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    // El metodo launch llama de manera interna al metodo "start"
    public static void main(String[] args) {
        launch();
    }
}