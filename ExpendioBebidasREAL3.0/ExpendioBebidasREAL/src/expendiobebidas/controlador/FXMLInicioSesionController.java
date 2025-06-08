/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.InicioSesionDAO;
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
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    configurarCampos();
    configurarListeners();
    }    
     private boolean validarCampos(String username, String password){
        lbErrorUsuario.setText(""); //limpian los campos 
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if(username.isEmpty()){
            lbErrorUsuario.setText("Usuario obligatorio");
            camposValidos= false;
        }
        if(password.isEmpty()){
           lbErrorPassword.setText("Contraseña obligatoria");
           camposValidos= false;
        }
        return camposValidos;
    }
    
      private void validarCredenciales(String username, String password){
        try {
            Usuario usuarioSesion = new InicioSesionDAO().iniciarSesion(username, password);
            if (usuarioSesion != null) {
                irPantallaPrincipal(usuarioSesion);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
                        "Credenciales incorrectas", 
                        "Usuario y/o contraseña incorrectos, por favor verifica tu información.");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                    "Problemas de conexión", ex.getMessage());
        }
    }

    
        private void irPantallaPrincipal(Usuario usuarioSesion){
            if (usuarioSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error interno", "El usuario no ha sido encontrado en el sistema");
             return;
}
        try{    
            if(usuarioSesion.getRol()!= null && usuarioSesion.getRol().equalsIgnoreCase("administrador")){
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow(); 
        FXMLLoader cargador= new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLPrincipal.fxml"));
        Parent vista= cargador.load();
        FXMLPrincipalController controlador= cargador.getController();
        controlador.inicializarInformacion(usuarioSesion);
        Scene escenaPrincipal= new Scene(vista);
        escenarioBase.setScene(escenaPrincipal);
        escenarioBase.setTitle("ColdRushPrincipal");
        escenarioBase.centerOnScreen();
        escenarioBase.show();
            }else if (usuarioSesion.getRol()!= null && usuarioSesion.getRol().equalsIgnoreCase("usuario")){
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow(); 
        FXMLLoader cargador= new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLPrincipalUsuario.fxml"));
        Parent vista= cargador.load();
        FXMLPrincipalUsuarioController controlador= cargador.getController();
        controlador.inicializarInformacion(usuarioSesion);
        Scene escenaPrincipal= new Scene(vista);
        escenarioBase.setScene(escenaPrincipal);
        escenarioBase.setTitle("ColdRushPrincipalUsuario");
        escenarioBase.centerOnScreen();
        escenarioBase.show();
            }
            else {
    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Rol desconocido", "El rol del usuario no está definido correctamente.");
}
        }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
        }
    }

    @FXML
    private void clicIngresar(ActionEvent event) {
         String username = tfUsuario.getText();
        String password = pfPassword.getText();
        
        if (validarCampos(username, password)){
            validarCredenciales(username, password);
        }
    }
    private void configurarCampos() {
    tfPassword.setVisible(false);
    tfPassword.setManaged(false);
}

private void configurarListeners() {

    pfPassword.textProperty().addListener((obs, oldText, newText) -> {
        tfPassword.setText(newText);
    });

    pfPassword.textProperty().addListener((obs, oldText, newText) -> {
        tfPassword.setText(newText);
    });
}
    @FXML
    private void clickMostrarPassword(ActionEvent event) {
        if (tfPassword.isVisible()) {
        ocultarContrasenaVisible();
    } else {
        mostrarContrasenaVisible();
    }
}

private void mostrarContrasenaVisible() {
    tfPassword.setVisible(true);
    tfPassword.setManaged(true);

    pfPassword.setVisible(false);
    pfPassword.setManaged(false);
}

private void ocultarContrasenaVisible() {
    tfPassword.setVisible(false);
    tfPassword.setManaged(false);

    pfPassword.setVisible(true);
    pfPassword.setManaged(true);
    }

}
