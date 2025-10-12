
package com.unpsjb.poo.controller;


import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



 // FXML Controller class

public class PrincipalVistaControlador implements Initializable {

        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }         
    
    @FXML private Button btnUsuarios;
    @FXML private Button btnClientes;
    @FXML private Button btnProductos;
    @FXML private Button btnFacturar;
    @FXML private Button btnReportes;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblNombreUsuario;
    
    
    
    
   
    
    @FXML private void usuariosAction (ActionEvent event){}
    @FXML private void clientesAction (ActionEvent event){}
    
    // BOTON PRODUCTOS //
    @FXML private void productosAction (ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productosVista.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage(); stage.setScene(new Scene(root)); stage.show(); // Crear la nueva ventana
    }
    
    @FXML private void facturarAction (ActionEvent event){}
    @FXML private void reportesAction (ActionEvent event){}
    
    // BOTON CERRAR SESION // 
    @FXML private void cerrarSesionAction(ActionEvent event) {
    try {
        // Cargar la vista de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = loader.load();
        // Crear una nueva escena para el login
        Scene loginScene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("Inicio de Sesión");
        stage.setMaximized(false);
        stage.centerOnScreen();
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
}
