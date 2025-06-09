/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ProveedorDAO;
import expendiobebidas.modelo.dao.pojo.Producto;
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
 * @author reino
 */
public class FXMLPedidoDeProductosController implements Initializable {

    @FXML
    private TableView<Proveedor> tvProveedores;
    @FXML
    private TableColumn<?, ?> colRazonSocial;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colCorreo;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private TableView<Producto> tvProductosStockMinimo;
    @FXML
    private TableColumn<?, ?> colNombreSM;
    @FXML
    private TableColumn<?, ?> colStockActualSM;
    @FXML
    private TableColumn<?, ?> colPrecioSM;
    @FXML
    private TableView<Producto> tvTodosProductos;
    @FXML
    private TableColumn<?, ?> colNombreT;
    @FXML
    private TableColumn<?, ?> colStockActualT;
    @FXML
    private TableColumn<?, ?> colPrecioT;
    @FXML
    private TableView<Producto> tvProductosSeleccionados;
    @FXML
    private TableColumn<?, ?> colNombreS;
    @FXML
    private TableColumn<?, ?> colStockActualS;
    @FXML
    private TableColumn<?, ?> colPrecioS;
    @FXML
    private TextField tfBuscarProveedor;
    private ObservableList<Proveedor> proveedores;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProveedores();
    }    

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void seleccionarProveedor(MouseEvent event) {
    }

    @FXML
    private void clicRealizarPedido(ActionEvent event) {
    }

    @FXML
    private void clicAgregarProducto(ActionEvent event) {
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
    
}
