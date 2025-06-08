/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author acrca
 */
public class VentasProductoClienteDAO {
    
    public static ArrayList<Producto> obtenerProductoMasVendidoCliente(int idCliente) throws SQLException{
        ArrayList<Producto> productos = new ArrayList();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idProducto, nombreProducto, cantidadVendida " + 
                              "FROM ( " + 
                              "SELECT p.idProducto, p.nombreProducto, SUM(dv.cantidadProducto) AS cantidadVendida " +
                              "FROM producto p " + 
                              "JOIN detalleVenta dv ON p.idProducto = dv.Producto_idProducto " +
                              "WHERE dv.Venta_Cliente_idCliente = ? " + 
                              "GROUP BY p.idProducto, p.nombreProducto " +
                              ") AS ventasCliente " + 
                              "WHERE cantidadVendida = ( " + 
                              "SELECT MAX(sub.cantidadTotal) " +
                              "FROM ( " + 
                              "SELECT SUM(dv2.cantidadProducto) AS cantidadTotal " + 
                              "FROM detalleVenta dv2 " + 
                              "WHERE dv2.Venta_Cliente_idCliente = ? " + 
                              "GROUP BY dv2.Producto_idProducto " + 
                              ") AS sub " +
                              ")";
                   
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idCliente);
            sentencia.setInt(2, idCliente);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                productos.add(convertirRegistroProducto(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return productos;
    }
    
    private static Producto convertirRegistroProducto(ResultSet resultado) throws SQLException{
        
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setTotalVendido(resultado.getInt("cantidadVendida"));
        
        return producto;
        
    }
    
}
