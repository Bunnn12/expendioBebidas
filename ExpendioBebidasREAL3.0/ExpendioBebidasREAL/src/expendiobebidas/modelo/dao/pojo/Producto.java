/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao.pojo;

/**
 *
 * @author reino
 */
public class Producto {
    private int idProducto;
    private double precio;
    private String nombreProducto;
    private String descripcion;
    private int stockMinimo;
    private int stockActual;
    private int totalVendido;
    
    public Producto(){
    }

    public Producto(int idProducto, double precio, String nombreProducto, String descripcion, int stockMinimo, int stockActual, int totalVendido) {
        this.idProducto = idProducto;
        this.precio = precio;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.stockMinimo = stockMinimo;
        this.stockActual = stockActual;
        this.totalVendido = totalVendido;
    }
    public Producto(String nombreProducto, String descripcion, double precio){
        this.descripcion= descripcion;
        this.nombreProducto= nombreProducto;
        this.precio= precio;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
    
    public double getPrecioConGanancia() {
        return precio * 1.15;
    }
    
    public String getPrecioConGananciaDosDecimales() {
        return String.format("%.2f", getPrecioConGanancia());
    }
    
}
