/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author reino
 */
public class RegistrarseDAO {
    public static boolean verificarUsuario(String username) throws SQLException {
    boolean usuarioUnico = true; 
    Connection conexionBD = Conexion.abrirConexion();

    if (conexionBD != null) {
        String consulta = "SELECT nombreUsuario FROM usuario WHERE nombreUsuario = ?";
        try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
            sentencia.setString(1, username);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    usuarioUnico = false;
                }
            }
        } finally {
            conexionBD.close();
        }
    }

    return usuarioUnico;
}
    
    public static boolean registrarUsuario(Usuario usuario) throws SQLException {
    boolean registrado = false;
    Connection conexion = Conexion.abrirConexion();

    if (conexion != null) {
        String sql = "INSERT INTO usuario (nombreUsuario, rol, contrasenia) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombreUsuario());
            sentencia.setString(2, usuario.getRol());
            sentencia.setString(3, usuario.getContrasenia());
            int filasAfectadas = sentencia.executeUpdate();
            registrado = filasAfectadas > 0;
        } finally {
            conexion.close();
        }
    }

    return registrado;
}


}
