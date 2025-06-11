/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class FXMLProductosConStockMinimoController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TableView<Producto> tvProductosStockMinimo;
    @FXML
    private TableColumn<Producto, Integer> colIdProducto;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, Integer> colStockMinimo;
    @FXML
    private TableColumn<Producto, Integer> colStockActual;

    ObservableList<Producto> listaProductos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductosConStockMinimo();
    }

    private void configurarTabla() {
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
    }

    private void cargarProductosConStockMinimo() {
        try {
            List<Producto> productos = ProductoDAO.obtenerProductosConStockMinimo();
            listaProductos = FXCollections.observableArrayList(productos);
            tvProductosStockMinimo.setItems(listaProductos);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,
                "Error al cargar",
                "Lo sentimos, por el momento no se puede cargar la lista de productos con stock mínimo. Inténtalo más tarde."
            );
            cerrarVentana();
        }
    }

    private void cerrarVentana() {
        ((Stage) tvProductosStockMinimo.getScene().getWindow()).close();
    }
    
}
