/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.ClienteDAO;
import expendiobebidas.modelo.dao.VentaDAO;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Venta;
import expendiobebidas.utilidades.Utilidad;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class FXMLAdministrarClientesController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCorreo;
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
    private TextField tfRazonSocial;
    private ObservableList<Cliente> clientes;
    @FXML
    private Label lbErrorRazonSocial;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       configurarTabla();
       cargarClientes();
    }
    private void configurarTabla(){
    colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
    colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
    colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
    colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
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
    
    private void limpiarCampos() {
        tfRazonSocial.clear();
        tfTelefono.clear();
        tfCorreo.clear();
        tfDireccion.clear();
    }   
    
    @FXML
    private void clicInsertar(ActionEvent event) throws SQLException {
        if (!validarRazonSocial()) {
        return;
        }
            Cliente nuevo = new Cliente(0,
            tfRazonSocial.getText(),
            tfTelefono.getText(),
            tfCorreo.getText(),
            tfDireccion.getText()
        );

        if (ClienteDAO.insertarCliente(nuevo)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Cliente insertado", "El cliente ha sido insertado correctamente");
            cargarClientes();
            limpiarCampos();
        }
        else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "cliente no insertado", "No se ha podido insertar el cliente, intenténtelo de nuevo más tarde");
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) throws SQLException {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Debes seleccionar un cliente para modificar.");
            return;
        }

        if (!validarRazonSocial()) {
            return;
        }

        seleccionado.setRazonSocial(tfRazonSocial.getText().trim());
        seleccionado.setTelefono(tfTelefono.getText().trim());
        seleccionado.setCorreo(tfCorreo.getText().trim());
        seleccionado.setDireccion(tfDireccion.getText().trim());

        if (ClienteDAO.actualizarCliente(seleccionado)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Cliente modificado", "El cliente ha sido modificado correctamente.");
            cargarClientes();
            limpiarCampos();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Cliente no modificado", "No se ha podido modificar al cliente, inténtelo de nuevo más tarde");
        }
    }
 
    @FXML
    private void clicEliminar(ActionEvent event) throws SQLException {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
    
        if (seleccionado != null) {
            if (ClienteDAO.tieneVentasAsociadas(seleccionado.getIdCliente())) {
              Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se puede eiminar", "El cliente seleccionado ya tiene ventas asociadas, por lo que no se puede eliminar");

            } else {
                ClienteDAO.eliminarCliente(seleccionado.getIdCliente());
                clientes.remove(seleccionado);
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Cliente eliminado", "El cliente ha sido eliminado exitosamente.");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Seleccione un cliente", "Por favor seleccione un cliente de la tabla.");
        }
    }

    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            tfRazonSocial.setText(seleccionado.getRazonSocial());
            tfTelefono.setText(seleccionado.getTelefono());
            tfCorreo.setText(seleccionado.getCorreo());
            tfDireccion.setText(seleccionado.getDireccion());
        }
    }

    @FXML
    private void buscarClientePorRazonSocial(KeyEvent event) {
        String razonBuscada = tfBuscarCliente.getText().toLowerCase();

        if (razonBuscada.isEmpty()) {
            tvClientes.setItems(clientes); 
            return;
        }

        ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
        for (Cliente cliente : clientes) {
            if (cliente.getRazonSocial().toLowerCase().contains(razonBuscada)) {
                filtrados.add(cliente);
            }
        }

        tvClientes.setItems(filtrados);
    }
  
    private boolean validarRazonSocial() {
        String razon = tfRazonSocial.getText().trim();
        if (razon.isEmpty()) {
            lbErrorRazonSocial.setText("La razón social es obligatoria.");
            return false;
        } else {
            lbErrorRazonSocial.setText("");
            return true;
        }
    }

    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) tfBuscarCliente.getScene().getWindow();
        stageActual.close();
    }

    
}
