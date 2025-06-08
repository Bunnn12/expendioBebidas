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
    public static void exportarPDFDesdeTableView(TableView<Venta> tableView, Stage stage) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        java.io.File archivo = fileChooser.showSaveDialog(stage);

        if (archivo == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            // Título
            Font tituloFont = FontFactory.getFont("Georgia", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Ventas", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Fuente para encabezados y celdas
            Font fontHeader = FontFactory.getFont("Georgia", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD);
            Font fontCell = FontFactory.getFont("Georgia", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);

            // Crear tabla
            int columnasVisibles = (int) tableView.getColumns().stream().filter(TableColumn::isVisible).count();
            PdfPTable pdfTable = new PdfPTable(columnasVisibles);
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10);

            // Encabezados
            for (TableColumn<Venta, ?> col : tableView.getColumns()) {
                if (col.isVisible()) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(col.getText(), fontHeader));
                    headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(headerCell);
                }
            }

            // Celdas de datos
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
