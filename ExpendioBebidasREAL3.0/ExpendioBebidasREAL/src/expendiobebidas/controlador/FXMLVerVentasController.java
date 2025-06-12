/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.modelo.dao.VentaDAO;
import expendiobebidas.modelo.dao.pojo.Usuario;
import expendiobebidas.modelo.dao.pojo.Venta;
import expendiobebidas.reporte.ExportarExcel;
import expendiobebidas.reporte.ExportarPDF;
import expendiobebidas.utilidades.Utilidad;
import java.io.File;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLVerVentasController implements Initializable {

    @FXML
    private TableView<Venta> tvVentas;
  
    private ObservableList<Venta> ventas;
    @FXML
    private TableColumn colSemana;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colTotalVentas;
    @FXML
    private TableColumn colTotalRecaudado;
    @FXML
    private TableColumn colMes;
    @FXML
    private Label lbTitulo;
    Usuario usuarioSesion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    
    private void configurarTabla(){
        colTotalRecaudado.setCellValueFactory(new PropertyValueFactory("totalRecaudado"));
        colSemana.setCellValueFactory(new PropertyValueFactory("semana"));
        colMes.setCellValueFactory(new PropertyValueFactory("mes"));
        colAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        colTotalVentas.setCellValueFactory(new PropertyValueFactory("totalVentas"));
        
    }
    
    private void cargarVentasPorSemanaTabla(){
        try{
        ventas= FXCollections.observableArrayList();
        ArrayList<Venta> ventasDAO = VentaDAO.obtenerResumenVentasPorSemana();
        ventas.addAll(ventasDAO);
        tvVentas.setItems(ventas);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }
            
    }
    
    private void cargarVentasPorAnioTabla(){
        try{
        ventas= FXCollections.observableArrayList();
        ArrayList<Venta> ventasDAO = VentaDAO.obtenerResumenVentasPorAnio();
        ventas.addAll(ventasDAO);
        tvVentas.setItems(ventas);
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }
    
    private void cargarVentasPorMesTabla() {
        try {
            ventas= FXCollections.observableArrayList();
            ArrayList<Venta> ventasDAO = VentaDAO.obtenerResumenVentasPorMes();
            ventas.addAll(ventasDAO);
            tvVentas.setItems(ventas);
        } catch(SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Lo sentimos, por el momento no se puede cargar la información de las ventas, por favor intentélo más tarde");
            cerrarVentana();
        }     
    }

    private void cargarVentasPorProductoTabla() {
        try {
            ventas = FXCollections.observableArrayList();
            ArrayList<Venta> ventasDAO = VentaDAO.obtenerResumenVentasPorProducto();
            ventas.addAll(ventasDAO);
            tvVentas.setItems(ventas);
            configurarTablaProducto(); // solo columnas necesarias
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron cargar las ventas por producto.");
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tvVentas.getScene().getWindow()).close();
    }

    @FXML
    private void clicVentasPorSemana(ActionEvent event) {
        cargarVentasPorSemanaTabla();
    }

    @FXML
    private void clicVentasPorAnio(ActionEvent event) {
        cargarVentasPorAnioTabla();
    }

    @FXML
    private void clicVentasPorMes(ActionEvent event) {
        cargarVentasPorMesTabla();
    }

    @FXML
    private void clicVentasPorProducto(ActionEvent event) {
        cargarVentasPorProductoTabla();
    }
    
    @FXML
    private void clicRegresar(ActionEvent event) {
        cerrarVentana();
       
    }

    @FXML
    private void clicExportarExcel(ActionEvent event) {
       try {
       Stage stage = (Stage) tvVentas.getScene().getWindow();
       ExportarExcel.exportarExcelDesdeTableView(tvVentas, stage);
       Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Exportación exitosa", "El archivo ha sido exportado exitosamente");
    } catch (IOException ex) {
        ex.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de exportación", "No se ha podido exportar ek archivo");
    }
    }

    @FXML
    private void clicExportarPDF(ActionEvent event) {
        try {
        Stage stage = (Stage) tvVentas.getScene().getWindow();
        ExportarPDF.exportarPDFDesdeTableView(tvVentas, stage);
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Exportación exitosa", "El archivo ha sido exportado exitosamente");
    } catch (Exception ex) {
        ex.printStackTrace();
        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de exportación", "No se ha podido exportar ek archivo");

    }
    }

    private void agregarColumnaProducto() {
        if (colProducto == null) {
            colProducto = new TableColumn<>("Producto");
            colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        }

        if (!tvVentas.getColumns().contains(colProducto)) {
            tvVentas.getColumns().add(0, colProducto); // Puedes ajustar el índice si quieres otro orden
        }
    }    

}
