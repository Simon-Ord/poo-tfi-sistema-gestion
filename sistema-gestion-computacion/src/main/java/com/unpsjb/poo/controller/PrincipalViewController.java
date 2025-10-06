
package com.unpsjb.poo.controller;

import java.awt.event.KeyEvent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;


 // FXML Controller class

public class PrincipalViewController implements Initializable {

        

    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txt1;
    
    @FXML
    private void eventBtn(ActionEvent event){
    JOptionPane.showMessageDialog(null, "Hola Mundo");
    }
//    @FXML
//    private void eventKey (KeyEvent event){
//    
//    Object evt = event.getSource();
//    
//    if (evt.equals(txt1)){
//       if(!Character.isLetter(event.getCharacter().charAt(0))) {
//       } else {}    
//      }
//    }
    
    
    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
