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
public class InicioSesionDAO {
    public Usuario iniciarSesion(String usuario, String contrasenia) throws SQLException {
    Usuario usuarioSesion = null;
    Connection conexion = Conexion.abrirConexion();

    if (conexion != null) {
        String query = "SELECT nombreUsuario, idUsuario, contrasenia, rol FROM usuario WHERE nombreUsuario = ? AND contrasenia = AES_ENCRYPT(?, ?)";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, usuario);
        preparedStatement.setString(2, contrasenia);
        preparedStatement.setString(3, Conexion.getAESKEY());

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            usuarioSesion = new Usuario();
            usuarioSesion.setIdUsuario(resultSet.getInt("idUsuario"));
            usuarioSesion.setNombreUsuario(resultSet.getString("nombreUsuario"));
            usuarioSesion.setRol(resultSet.getString("rol"));
        }

        conexion.close();
    }

    return usuarioSesion;
}

}

