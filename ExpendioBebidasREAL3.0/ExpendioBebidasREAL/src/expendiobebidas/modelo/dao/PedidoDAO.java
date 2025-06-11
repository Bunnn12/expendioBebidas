/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao;
import expendiobebidas.modelo.Conexion;
import expendiobebidas.modelo.dao.pojo.Pedido;
import expendiobebidas.modelo.dao.pojo.Producto;
import expendiobebidas.modelo.dao.pojo.ProductoPedido;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author reino
 */
public class PedidoDAO {
    
    public static ArrayList<Pedido> obtenerPedidos() throws SQLException{
        ArrayList<Pedido> pedidos = new ArrayList<>();
        Connection conexionBD = Conexion.abrirConexion();
        if (conexionBD != null){
            String consulta = "SELECT idPedidoCliente, fechaPedidoCliente, Cliente_idCliente FROM pedidoCliente";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()){
                pedidos.add(convertirRegistroPedidoCliente(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexión a la Base de Datos");
        }
        return pedidos;
    }
    
    public static boolean insertarPedidoDeCliente(int idCliente, List<ProductoPedido> productosPedido) throws SQLException {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El idCliente debe ser mayor que 0");
        }
        if (productosPedido == null || productosPedido.isEmpty()) {
            throw new IllegalArgumentException("La lista de productos no puede estar vacía");
        }

        Connection conn = Conexion.abrirConexion();
        if (conn == null) throw new SQLException("Sin conexión a la base de datos");

        boolean exito = false;
        conn.setAutoCommit(false);
        try {
            String sqlPedido = "INSERT INTO pedidoCliente(fechaPedidoCliente, Cliente_idCliente) VALUES (CURDATE(), ?)";
            PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, idCliente);
            psPedido.executeUpdate();

            ResultSet rs = psPedido.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se generó idPedidoCliente");
            int idPedido = rs.getInt(1);
            rs.close();
            psPedido.close();

            String sqlDetalle = "INSERT INTO detallePedidoCliente (PedidoCliente_idPedidoCliente, PedidoCliente_Cliente_idCliente, Producto_idProducto, cantidadSolicitada) VALUES (?, ?, ?, ?)";
            PreparedStatement psDet = conn.prepareStatement(sqlDetalle);

            for (ProductoPedido pp : productosPedido) {
                psDet.setInt(1, idPedido);                    // idPedidoCliente
                psDet.setInt(2, idCliente);                   // Cliente_idCliente
                psDet.setInt(3, pp.getProducto().getIdProducto()); // idProducto
                psDet.setInt(4, pp.getCantidad());            // cantidad solicitada
                psDet.addBatch();
            }

            psDet.executeBatch();
            psDet.close();

            conn.commit();
            exito = true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }

        return exito;
    }
    
   public static List<Producto> obtenerProductosDePedidosPorProveedor(int idProveedor) throws SQLException {
    List<Producto> productos = new ArrayList<>();
    Connection conn = Conexion.abrirConexion();
    if (conn != null) {
        String consulta = 
            "SELECT pr.idProducto, pr.nombreProducto, pr.precio, pr.stockActual, pr.stockMinimo " +
            "FROM pedido p " +
            "JOIN detallepedido dp ON p.idPedido = dp.Pedido_idPedido " +
            "JOIN producto pr ON dp.Producto_idProducto = pr.idProducto " +
            "WHERE p.Proveedor_idProveedor = ? " +
            "AND pr.stockActual <= pr.stockMinimo";

        PreparedStatement ps = conn.prepareStatement(consulta);
        ps.setInt(1, idProveedor);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Producto producto = new Producto();
            producto.setIdProducto(rs.getInt("idProducto"));
            producto.setNombreProducto(rs.getString("nombreProducto"));
            producto.setPrecio(rs.getDouble("precio"));
            producto.setStockActual(rs.getInt("stockActual"));
            producto.setStockMinimo(rs.getInt("stockMinimo"));
            productos.add(producto);
        }

        rs.close();
        ps.close();
        conn.close();
    } else {
        throw new SQLException("Sin conexión con la base de datos");
    }

