package Inventario_Fusionado.model; // O el paquete de modelo que hayas definido

public class Inventory {
    private int idInventario;
    private String nombre;
    private String descripcion;

    // Constructor vacío (útil para algunas operaciones)
    public Inventory() {
    }

    // Constructor con todos los campos
    public Inventory(int idInventario, String nombre, String descripcion) {
        this.idInventario = idInventario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
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

    @Override
    public String toString() {
        return "Inventory{" +
                "idInventario=" + idInventario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}