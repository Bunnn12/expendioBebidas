/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.VentasProductoClienteDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLProductoMasVendidoAClienteController implements Initializable {

    @FXML
    private TableView tvProductosMasVendidos;
    @FXML
    private TableColumn colNombreProducto;
    @FXML
    private TableColumn colCantidadCompra;
    @FXML
    private TextField tfBuscarRazon;
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
    
    ObservableList<Cliente> clientes;
    ObservableList<Producto> productos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarClientes();
        configurarTablaClientes();
        configurarTablaProductos();
    }    
    
    private void cargarClientes(){
        try {
           clientes = FXCollections.observableArrayList();
           List<Cliente> clientesDAO = ClienteDAO.obtenerClientes(); 
           clientes.addAll(clientesDAO);
           tvClientes.setItems(clientes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }
    }
    
    private void configurarTablaClientes(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }
    
    private void configurarTablaProductos(){
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colCantidadCompra.setCellValueFactory(new PropertyValueFactory("totalVendido"));
    }
     
    private void cargarInformacionTablaProductos(int idCliente){
        try{
            productos = FXCollections.observableArrayList();
            ArrayList<Producto> productosDAO = VentasProductoClienteDAO.obtenerProductoMasVendidoCliente(idCliente);
            productos.addAll(productosDAO);
            tvProductosMasVendidos.setItems(productos);
        } catch (SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos por el momento no se puede mostrar");
            cerrarVentana();
        }    
    }

    @FXML
    private void buscarClientePorRazonSocial(KeyEvent event) {
        String razonBuscada = tfBuscarRazon.getText().toLowerCase();

        if (razonBuscada.isEmpty()) {
            tvClientes.setItems(clientes); 
            return;
        } 
        
        ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
        for (Cliente c : clientes) {
            if (c.getRazonSocial().toLowerCase().contains(razonBuscada)) {
                clientesFiltrados.add(c);
            }
        }
        tvClientes.setItems(clientesFiltrados);
    } 

    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            cargarInformacionTablaProductos(seleccionado.getIdCliente());
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tvProductosMasVendidos.getScene().getWindow()).close();
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }
    
}
