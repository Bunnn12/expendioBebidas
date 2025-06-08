/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Cliente;
import expendiobebidas.utilidades.Utilidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author acrca
 */
public class ClienteDAO {
    
    public static ArrayList<Cliente> obtenerClientes() throws SQLException{
        ArrayList<Cliente> clientes = new ArrayList();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idCliente, razonSocial, telefono, correo, direccion FROM cliente";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                clientes.add(convertirRegistroCliente(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return clientes;
        
    }
        
    public static boolean insertarCliente(Cliente cliente) throws SQLException {
    Connection conexionBD = Conexion.abrirConexion();
    PreparedStatement sentencia = null;
    
    if (conexionBD != null) {
        String consulta = "INSERT INTO cliente (razonSocial, telefono, correo, direccion) VALUES (?, ?, ?, ?)";
        try {
            sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, cliente.getRazonSocial());
            sentencia.setString(2, cliente.getTelefono());
            sentencia.setString(3, cliente.getCorreo());
            sentencia.setString(4, cliente.getDireccion());

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
    public static boolean eliminarCliente(int idCliente) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "DELETE FROM cliente WHERE idCliente = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setInt(1, idCliente);
                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
     public static boolean actualizarCliente(Cliente cliente) throws SQLException {
        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD != null) {
                String consulta = "UPDATE cliente SET razonSocial = ?, telefono = ?, correo = ?, direccion = ? WHERE idCliente = ?";
                try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                    sentencia.setString(1, cliente.getRazonSocial());
                    sentencia.setString(2, cliente.getTelefono());
                    sentencia.setString(3, cliente.getCorreo());
                    sentencia.setString(4, cliente.getDireccion());
                    sentencia.setInt(5, cliente.getIdCliente());

                    int filasAfectadas = sentencia.executeUpdate();
                    return filasAfectadas > 0;
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión", "No hay conexión con la base de datos");
                return false;
            }
        }
    }
    
        private static Cliente convertirRegistroCliente(ResultSet resultado) throws SQLException{
        
        Cliente cliente = new Cliente();
        cliente.setIdCliente(resultado.getInt("idCliente"));
        cliente.setRazonSocial(resultado.getString("razonSocial"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setCorreo(resultado.getString("correo"));
        cliente.setDireccion(resultado.getString("direccion"));

        return cliente;
        
    }
       public static boolean tieneVentasAsociadas(int idCliente) {
    String query = "SELECT COUNT(*) FROM venta WHERE Cliente_idCliente = ?";
    try (Connection conn = Conexion.abrirConexion();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, idCliente);
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
