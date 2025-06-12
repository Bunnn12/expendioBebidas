/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reino
 */
public class DetallePedidoDAO {
    public static List<DetallePedido> obtenerDetallesPorPedido(int idPedido) throws SQLException {
    List<DetallePedido> lista = new ArrayList<>();
    Connection conn = Conexion.abrirConexion();

    if (conn != null) {
        String consulta = "SELECT Pedido_idPedido, Producto_idProducto, cantidadPedida " +
                          "FROM detallePedido WHERE Pedido_idPedido = ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(rs.getInt("Pedido_idPedido"));
                    detalle.setIdProducto(rs.getInt("Producto_idProducto"));
                    detalle.setCantidadPedida(rs.getInt("cantidadPedida"));
                    lista.add(detalle);
                }
            }
        } finally {
            conn.close();
        }
    } else {
        throw new SQLException("Sin conexión a la base de datos");
    }

    return lista;
}

}
