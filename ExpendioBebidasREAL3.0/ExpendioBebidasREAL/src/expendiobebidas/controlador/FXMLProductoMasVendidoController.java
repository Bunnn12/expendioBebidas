/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.VentaDAO;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.Venta;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLProductoMasVendidoController implements Initializable {

    @FXML
    private TableView<Producto> tvProductoMasVendido;
    @FXML
    private TableColumn colIdProducto;
    @FXML
    private TableColumn  colNombreProducto;
    @FXML
    private TableColumn colTotal;
    ObservableList<Producto> productoMasVendido;
    @FXML
    private Label lbTitulo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductoMasVendidoTabla();
    }    

       private void configurarTabla(){
        colIdProducto.setCellValueFactory(new PropertyValueFactory("idProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colTotal.setCellValueFactory(new PropertyValueFactory("totalVendido"));
        
    }
    private void cargarProductoMasVendidoTabla() {
    try {
        productoMasVendido = FXCollections.observableArrayList();
        Producto producto = ProductoDAO.obtenerProductoMasVendido();  
        if (producto != null) {
            productoMasVendido.add(producto);
            tvProductoMasVendido.setItems(productoMasVendido);
        }
    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(
            Alert.AlertType.ERROR,"Error al cargar","Lo sentimos, por el momento no se puede cargar la información del producto más vendido. Por favor, inténtelo más tarde."
        );
        cerrarVentana();
    }
}

    private void cerrarVentana(){
        ((Stage) tvProductoMasVendido.getScene().getWindow()).close();
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }
}
