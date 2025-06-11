/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendiobebidas.controlador;

import expendiobebidas.ExpendioBebidas;
import expendiobebidas.utilidades.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author reino
 */
public class FXMLRealizarPedidoPorEmpleadoController implements Initializable {

    @FXML
    private Label lbTitulo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicRegresar(ActionEvent event) {
        Stage stageActual = (Stage) lbTitulo.getScene().getWindow();
        stageActual.close();
    }

    @FXML
    private void clickRealizarPedidoDeCliente(ActionEvent event) {
         try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLPedidoDeCliente.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar pedido de clentes");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "Lo sentimos, por el momento no se puede mostrar la ventana");
        }
    }

    @FXML
    private void clickRealizarPedidoDeProductos(ActionEvent event) {
        try{
        Stage escenarioAdmin = new Stage();
        Parent vista = FXMLLoader.load(ExpendioBebidas.class.getResource("vista/FXMLPedidoDeProductos.fxml"));
        Scene escena= new Scene(vista);
        escenarioAdmin.setScene(escena);
        escenarioAdmin.setTitle("Realizar pedido de productos");
        escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
        escenarioAdmin.showAndWait();
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "Lo sentimos, por el momento no se puede mostrar la ventana");
        }
    }
    
}
