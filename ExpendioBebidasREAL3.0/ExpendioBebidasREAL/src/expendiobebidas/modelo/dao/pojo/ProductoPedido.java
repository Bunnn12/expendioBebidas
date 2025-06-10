/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao.pojo;

/**
 *
 * @author reino
 */
public class ProductoPedido {
    private Producto producto;
    private int cantidad;

    public ProductoPedido(Producto producto) {
        this.producto = producto;
        this.cantidad = 1;
    }
    public ProductoPedido(){
        
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void incrementarCantidad() {
        cantidad++;
    }

    public double getPrecioTotal() {
        return producto.getPrecio() * cantidad;
    }

    // Métodos para las propiedades para la tabla
    public String getNombreProducto() {
        return producto.getNombreProducto();
    }
}

