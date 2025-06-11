/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.ProductoDAO;
import expendiobebidas.modelo.dao.PromocionDAO;
import expendiobebidas.modelo.dao.VentaDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoElegidoVenta;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
import expendiobebidas.modelo.dao.pojo.Promocion;
import expendiobebidas.modelo.dao.pojo.Venta;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
    private List<Promocion> promocionesActivasCliente;
    @FXML
    private Button btnIdQuitarProducto;
    @FXML
    private Label lbFecha;
    @FXML
    private TextField tfFolioFactura;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProductos();
        configurarTablaProductosElegidos();
        configurarTablaPromociones();
        cargarProductosTabla();
        cargarFechaDeVenta();
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
    
    private void cargarFechaDeVenta(){
        LocalDate fechaActual = LocalDate.now();
        lbFecha.setText(fechaActual.toString());
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
    private void seleccionarProducto(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
        }
    }
    
    @FXML
    private void btnAgregarProducto(ActionEvent event) {
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            cargarProductosElegidosTabla(productoSeleccionado.getIdProducto());
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Producto no seleccionado", "Debes seleccionar un producto para agregar.");
        }
    }  
    
    private void agregarProductosDePedido(List<ProductoPedido> productosPedido) {
        if (productosPedido == null || productosPedido.isEmpty()) {
            return;
        }
        
       for (ProductoPedido pp : productosPedido) {
            Producto producto = pp.getProducto();
            // Buscar si ya está en la lista
            boolean encontrado = false;
            for (ProductoElegidoVenta pev : productosElegidos) {
                if (pev.getIdProducto() == producto.getIdProducto()) {
                    pev.setCantidad(pev.getCantidad() + pp.getCantidad());
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                
                productosElegidos.add(new ProductoElegidoVenta(
                    producto.getIdProducto(),
                    producto.getNombreProducto(),
                    producto.getDescripcion(),
                    pp.getCantidad(),
                    producto.getPrecioConGanancia()
                ));
            }
        }
        tvProductosElegidos.refresh();
        actualizarTotal();
    }
    
    private void configurarTablaPromociones() {
        colNombreProdDesc.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
        colPrecioDescuento.setCellValueFactory(new PropertyValueFactory<>("precioConDescuentoDosDecimales"));
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
    }
       
    private void cargarPromocionesTabla(int idCliente) {
        try {
            List<Promocion> listaPromociones = PromocionDAO.obtenerPromocionesDeCliente(idCliente);
            promocionesActivasCliente = listaPromociones;
            if (listaPromociones != null && !listaPromociones.isEmpty()) {
                ObservableList<Promocion> promociones = FXCollections.observableArrayList(listaPromociones);
                tvPromociones.setItems(promociones);
            } else {
                tvPromociones.setItems(FXCollections.emptyObservableList());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR, "Error al cargar promociones", "No se pudieron cargar las promociones del cliente. Intente más tarde.");
        }
    }
    
    private void configurarTablaProductosElegidos() {
        colNombreProdEleg.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colDescripcionEleg.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tvProductosElegidos.setItems(productosElegidos);
    }
       
    private void cargarProductosElegidosTabla(int idProducto) {
        //Validar que la cantidad ingresada sea un entero válido y mayor que 0
        String cantidadTexto = tfCantidad.getText().trim();
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cantidad inválida", "Ingresa una cantidad válida mayor que 0.");
                return;
            }
        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cantidad inválida", "Ingresa un dato numérico.");
            return;
        }

        Producto producto = null;
        for (Producto p : productos) {
            if (p.getIdProducto() == idProducto) {
                producto = p;
                break;
            }
        }
        if (producto == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se encontró el producto seleccionado.");
            return;
        }

        // Determinar si hay promoción activa para ese producto para el cliente
        double precioUnitario = producto.getPrecioConGanancia();
        if (promocionesActivasCliente != null) {
            for (Promocion promo : promocionesActivasCliente) {
                if (promo.getProducto() != null && promo.getProducto().getIdProducto() == idProducto) {
                    precioUnitario = promo.getPrecioConDescuento();
                    break;
                }
            }
        }

        // Buscar si el producto ya está en la lista de productos elegidos
        ProductoElegidoVenta productoExistente = null;
        for (ProductoElegidoVenta pev : productosElegidos) {
            if (pev.getNombreProducto().equals(producto.getNombreProducto())) {
                productoExistente = pev;
                break;
            }
        }

        if (productoExistente != null) {
            // Si ya está en la lista, actualizar cantidad y subtotal
            int nuevaCantidad = productoExistente.getCantidad() + cantidad;
            productoExistente.setCantidad(nuevaCantidad);
            productoExistente.setPrecioUnitario(precioUnitario);
            tvProductosElegidos.refresh();
        } else {
            ProductoElegidoVenta nuevoProductoElegido = new ProductoElegidoVenta(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                cantidad,
                precioUnitario
            );
            productosElegidos.add(nuevoProductoElegido);
        }  
        
        actualizarTotal();
    }
    
    @FXML
    private void btnAgregarArticuloPedido(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/expendiobebidas/vista/FXMLAgregarPedidoAVenta.fxml"));
            Parent vista = loader.load();

            FXMLAgregarPedidoAVentaController controller = loader.getController();

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Seleccionar Pedido");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.initOwner(tvProductos.getScene().getWindow());
            escenario.showAndWait();

            if (controller.getProductosSeleccionados() != null) {
                agregarProductosDePedido(controller.getProductosSeleccionados());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir el selector de pedidos");
        }
    }
    
    private void manejarSeleccionPedido(List<ProductoPedido> productosPedido) {
        if (productosPedido != null && !productosPedido.isEmpty()) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar agregar pedido");
            confirmacion.setHeaderText("¿Deseas agregar " + productosPedido.size() + " productos del pedido seleccionado?");
            confirmacion.setContentText("Se agregarán a los productos actuales de la venta.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                agregarProductosDePedido(productosPedido);
            }
        }
    }
    
    @FXML
    private void seleccionarProductoYaELegido(MouseEvent event) {
        ProductoElegidoVenta productoSeleccionado = tvProductosElegidos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            btnIdQuitarProducto.setDisable(false); 
        } 
    }
    
    @FXML
    private void btnQuitarProducto(ActionEvent event) {
         ProductoElegidoVenta productoSeleccionado = tvProductosElegidos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado != null) {
            int cantidad = productoSeleccionado.getCantidad();

            if (cantidad > 1) {
                productoSeleccionado.setCantidad(cantidad - 1);  
                tvProductosElegidos.refresh(); 
            } else {
                productosElegidos.remove(productoSeleccionado);
            }
            // Desactiva el botón si no hay selección
            btnIdQuitarProducto.setDisable(true);
        }
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for(ProductoElegidoVenta pev : productosElegidos){
            total += pev.getPrecioUnitario() * pev.getCantidad();
        }
        lbTotal.setText(String.format("$ %.2f", total));
    }
    
    @FXML
    private void btnCobrar(ActionEvent event) {

        if (productosElegidos.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Venta vacía", "Debes agregar al menos un producto para realizar la venta.");
            return;
        }

        // Validar folio de factura
        String folioFactura = tfFolioFactura.getText().trim();

        try {
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now().toString());
            venta.setIdCliente(clienteElegido.getIdCliente());
            venta.setFolioFactura(folioFactura.isEmpty() ? null : folioFactura);
            List<ProductoElegidoVenta> productosVenta = new ArrayList<>(productosElegidos);

            boolean exito = VentaDAO.registrarVenta(venta, productosVenta);

            if (exito) {

                // Limpiar campos para nueva venta
                productosElegidos.clear();
                lbTotal.setText("$ 0.00");
                tfFolioFactura.clear();

                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Venta exitosa", "La venta se ha registrado correctamente.");

                // Actualizar la lista de productos para reflejar cambios en stock
                cargarProductosTabla();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("No hay suficiente stock")) {Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Stock insuficiente", e.getMessage());
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en base de datos", "Ocurrió un error al registrar la venta: " + e.getMessage());
            }
        }
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
    private void btnRegresar(ActionEvent event) {
        irBusquedaCliente();
    }
    
}
