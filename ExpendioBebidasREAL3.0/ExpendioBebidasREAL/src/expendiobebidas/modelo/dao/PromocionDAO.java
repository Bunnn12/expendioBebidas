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
        Connection conexionBD = Conexion.abrirConexion();
        PreparedStatement sentencia = null;

        if (conexionBD != null) {
            String consulta = "INSERT INTO promocion (descripcion, descuento, fechaEmision, fechaVencimiento, "
                              + "Producto_idProducto, Cliente_idCliente) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setString(1, promocion.getDescripcion());
                sentencia.setInt(2, promocion.getDescuento());
                sentencia.setDate(3, java.sql.Date.valueOf(promocion.getFechaEmision()));
                sentencia.setDate(4, java.sql.Date.valueOf(promocion.getFechaVencimiento()));
                sentencia.setInt(5, promocion.getProducto().getIdProducto());
                sentencia.setInt(6, promocion.getCliente().getIdCliente());
                
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
      
    public static List<Promocion> obtenerPromocionesDeCliente(int idCliente) throws SQLException{
        List<Promocion> promociones = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT p.*, prod.idProducto, prod.nombreProducto, prod.precio " +
                              "FROM promocion p " +                          
                              "INNER JOIN producto prod ON p.Producto_idProducto = prod.idProducto " +
                              "WHERE p.Cliente_idCliente = ? AND CURRENT_DATE BETWEEN p.fechaEmision AND p.fechaVencimiento";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idCliente);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                promociones.add(convertirRegistroPromocion(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return promociones;
    }
    
    private static Promocion convertirRegistroPromocion(ResultSet resultado) throws SQLException{
        
        Promocion promocion = new Promocion();
        promocion.setIdPromocion(resultado.getInt("idPromocion"));
        promocion.setDescripcion(resultado.getString("descripcion"));
        promocion.setDescuento(resultado.getInt("descuento"));
        promocion.setFechaEmision(resultado.getDate("fechaEmision").toString());
        promocion.setFechaVencimiento(resultado.getDate("fechaVencimiento").toString());
        
        //Crea el producto relacionado
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombreProducto(resultado.getString("nombreProducto"));
        producto.setPrecio(resultado.getDouble("precio"));
        promocion.setProducto(producto);

        return promocion;
        
    }
    
}
