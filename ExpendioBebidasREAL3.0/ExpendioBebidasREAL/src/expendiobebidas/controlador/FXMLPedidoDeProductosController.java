/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.PedidoDAO;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.ProveedorDAO;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
import expendiobebidas.modelo.dao.pojo.Proveedor;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
 * @author reino
 */
public class FXMLPedidoDeProductosController implements Initializable {

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
    private TableView<Producto> tvProductosStockMinimo;
    @FXML
    private TableColumn colNombreSM;
    @FXML
    private TableColumn colStockActualSM;
    @FXML
    private TableColumn colPrecioSM;
    @FXML
    private TableView<Producto> tvTodosProductos;
    @FXML
    private TableColumn colNombreT;
    @FXML
    private TableColumn colStockActualT;
    @FXML
    private TableColumn colPrecioT;
    @FXML
    private TableView<ProductoPedido> tvProductosSeleccionados;
    @FXML
    private TextField tfBuscarProveedor;
    private ObservableList<Proveedor> proveedores;
    private ObservableList<Producto> todosProductos = FXCollections.observableArrayList();
    private ObservableList<ProductoPedido> productosSeleccionados = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ProductoPedido, String> colNombrePedido;
    @FXML
    private TableColumn<ProductoPedido, String> colPrecioPedido;
    @FXML
    private TableColumn<ProductoPedido, Integer> colCantidad;
    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private TableColumn colStockMinimoPSM;
    @FXML
    private TableColumn colStockMinimoT;
    @FXML
    private Label lbPrecioPedido;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProveedores();
        configurarTablaTodosProductos();
        configurarTablaProductosStockMinimo();
        configurarTablaProductosPedido();
        cargarProveedores();
        
    }    

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void seleccionarProveedor(MouseEvent event) {
        Proveedor proveedorSeleccionado = tvProveedores.getSelectionModel().getSelectedItem();
    if (proveedorSeleccionado != null) {
        int idProveedor = proveedorSeleccionado.getIdProveedor();

        cargarProductosPorProveedor(idProveedor);
        cargarProductosDePedidoPorStockMinimo(idProveedor);
    }
    }

    @FXML
    private void clicRealizarPedido(ActionEvent event) {
         Proveedor proveedorSeleccionado = tvProveedores.getSelectionModel().getSelectedItem();

    if (proveedorSeleccionado == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Proveedor no seleccionado", "Por favor, selecciona un proveedor antes de realizar el pedido.");
        return;
    }

    if (productosSeleccionados.isEmpty()) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Productos no seleccionados", "Por favor, agrega productos al pedido antes de realizarlo.");
        return;
    }

    try {
        boolean resultado = PedidoDAO.insertarPedido(proveedorSeleccionado.getIdProveedor(), new ArrayList<>(productosSeleccionados));
        if (resultado) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Pedido realizado", "El pedido se ha realizado correctamente.");
            productosSeleccionados.clear();
            tvProductosSeleccionados.refresh();
            lbPrecioPedido.setText("$0.00");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo realizar el pedido.");
        }
    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al realizar pedido", "Ocurrió un error al intentar realizar el pedido: " + e.getMessage());
        e.printStackTrace();
    }
    }

    @FXML
    private void clicAgregarProducto(ActionEvent event) {
        Producto seleccionado = tvTodosProductos.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        ProductoPedido encontrado = null;
        for (ProductoPedido pp : productosSeleccionados) {
            if (pp.getProducto().getIdProducto() == seleccionado.getIdProducto()) {
                encontrado = pp;
                break;
            }
        }

        if (encontrado != null) {
            encontrado.incrementarCantidad();
        } else {
            productosSeleccionados.add(new ProductoPedido(seleccionado));
        }
        tvProductosSeleccionados.setItems(productosSeleccionados);
        tvProductosSeleccionados.refresh();
        actualizarTotal();
    } else {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", "Debes seleccionar un producto para agregarlo");
    }
    }
     private void cerrarVentana(){
           ((Stage) tvProductosSeleccionados.getScene().getWindow()).close();
    }
       
       private void actualizarTotal() {
        double total = productosSeleccionados.stream()
        .mapToDouble(pp -> pp.getProducto().getPrecio() * pp.getCantidad())
        .sum();
        lbPrecioPedido.setText(" $" + String.format("%.2f", total));
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
     
     
    private void configurarTablaProveedores(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }
    
     private void configurarTablaTodosProductos(){
        colNombreT.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colPrecioT.setCellValueFactory(new PropertyValueFactory("precio"));
        colStockActualT.setCellValueFactory(new PropertyValueFactory("stockActual"));
        colStockMinimoT.setCellValueFactory(new PropertyValueFactory("stockMinimo"));
     }
     private void cargarProductosPorProveedor(int idProveedor) {
    try {
        List<Producto> productosDAO = ProductoDAO.obtenerProductosPorProveedor(idProveedor);
        todosProductos.setAll(productosDAO);
        tvTodosProductos.setItems(todosProductos);
    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los productos del proveedor.");
        e.printStackTrace();
    }
}
    private void cargarProductosDePedidoPorStockMinimo(int idProveedor) {
    try {
        List<Producto> lista = PedidoDAO.obtenerProductosDePedidosPorProveedor(idProveedor);
        ObservableList<Producto> productosMinimos = FXCollections.observableArrayList(lista);
        tvProductosStockMinimo.setItems(productosMinimos);
    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la lista de productos con stock mínimo.");
        e.printStackTrace();
    }
}
    
    private void configurarTablaProductosPedido() {
        colNombrePedido.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getProducto().getNombreProducto())
    );
    colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    colPrecioPedido.setCellValueFactory(cellData -> 
        new SimpleStringProperty(
            String.format("%.2f", 
                cellData.getValue().getProducto().getPrecio() * cellData.getValue().getCantidad()
            )
        )
    );
}

      private void configurarTablaProductosStockMinimo(){
        colNombreSM.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colPrecioSM.setCellValueFactory(new PropertyValueFactory("precio"));
        colStockActualSM.setCellValueFactory(new PropertyValueFactory("stockActual"));
        colStockMinimoPSM.setCellValueFactory(new PropertyValueFactory("stockMinimo"));
     }
    @FXML
    private void buscarProductoPorNombre(KeyEvent event) {
        String texto = tfBuscarProducto.getText();
    try {
        List<Producto> productos = ProductoDAO.buscarProductoPorNombre(texto);
        ObservableList<Producto> productosObservable = FXCollections.observableArrayList(productos);
        tvTodosProductos.setItems(productosObservable);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    
    
}
