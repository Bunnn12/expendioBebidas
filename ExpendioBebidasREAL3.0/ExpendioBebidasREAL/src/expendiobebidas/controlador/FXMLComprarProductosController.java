/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.PedidoDAO;
import expendiobebidas.modelo.dao.ProveedorDAO;
import expendiobebidas.modelo.dao.pojo.Pedido;
import expendiobebidas.modelo.dao.pojo.Proveedor;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
public class FXMLComprarProductosController implements Initializable {

    @FXML
    private DatePicker dpFechaCompra;
    @FXML
    private TextField tfFolioFactura;
    @FXML
    private TableView<Pedido> tvPedidosPendientes;
    @FXML
    private TableColumn<Pedido, String> colFecha;
    @FXML
    private TableView<?> tvDetallePedido;
    @FXML
    private TableColumn<?, ?> colProductoDetallePedido;
    @FXML
    private TableColumn<?, ?> colCantidadPedida;
    @FXML
    private TableColumn<?, ?> colTotalDetallePedido;
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
    private TextField tfBuscarProveedor;
    private ObservableList<Proveedor> proveedores;
    private ObservableList<Pedido> pedidos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProveedores();
        configurarTablaPedidosPendientes();
        cargarProveedores();
    }    

    @FXML
    private void clicRegresar(ActionEvent event) {
    }

    @FXML
    private void clicRealizarCompra(ActionEvent event) {
    }

    @FXML
    private void seleccionarProveedor(MouseEvent event) {
        Proveedor proveedorSeleccionado = tvProveedores.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado != null) {
            cargarPedidosProveedor(proveedorSeleccionado.getIdProveedor());
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
           ((Stage) tfBuscarProveedor.getScene().getWindow()).close();
    }
     
    private void configurarTablaProveedores(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }

    public void cargarPedidosProveedor(int idProveedor) {
    try {
        List<Pedido> pedidos = PedidoDAO.obtenerPedidosPendientesPorProveedor(idProveedor);
        System.out.println("Pedidos obtenidos: " + pedidos.size()); // << Aquí
        ObservableList<Pedido> data = FXCollections.observableArrayList(pedidos);
        tvPedidosPendientes.setItems(data);
    } catch (SQLException ex) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "Lo sentimos, por el momento no se pueden cargar los pedidos");
        ex.printStackTrace();
    }
}

    private void configurarTablaPedidosPendientes(){
    colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
}


}


