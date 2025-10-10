/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.unpsjb.poo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class ProductosVistaControlador implements Initializable {

    // BOTONES DE LA VENTANA 
    @FXML private Button btnModificarAction;
    @FXML private Button btnRegistrarAction;
    @FXML private Button btnEliminarAction;
    @FXML private Button btnCancelarAction; 
    @FXML private Button btnAgregarCategoriaAction;    
    @FXML private Button btnAgregarFabricanteAction;  
    // TEXTFIELDS DE LA VENTANA 
    
    @FXML private void btnRegistrarAction (ActionEvent event){} 
    @FXML private void btnModificarAction (ActionEvent event){}
    @FXML private void btnEliminarAction (ActionEvent event){}
    @FXML private void btnCancelarAction (ActionEvent event){}
    @FXML private void AgregarCategoriaAction (ActionEvent event){}
    @FXML private void AgregarFabricanteAction (ActionEvent event){}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
