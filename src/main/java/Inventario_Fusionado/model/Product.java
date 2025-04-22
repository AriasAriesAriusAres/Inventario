package Inventario_Fusionado.model; // O el paquete de modelo que hayas definido

import java.math.BigDecimal;

public class Product {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio; // Usar BigDecimal para precios es buena práctica
    private int stock;
    private int idInventario; // Clave foránea para relacionar con Inventory

    // Constructor vacío
    public Product() {
    }

    // Constructor con todos los campos
    public Product(int idProducto, String nombre, String descripcion, BigDecimal precio, int stock, int idInventario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idInventario = idInventario;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", idInventario=" + idInventario +
                '}';
    }
}