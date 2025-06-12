/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.utilidades.Utilidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

/***
 *
 * @author reino
 */
public class ProductoDAO {

    public static List<Producto> obtenerTodosLosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT idProducto, nombreProducto, precio, descripcion FROM producto";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Producto producto = convertirRegistroProductoBasico(resultado);
                productos.add(producto);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion con la base de datos");
        }
        return productos;
    }

    public static Producto obtenerProductoMasVendido() throws SQLException {
        Producto productoMasVendido = null;
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT p.idProducto, p.nombreProducto, SUM(dv.cantidadProducto) AS totalVendido " +
                    "FROM producto p " +
                    "JOIN detalleVenta dv ON p.idProducto = dv.Producto_idProducto " +
                    "GROUP BY p.idProducto, p.nombreProducto " +
                    "HAVING SUM(dv.cantidadProducto) = ( " +
                    "    SELECT MAX(totalVendido) FROM ( " +
                    "        SELECT SUM(dv2.cantidadProducto) AS totalVendido " +
                    "        FROM producto p2 " +
                    "        JOIN detalleVenta dv2 ON p2.idProducto = dv2.Producto_idProducto " +
                    "        GROUP BY p2.idProducto, p2.nombreProducto " +
                    "    ) AS subquery " +
                    ")";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                productoMasVendido = convertirRegistroProducto(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion con la base de datos");
        }
        return productoMasVendido;
    }
    public static ArrayList<Producto> obtenerProductos() throws SQLException {
        ArrayList<Producto> productos = new ArrayList();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idProducto, nombreProducto, descripcion, stockMinimo, stockActual, precio FROM producto";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                productos.add(convertirRegistroProductoCompleto(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return productos;
    }

    public static boolean insertarProducto(Producto producto) throws SQLException {
        Connection conexionBD = Conexion.abrirConexion();
        PreparedStatement sentencia = null;
    
        if (conexionBD != null) {
            String consulta = "INSERT INTO producto (nombreProducto, descripcion, stockMinimo, stockActual, precio) VALUES (?, ?, ?, ?, ?)";
            try {
                sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setString(1, producto.getNombreProducto());
                sentencia.setString(2, producto.getDescripcion());
                sentencia.setInt(3, producto.getStockMinimo());
                sentencia.setInt(4, producto.getStockActual());
                sentencia.setDouble(5, producto.getPrecio());

                int filasAfectadas = sentencia.executeUpdate();
                return filasAfectadas > 0;
            } finally {
                if (sentencia != null) {
                    sentencia.close();
                }
                conexionBD.close(); 
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
            return false;
        }
    }
    public static boolean eliminarProducto(int idProducto) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "DELETE FROM producto WHERE idProducto = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setInt(1, idProducto);
                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
    public static boolean actualizarProducto(Producto producto) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "UPDATE producto SET nombreProducto = ?, descripcion = ?, "
                        + "stockMinimo  = ?, stockActual = ?, precio = ? WHERE idProducto = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setString(1, producto.getNombreProducto());
                    sentencia.setString(2, producto.getDescripcion());
                    sentencia.setInt(3, producto.getStockMinimo());
                    sentencia.setInt(4, producto.getStockActual());
                    sentencia.setDouble(5, producto.getPrecio());
                    sentencia.setInt(6, producto.getIdProducto());

                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
    public static List<Producto> obtenerProductoMenosVendido() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta =
                    "SELECT p.idProducto, p.nombreProducto, IFNULL(SUM(dv.cantidadProducto), 0) AS totalVendido " +
                    "FROM producto p " +
                    "LEFT JOIN detalleVenta dv ON p.idProducto = dv.Producto_idProducto " +
                    "GROUP BY p.idProducto, p.nombreProducto " +
                    "HAVING totalVendido = ( " +
                    "    SELECT MIN(totalVendidos) FROM ( " +
                    "        SELECT p2.idProducto, IFNULL(SUM(dv2.cantidadProducto), 0) AS totalVendidos " +
                    "        FROM producto p2 " +
                    "        LEFT JOIN detalleVenta dv2 ON p2.idProducto = dv2.Producto_idProducto " +
                    "        GROUP BY p2.idProducto " +
                    "    ) AS subconsulta " +
                    ")";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Producto producto = convertirRegistroProducto(resultado);
                lista.add(producto);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return lista;
    }

    public static List<Producto> obtenerProductosNoVendidos(int idCliente) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
            String consulta =
                    "SELECT p.nombreProducto, p.descripcion, p.precio " +
                    "FROM producto p " +
                    "WHERE p.idProducto NOT IN ( " +
                    "    SELECT dv.Producto_idProducto " +
                    "    FROM detalleventa dv " +
                    "    WHERE dv.Venta_Cliente_idCliente = ? " +
                    ")";
            PreparedStatement stmt = conexionBD.prepareStatement(consulta);
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getString("nombreProducto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio")
                ));
            }
            rs.close();
            stmt.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }
        return productos;
    }

    public static List<Producto> obtenerProductosConStockMinimo() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idProducto, nombreProducto, stockMinimo, stockActual FROM producto WHERE stockActual <= stockMinimo";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getInt("idProducto"));
                producto.setNombreProducto(resultado.getString("nombreProducto"));
                producto.setStockMinimo(resultado.getInt("stockMinimo"));
                producto.setStockActual(resultado.getInt("stockActual"));
                lista.add(producto);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return lista;
    }

    public static Producto convertirRegistroProductoBasico(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setPrecio(resultado.getDouble("precio"));
        producto.setDescripcion(resultado.getString("descripcion"));
        return producto;
    }

    public static Producto convertirRegistroProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setTotalVendido(resultado.getInt("totalVendido"));
        return producto;
    }
    
   public static Producto convertirRegistroProductoCompleto(ResultSet resultado) throws SQLException {
    Producto producto = new Producto();
    producto.setIdProducto(resultado.getInt("idProducto"));
    producto.setNombreProducto(resultado.getString("nombreProducto"));
    producto.setDescripcion(resultado.getString("descripcion"));
    producto.setStockMinimo(resultado.getInt("stockMinimo"));
    producto.setStockActual(resultado.getInt("stockActual"));
    producto.setPrecio(resultado.getDouble("precio"));
    return producto;
}

    public static List<Producto> buscarProductoPorNombre(String nombre) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idProducto, nombreProducto, precio, descripcion " +
                    "FROM producto WHERE nombreProducto LIKE ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, "%" + nombre + "%");
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Producto producto = convertirRegistroProductoBasico(resultado);
                productos.add(producto);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return productos;
    }

    public static List<Producto> obtenerProductosPorProveedor(int idProveedor) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT idProducto, nombreProducto, precio, descripcion, stockActual, stockMinimo " +
                    "FROM producto WHERE idProveedor = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idProveedor);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getInt("idProducto"));
                producto.setNombreProducto(resultado.getString("nombreProducto"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setDescripcion(resultado.getString("descripcion"));
                producto.setStockActual(resultado.getInt("stockActual"));
                producto.setStockMinimo(resultado.getInt("stockMinimo"));
                productos.add(producto);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return productos;
    }
}



