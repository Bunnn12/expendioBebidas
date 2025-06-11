/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendiobebidas.modelo.dao.pojo;

/**
 *
 * @author reino
 */
public class Promocion {
    private int idPromocion;
    private String descripcion;
    private int descuento;
    private String fechaEmision;
    private String fechaVencimiento;
    private Producto producto;
    private Cliente cliente;

    public Promocion() {
    }

    public Promocion(int idPromocion, String descripcion, int descuento, String fechaEmision, String fechaVencimiento, Producto producto) {
        this.idPromocion = idPromocion;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.producto = producto;
    }
    
    public Promocion(int idPromocion, String descripcion, int descuento, String fechaEmision, String fechaVencimiento, Producto producto, Cliente cliente) {
        this.idPromocion = idPromocion;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.producto = producto;
        this.cliente = cliente;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
   public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getNombreProducto() {
        return producto != null ? producto.getNombreProducto() : "";
    }
    
    public double getPrecioConDescuento() {
        if (producto != null) {
            double precioConGanancia = producto.getPrecioConGanancia();
            return precioConGanancia * (1 - descuento / 100.0);
        }
        return 0.0;
    }
    
    public String getPrecioConDescuentoDosDecimales() {
        return String.format("%.2f", getPrecioConDescuento());
    }
    
}
