/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.CompraDAO;
import expendiobebidas.modelo.dao.PedidoDAO;
import expendiobebidas.modelo.dao.ProveedorDAO;
import expendiobebidas.modelo.dao.pojo.Pedido;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
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
import javafx.scene.control.DatePicker;
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
    private TableView<ProductoPedido> tvDetallePedido;
    @FXML
    private TableColumn<?, ?> colProductoDetallePedido;
    @FXML
    private TableColumn<?, ?> colCantidadPedida;
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
    
    @FXML
    private Label lbprecioTotalPedido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProveedores();
        configurarTablaPedidosPendientes();
        configurarTablaDetallePedido();
        cargarProveedores();
    }    

    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) tfBuscarProveedor.getScene().getWindow();
        stageActual.close();
    }

    @FXML
    private void clicRealizarCompra(ActionEvent event) {
        Proveedor proveedorSeleccionado = tvProveedores.getSelectionModel().getSelectedItem();
        Pedido pedidoSeleccionado = tvPedidosPendientes.getSelectionModel().getSelectedItem();
        ProductoPedido productopedidoSeleccionado = tvDetallePedido.getSelectionModel().getSelectedItem();
        
        if (pedidoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un pedido para realizar la compra.");
            return;
        }

        if (dpFechaCompra.getValue() == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Atención", "Debe seleccionar una fecha de compra.");
            return;
        }

        String folioFactura = tfFolioFactura.getText();
        if (folioFactura == null || folioFactura.trim().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Atención", "Debe ingresar el folio de la factura.");
            return;
        }

        try {         
            boolean exito = CompraDAO.realizarCompraConTransaccion(
                    pedidoSeleccionado.getIdPedido(), dpFechaCompra.getValue(), folioFactura,
                    productopedidoSeleccionado.getCantidad(),
                    productopedidoSeleccionado.getProducto().getPrecio(),
                    proveedorSeleccionado.getIdProveedor());

            if (exito) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Compra realizada", "La compra se ha registrado correctamente.");
                cargarPedidosProveedor(pedidoSeleccionado.getIdProveedor());
                tvDetallePedido.setItems(FXCollections.observableArrayList());
                lbprecioTotalPedido.setText("Total: $0.00");
                tfFolioFactura.clear();
                dpFechaCompra.setValue(null);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo registrar la compra.");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al intentar realizar la compra.");
            e.printStackTrace();
        }
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
    
    private void cargarProveedores() {
        try {
            proveedores= FXCollections.observableArrayList();
            ArrayList<Proveedor> proveedorDAO = ProveedorDAO.obtenerProveedores();
            proveedores.addAll(proveedorDAO);
            tvProveedores.setItems(proveedores);
        } catch(SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los proveedores, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
    
    private void cerrarVentana() {
        ((Stage) tfBuscarProveedor.getScene().getWindow()).close();
    }
     
    private void configurarTablaProveedores() {
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

    private void configurarTablaPedidosPendientes() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
    }

    private void configurarTablaDetallePedido() {
        colProductoDetallePedido.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidadPedida.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    @FXML
    private void seleccionarPedido(MouseEvent event) {
        Pedido pedidoSeleccionado = tvPedidosPendientes.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado != null) {
            try {
                List<ProductoPedido> detalles = PedidoDAO.obtenerProductosDePedidoProveedor(pedidoSeleccionado.getIdPedido());
                ObservableList<ProductoPedido> data = FXCollections.observableArrayList(detalles);
                tvDetallePedido.setItems(data);

                // Calcular el precio total sumando el total de cada producto
                double precioTotal = 0;
                for (ProductoPedido producto : detalles) {
                    precioTotal += producto.getTotal(); // asumiendo que getTotal() devuelve el precio total por producto (cantidad * precio unitario)
                }

                // Mostrar el precio total en el label
                lbprecioTotalPedido.setText(String.format("Total: $%.2f", precioTotal));

            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los detalles del pedido");
                e.printStackTrace();
            }
        } else {
            // Si no hay pedido seleccionado, limpiar detalles y el label
            tvDetallePedido.setItems(FXCollections.observableArrayList());
            lbprecioTotalPedido.setText("Total: $0.00");
        }
    }

    @FXML
    private void seleccionarDetallePedido(MouseEvent event) {
    }
}
