/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;
import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author reino
 */
public class PedidoDAO {
    public static boolean insertarPedidoDeCliente(int idCliente, List<ProductoPedido> productosPedido) throws SQLException {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El idCliente debe ser mayor que 0");
        }
        if (productosPedido == null || productosPedido.isEmpty()) {
            throw new IllegalArgumentException("La lista de productos no puede estar vacía");
        }

        Connection conn = Conexion.abrirConexion();
        if (conn == null) throw new SQLException("Sin conexión a la base de datos");

        boolean exito = false;
        conn.setAutoCommit(false);
        try {
            String sqlPedido = "INSERT INTO pedidoCliente(fechaPedidoCliente, Cliente_idCliente) VALUES (CURDATE(), ?)";
            PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, idCliente);
            psPedido.executeUpdate();

            ResultSet rs = psPedido.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se generó idPedidoCliente");
            int idPedido = rs.getInt(1);
            rs.close();
            psPedido.close();

            String sqlDetalle = "INSERT INTO detallePedidoCliente (PedidoCliente_idPedidoCliente, PedidoCliente_Cliente_idCliente, Producto_idProducto, cantidadSolicitada) VALUES (?, ?, ?, ?)";
            PreparedStatement psDet = conn.prepareStatement(sqlDetalle);

            for (ProductoPedido pp : productosPedido) {
                psDet.setInt(1, idPedido);                    // idPedidoCliente
                psDet.setInt(2, idCliente);                   // Cliente_idCliente
                psDet.setInt(3, pp.getProducto().getIdProducto()); // idProducto
                psDet.setInt(4, pp.getCantidad());            // cantidad solicitada
                psDet.addBatch();
            }

            psDet.executeBatch();
            psDet.close();

            conn.commit();
            exito = true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }

        return exito;
    }



}
