/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.PedidoDAO;
import expendiobebidas.modelo.dao.pojo.Pedido;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLAgregarPedidoAVentaController implements Initializable {

    @FXML
    private ComboBox<Pedido> cbPedidos;
    @FXML
    private TableView<ProductoPedido> tvListaPedidos;
    @FXML
    private TableColumn<ProductoPedido, String> colNombreProd;
    @FXML
    private TableColumn<ProductoPedido, String> colPrecio;
    @FXML
    private TableColumn<ProductoPedido, Integer> colCantidad;
    
    ObservableList<Pedido> pedidos;
    private ObservableList<ProductoPedido> productosPedido = FXCollections.observableArrayList();
     
     private List<ProductoPedido> productosSeleccionados;

    public List<ProductoPedido> getProductosSeleccionados() {
        return this.productosSeleccionados;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaPedidos();
        cargarPedidos();
        seleccionarPedido();
    } 
    
    private void configurarTablaPedidos() {
        colNombreProd.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getProducto().getNombreProducto())
        );

        colCantidad.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject()
        );

        colPrecio.setCellValueFactory(cellData -> {
            double precioTotal = cellData.getValue().getProducto().getPrecioConGanancia() * cellData.getValue().getCantidad();
            return new SimpleStringProperty(String.format("%.2f", precioTotal));
        });
    }
    
    private void cargarPedidos(){
        try {
            pedidos = FXCollections.observableArrayList();
            List<Pedido> pedidoDAO = PedidoDAO.obtenerPedidos(); 
            pedidos.addAll(pedidoDAO);
            cbPedidos.setItems(pedidos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos por el momento no se pueden mostrar los pedidos");
            irRealizarVenta();
        }
    }
        
    private void seleccionarPedido(){
        cbPedidos.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarProductosPedido(newValue.getIdPedido(), newValue.getIdCliente()); 
            } else {
                productosPedido.clear();
                tvListaPedidos.setItems(productosPedido);
            }
        });
    }
    
    private void cargarProductosPedido(int idPedido, int idCliente) {
        try {
            List<ProductoPedido> listaProductosPedido = PedidoDAO.obtenerProductosDePedido(idPedido, idCliente);
            productosPedido.clear();
            productosPedido.addAll(listaProductosPedido);
            tvListaPedidos.setItems(productosPedido);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron cargar los productos del pedido");
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicAgregarPedidoSelec(ActionEvent event) {
        Pedido pedidoSeleccionado = cbPedidos.getValue();
       
        if (pedidoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", "Debes seleccionar un pedido.");
            return;
        }

        try {
            productosSeleccionados = PedidoDAO.obtenerProductosDePedido(
                pedidoSeleccionado.getIdPedido(),
                pedidoSeleccionado.getIdCliente()
            );
            ((Stage) cbPedidos.getScene().getWindow()).close();
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron cargar los productos del pedido");
            ex.printStackTrace();
        }
    }
    
    private void irRealizarVenta(){
        try {
            Stage escenarioBase = (Stage) tvListaPedidos.getScene().getWindow();
            
            FXMLLoader cargador = new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLRealizarVentaACliente.fxml"));      
            Parent vista = cargador.load();
            Scene escenaPrincial = new Scene(vista);
            escenarioBase.setScene(escenaPrincial);
            escenarioBase.setTitle("Realizar venta");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicRegresar(ActionEvent event) {
        productosSeleccionados = null;
        irRealizarVenta();
    }
    
}
