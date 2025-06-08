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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author reino
 */
public class ExportarExcel {
    
    public static void exportarExcelDesdeTableView(TableView<Venta> tableView, Stage stage) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");

        // Crear estilo de título
        CellStyle estiloTitulo = workbook.createCellStyle();
        Font fuenteTitulo = workbook.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 16);
        estiloTitulo.setFont(fuenteTitulo);
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);

        // Crear fila para el título
        Row tituloRow = sheet.createRow(0);
        Cell tituloCell = tituloRow.createCell(0);
        tituloCell.setCellValue("Reporte de Ventas");
        tituloCell.setCellStyle(estiloTitulo);

        // Combinar celdas para el título
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, tableView.getColumns().size() - 1));

        // Crear estilo para encabezados
        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fuenteEncabezado = workbook.createFont();
        fuenteEncabezado.setBold(true);
        estiloEncabezado.setFont(fuenteEncabezado);

        // Crear fila de encabezados en la fila 1
        Row headerRow = sheet.createRow(1);
        int colNum = 0;
        for (TableColumn<Venta, ?> col : tableView.getColumns()) {
            if (col.isVisible()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(col.getText());
                cell.setCellStyle(estiloEncabezado);
            }
        }

        // Agregar filas con datos
        int rowNum = 2;
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

        // Autoajustar columnas
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar archivo
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
