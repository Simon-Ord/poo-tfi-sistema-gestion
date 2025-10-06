package com.unpsjb.poo.controller;

import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginViewController implements Initializable {

    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private Button btnLogin;    
    
    @FXML
    private void eventKey(KeyEvent event){
        // Para comparar que nodo acciono el evento
        Object evt = event.getSource(); 
        
        if (evt.equals(txtUser)){
            
            if(event.getCharacter().equals(" ")){event.consume();}
            
        } else if(evt.equals(txtPassword)){
            
            if (event.getCharacter().equals(" ")){event.consume();}
        }                
    }
    
    @FXML
    private void eventAction (ActionEvent event){
    
        Object evt = event.getSource(); 
        
        if (evt.equals(btnLogin)){};
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
