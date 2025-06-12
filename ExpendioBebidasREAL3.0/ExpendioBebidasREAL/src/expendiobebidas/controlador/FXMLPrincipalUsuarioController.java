/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.pojo.Usuario;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLPrincipalUsuarioController implements Initializable {
    private Usuario usuarioSesion;
    @FXML
    private Label lbSaludoUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
 public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion= usuarioSesion;
        cargarInformacionUsuario();
    }
    
     private void cargarInformacionUsuario(){
        if(usuarioSesion != null){
            lbSaludoUsuario.setText("Hola, "+ usuarioSesion.getNombreUsuario());
        }
    }    

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
    Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    alertaConfirmacion.setTitle("Confirmar cierre de sesión");
    alertaConfirmacion.setHeaderText(null);
    alertaConfirmacion.setContentText("¿Estás seguro de que deseas cerrar sesión?");

    Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();

    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
        try {
            Stage escenarioBase = (Stage) lbSaludoUsuario.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vista = cargador.load();
            Scene escenaInicio = new Scene(vista);
            escenarioBase.setScene(escenaInicio);
            escenarioBase.setTitle("Inicio de Sesión");
            escenarioBase.centerOnScreen();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error interno", "No se ha cargado la sesión del usuario");
            e.printStackTrace();
        }
    }
    }

    @FXML
    private void clicAdministrarClientes(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLAdministrarClientes.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Administrar Clientes");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clickProductosConStockMinimo(ActionEvent event) {
        try {
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLProductosConStockMinimo.fxml"));
            Scene escena = new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Productos con stock mínimo");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickRealizarVenta(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLBuscarClienteVenta.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar venta a cliente");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "Lo sentimos, por el momento no se puede mostrar la ventana");
        }
    }

    @FXML
    private void clickRealizarPedido(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLRealizarPedidoPorEmpleado.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar pedidos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "Lo sentimos, por el momento no se puede mostrar la ventana");
        }
    }
    
    
    
}
