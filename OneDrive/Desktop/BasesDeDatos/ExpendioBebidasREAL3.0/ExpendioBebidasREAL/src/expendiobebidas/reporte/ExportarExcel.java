/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.reporte;

import expendiobebidas.modelo.dao.pojo.Venta;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author reino
 */
public class ExportarExcel {
    
    public static void exportarDesdeTableView(TableView<Venta> tableView, Stage stage) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");

        // Crear fila encabezados con columnas visibles
        Row headerRow = sheet.createRow(0);
        int colNum = 0;
        for (TableColumn<Venta, ?> col : tableView.getColumns()) {
            if (col.isVisible()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(col.getText());
            }
        }

        // Agregar filas con datos del TableView
        int rowNum = 1;
        for (Venta v : tableView.getItems()) {
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            for (TableColumn<Venta, ?> col : tableView.getColumns()) {
                if (col.isVisible()) {
                    Object cellValue = col.getCellData(v);
                    String valor = cellValue != null ? cellValue.toString() : "";
                    row.createCell(colNum++).setCellValue(valor);
                }
            }
        }

        // Auto ajustar columnas
        for (int i = 0; i < colNum; i++) {
            sheet.autoSizeColumn(i);
        }

        // Selector para guardar archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        java.io.File archivo = fileChooser.showSaveDialog(stage);

        if (archivo != null) {
            try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
                workbook.write(fileOut);
            }
        }

        workbook.close();
    }
}