    return productos;
}
public static void generarPedidoPorProducto(int idProducto) throws SQLException {
    Connection conn = Conexion.abrirConexion();

    if (conn != null) {
        String sql = "{CALL gestionar_pedidos_proveedor(?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, idProducto);
        cs.execute();
        cs.close();
        conn.close();
    } else {
        throw new SQLException("Sin conexión a la base de datos");
    }
}
    public static boolean insertarPedido(int idProveedor, List<ProductoPedido> productosPedido) throws SQLException {
    if (idProveedor <= 0) {
        throw new IllegalArgumentException("El idProveedor debe ser mayor que 0");
    }
    if (productosPedido == null || productosPedido.isEmpty()) {
        throw new IllegalArgumentException("La lista de productos no puede estar vacía");
    }

    Connection conn = Conexion.abrirConexion();
    if (conn == null) throw new SQLException("Sin conexión a la base de datos");

    boolean exito = false;
    conn.setAutoCommit(false);
    try {
        // Insertar pedido
        String sqlPedido = "INSERT INTO pedido (fechaPedido, Proveedor_idProveedor) VALUES (CURDATE(), ?)";
        PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
        psPedido.setInt(1, idProveedor);
        psPedido.executeUpdate();

        ResultSet rs = psPedido.getGeneratedKeys();
        if (!rs.next()) throw new SQLException("No se generó idPedido");
        int idPedido = rs.getInt(1);
        rs.close();
        psPedido.close();

        // Insertar detalles
        String sqlDetalle = "INSERT INTO detallePedido (Pedido_idPedido, Producto_idProducto, cantidadPedida) VALUES (?, ?, ?)";
        PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);

        for (ProductoPedido pp : productosPedido) {
            psDetalle.setInt(1, idPedido);
            psDetalle.setInt(2, pp.getProducto().getIdProducto());
            psDetalle.setInt(3, pp.getCantidad());
            psDetalle.addBatch();
        }

        psDetalle.executeBatch();
        psDetalle.close();

        conn.commit();
        exito = true;
    } catch (SQLException ex) {
        conn.rollback();
        throw ex;
    } finally {
        conn.setAutoCommit(true);
        conn.close();
    }

    return exito;
}
    public static List<ProductoPedido> obtenerProductosDePedido(int idPedido, int idCliente) throws SQLException {
        List<ProductoPedido> productosPedido = new ArrayList<>();

        try (Connection conexionBD = Conexion.abrirConexion()) {
            if (conexionBD == null) {
                throw new SQLException("Sin conexión a la Base de Datos");
            }

            String sql = "SELECT dp.Producto_idProducto, dp.cantidadSolicitada, "
                       + "p.nombreProducto, p.precio, p.descripcion, "
                       + "p.stockMinimo, p.stockActual "
                       + "FROM detallePedidoCliente dp "
                       + "JOIN producto p ON dp.Producto_idProducto = p.idProducto "
                       + "WHERE dp.PedidoCliente_idPedidoCliente = ? "
                       + "AND dp.PedidoCliente_Cliente_idCliente = ?";

            try (PreparedStatement ps = conexionBD.prepareStatement(sql)) {
                ps.setInt(1, idPedido);
                ps.setInt(2, idCliente);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Producto producto = new Producto();
                        producto.setIdProducto(rs.getInt("Producto_idProducto"));
                        producto.setNombreProducto(rs.getString("nombreProducto"));
                        producto.setPrecio(rs.getDouble("precio"));
                        producto.setDescripcion(rs.getString("descripcion"));
                        producto.setStockMinimo(rs.getInt("stockMinimo"));
                        producto.setStockActual(rs.getInt("stockActual"));

                        ProductoPedido pp = new ProductoPedido();
                        pp.setProducto(producto);
                        pp.setCantidad(rs.getInt("cantidadSolicitada"));

                        productosPedido.add(pp);
                    }
                }
            }
        }

        return productosPedido;
    }
    
    
    private static Pedido convertirRegistroPedidoCliente(ResultSet resultado) throws SQLException{
        
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("idPedidoCliente"));
        pedido.setFechaPedido(resultado.getDate("fechaPedidoCliente").toString());
        pedido.setIdCliente(resultado.getInt("Cliente_idCliente"));
        return pedido;
        
    }

}
