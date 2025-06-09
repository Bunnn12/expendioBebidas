/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/***
 *
 * @author reino
 */
public class ProductoDAO {
    
    public static List<Producto> obtenerTodosLosProductos() throws SQLException{
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if(conexionBD != null){
            String consulta= "SELECT idProducto, nombreProducto, precio, descripcion FROM producto"; 
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            
            while(resultado.next()){
               Producto producto = convertirRegistroProductoBasico(resultado);
               productos.add(producto);
            }
            
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }
        else{
             throw new SQLException("Sin conexion con la base de datos");
        }
        return productos;
    }
    
   public static Producto obtenerProductoMasVendido() throws SQLException{
        Producto productoMasVendido = null;
        Connection conexionBD = Conexion.abrirConexion();
        if(conexionBD != null){
            String consulta= "SELECT p.idProducto, p.nombreProducto, SUM(dv.cantidadProducto) AS totalVendido " +
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
            if(resultado.next()){
                productoMasVendido = convertirRegistroProducto(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }
        else{
             throw new SQLException("Sin conexion con la base de datos");
        }
        return productoMasVendido;
    }
    
    public static Producto obtenerProductoMenosVendido() throws SQLException {
        Producto productoMenosVendido = null;
        Connection conexionBD = Conexion.abrirConexion();
    
        if (conexionBD != null) {
            String consulta =
                "SELECT p.idProducto, p.nombreProducto, IFNULL(SUM(dv.cantidadProducto), 0) AS totalVendido " +
                "FROM producto p " +
                "LEFT JOIN detalleVenta dv ON p.idProducto = dv.Producto_idProducto " +
                "GROUP BY p.idProducto, p.nombreProducto " +
                "ORDER BY totalVendido ASC " +
                "LIMIT 1";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                productoMenosVendido = convertirRegistroProducto(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión con la base de datos");
        }

        return productoMenosVendido;
    }

    public static List<Producto> obtenerProductosNoVendidos(int idCliente) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null) {
        String consulta= 
                "SELECT p.nombreProducto, p.descripcion, p.precio " +
            "FROM producto p " +
            "WHERE p.idProducto NOT IN ( " +
            "    SELECT dv.Producto_idProducto " +
            "    FROM detalleventa dv " +
            "    WHERE dv.Venta_Cliente_idCliente = ? " +
            ");";;

        PreparedStatement stmt = conexionBD.prepareStatement(consulta);
        stmt.setInt(1, idCliente);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            productos.add(new Producto(
                rs.getString("nombreProducto"),
                rs.getString("descripcion"),
                rs.getDouble("precio")
            ));
            conexionBD.close();
        }
        }else{
            throw new SQLException("Sin conexión con la base de datos");
        }
        return productos;
    }

    public static Producto convertirRegistroProductoBasico(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setPrecio(resultado.getDouble("precio"));
        producto.setDescripcion(resultado.getString("descripcion"));
        return producto;
    }
    
    public static Producto convertirRegistroProducto(ResultSet resultado) throws SQLException{
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setTotalVendido(resultado.getInt("totalVendido"));
        return producto;
    }
    
}
