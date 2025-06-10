/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.PedidoDAO;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.VentasProductoClienteDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
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
public class FXMLPedidoDeClienteController implements Initializable {

    @FXML
    private TableView<Producto> tvTodosProductos;
    @FXML
    private TableColumn colNombreTP;
    @FXML
    private TableColumn colPrecioTP;
    @FXML
    private TableColumn<ProductoPedido, String> colNombrePedido;
    @FXML
    private TableColumn<ProductoPedido, String> colPrecioPedido;
    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private TableView<ProductoPedido> tvProductosPedido;
    @FXML
    private Label lbPrecioPedido;
    ObservableList<Producto> productos= FXCollections.observableArrayList();
    ObservableList<ProductoPedido> productosPedido = FXCollections.observableArrayList();
    private ObservableList<Cliente> clientes;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private TableView<Cliente> tvClientes;
    @FXML
    private TableColumn colRazonSocial;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colDireccion;
    private Cliente clienteSeleccionado;
    @FXML
    private TableColumn<ProductoPedido, Integer> colCantidad;
    @FXML
    private Label lbClienteSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaClientes();
        configurarTablaTodosProductos();
        configurarTablaProductosPedido();
        cargarInformacionTablaProductos();
        cargarClientes();
    }    
    
     private void configurarTablaTodosProductos(){
        colNombreTP.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colPrecioTP.setCellValueFactory(new PropertyValueFactory("precio"));
     }
       private void cargarInformacionTablaProductos(){
        try{
            productos = FXCollections.observableArrayList();
            List<Producto> productosDAO = ProductoDAO.obtenerTodosLosProductos();
            productos.addAll(productosDAO);
            tvTodosProductos.setItems(productos);
        } catch (SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos por el momento no se puede mostrar");
            cerrarVentana();
        }    
    }
       private void cerrarVentana(){
           ((Stage) tvProductosPedido.getScene().getWindow()).close();
    }
       
       private void actualizarTotal() {
        double total = productosPedido.stream()
        .mapToDouble(pp -> pp.getProducto().getPrecio() * pp.getCantidad())
        .sum();
        lbPrecioPedido.setText(" $" + String.format("%.2f", total));
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


    @FXML
    private void clicAgregarProducto(ActionEvent event) {
         Producto seleccionado = tvTodosProductos.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        ProductoPedido encontrado = null;
        for (ProductoPedido pp : productosPedido) {
            if (pp.getProducto().getIdProducto() == seleccionado.getIdProducto()) {
                encontrado = pp;
                break;
            }
        }

        if (encontrado != null) {
            encontrado.incrementarCantidad();
        } else {
            productosPedido.add(new ProductoPedido(seleccionado));
        }
        tvProductosPedido.setItems(productosPedido);
        tvProductosPedido.refresh();
        actualizarTotal();
    } else {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", "Debes seleccionar un producto para agregarlo");
    }
    }
     private void configurarTablaClientes(){
    colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
    colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
    colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
    colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }

    private void cargarClientes(){
        try{
        clientes= FXCollections.observableArrayList();
        ArrayList<Cliente> clientesDAO = ClienteDAO.obtenerClientes();
        clientes.addAll(clientesDAO);
        tvClientes.setItems(clientes);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }

    @FXML
    private void clicRealizarPedido(ActionEvent event) {
        System.out.println("ID cliente seleccionado: " + (clienteSeleccionado != null ? clienteSeleccionado.getIdCliente() : "null"));
         if (clienteSeleccionado == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
            "Cliente no seleccionado", 
            "Por favor selecciona un cliente antes de realizar el pedido.");
        return;
    }

    if (productosPedido.isEmpty()) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
            "Sin productos", 
            "Agrega al menos un producto al pedido.");
        return;
    }

    try {
        boolean pedidoRealizado = PedidoDAO.insertarPedidoDeCliente(clienteSeleccionado.getIdCliente(), productosPedido);
        if (pedidoRealizado) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, 
                "Pedido realizado", 
                "El pedido se registró exitosamente.");
            cerrarVentana();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                "Error al registrar", 
                "No se pudo registrar el pedido. Intenta más tarde.");
        }
    } catch (SQLException ex) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
            "Error de BD", 
            "Hubo un error al guardar el pedido: " + ex.getMessage());
        ex.printStackTrace();
    }
       
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
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

    @FXML
    private void buscarClientePorRazonSocial(KeyEvent event) {
        String razonBuscada = tfBuscarCliente.getText().toLowerCase();

        if (razonBuscada.isEmpty()) {
            tvClientes.setItems(clientes); 
            return;
        }

        ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
        for (Cliente cliente : clientes) {
            if (cliente.getRazonSocial().toLowerCase().contains(razonBuscada)) {
                filtrados.add(cliente);
            }
        }

        tvClientes.setItems(filtrados);
    }

    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
        lbClienteSeleccionado.setText(clienteSeleccionado.getRazonSocial());
        } else {
        lbClienteSeleccionado.setText("");  // Limpia el label si no hay selección
        }
    }
    
}
