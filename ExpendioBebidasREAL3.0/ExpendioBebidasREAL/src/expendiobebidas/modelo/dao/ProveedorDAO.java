/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Proveedor;
import expendiobebidas.utilidades.Utilidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author reino
 */
public class ProveedorDAO {
     public static ArrayList<Proveedor> obtenerProveedores() throws SQLException{
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idProveedor, razonSocial, telefono, correo, direccion FROM proveedor";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                proveedores.add(convertirRegistroProveedores(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return proveedores;
        
    }
    
    private static Proveedor convertirRegistroProveedores(ResultSet resultado) throws SQLException{
        
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(resultado.getInt("idProveedor"));
        proveedor.setRazonSocial(resultado.getString("razonSocial"));
        proveedor.setTelefono(resultado.getString("telefono"));
        proveedor.setCorreo(resultado.getString("correo"));
        proveedor.setDireccion(resultado.getString("direccion"));

        return proveedor;
    }
        
    public static boolean insertarProveedor(Proveedor proveedor) throws SQLException {
        Connection conexionBD = Conexion.abrirConexion();
        PreparedStatement sentencia = null;

        if (conexionBD != null) {
            String consulta = "INSERT INTO proveedor (razonSocial, telefono, correo, direccion) VALUES (?, ?, ?, ?)";
            try {
                sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setString(1, proveedor.getRazonSocial());
                sentencia.setString(2, proveedor.getTelefono());
                sentencia.setString(3, proveedor.getCorreo());
                sentencia.setString(4, proveedor.getDireccion());

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

     public static boolean actualizarProveedor(Proveedor proveedor) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "UPDATE proveedor SET razonSocial = ?, telefono = ?, correo = ?, direccion = ? WHERE idProveedor = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setString(1, proveedor.getRazonSocial());
                    sentencia.setString(2, proveedor.getTelefono());
                    sentencia.setString(3, proveedor.getCorreo());
                    sentencia.setString(4, proveedor.getDireccion());
                    sentencia.setInt(5, proveedor.getIdProveedor());

                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
     
    public static boolean eliminarProveedor(int idProveedor) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "DELETE FROM proveedor WHERE idProveedor = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setInt(1, idProveedor);
                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
    
    public static boolean tieneProductosAsociados(int idProveedor) {
        String query = "SELECT COUNT(*) FROM producto WHERE idProveedor = ?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idProveedor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
