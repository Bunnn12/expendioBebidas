/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;

import expendiobebidas.modelo.dao.pojo.Usuario;
import expendiobebidas.modelo.Conexion;
import java.sql.*;

/**
 *
 * @author rodri
 */
public class UsuarioDAO {
    public static Usuario verificarUsuario(String nombreUsuario, String contrasenia) {
        Usuario usuario = null;

        try (Connection conexionBD = Conexion.abrirConexion();
            PreparedStatement sentencia = conexionBD.prepareStatement(
            "SELECT * FROM usuario WHERE nombreUsuario = ? AND contrasenia = ?")) {

            sentencia.setString(1, nombreUsuario);
            sentencia.setString(2, contrasenia);

            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("idUsuario"));
                usuario.setNombreUsuario(resultado.getString("nombreUsuario"));
                usuario.setContrasenia(resultado.getString("contrasenia"));
                usuario.setRol(resultado.getString("rol"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}
