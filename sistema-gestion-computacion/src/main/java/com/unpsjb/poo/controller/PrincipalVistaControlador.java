
package com.unpsjb.poo.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



 // FXML Controller class

public class PrincipalVistaControlador implements Initializable {

        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
 
    // Helpers para abrir// ventanas internas
    private void openInternal(String title, Node content, double w, double h) {
        VentanaVistaControlador win = new VentanaVistaControlador(title, content);
        win.setPrefSize(w, h);
        int count = desktop.getChildren().size();
        //win.relocate(30 + 24 * count, 30 + 18 * count);
        desktop.getChildren().add(win);
        win.toFront();
    }
    private Node loadView(String resource) {
        try {
            return FXMLLoader.load(getClass().getResource(resource));
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Error cargando " + resource + ": " + ex.getMessage()).showAndWait();
            throw new RuntimeException(ex);
        }
    }



    @FXML private Pane desktop; 
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
    @FXML private void productosAction() {
    Node view = loadView("/view/productosVista.fxml");
    openInternal("Productos", view, 800, 500);

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
