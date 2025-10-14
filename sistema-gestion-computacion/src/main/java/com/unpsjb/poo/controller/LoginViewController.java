package com.unpsjb.poo.controller;

// Importaciones de JavaFX
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

// Importaciones del modelo y DAO
import com.unpsjb.poo.model.Usuario;
import com.unpsjb.poo.persistence.dao.impl.UsuarioDAOImpl;

/**
 * Controlador para la pantalla de login.
 * Esta clase se encarga de manejar los eventos del formulario
 * y verificar si el usuario y contraseña son correctos en la base de datos.
 */
public class LoginViewController implements Initializable {

    // ================================
    // 🔹 Elementos del formulario (FXML)
    // ================================
    @FXML
    private PasswordField txtPassword; // Campo para contraseña

    @FXML
    private TextField txtUser; // Campo para usuario

    @FXML
    private Button btnLogin; // Botón para iniciar sesión

    @FXML
    private Label lblError; // Etiqueta para mostrar mensajes de error

    // ================================
    // 🔹 Objeto DAO (acceso a la base de datos)
    // ================================
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    // ================================
    // 🔹 Método para evitar espacios en los campos
    // ================================
    @FXML
    private void eventKey(KeyEvent event) {
        Object evt = event.getSource();

        // Evitar que se escriban espacios en los campos de texto
        if (evt.equals(txtUser) || evt.equals(txtPassword)) {
            if (event.getCharacter().equals(" ")) {
                event.consume(); // Ignora la tecla presionada
            }
        }
    }

    // ================================
    // 🔹 Evento que se ejecuta al presionar el botón "Login"
    // ================================
    @FXML
    private void eventAction(ActionEvent event) {
        Object evt = event.getSource();

        // Si el evento fue generado por el botón de login...
        if (evt.equals(btnLogin)) {
            String usuario = txtUser.getText().trim();      // Obtiene el texto sin espacios
            String contrasena = txtPassword.getText().trim();

            // Validar que no estén vacíos
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta("Error", "Debe ingresar usuario y contraseña.", Alert.AlertType.ERROR);
                return;
            }

            // Llama al método para verificar el login
            verificarLogin(usuario, contrasena);
        }
    }

    // ================================
    // 🔹 Verificar usuario y contraseña en la base de datos
    // ================================
    private void verificarLogin(String usuario, String contrasena) {
        // Consulta al DAO para buscar al usuario
        Usuario u = usuarioDAO.verificarLogin(usuario, contrasena);

        if (u != null) {
            // Si se encontró, muestra mensaje de bienvenida
            mostrarAlerta("Ingreso correcto", 
                          "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")", 
                          Alert.AlertType.INFORMATION);

            // 🔹 Dependiendo del rol, podrías abrir distintas pantallas
            // Ejemplo:
            // if (u.getRol().equalsIgnoreCase("ADMIN")) {
            //     abrirVentanaAdmin();
            // } else if (u.getRol().equalsIgnoreCase("RECEPCION")) {
            //     abrirVentanaRecepcion();
            // }

        } else {
            // Si no existe el usuario o la contraseña es incorrecta
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }

    // ================================
    // 🔹 Método auxiliar para mostrar alertas (ventanas emergentes)
    // ================================
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // No muestra encabezado adicional
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera que el usuario la cierre
    }

    // ================================
    // 🔹 Inicialización del controlador
    // ================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Este método se ejecuta automáticamente cuando se carga el FXML.
        // Por ahora no necesitamos inicializar nada aquí.
    }
}
