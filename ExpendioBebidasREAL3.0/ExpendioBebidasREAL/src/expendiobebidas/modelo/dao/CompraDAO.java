/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Compra;
import expendiobebidas.modelo.dao.pojo.DetalleCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author reino
 */
public class CompraDAO {
    public static boolean realizarCompra(Compra compra, List<DetalleCompra> detalles) throws SQLException {
    boolean exito = false;
    Connection conn = Conexion.abrirConexion();
    try {
        conn.setAutoCommit(false);

        // Insertar compra
        String insertCompra = "INSERT INTO compra (fechaCompra, folioFactura, Proveedor_idProveedor) VALUES (?, ?, ?)";
        PreparedStatement psCompra = conn.prepareStatement(insertCompra, Statement.RETURN_GENERATED_KEYS);
        psCompra.setDate(1, java.sql.Date.valueOf(compra.getFechaCompra()));
        psCompra.setString(2, compra.getFolioFactura());
        psCompra.setInt(3, compra.getProveedor().getIdProveedor());
        psCompra.executeUpdate();

        ResultSet rs = psCompra.getGeneratedKeys();
        int idCompra = -1;
        if (rs.next()) idCompra = rs.getInt(1);

        for (DetalleCompra d : detalles) {
            // Insertar detallecompra
            String insertDetalle = "INSERT INTO detallecompra (Compra_idCompra, Producto_idProducto, cantidad, precioUnitario) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetalle = conn.prepareStatement(insertDetalle);
            psDetalle.setInt(1, idCompra);
            psDetalle.setInt(2, d.getProducto().getIdProducto());
            psDetalle.setInt(3, d.getCantidad());
            psDetalle.setDouble(4, d.getPrecioUnitario());
            psDetalle.executeUpdate();

            // Aumentar stock
            String updateStock = "UPDATE producto SET stockActual = stockActual + ? WHERE idProducto = ?";
            PreparedStatement psStock = conn.prepareStatement(updateStock);
            psStock.setInt(1, d.getCantidad());
            psStock.setInt(2, d.getProducto().getIdProducto());
            psStock.executeUpdate();

            // Eliminar de detallepedido
            String eliminarDetallePedido = "DELETE FROM detallepedido WHERE Pedido_idPedido = ? AND Producto_idProducto = ?";
            PreparedStatement psEliminar = conn.prepareStatement(eliminarDetallePedido);
            psEliminar.setInt(1, compra.getPedido().getIdPedido());
            psEliminar.setInt(2, d.getProducto().getIdProducto());
            psEliminar.executeUpdate();
        }

        // Verificar si aún quedan productos en el pedido
        String verificar = "SELECT COUNT(*) FROM detallepedido WHERE Pedido_idPedido = ?";
        PreparedStatement psVerificar = conn.prepareStatement(verificar);
        psVerificar.setInt(1, compra.getPedido().getIdPedido());
        ResultSet rsVerificar = psVerificar.executeQuery();
        if (rsVerificar.next() && rsVerificar.getInt(1) == 0) {
            // Marcar el pedido como atendido
            String actualizarPedido = "UPDATE pedido SET estado = 'Comprado' WHERE idPedido = ?";
            PreparedStatement psActualizar = conn.prepareStatement(actualizarPedido);
            psActualizar.setInt(1, compra.getPedido().getIdPedido());
            psActualizar.executeUpdate();
        }

        conn.commit();
        exito = true;
    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
        conn.close();
    }
    return exito;
}

}
