/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.VentasProductoClienteDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.utilidades.Utilidad;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLProductoMasVendidoAClienteController implements Initializable {

    @FXML
    private ComboBox<Cliente> cbCliente;
    @FXML
    private TableView tvProductosMasVendidos;
    @FXML
    private TableColumn colNombreProducto;
    @FXML
    private TableColumn colCantidadCompra;
    ObservableList<Cliente> clientes;
    ObservableList<Producto> productos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarClientes();
        seleccionarCliente();
        configurarTabla();
    }    
    
    private void cargarClientes(){
        try {
           clientes = FXCollections.observableArrayList();
           List<Cliente> clientesDAO = ClienteDAO.obtenerClientes(); 
           clientes.addAll(clientesDAO);
           cbCliente.setItems(clientes);
        } catch (SQLException ex) {
            //TODO
        }
    }
    
    private void seleccionarCliente(){
        cbCliente.valueProperty().addListener(new ChangeListener<Cliente>() {
            @Override
            public void changed(ObservableValue<? extends Cliente> observable, 
                    Cliente oldValue, 
                    Cliente newValue) {
                if(newValue!= null){
                    cargarInformacionTabla(newValue.getIdCliente());
                }
            }
        });
    }
     
     private void configurarTabla(){
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colCantidadCompra.setCellValueFactory(new PropertyValueFactory("totalVendido"));
    }
     
     private void cargarInformacionTabla(int idCliente){
        
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
     
     private void cerrarVentana(){
        ((Stage) tvProductosMasVendidos.getScene().getWindow()).close();
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }
    
}
