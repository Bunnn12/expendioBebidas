/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.ProveedorDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Proveedor;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLAdministrarProveedoresController implements Initializable {

    @FXML
    private TableView<Proveedor> tvProveedores;
    @FXML
    private TableColumn colRazonSocial;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TextField tfBuscarProveedor;
    @FXML
    private TextField tfRazonSocial;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfDireccion;
    
    private ObservableList<Proveedor> proveedores;
    @FXML
    private Label lbErrorRazonSocial;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProveedores();
    }    
    
     private void cargarProveedores(){
        try{
        proveedores= FXCollections.observableArrayList();
        ArrayList<Proveedor> proveedorDAO = ProveedorDAO.obtenerProveedores();
        proveedores.addAll(proveedorDAO);
        tvProveedores.setItems(proveedores);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los proveedores, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
     
     private void cerrarVentana(){
        ((Stage) tvProveedores.getScene().getWindow()).close();
    }
     
    private void configurarTabla(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }
  
    @FXML
    private void seleccionarProveedor(MouseEvent event) {
        Proveedor seleccionado = tvProveedores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            tfRazonSocial.setText(seleccionado.getRazonSocial());
            tfTelefono.setText(seleccionado.getTelefono());
            tfCorreo.setText(seleccionado.getCorreo());
            tfDireccion.setText(seleccionado.getDireccion());
        }
    }
    
    @FXML
    private void buscarProveedorPorRazonSocial(KeyEvent event) {
        String razonBuscada = tfBuscarProveedor.getText().toLowerCase();

        if (razonBuscada.isEmpty()) {
            tvProveedores.setItems(proveedores); 
            return;
        }

        ObservableList<Proveedor> filtrados = FXCollections.observableArrayList();
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getRazonSocial().toLowerCase().contains(razonBuscada)) {
                filtrados.add(proveedor);
            }
        }

        tvProveedores.setItems(filtrados);
    }

    @FXML
    private void clicInsertar(ActionEvent event) throws SQLException{
        
        if (!validarRazonSocial()) {
            return;
        }
            Proveedor nuevo = new Proveedor(0,
            tfRazonSocial.getText(),
            tfTelefono.getText(),
            tfCorreo.getText(),
            tfDireccion.getText()
        );

        if (ProveedorDAO.insertarProveedor(nuevo)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Proveedor insertado", "El proveedor ha sido insertado correctamente");
            cargarProveedores();
            limpiarCampos();
        }
        else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Proveedor no insertado", "No se ha podido insertar el proveedor, intenténtelo de nuevo más tarde");
        }
        
    }

    @FXML
    private void clicModificar(ActionEvent event) throws SQLException{
        
        Proveedor seleccionado = tvProveedores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Debes seleccionar un proveedor para modificar.");
            return;
        }

        if (!validarRazonSocial()) {
            return;
        }

        seleccionado.setRazonSocial(tfRazonSocial.getText().trim());
        seleccionado.setTelefono(tfTelefono.getText().trim());
        seleccionado.setCorreo(tfCorreo.getText().trim());
        seleccionado.setDireccion(tfDireccion.getText().trim());

        if (ProveedorDAO.actualizarProveedor(seleccionado)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Proveedor modificado", "El proveedor ha sido modificado correctamente.");
            cargarProveedores();
            limpiarCampos();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Proveedor no modificado", "No se ha podido modificar al proveedor, inténtelo de nuevo más tarde");
        }
        
    }
    
    @FXML
    private void clicEliminar(ActionEvent event) throws SQLException{
        Proveedor seleccionado = tvProveedores.getSelectionModel().getSelectedItem();
    
        if (seleccionado != null) {
            if (ProveedorDAO.tieneProductosAsociados(seleccionado.getIdProveedor())) {
              Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se puede eiminar", "El proveedor seleccionado ya tiene productos asociados, por lo que no se puede eliminar");

            } else {
                ProveedorDAO.eliminarProveedor(seleccionado.getIdProveedor());
                proveedores.remove(seleccionado);
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Proveedor eliminado", "El proveedor ha sido eliminado exitosamente.");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Seleccione un proveedor", "Por favor seleccione un proveedor de la tabla.");
        }
    }
    
    private boolean validarRazonSocial() {
        String razon = tfRazonSocial.getText().trim();
        if (razon.isEmpty()) {
            lbErrorRazonSocial.setText("La razón social es obligatoria.");
            return false;
        } else {
            lbErrorRazonSocial.setText("");
            return true;
        }
    }
    
     private void limpiarCampos() {
        tfRazonSocial.clear();
        tfTelefono.clear();
        tfCorreo.clear();
        tfDireccion.clear();
    }   

    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) tfBuscarProveedor.getScene().getWindow();
        stageActual.close();
    }
    
}
