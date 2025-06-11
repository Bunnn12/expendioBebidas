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
 * @author rodri
 */
public class FXMLProductoMenosVendidoController implements Initializable {

    @FXML
    private TableView<Producto> tvProductoMenosVendido;
    @FXML
    private TableColumn colIdProducto;
    @FXML
    private TableColumn  colNombreProducto;
    @FXML
    private TableColumn colTotal;
    ObservableList<Producto> productoMenosVendido;
    @FXML
    private Label lbTitulo;

    ObservableList<Producto> listaProductos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductoMenosVendidoTabla();
    }    

    private void configurarTabla() {
        colIdProducto.setCellValueFactory(new PropertyValueFactory("idProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colTotal.setCellValueFactory(new PropertyValueFactory("totalVendido"));
    }
       
    private void cargarProductoMenosVendidoTabla() {
        try {
            List<Producto> productos = ProductoDAO.obtenerProductosMenosVendidos();
            listaProductos = FXCollections.observableArrayList(productos);
            tvProductoMenosVendido.setItems(listaProductos);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,"Error al cargar", "Lo sentimos, por el momento no se puede cargar la información del producto. Por favor, inténtelo más tarde."
            );
            cerrarVentana();
        }
    }

    private void cerrarVentana() {
        ((Stage) tvProductoMenosVendido.getScene().getWindow()).close();
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
    }
}
