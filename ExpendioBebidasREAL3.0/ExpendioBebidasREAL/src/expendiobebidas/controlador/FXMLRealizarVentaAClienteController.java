/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
/*package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.PromocionDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoElegidoVenta;
import expendiobebidas.modelo.dao.pojo.Promocion;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
/*
public class FXMLRealizarVentaAClienteController implements Initializable {

    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn colNombreProd;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableView<ProductoElegidoVenta> tvProductosElegidos;
    @FXML
    private TableColumn colNombreProdEleg;
    @FXML
    private TableColumn colDescripcionEleg;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colSubtotal;
    @FXML
    private TableView<Promocion> tvPromociones;
    @FXML
    private TableColumn colNombreProdDesc;
    @FXML
    private TableColumn colDescuento;
    @FXML
    private TableColumn colPrecioDescuento;
    @FXML
    private TableColumn colFechaVencimiento;
    @FXML
    private Label lbTotal;
    @FXML
    private Label lbNombreCliente;
    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private TextField tfCantidad;
    
    private Cliente clienteElegido; 
    
    ObservableList<Producto> productos;
    ObservableList<ProductoElegidoVenta> productosElegidos = FXCollections.observableArrayList();    
    private List <Promocion> promocionesCliente;    

    /**
     * Initializes the controller class.
     */
/*
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProductos();
        configurarTablaProductosElegidos();
        configurarTablaPromociones();
        cargarProductosTabla();
    }    
    
    public void inicializarInformacion(Cliente clienteElegido){
        this.clienteElegido = clienteElegido;
        cargarInformacionCliente();
        cargarPromocionesTabla(clienteElegido.getIdCliente());
    }
    
    private void cargarInformacionCliente(){
        if(clienteElegido != null){
            lbNombreCliente.setText(clienteElegido.toString());
        }
    }
    
    private void configurarTablaProductos() {
        colNombreProd.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precioConGananciaDosDecimales"));
    }
       
    private void cargarProductosTabla() {
        try {
            List<Producto> listaProductos = ProductoDAO.obtenerTodosLosProductos();  
            if (listaProductos != null && !listaProductos.isEmpty()) {
                productos = FXCollections.observableArrayList(listaProductos);
                tvProductos.setItems(productos);
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,"Error al cargar", "Lo sentimos, por el momento no se pueden realizar ventas. Por favor, inténtelo más tarde."
            );
            irBusquedaCliente();
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
    private void seleccionarProducto(MouseEvent event) throws SQLException {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            cargarProductosElegidosTabla(seleccionado.getIdProducto());
        }
    }
    
    @FXML
    private void btnAgregarProducto(ActionEvent event) throws SQLException {
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            cargarProductosElegidosTabla(productoSeleccionado.getIdProducto());
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Producto no seleccionado", "Debes seleccionar un producto para agregar.");
        }
    }
    
    @FXML
        private void btnAgregarProductoPedido(ActionEvent event) {
    }
    
    private void configurarTablaPromociones() {
        colNombreProdDesc.setCellValueFactory(new PropertyValueFactory<Promocion, String>("nombreProducto"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("descuento"));
        colPrecioDescuento.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioConDescuento"));
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<Promocion, LocalDate>("fechaVencimiento"));
    }
       
    private void cargarPromocionesTabla(int idCliente) {
        try {
            promocionesCliente = PromocionDAO.obtenerPromocionesDeCliente(idCliente);
                for (Promocion promocion : promocionesCliente) {
                    Producto producto = ProductoDAO.obtenerProductoPorId(Integer.parseInt(promocion.getIdProducto()));
                    double precioConGanancia = producto.getPrecioConGanancia();
                    double precioFinal = precioConGanancia * (1 - promocion.getDescuento() / 100.0);
                    promocion.setPrecioConDescuento(precioFinal);
                }
            if (promocionesCliente != null && !promocionesCliente.isEmpty()) {
                tvPromociones.setItems(FXCollections.observableArrayList(promocionesCliente));
            }
            else {
                tvPromociones.getItems().clear();
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar promociones", "No se pudieron cargar las promociones activas.");
        }
    }
    
    private void configurarTablaProductosElegidos() {
        colNombreProdEleg.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colDescripcionEleg.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tvProductosElegidos.setItems(productosElegidos);
        
    }
       
    private void cargarProductosElegidosTabla(int idProducto) throws SQLException {
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        String cantidadTexto = tfCantidad.getText();

        if (productoSeleccionado == null || cantidadTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cantidad no ingresada", "Selecciona un producto y escribe la cantidad.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) throw new NumberFormatException();

            double precioFinal = productoSeleccionado.getPrecioConGanancia();

            // Buscar si hay una promoción para este producto
            List<Promocion> promociones = PromocionDAO.obtenerPromocionesDeCliente(clienteElegido.getIdCliente());
            if (promociones != null) {
                for (Promocion promo : promociones) {
                    if (promo.getIdProducto() == productoSeleccionado.getIdProducto()) {
                        precioFinal = promo.getPrecioConDescuento();
                        break;
                    }
                }
            }

            ProductoElegidoVenta elegido = new ProductoElegidoVenta(
                productoSeleccionado.getNombreProducto(),
                productoSeleccionado.getDescripcion(),
                cantidad,
                precioFinal
            );

            productosElegidos.add(elegido);
            tfCantidad.clear();
            actualizarTotal();

        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Cantidad inválida", "La cantidad debe ser un número entero positivo.");
        }
    }
    
    private void actualizarTotal() {
        float total = 0;
        for (ProductoElegidoVenta p : productosElegidos) {
            total += p.getSubtotal();
        }
        lbTotal.setText("$" + String.format("%.2f", total));
    }
    
    @FXML
    private void btnQuitarProducto(ActionEvent event) {
    }

    @FXML
    private void btnCobrar(ActionEvent event) {
    }   

    private void irBusquedaCliente(){
        try {
            Stage escenarioBase = (Stage) tvProductosElegidos.getScene().getWindow();
            
            FXMLLoader cargador = new FXMLLoader(ExpendioBebidas.class.getResource("vista/FXMLBuscarClienteVenta.fxml"));      
            Parent vista = cargador.load();
            Scene escenaPrincial = new Scene(vista);
            escenarioBase.setScene(escenaPrincial);
            escenarioBase.setTitle("Sleccionar cliente");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void btnAgregarArticuloPedido(ActionEvent event) {
    }
    
    @FXML
    private void btnRegresar(ActionEvent event) {
        irBusquedaCliente();
    }


    @FXML
    private void seleccionarProductoYaELegido(MouseEvent event) {
    }
    
}
*/