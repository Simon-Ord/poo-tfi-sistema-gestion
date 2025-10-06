package com.unpsjb.poo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label lblMensaje;

    @FXML
    private void onClickBoton() {
        lblMensaje.setText("¡Funciona el FXML con CSS en NetBeans!");
    }
}

