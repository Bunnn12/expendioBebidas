/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.InicioSesionDAO;
import expendiobebidas.modelo.dao.UsuarioDAO;
import expendiobebidas.modelo.dao.pojo.Usuario;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXMLModificarStockActualController implements Initializable {

    @FXML
    private TextField tfStockActual;
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasenia;
    
    private TextField campoStockActualPrincipal;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void clicGuardarStockActual(ActionEvent event) {
         String nombreUsuario = tfUsuario.getText();
        String contrasenia = pfContrasenia.getText();
        String nuevoStock = tfStockActual.getText();

        Usuario usuario = null;
        try {
            usuario = new InicioSesionDAO().iniciarSesion(nombreUsuario, contrasenia);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                "Error de conexión", "No se pudo validar al usuario: " + ex.getMessage());
            return;
        }

        if (usuario != null && "Administrador".equalsIgnoreCase(usuario.getRol())) {
            if (campoStockActualPrincipal != null) {
                campoStockActualPrincipal.setText(nuevoStock);
            }

            Stage stage = (Stage) tfStockActual.getScene().getWindow();
            stage.close();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Acceso denegado", "Credenciales incorrectas. No es un administrador");
        }
    }
    
    public void setCampoStockActualPrincipal(TextField campoStockActualPrincipal) {
        this.campoStockActualPrincipal = campoStockActualPrincipal;
    }
}
