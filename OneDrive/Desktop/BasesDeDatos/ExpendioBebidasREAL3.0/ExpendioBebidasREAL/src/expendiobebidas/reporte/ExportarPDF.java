/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.reporte;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import expendiobebidas.modelo.dao.pojo.Venta;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author reino
 */
public class ExportarPDF {
    public static void exportarDesdeTableView(TableView<Venta> tableView, Stage stage) throws IOException, DocumentException {
        // Crear documento PDF
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // A4 horizontal
        // Selector para guardar archivo PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        java.io.File archivo = fileChooser.showSaveDialog(stage);

        if (archivo == null) {
            return; // Usuario canceló
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            // Fuente para la tabla y títulos
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontCell = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Crear tabla con número de columnas visibles
            int columnasVisibles = (int) tableView.getColumns().stream().filter(TableColumn::isVisible).count();
            PdfPTable pdfTable = new PdfPTable(columnasVisibles);
            pdfTable.setWidthPercentage(100);

            // Agregar encabezados
            for (TableColumn<Venta, ?> col : tableView.getColumns()) {
                if (col.isVisible()) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(col.getText(), fontHeader));
                    headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(headerCell);
                }
            }

            // Agregar filas
            for (Venta v : tableView.getItems()) {
                for (TableColumn<Venta, ?> col : tableView.getColumns()) {
                    if (col.isVisible()) {
                        Object value = col.getCellData(v);
                        String text = value != null ? value.toString() : "";
                        PdfPCell cell = new PdfPCell(new Phrase(text, fontCell));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfTable.addCell(cell);
                    }
                }
            }

            document.add(pdfTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error al exportar PDF: " + e.getMessage());
        }
    }
}
