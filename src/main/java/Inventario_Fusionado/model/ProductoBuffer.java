package Inventario_Fusionado.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// Usaremos el mismo formateador que en Movimiento
// import static Inventario_Fusionado.model.Movimiento.SQLITE_DATETIME_FORMATTER;
import java.time.format.DateTimeFormatter;


/**
 * Representa un registro en la tabla 'productos_buffer',
 * almacenando solicitudes pendientes de creación, modificación o borrado de productos.
 */
public class ProductoBuffer {

    // Re-declaramos o importamos estáticamente el formateador
    public static final DateTimeFormatter SQLITE_DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private int idBuffer;
    // Campos del producto (coinciden con la tabla 'productos')
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private int idInventario;         // FK a inventarios
    // Campos específicos del buffer
    private String estado;            // Ej: "PENDIENTE_CREAR", "PENDIENTE_MODIFICAR", "PENDIENTE_BORRAR", "APROBADO", "RECHAZADO" (Podría ser Enum)
    private int idUsuarioSolicitud; // FK a usuarios (quién solicitó el cambio web)
    private LocalDateTime timestampSolicitud; // Fecha/hora de la solicitud
    // Campos adicionales que podríamos necesitar (basado en ProductDAO propuesto antes)
    private Integer idProductoOriginal; // ID del producto en tabla 'productos' si es MODIF/BORRAR (Nullable)
    private String mensajeRechazo;    // Motivo si se rechaza (Nullable)


    // Constructores
    public ProductoBuffer() {
        // this.timestampSolicitud = LocalDateTime.now(); // Establecer por defecto si se desea
    }

    // --- Getters y Setters ---

    public int getIdBuffer() { return idBuffer; }
    public void setIdBuffer(int idBuffer) { this.idBuffer = idBuffer; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int idInventario) { this.idInventario = idInventario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdUsuarioSolicitud() { return idUsuarioSolicitud; }
    public void setIdUsuarioSolicitud(int idUsuarioSolicitud) { this.idUsuarioSolicitud = idUsuarioSolicitud; }

    // Getters/Setters para timestamp usando String (para BD) y LocalDateTime (para Java)
    public String getTimestampSolicitudString() {
        return (this.timestampSolicitud != null) ? this.timestampSolicitud.format(SQLITE_DATETIME_FORMATTER) : null;
    }
    public void setTimestampSolicitudFromString(String timestampStr) {
        this.timestampSolicitud = (timestampStr != null && !timestampStr.isEmpty()) ? LocalDateTime.parse(timestampStr, SQLITE_DATETIME_FORMATTER) : null;
    }
    public LocalDateTime getTimestampSolicitud() { return timestampSolicitud; }
    public void setTimestampSolicitud(LocalDateTime timestampSolicitud) { this.timestampSolicitud = timestampSolicitud; }

    // Getters/Setters para campos adicionales
    public Integer getIdProductoOriginal() { return idProductoOriginal; }
    public void setIdProductoOriginal(Integer idProductoOriginal) { this.idProductoOriginal = idProductoOriginal; }

    public String getMensajeRechazo() { return mensajeRechazo; }
    public void setMensajeRechazo(String mensajeRechazo) { this.mensajeRechazo = mensajeRechazo; }

    @Override
    public String toString() {
        return "ProductoBuffer{" +
                "idBuffer=" + idBuffer +
                ", nombre='" + nombre + '\'' +
                ", estado='" + estado + '\'' +
                ", idUsuarioSolicitud=" + idUsuarioSolicitud +
                ", timestampSolicitud=" + getTimestampSolicitudString() +
                ", idProductoOriginal=" + idProductoOriginal +
                '}';
    }
}