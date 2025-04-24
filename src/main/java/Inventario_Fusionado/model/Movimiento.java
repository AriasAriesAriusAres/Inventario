package Inventario_Fusionado.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un registro en la tabla 'movimientos',
 * que rastrea las acciones realizadas en el sistema.
 */
public class Movimiento {

    // Formato ISO para almacenar LocalDateTime como TEXT en SQLite
    public static final DateTimeFormatter SQLITE_DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private int idMovimiento;
    private int idUsuario;
    private LocalDateTime fechaHora;
    private String tablaAfectada;
    private int idRegistroAfectado;
    private String accion;
    private String estado;
    private String detallesCambio;

    // Constructor por defecto
    public Movimiento() {
    }

    // --- Getters y Setters ---

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaHoraString() {
        return (this.fechaHora != null) ? this.fechaHora.format(SQLITE_DATETIME_FORMATTER) : null;
    }

    public void setFechaHoraFromString(String fechaHoraStr) {
        this.fechaHora = (fechaHoraStr != null && !fechaHoraStr.isEmpty()) ?
                LocalDateTime.parse(fechaHoraStr, SQLITE_DATETIME_FORMATTER) : null;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public int getIdRegistroAfectado() {
        return idRegistroAfectado;
    }

    public void setIdRegistroAfectado(int idRegistroAfectado) {
        this.idRegistroAfectado = idRegistroAfectado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetallesCambio() {
        return detallesCambio;
    }

    public void setDetallesCambio(String detallesCambio) {
        this.detallesCambio = detallesCambio;
    }

    @Override
    public String toString() {
        String resumenCambio = detallesCambio;
        if (detallesCambio != null && detallesCambio.length() > 50) {
            resumenCambio = detallesCambio.substring(0, 50) + "...";
        }

        return "Movimiento{" +
                "idMovimiento=" + idMovimiento +
                ", idUsuario=" + idUsuario +
                ", fechaHora=" + getFechaHoraString() +
                ", tablaAfectada='" + tablaAfectada + '\'' +
                ", idRegistroAfectado=" + idRegistroAfectado +
                ", accion='" + accion + '\'' +
                ", estado='" + estado + '\'' +
                ", detallesCambio='" + resumenCambio + '\'' +
                '}';
    }
}
