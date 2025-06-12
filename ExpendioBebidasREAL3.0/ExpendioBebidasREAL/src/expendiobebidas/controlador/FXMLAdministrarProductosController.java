/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author rodri
 */
public class FXMLAdministrarProductosController implements Initializable {

    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private TextField tfNombreProducto;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfStockMinimo;
    @FXML
    private TextField tfStockActual;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    @FXML
    private TableColumn<Producto, Integer> colStockMinimo;
    @FXML
    private TableColumn<Producto, Integer> colStockActual;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private Label lbErrorNombreProducto;
    
    private ObservableList<Producto> productos;
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbErrorDescripcionProducto;
    @FXML
    private Label lbErrorSMProducto;
    @FXML
    private Label lbErrorSAProducto;
    @FXML
    private Label lbErrorPrecioProducto;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductos();
    }    
    

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory("stockMinimo"));
        colStockActual.setCellValueFactory(new PropertyValueFactory("stockActual"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
    }

    private void cargarProductos(){
        try {
            productos = FXCollections.observableArrayList();
            ArrayList<Producto> productosDAO = ProductoDAO.obtenerProductos();
            productos.addAll(productosDAO);
            tvProductos.setItems(productos);
        } catch(SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
    
    private void cerrarVentana(){
        ((Stage) tvProductos.getScene().getWindow()).close();
    }
    
    private void limpiarCampos() {
        tfNombreProducto.clear();
        tfDescripcion.clear();
        tfStockActual.clear();
        tfStockMinimo.clear();
        tfPrecio.clear();
    }   
    
    @FXML
    private void clicInsertar(ActionEvent event) throws SQLException {
        if (!validarCampos()) {
            return;
        }
        
        int stockMinimo = Integer.parseInt(tfStockMinimo.getText());
        int stockActual = Integer.parseInt(tfStockActual.getText());
        double precio = Double.parseDouble(tfPrecio.getText());
        
        Producto nuevo = new Producto(0,
            tfNombreProducto.getText(),
            tfDescripcion.getText(),
            stockMinimo,
            stockActual,
            precio
        );

        if (ProductoDAO.insertarProducto(nuevo)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Producto insertado", "El producto ha sido insertado correctamente");
            cargarProductos();
            limpiarCampos();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Producto no insertado", "No se ha podido insertar el producto, intenténtelo de nuevo más tarde");
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) throws SQLException {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Debes seleccionar un producto para modificar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        int stockMinimo = Integer.parseInt(tfStockMinimo.getText().trim());
        int stockActual = Integer.parseInt(tfStockActual.getText().trim());
        double precio = Double.parseDouble(tfPrecio.getText().trim());

        seleccionado.setNombreProducto(tfNombreProducto.getText().trim());
        seleccionado.setDescripcion(tfDescripcion.getText().trim());
        seleccionado.setStockMinimo(stockMinimo);
        seleccionado.setStockActual(stockActual);
        seleccionado.setPrecio(precio);

        if (ProductoDAO.actualizarProducto(seleccionado)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Producto modificado", "El producto ha sido modificado correctamente.");
            cargarProductos();
            limpiarCampos();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Producto no modificado", "No se ha podido modificar el producto, inténtelo de nuevo más tarde");
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) throws SQLException {

        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Seleccione un producto", "Por favor seleccione un producto de la tabla.");
        return;
    }

    try {
        boolean eliminado = ProductoDAO.eliminarProducto(seleccionado.getIdProducto());
        if (eliminado) {
            productos.remove(seleccionado);
            limpiarCampos();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Producto eliminado", "El producto ha sido eliminado exitosamente.");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al eliminar", "No se pudo eliminar el producto.");
        }
    } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error con la base de datos",
                    "Ha ocurrido un error con la base de datos, probablemente el producto ya este asociado a una venta o pedido por lo que no se puede eliminar " );
    }
        
    }

    @FXML
    private void buscarProductoPorNombre(KeyEvent event) {
        String productoBuscado = tfBuscarProducto.getText().toLowerCase();

        if (productoBuscado.isEmpty()) {
            tvProductos.setItems(productos); 
            return;
        }

        ObservableList<Producto> filtrados = FXCollections.observableArrayList();
        for (Producto producto : productos) {
            if (producto.getNombreProducto().toLowerCase().contains(productoBuscado)) {
                filtrados.add(producto);
            }
        }

        tvProductos.setItems(filtrados);
    }

    private boolean validarCampos() {
    boolean valido = true;

    // Validar nombre
    String nombre = tfNombreProducto.getText().trim();
    if (nombre.isEmpty()) {
        lbErrorNombreProducto.setText("El nombre del producto es obligatorio.");
        valido = false;
    } else {
        lbErrorNombreProducto.setText("");
    }

    // Validar descripción
    String descripcion = tfDescripcion.getText().trim();
    if (descripcion.isEmpty()) {
        lbErrorDescripcionProducto.setText("La descripción es obligatoria.");
        valido = false;
    } else {
        lbErrorDescripcionProducto.setText("");
    }

    // Validar stock mínimo
    String stockMinimoStr = tfStockMinimo.getText().trim();
    if (stockMinimoStr.isEmpty()) {
        lbErrorSMProducto.setText("El stock mínimo es obligatorio.");
        valido = false;
    } else {
        try {
            int stockMinimo = Integer.parseInt(stockMinimoStr);
            if (stockMinimo < 0) {
                lbErrorSMProducto.setText("El stock mínimo no puede ser negativo.");
                valido = false;
            } else {
                lbErrorSMProducto.setText("");
            }
        } catch (NumberFormatException e) {
            lbErrorSMProducto.setText("El stock mínimo debe ser un número entero.");
            valido = false;
        }
    }

    // Validar stock actual
    String stockActualStr = tfStockActual.getText().trim();
    if (stockActualStr.isEmpty()) {
        lbErrorSAProducto.setText("El stock actual es obligatorio.");
        valido = false;
    } else {
        try {
            int stockActual = Integer.parseInt(stockActualStr);
            if (stockActual < 0) {
                lbErrorSAProducto.setText("El stock actual no puede ser negativo.");
                valido = false;
            } else {
                lbErrorSAProducto.setText("");
            }
        } catch (NumberFormatException e) {
            lbErrorSAProducto.setText("El stock actual debe ser un número entero.");
            valido = false;
        }
    }

    // Validar precio
    String precioStr = tfPrecio.getText().trim();
    if (precioStr.isEmpty()) {
        lbErrorPrecioProducto.setText("El precio es obligatorio.");
        valido = false;
    } else {
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                lbErrorPrecioProducto.setText("El precio no puede ser negativo.");
                valido = false;
            } else {
                lbErrorPrecioProducto.setText("");
            }
        } catch (NumberFormatException e) {
            lbErrorPrecioProducto.setText("El precio debe ser un número válido.");
            valido = false;
        }
    }

    return valido;
}

    
    @FXML
    private void seleccionarProducto(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            tfNombreProducto.setText(seleccionado.getNombreProducto());
            tfDescripcion.setText(seleccionado.getDescripcion());
            tfStockMinimo.setText(String.valueOf(seleccionado.getStockMinimo()));
            tfStockActual.setText(String.valueOf(seleccionado.getStockActual()));
            tfPrecio.setText(String.valueOf(seleccionado.getPrecio()));
        }
    }

    @FXML
    private void clicTfStockMinimo(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("/expendiobebidas/vista/FXMLModificarStockMinimo.fxml"));
                Parent vista = cargador.load();
                FXMLModificarStockMinimoController controlador = cargador.getController();

                // Pasar referencia al TextField de esta ventana (tfStockMinimo)
                controlador.setCampoStockMinimoPrincipal(tfStockMinimo);

                Stage escenarioEmergente = new Stage();
                escenarioEmergente.setScene(new Scene(vista));
                escenarioEmergente.setTitle("Modificar Stock Minimo");
                escenarioEmergente.initOwner(tfStockMinimo.getScene().getWindow());
                escenarioEmergente.initModality(Modality.APPLICATION_MODAL);
                escenarioEmergente.setResizable(false);
                escenarioEmergente.centerOnScreen();
                escenarioEmergente.showAndWait();
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana para modificar el stock minimo.");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void clicTfStockActual(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("/expendiobebidas/vista/FXMLModificarStockActual.fxml"));
                Parent vista = cargador.load();
                FXMLModificarStockActualController controlador = cargador.getController();

                // Pasar referencia al TextField de esta ventana (tfStockActual)
                controlador.setCampoStockActualPrincipal(tfStockActual);

                Stage escenarioEmergente = new Stage();
                escenarioEmergente.setScene(new Scene(vista));
                escenarioEmergente.setTitle("Modificar Stock Actual");
                escenarioEmergente.initOwner(tfStockActual.getScene().getWindow());
                escenarioEmergente.initModality(Modality.APPLICATION_MODAL);
                escenarioEmergente.setResizable(false);
                escenarioEmergente.centerOnScreen();
                escenarioEmergente.showAndWait();
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana para modificar el stock actual.");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) tfBuscarProducto.getScene().getWindow();
        stageActual.close();
    }
    
}
