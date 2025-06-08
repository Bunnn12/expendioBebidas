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
public class FXMLPrincipalController implements Initializable {

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
    private void clicRegistrarUsuario(ActionEvent event) {
        if (usuarioSesion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error interno", "No se ha cargado la sesión del usuario");
            return;
        }
        if (!usuarioSesion.getRol().equalsIgnoreCase("administrador")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Acceso denegado", "Solo los administradores pueden registrar nuevos usuarios.");
            return; 
        }

        try {
            FXMLLoader cargador = new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLRegistrarse.fxml"));
            Parent vista = cargador.load();
            Scene escena = new Scene(vista);
            Stage ventanaRegistro = new Stage();
            ventanaRegistro.setScene(escena);
            ventanaRegistro.setTitle("Registrar Usuario");
            ventanaRegistro.initOwner(lbSaludoUsuario.getScene().getWindow());
            ventanaRegistro.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    @FXML
    private void clicVerVentas(ActionEvent event) {
        try{
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLVerVentas.fxml"));
            Scene escena= new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Ver ventas");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clicEstadisticasProductos(ActionEvent event) {
          try{
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLEstadisticasProductos.fxml"));
            Scene escena= new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Estadisticas de productos");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
            }catch(IOException e){
            
        }
    }

    @FXML
    private void clicAdminProveedores(ActionEvent event) {
        try{
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLAdministrarProveedores.fxml"));
            Scene escena= new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.setTitle("Administración de proveedores");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clicAdminPromociones(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLAdministrarPromociones.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Administrar Promociones");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clicAdminProductos(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLAdministrarProductos.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Administrar Productos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }


    @FXML
    private void clicAdminClientes(ActionEvent event) {
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
    private void clicVentaACliente(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLRealizarVentaAClienteCliente.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar venta a cliente");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clickPedidoDeCliente(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLPedidoDeCliente.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar pedido de clentes");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clickPedidoDeProductos(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLAdministrarProductos.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar pedido de productos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }

    @FXML
    private void clickComprarProductos(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLComprarProductos.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar compra de productos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            
        }
    }
       
    
}
   
