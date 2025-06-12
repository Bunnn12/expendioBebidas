/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Compra;
import expendiobebidas.modelo.dao.pojo.DetalleCompra;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author reino
 */
public class CompraDAO {
     public static boolean realizarCompraConTransaccion(int idPedido, String folioFactura, double costoTotalCompra, int cantidadProducto, double costoCompra, int idProveedor) throws SQLException {
    Connection conexion = null;
    PreparedStatement psActualizarPedido = null;
    PreparedStatement psInsertarCompra = null;
    PreparedStatement psActualizarStock = null;
    ResultSet rsProductosPedido = null;

    try {
        conexion = Conexion.abrirConexion();
        conexion.setAutoCommit(false);

        // 1. Actualizar pedido
        String sqlActualizarPedido = "UPDATE pedido SET fechaCompra = CURRENT_DATE, folioFactura = ?, estado = 'Comprado' WHERE idPedido = ?";
        psActualizarPedido = conexion.prepareStatement(sqlActualizarPedido);
        psActualizarPedido.setString(1, folioFactura);
        psActualizarPedido.setInt(2, idPedido);

        int filasPedido = psActualizarPedido.executeUpdate();
        if (filasPedido != 1) {
            conexion.rollback();
            return false;
        }

        // 2. Insertar en tabla compra con fechaCompra = CURRENT_DATE
        String sqlInsertarCompra = "INSERT INTO compra (Pedido_idPedido, fechaCompra, folioFactura, costoTotalCompra, cantidadProducto, costoCompra, Proveedor_idProveedor1) "
                                 + "VALUES (?, CURRENT_DATE, ?, ?, ?, ?, ?)";
        psInsertarCompra = conexion.prepareStatement(sqlInsertarCompra);
        psInsertarCompra.setInt(1, idPedido);
        psInsertarCompra.setString(2, folioFactura);
        psInsertarCompra.setDouble(3, costoTotalCompra);
        psInsertarCompra.setInt(4, cantidadProducto);
        psInsertarCompra.setDouble(5, costoCompra);
        psInsertarCompra.setInt(6, idProveedor);

        int filasCompra = psInsertarCompra.executeUpdate();
        if (filasCompra != 1) {
            conexion.rollback();
            return false;
        }

        // 3. Actualizar stock
        String sqlProductosPedido = "SELECT idProducto, cantidad FROM producto_pedido WHERE idPedido = ?";
        try (PreparedStatement psProductosPedido = conexion.prepareStatement(sqlProductosPedido)) {
            psProductosPedido.setInt(1, idPedido);
            rsProductosPedido = psProductosPedido.executeQuery();

            String sqlActualizarStockStr = "UPDATE producto SET stock = stock + ? WHERE idProducto = ?";
            psActualizarStock = conexion.prepareStatement(sqlActualizarStockStr);

            while (rsProductosPedido.next()) {
                int idProducto = rsProductosPedido.getInt("idProducto");
                int cantidad = rsProductosPedido.getInt("cantidad");

                psActualizarStock.setInt(1, cantidad);
                psActualizarStock.setInt(2, idProducto);

                int filasStock = psActualizarStock.executeUpdate();
                if (filasStock != 1) {
                    conexion.rollback();
                    return false;
                }
            }
        }

        conexion.commit();
        return true;

    } catch (SQLException e) {
        if (conexion != null) {
            conexion.rollback();
        }
        throw e;
    } finally {
        if (rsProductosPedido != null) rsProductosPedido.close();
        if (psActualizarPedido != null) psActualizarPedido.close();
        if (psInsertarCompra != null) psInsertarCompra.close();
        if (psActualizarStock != null) psActualizarStock.close();
        if (conexion != null) {
            conexion.setAutoCommit(true);
            conexion.close();
        }
    }
}

}



