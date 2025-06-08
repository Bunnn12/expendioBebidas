/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.utilidades;

import javafx.scene.control.Alert;

/**
 *
 * @author reino
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido){
         Alert alerta = new Alert(tipo);
               alerta.setTitle(titulo);
               alerta.setHeaderText(null);
               alerta.setContentText(contenido);
               alerta.showAndWait();
    }
}
