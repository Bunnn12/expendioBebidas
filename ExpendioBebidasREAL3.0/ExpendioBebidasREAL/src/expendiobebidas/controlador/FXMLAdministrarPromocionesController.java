/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.PromocionDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.Promocion;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.scene.control.DatePicker;
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
public class FXMLAdministrarPromocionesController implements Initializable {

    @FXML
    private TableView<Cliente> tvClientes;
    @FXML
    private TableColumn colRazonSocial;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfDescuento;
    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private DatePicker dpFechaEmision;
    @FXML
    private DatePicker dpFechaVencimiento;
    
    private ObservableList<Cliente> clientes;
    private ObservableList<Producto> productos;
    private ObservableList<Promocion> promociones;
    @FXML
    private Label lbErrorDescrip;
    @FXML
    private Label lbErrorDescuento;
    @FXML
    private Label lbErrorFechaEmi;
    @FXML
    private Label lbErrorFechaVenci;
    @FXML
    private Label lbErrorProducto;
    @FXML
    private Label lbErrorCliente;
    @FXML
    private TableView<Promocion> tvPromociones;
    @FXML
    private TableColumn<?, ?> colEmisionPromo;
    @FXML
    private TableColumn<?, ?> colVencimientoPromo;
    @FXML
    private TextField tfBuscarPromo;
    @FXML
    private TableColumn<?, ?> colDescripcionPromo;
    private Promocion promocionSeleccionada;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaClientes();
        configurarTablaProductos();
        configurarTablaPromociones();
        cargarClientes();
        cargarProductos();
        cargarPromociones();
    }    

    private void configurarTablaClientes(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
    }
    
    private void configurarTablaProductos() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
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
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los clientes, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
    
    private void cargarProductos(){
        try{
            productos= FXCollections.observableArrayList();
            ArrayList<Producto> productosDAO = (ArrayList<Producto>) ProductoDAO.obtenerTodosLosProductos();
            productos.addAll(productosDAO);
            tvProductos.setItems(productos);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de los clientes, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
    private void configurarTablaPromociones() {
    colDescripcionPromo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    colEmisionPromo.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
    colVencimientoPromo.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
}

    private void cargarPromociones() {
    try {
        promociones = FXCollections.observableArrayList(PromocionDAO.obtenerPromociones());
        tvPromociones.setItems(promociones);
    } catch (SQLException e) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron cargar las promociones.");
    }
}
    @FXML
    private void buscarProductoPorNombre(KeyEvent event) {
        String nombreBuscado = tfBuscarProducto.getText().toLowerCase();

        if (nombreBuscado.isEmpty()) {
            tvProductos.setItems(productos); 
            return;
        } 
        
        ObservableList<Producto> productosFiltrados = FXCollections.observableArrayList();
        for (Producto p : productos) {
            if (p.getNombreProducto().toLowerCase().contains(nombreBuscado)) {
                productosFiltrados.add(p);
            }
        }
        tvProductos.setItems(productosFiltrados);
    }

    @FXML
    private void buscarClientePorRazonSocial(KeyEvent event) {
        String nombreBuscado = tfBuscarCliente.getText().toLowerCase();

        if (nombreBuscado.isEmpty()) {
            tvClientes.setItems(clientes); 
            return;
        } 
        
        ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
        for (Cliente c : clientes) {
            if (c.getRazonSocial().toLowerCase().contains(nombreBuscado)) {
                clientesFiltrados.add(c);
            }
        }
        tvClientes.setItems(clientesFiltrados);
    }
       
    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
        }
    }

    @FXML
    private void seleccionarProducto(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
        }
    }

    @FXML
    private void clicInsertar(ActionEvent event) throws SQLException{
        if (!validarDatosLlenos()) {
            return;
        }
        
        int descuento = Integer.parseInt(tfDescuento.getText());
        String fechaEmision = dpFechaEmision.getValue().toString();
        String fechaVencimiento = dpFechaVencimiento.getValue().toString();
        Producto producto = tvProductos.getSelectionModel().getSelectedItem();
        Cliente cliente = tvClientes.getSelectionModel().getSelectedItem();
        Promocion nuevo = new Promocion(0,
            tfDescripcion.getText(),
            descuento,
            dpFechaEmision.getValue().toString(),
            dpFechaVencimiento.getValue().toString(),
            producto,
            cliente     
        );

        if (PromocionDAO.insertarPromocion(nuevo)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Promocion insertada", "La promocion ha sido insertada correctamente");
            limpiarCampos();
            cargarPromociones();
        }
        else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Promocion no insertada", "No se ha podido insertar la promocion, intenténtelo de nuevo más tarde");
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        if (!validarDatosLlenos() || promocionSeleccionada == null) {
        return;
    }

    promocionSeleccionada.setDescripcion(tfDescripcion.getText().trim());
    promocionSeleccionada.setDescuento(Integer.parseInt(tfDescuento.getText().trim()));
    promocionSeleccionada.setFechaEmision(dpFechaEmision.getValue().toString());
    promocionSeleccionada.setFechaVencimiento(dpFechaVencimiento.getValue().toString());
    promocionSeleccionada.setCliente(tvClientes.getSelectionModel().getSelectedItem());
    promocionSeleccionada.setProducto(tvProductos.getSelectionModel().getSelectedItem());

    try {
        if (PromocionDAO.actualizarPromocion(promocionSeleccionada)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Promoción actualizada", "La promoción ha sido modificada exitosamente.");
            limpiarCampos();
            cargarPromociones();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No actualizada", "No se encontró la promoción para modificar.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al modificar la promoción.");
    }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        if (promocionSeleccionada == null) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No seleccionada", "Debes seleccionar una promoción para eliminar.");
        return;
    }

    try {
        if (PromocionDAO.eliminarPromocion(promocionSeleccionada.getIdPromocion())) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Promoción eliminada", "La promoción ha sido eliminada correctamente.");
            limpiarCampos();
            cargarPromociones();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No eliminada", "No se encontró la promoción para eliminar.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Ocurrió un error al intentar eliminar la promoción.");
    }
    }
    
    private boolean validarDatosLlenos() {
        
        boolean datosValidos = true;
        
        String descripcion = tfDescripcion.getText().trim();
        String descuentoTexto = tfDescuento.getText().trim();
        LocalDate fechaEmision = dpFechaEmision.getValue();
        LocalDate fechaVencim = dpFechaVencimiento.getValue();
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();
        
        if (descripcion.isEmpty()) {
            lbErrorDescrip.setText("La descripcion es obligatoria.");
            datosValidos = false;
        } else {
            lbErrorDescrip.setText("");
        }
        
        int descuento = 0;
        try {
            descuento = Integer.parseInt(descuentoTexto);
            if (descuento < 0 || descuento > 100) {
                lbErrorDescuento.setText("Debe estar entre 0 y 100");
                datosValidos = false;
            } else {
                lbErrorDescuento.setText("");
            }
        } catch (NumberFormatException e) {
            lbErrorDescuento.setText("Debe ser un número");
            datosValidos = false;
        }

           if (fechaEmision == null) {
                lbErrorFechaEmi.setText("La fecha de emisión es obligatoria.");
                datosValidos = false;
            } else {
                lbErrorFechaEmi.setText("");
            }
       
       if (fechaVencim == null) {
            lbErrorFechaVenci.setText("La fecha de vencimiento es obligatoria.");
            datosValidos = false;
        } else {
            lbErrorFechaVenci.setText("");
        }
       
        if (productoSeleccionado == null) {
            lbErrorProducto.setText("Selecciona un producto");
            datosValidos = false;
        } else {
            lbErrorProducto.setText("");
        }
        
        if (clienteSeleccionado == null) {
            lbErrorCliente.setText("Selecciona un cliente");
            datosValidos = false;
        } else {
            lbErrorCliente.setText("");
        }
        if (fechaEmision != null && fechaVencim != null && fechaEmision.isAfter(fechaVencim)) {
            lbErrorFechaVenci.setText("La fecha de vencimiento debe ser después de la emisión.");
            datosValidos = false;
        }


       return datosValidos;
    }
    
    private void limpiarCampos() {
        tfDescripcion.clear();
        tfDescuento.clear();
        dpFechaEmision.setValue(null);
        dpFechaVencimiento.setValue(null);
    }   
    
    private void cerrarVentana(){
        ((Stage) tvClientes.getScene().getWindow()).close();
    }
    
    @FXML
    private void btnRegresar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void buscarPromoPorNombre(KeyEvent event) {
        String textoBusqueda = tfBuscarPromo.getText().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            tvPromociones.setItems(promociones);
            return;
        }

    ObservableList<Promocion> promocionesFiltradas = FXCollections.observableArrayList();
    for (Promocion p : promociones) {
        if (p.getDescripcion().toLowerCase().contains(textoBusqueda)) {
            promocionesFiltradas.add(p);
        }
    }
    tvPromociones.setItems(promocionesFiltradas);
    }

    @FXML
    private void seleccionarPromocion(MouseEvent event) {
         promocionSeleccionada = tvPromociones.getSelectionModel().getSelectedItem();
    if (promocionSeleccionada != null) {
        tfDescripcion.setText(promocionSeleccionada.getDescripcion());
        tfDescuento.setText(String.valueOf(promocionSeleccionada.getDescuento()));
        dpFechaEmision.setValue(LocalDate.parse(promocionSeleccionada.getFechaEmision()));
        dpFechaVencimiento.setValue(LocalDate.parse(promocionSeleccionada.getFechaVencimiento()));

        for (Cliente cliente : clientes) {
            if (cliente.getIdCliente() == promocionSeleccionada.getCliente().getIdCliente()) {
                tvClientes.getSelectionModel().select(cliente);
                tvClientes.scrollTo(cliente);
                break;
            }
        }

        for (Producto producto : productos) {
            if (producto.getIdProducto() == promocionSeleccionada.getProducto().getIdProducto()) {
                tvProductos.getSelectionModel().select(producto);
                tvProductos.scrollTo(producto);
                break;
            }
        }
    }
    }

    
}
