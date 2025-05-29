/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.RegistrarseDAO;
import expendiobebidas.modelo.dao.pojo.Usuario;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLRegistrarseController implements Initializable {
private Usuario usuarioSesion;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfRol;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private Label lbErrorRol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void clicRegistrarse(ActionEvent event) {
    String username = tfUsuario.getText().trim();
    String password = pfPassword.getText().trim();
    String rol = tfRol.getText().trim();

    if (!validarCampos(username, password, rol)) {
        return;
    }

    try {
        boolean usuarioUnico = RegistrarseDAO.verificarUsuario(username);
        if (!usuarioUnico) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Usuario ya existe", "El nombre de usuario ya está en uso");
            return;
        }
       if (!rol.equalsIgnoreCase("administrador") && !rol.equalsIgnoreCase("usuario")) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Rol no válido", "El rol ingresado no es válido, por favor inténtelo de nuevo");
        return;
    }

        Usuario nuevoUsuario = new Usuario(username, rol, password);
        boolean registrado = RegistrarseDAO.registrarUsuario(nuevoUsuario);
        if (registrado) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Registro exitoso", "El usuario ha sido registrado correctamente");
            tfRol.setText("");
            tfUsuario.setText("");
            pfPassword.setText("");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de registro", "No se pudo registrar el usuario");
        }

    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", e.getMessage());
    }
    }
    
    private boolean validarCampos(String username, String password, String rol){
        lbErrorUsuario.setText(""); 
        lbErrorPassword.setText("");
        lbErrorRol.setText("");
        boolean camposValidos = true;
        if(username.isEmpty()){
            lbErrorUsuario.setText("Usuario obligatorio");
            camposValidos= false;
        }
        if(password.isEmpty()){
           lbErrorPassword.setText("Contraseña obligatoria");
           camposValidos= false;
        }
        if(rol.isEmpty()){
            lbErrorRol.setText("Rol obligatorio");
            camposValidos= false;
        }
        return camposValidos;
    }
    

    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) tfUsuario.getScene().getWindow();
        stageActual.close();
    }
    
}
