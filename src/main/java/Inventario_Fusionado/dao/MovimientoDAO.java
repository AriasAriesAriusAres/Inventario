package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.Movimiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection.
     * @throws SQLException Si ocurre un error al conectar.
     */
    private Connection getConnection() throws SQLException {
        return DBManager.getConnection();
    }

    /**
     * Añade un nuevo registro de movimiento a la base de datos.
     * La fecha/hora se establece automáticamente si no se proporciona en el objeto.
     * @param movimiento El objeto Movimiento a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean addMovimiento(Movimiento movimiento) {
        // SQL para insertar en la tabla movimientos
        String sql = "INSERT INTO movimientos (id_usuario, fecha_hora, tabla_afectada, " +
                "id_registro_afectado, accion, estado, detalles_cambio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, movimiento.getIdUsuario());

            // Usar fecha/hora actual si el objeto no la tiene, y formatearla como TEXT
            LocalDateTime fecha = (movimiento.getFechaHora() != null) ? movimiento.getFechaHora() : LocalDateTime.now();
            pstmt.setString(2, fecha.format(Movimiento.SQLITE_DATETIME_FORMATTER)); // Asume formatter en Movimiento

            pstmt.setString(3, movimiento.getTablaAfectada());
            pstmt.setInt(4, movimiento.getIdRegistroAfectado());
            pstmt.setString(5, movimiento.getAccion());
            pstmt.setString(6, movimiento.getEstado());

            // Manejar detalles_cambio (puede ser NULL)
            if (movimiento.getDetallesCambio() != null) {
                pstmt.setString(7, movimiento.getDetallesCambio());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
            }

            int affectedRows = pstmt.executeUpdate();

            // Opcional: recuperar y asignar el ID generado al objeto movimiento
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movimiento.setIdMovimiento(generatedKeys.getInt(1));
                    }
                }
                return true; // Inserción exitosa
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar movimiento: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para diagnóstico
        }
        return false; // Fallo en la inserción
    }

    /**
     * Obtiene una lista de todos los movimientos, ordenados por fecha descendente.
     * @return Lista de objetos Movimiento.
     */
    public List<Movimiento> getAllMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();
        // Ajusta nombres de columnas si son diferentes en tu SQL
        String sql = "SELECT id_movimiento, id_usuario, fecha_hora, tabla_afectada, " +
                "id_registro_afectado, accion, estado, detalles_cambio " +
                "FROM movimientos ORDER BY fecha_hora DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(); // Statement es suficiente aquí
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movimientos.add(mapResultSetToMovimiento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los movimientos: " + e.getMessage());
            e.printStackTrace();
        }
        return movimientos;
    }

    /**
     * Obtiene movimientos filtrados por estado.
     * @param estado El estado a filtrar (ej: "PENDIENTE", "APROBADO").
     * @return Lista de objetos Movimiento filtrados.
     */
    public List<Movimiento> getMovimientosByEstado(String estado) {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT id_movimiento, id_usuario, fecha_hora, tabla_afectada, " +
                "id_registro_afectado, accion, estado, detalles_cambio " +
                "FROM movimientos WHERE estado = ? ORDER BY fecha_hora DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado);

            // El ResultSet también debe cerrarse, try-with-resources es ideal
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapResultSetToMovimiento(rs));
                }
            } // rs.close() implícito aquí

        } catch (SQLException e) {
            System.err.println("Error al obtener movimientos por estado '" + estado + "': " + e.getMessage());
            e.printStackTrace();
        }
        return movimientos;
    }

    /**
     * Actualiza el estado y opcionalmente los detalles de un movimiento existente.
     * Si se proporcionan nuevosDetalles, se añaden a los existentes (si los hay) con un prefijo.
     * @param idMovimiento ID del movimiento a actualizar.
     * @param nuevoEstado Nuevo estado (ej: "APROBADO", "RECHAZADO").
     * @param nuevosDetalles Detalles adicionales a añadir (puede ser null o vacío si no se añaden detalles).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean updateMovimientoEstado(int idMovimiento, String nuevoEstado, String nuevosDetalles) {
        // Construimos la parte de la consulta para actualizar detalles condicionalmente
        String sqlSetDetalles;
        boolean hasNewDetails = (nuevosDetalles != null && !nuevosDetalles.trim().isEmpty());

        if (hasNewDetails) {
            // Si hay nuevos detalles, los concatenamos a los existentes (usando CHAR(10) para nueva línea)
            // COALESCE(detalles_cambio, '') maneja el caso donde detalles_cambio era NULL.
            // Añadimos un prefijo para indicar qué acción causó este detalle adicional.
            sqlSetDetalles = ", detalles_cambio = COALESCE(detalles_cambio, '') || CHAR(10) || ?";
        } else {
            // Si no hay nuevos detalles, no modificamos la columna detalles_cambio
            sqlSetDetalles = "";
        }

        // Construcción final del SQL UPDATE
        String sql = "UPDATE movimientos SET estado = ? " + sqlSetDetalles + " WHERE id_movimiento = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            pstmt.setString(paramIndex++, nuevoEstado); // 1: Establecer el nuevo estado

            // Establecer los nuevos detalles solo si es necesario
            if (hasNewDetails) {
                pstmt.setString(paramIndex++, "[" + nuevoEstado + "] " + nuevosDetalles.trim()); // 2: Añadir prefijo y detalle
            }

            pstmt.setInt(paramIndex, idMovimiento); // Último param: Establecer el ID para la cláusula WHERE

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del movimiento ID " + idMovimiento + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Fallo en la actualización
    }

    /**
     * Mapea una fila del ResultSet a un objeto Movimiento.
     * @param rs El ResultSet posicionado en la fila a mapear.
     * @return Objeto Movimiento poblado.
     * @throws SQLException Si ocurre un error al leer el ResultSet.
     */
    private Movimiento mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        Movimiento movimiento = new Movimiento();

        movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
        movimiento.setIdUsuario(rs.getInt("id_usuario"));
        movimiento.setFechaHoraFromString(rs.getString("fecha_hora")); // Usa el método de conversión
        movimiento.setTablaAfectada(rs.getString("tabla_afectada"));
        movimiento.setIdRegistroAfectado(rs.getInt("id_registro_afectado"));
        movimiento.setAccion(rs.getString("accion"));
        movimiento.setEstado(rs.getString("estado"));
        movimiento.setDetallesCambio(rs.getString("detalles_cambio")); // Puede ser null

        return movimiento;
    }

    // --- Puedes añadir más métodos de consulta aquí si los necesitas ---
    // Ejemplo: Buscar un movimiento por ID
    public Movimiento getMovimientoById(int idMovimiento) {
        String sql = "SELECT id_movimiento, id_usuario, fecha_hora, tabla_afectada, " +
                "id_registro_afectado, accion, estado, detalles_cambio " +
                "FROM movimientos WHERE id_movimiento = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMovimiento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovimiento(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener movimiento por ID " + idMovimiento + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra o hay error
    }

}