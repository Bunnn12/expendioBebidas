/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.Promocion;
import expendiobebidas.utilidades.Utilidad;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;


/**
 *
 * @author acrca
 */
public class PromocionDAO {

    public static boolean insertarPromocion(Promocion promocion) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "INSERT INTO promocion (descripcion, descuento, fechaEmision, fechaVencimiento, "
                        + "Producto_idProducto, Cliente_idCliente) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setString(1, promocion.getDescripcion());
                    sentencia.setInt(2, promocion.getDescuento());
                    sentencia.setDate(3, Date.valueOf(promocion.getFechaEmision()));
                    sentencia.setDate(4, Date.valueOf(promocion.getFechaVencimiento()));
                    sentencia.setInt(5, promocion.getProducto().getIdProducto());
                    // Cliente_idCliente puede ser null
                    if (promocion.getCliente() != null) {
                        sentencia.setInt(6, promocion.getCliente().getIdCliente());
                    } else {
                        sentencia.setNull(6, java.sql.Types.INTEGER);
                    }
                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }

    public static List<Promocion> obtenerPromocionesDeCliente(int idCliente) throws SQLException {
        List<Promocion> promociones = new ArrayList<>();
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "SELECT p.*, prod.idProducto, prod.nombreProducto, prod.precio "
                        + "FROM promocion p "
                        + "INNER JOIN producto prod ON p.Producto_idProducto = prod.idProducto "
                        + "WHERE p.Cliente_idCliente = ? AND CURRENT_DATE BETWEEN p.fechaEmision AND p.fechaVencimiento";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setInt(1, idCliente);
                    try (ResultSet resultado = sentencia.executeQuery()) {
                        while (resultado.next()) {
                            promociones.add(convertirRegistroPromocion(resultado));
                        }
                    }
                }
            } else {
                throw new SQLException("Sin conexión a la Base de Datos");
            }
        }
        return promociones;
    }

    private static Promocion convertirRegistroPromocion(ResultSet resultado) throws SQLException {
        Promocion promocion = new Promocion();
        promocion.setIdPromocion(resultado.getInt("idPromocion"));
        promocion.setDescripcion(resultado.getString("descripcion"));
        promocion.setDescuento(resultado.getInt("descuento"));
        promocion.setFechaEmision(resultado.getDate("fechaEmision").toLocalDate().toString());
        promocion.setFechaVencimiento(resultado.getDate("fechaVencimiento").toLocalDate().toString());

        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setPrecio(resultado.getDouble("precio"));
        promocion.setProducto(producto);

        // Puede que Cliente_idCliente sea null
        int idCliente = resultado.getInt("Cliente_idCliente");
        if (!resultado.wasNull()) {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(idCliente);
            promocion.setCliente(cliente);
        } else {
            promocion.setCliente(null);
        }

        return promocion;
    }

    public static boolean eliminarPromocion(int idPromocion) throws SQLException {
    try (Connection conexionBD = Conexion.abrirConexion()) {
        if (conexionBD != null) {
            String consulta = "DELETE FROM promocion WHERE idPromocion = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idPromocion);
                int filasAfectadas = sentencia.executeUpdate();
                return filasAfectadas > 0;
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            return false;
        }
    }
}


    public static boolean actualizarPromocion(Promocion promocion) throws SQLException {
    try (Connection conexionBD = Conexion.abrirConexion()) {
        if (conexionBD != null) {
            String consulta = "UPDATE promocion SET descripcion = ?, descuento = ?, fechaEmision = ?, fechaVencimiento = ?, "
                    + "Producto_idProducto = ?, Cliente_idCliente = ? WHERE idPromocion = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setString(1, promocion.getDescripcion());
                sentencia.setInt(2, promocion.getDescuento());
                sentencia.setDate(3, Date.valueOf(promocion.getFechaEmision()));
                sentencia.setDate(4, Date.valueOf(promocion.getFechaVencimiento()));
                sentencia.setInt(5, promocion.getProducto().getIdProducto());

                if (promocion.getCliente() != null) {
                    sentencia.setInt(6, promocion.getCliente().getIdCliente());
                } else {
                    sentencia.setNull(6, java.sql.Types.INTEGER);
                }

                sentencia.setInt(7, promocion.getIdPromocion()); 

                int filasAfectadas = sentencia.executeUpdate();
                return filasAfectadas > 0;
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Conexión fallida", "No hay conexión a la base de datos.");
            return false;
        }
    }
}

    public static List<Promocion> obtenerPromociones() throws SQLException {
        List<Promocion> promociones = new ArrayList<>();
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "SELECT p.*, prod.idProducto, prod.nombreProducto, prod.precio "
                        + "FROM promocion p "
                        + "INNER JOIN producto prod ON p.Producto_idProducto = prod.idProducto";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                     ResultSet resultado = sentencia.executeQuery()) {
                    while (resultado.next()) {
                        promociones.add(convertirRegistroPromocion(resultado));
                    }
                }
            } else {
                throw new SQLException("Sin conexión a la Base de Datos");
            }
        }
        return promociones;
    }

    public static List<Promocion> buscarPromocionPorNombre(String nombre) throws SQLException {
        List<Promocion> promociones = new ArrayList<>();
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "SELECT p.*, prod.idProducto, prod.nombreProducto, prod.precio "
                        + "FROM promocion p "
                        + "INNER JOIN producto prod ON p.Producto_idProducto = prod.idProducto "
                        + "WHERE p.descripcion LIKE ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setString(1, "%" + nombre + "%");
                    try (ResultSet resultado = sentencia.executeQuery()) {
                        while (resultado.next()) {
                            promociones.add(convertirRegistroPromocion(resultado));
                        }
                    }
                }
            } else {
                throw new SQLException("Sin conexión a la Base de Datos");
            }
        }
        return promociones;
    }

}