/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author reino
 */
public class VentaDAO {
    public static ArrayList<Venta> obtenerVentas() throws SQLException{
    ArrayList<Venta> ventas= new ArrayList<>();
    Connection conexionBD= Conexion.abrirConexion();
    if (conexionBD != null){
        String consulta= 
        "SELECT " +
        "v.idVenta, " +
        "v.fechaVenta, " +
        "v.Cliente_idCliente AS idCliente, " +
        "v.folioFactura, " +
        "c.razonSocial, " +
        "dv.Producto_idProducto AS idProducto, " +
        "p.nombreProducto, " +
        "dv.cantidadProducto, " +
        "dv.costoVenta " +
        "FROM venta v " +
        "JOIN cliente c ON v.Cliente_idCliente = c.idCliente " +
        "JOIN detalleVenta dv ON v.idVenta = dv.Venta_idVenta " +
        "AND v.Cliente_idCliente = dv.Venta_Cliente_idCliente " +
        "JOIN producto p ON dv.Producto_idProducto = p.idProducto;";
        PreparedStatement sentencia= conexionBD.prepareStatement(consulta);
        ResultSet resultado= sentencia.executeQuery();
        while(resultado.next()){
            ventas.add(convertirRegistroVenta(resultado));
        }
        resultado.close();
        sentencia.close();
        conexionBD.close();
    }else{
        throw new SQLException("Sin conexion con la base de datos");
    }
    
    return ventas;
}
    
            
private static Venta convertirRegistroVenta(ResultSet resultado) throws SQLException{
        Venta venta= new Venta();
        venta.setIdVenta(resultado.getInt("idVenta"));
        venta.setIdCliente(resultado.getInt("idCliente"));
        venta.setRazonSocial(resultado.getString("razonSocial"));
        venta.setFechaVenta(resultado.getString("fechaVenta"));
        venta.setFolioFactura(resultado.getString("folioFactura"));
        venta.setIdProducto(resultado.getInt("idProducto"));
        venta.setNombreProducto(resultado.getString("nombreProducto"));
        venta.setCantidadProducto(resultado.getInt("cantidadProducto"));
        return venta;
}

public static ArrayList<Venta> obtenerResumenVentasPorSemana() throws SQLException {
    ArrayList<Venta> resumen = new ArrayList<>();
    Connection conexionBD = Conexion.abrirConexion();

    if (conexionBD != null) {
        String consulta =
            "SELECT YEAR(v.fechaVenta) AS anio, " +
            "WEEK(v.fechaVenta, 1) AS semana, " +
            "COUNT(DISTINCT v.idVenta) AS totalVentas, " +
            "SUM(dv.costoVenta) AS totalRecaudado " +
            "FROM venta v " +
            "JOIN detalleVenta dv ON v.idVenta = dv.Venta_idVenta " +
            "AND v.Cliente_idCliente = dv.Venta_Cliente_idCliente " +
            "GROUP BY anio, semana " +
            "ORDER BY anio DESC, semana DESC;";

        PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        while (resultado.next()) {
            resumen.add(convertirRegistroVentasPorSemana(resultado));
        }

        resultado.close();
        sentencia.close();
        conexionBD.close();
    } else {
        throw new SQLException("Sin conexión a la base de datos");
    }

    return resumen;
}

 private static Venta convertirRegistroVentasPorSemana(ResultSet resultado) throws SQLException{
     Venta resumenVenta = new Venta();
        resumenVenta.setAnio(resultado.getInt("anio"));
        resumenVenta.setSemana(resultado.getInt("semana"));
        resumenVenta.setTotalVentas(resultado.getInt("totalVentas"));
        resumenVenta.setTotalRecaudado(resultado.getDouble("totalRecaudado")); 
            return resumenVenta;
}
    
    public static ArrayList<Venta> obtenerResumenVentasPorAnio() throws SQLException {
       ArrayList<Venta> resumen = new ArrayList<>();
       Connection conexionBD = Conexion.abrirConexion();

       if (conexionBD != null) {
           String consulta =
               "SELECT YEAR(v.fechaVenta) AS anio, " +
               "COUNT(DISTINCT v.idVenta) AS totalVentas, " +
               "SUM(dv.costoVenta) AS totalRecaudado " +
               "FROM venta v " +
               "JOIN detalleVenta dv ON v.idVenta = dv.Venta_idVenta " +
               "AND v.Cliente_idCliente = dv.Venta_Cliente_idCliente " +
               "GROUP BY anio " +
               "ORDER BY anio DESC;";

           PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
           ResultSet resultado = sentencia.executeQuery();

           while (resultado.next()) {
               resumen.add(convertirRegistroVentasPorAnio(resultado));
           }

           resultado.close();
           sentencia.close();
           conexionBD.close();
       } else {
           throw new SQLException("Sin conexión a la base de datos");
       }

       return resumen;
    }
    
    private static Venta convertirRegistroVentasPorAnio(ResultSet resultado) throws SQLException{
     Venta resumenVenta = new Venta();
        resumenVenta.setAnio(resultado.getInt("anio"));
        resumenVenta.setTotalVentas(resultado.getInt("totalVentas"));
        resumenVenta.setTotalRecaudado(resultado.getDouble("totalRecaudado")); 
            return resumenVenta;
    }
    
    public static ArrayList<Venta> obtenerResumenVentasPorMes() throws SQLException {
       ArrayList<Venta> resumen = new ArrayList<>();
       Connection conexionBD = Conexion.abrirConexion();

       if (conexionBD != null) {
           String consulta =
               "SELECT YEAR(v.fechaVenta) AS anio, " +
               "MONTH(v.fechaVenta) AS mes, " +
               "COUNT(DISTINCT v.idVenta) AS totalVentas, " +
               "SUM(dv.costoVenta) AS totalRecaudado " +
               "FROM venta v " +
               "JOIN detalleVenta dv ON v.idVenta = dv.Venta_idVenta " +
               "AND v.Cliente_idCliente = dv.Venta_Cliente_idCliente " +
               "GROUP BY anio, mes " +
               "ORDER BY anio DESC, mes DESC;";

           PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
           ResultSet resultado = sentencia.executeQuery();

           while (resultado.next()) {
               resumen.add(convertirRegistroVentasPorMes(resultado));
           }

           resultado.close();
           sentencia.close();
           conexionBD.close();
       } else {
           throw new SQLException("Sin conexión a la base de datos");
       }

       return resumen;
    }
    
    private static Venta convertirRegistroVentasPorMes(ResultSet resultado) throws SQLException{
     Venta resumenVenta = new Venta();
        resumenVenta.setAnio(resultado.getInt("anio"));
        resumenVenta.setMes(resultado.getInt("mes"));
        resumenVenta.setTotalVentas(resultado.getInt("totalVentas"));
        resumenVenta.setTotalRecaudado(resultado.getDouble("totalRecaudado")); 
            return resumenVenta;
    }
  
}
