
package com.unpsjb.poo.controller;

import java.awt.event.KeyEvent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;


 // FXML Controller class

public class PrincipalVistaControlador implements Initializable {

        

    @FXML private Button btnUsuarios;
    @FXML private Button btnClientes;
    @FXML private Button btnProductos;
    @FXML private Button btnFacturar;
    @FXML private Button btnReportes;
    @FXML private Button btnCerrarSesion;
    @FXML private Label lblNombreUsuario;
    
    
    
    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML private void usuariosAction (ActionEvent event){}
    @FXML private void clientesAction (ActionEvent event){}
    @FXML private void productosAction (ActionEvent event){}
    @FXML private void facturarAction (ActionEvent event){}
    @FXML private void reportesAction (ActionEvent event){}
    @FXML private void cerrarSesionAction (ActionEvent event){}
    
}
