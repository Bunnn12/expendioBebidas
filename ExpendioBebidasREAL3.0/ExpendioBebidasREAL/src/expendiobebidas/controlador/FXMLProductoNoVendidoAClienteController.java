/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *hola esta es una prueba
 * @author reino
 */
public class FXMLProductoNoVendidoAClienteController implements Initializable {

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
    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn colNombreProducto;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecio;
    private ObservableList<Cliente> clientes;
    private int prueba;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     configurarTablaClientes();
    configurarTablaProductos();
    cargarClientes();
    }    
    private void configurarTablaClientes(){
    colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
    colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
    colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
    colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }
    private void configurarTablaProductos() {
    colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
    colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
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
    private void cerrarVentana(){
        ((Stage) tvClientes.getScene().getWindow()).close();
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
    private void mostrarProductosNoVendidos(int idCliente) {
    try {
        List<Producto> productos = ProductoDAO.obtenerProductosNoVendidos(idCliente);
        tvProductos.getItems().setAll(productos);
    } catch (SQLException e) {
        e.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al consultar", "No se pudieron obtener los productos no vendidos a este cliente, intentélo de nuevo mas tarde");
    }
}

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        mostrarProductosNoVendidos(seleccionado.getIdCliente());
    }
    }
}
