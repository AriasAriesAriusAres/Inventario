package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.ProductoBuffer;
// Necesitaremos BigDecimal y LocalDateTime si los usamos directamente
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las operaciones CRUD y consultas
 * sobre la tabla 'productos_buffer' (cambios pendientes de productos).
 */
public class productbufferDAO {

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection.
     * @throws SQLException Si ocurre un error al conectar.
     */
    private Connection getConnection() throws SQLException {
        return DBManager.getConnection();
    }

    /**
     * Inserta una nueva solicitud de cambio de producto en el buffer.
     * @param bufferData Objeto ProductoBuffer con los datos de la solicitud.
     * @return El ID generado para la entrada del buffer (id_buffer), o -1 si falla.
     */
    public int addProductoBuffer(ProductoBuffer bufferData) {
        // Asegúrate que los nombres de columna coincidan EXACTAMENTE con tu tabla Inventario.sql
        // Incluye las columnas opcionales id_producto_original y mensaje_rechazo si las añadiste
        String sql = "INSERT INTO productos_buffer (nombre, descripcion, precio, stock, id_inventario, " +
                "estado, id_usuario_solicitud, timestamp_solicitud, id_producto_original, mensaje_rechazo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, bufferData.getNombre());
            pstmt.setString(2, bufferData.getDescripcion());
            pstmt.setBigDecimal(3, bufferData.getPrecio());
            pstmt.setInt(4, bufferData.getStock());
            pstmt.setInt(5, bufferData.getIdInventario());
            pstmt.setString(6, bufferData.getEstado()); // Ej: PENDIENTE_CREAR
            pstmt.setInt(7, bufferData.getIdUsuarioSolicitud());

            // Usar fecha/hora actual si no viene una específica en el objeto
            LocalDateTime fecha = (bufferData.getTimestampSolicitud() != null) ? bufferData.getTimestampSolicitud() : LocalDateTime.now();
            pstmt.setString(8, fecha.format(ProductoBuffer.SQLITE_DATETIME_FORMATTER));

            // Manejar campos opcionales (requiere que existan en la tabla)
            if (bufferData.getIdProductoOriginal() != null) {
                pstmt.setInt(9, bufferData.getIdProductoOriginal());
            } else {
                pstmt.setNull(9, Types.INTEGER);
            }
            // Mensaje de rechazo normalmente será null al insertar la solicitud inicial
            if (bufferData.getMensajeRechazo() != null) {
                pstmt.setString(10, bufferData.getMensajeRechazo());
            } else {
                pstmt.setNull(10, Types.VARCHAR);
            }


            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        bufferData.setIdBuffer(generatedId); // Actualizar ID en el objeto
                        return generatedId; // Devolver el ID generado
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar en productos_buffer: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Indicar fallo
    }

    /**
     * Obtiene todas las solicitudes pendientes (estado empieza con "PENDIENTE_").
     * @return Lista de objetos ProductoBuffer pendientes.
     */
    public List<ProductoBuffer> getPendingChanges() {
        List<ProductoBuffer> pending = new ArrayList<>();
        // Buscar por prefijo de estado
        String sql = "SELECT * FROM productos_buffer WHERE estado LIKE 'PENDIENTE_%' ORDER BY timestamp_solicitud";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pending.add(mapResultSetToProductoBuffer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cambios pendientes de productos_buffer: " + e.getMessage());
            e.printStackTrace();
        }
        return pending;
    }

    /**
     * Obtiene una solicitud específica del buffer por su ID.
     * @param idBuffer El ID del buffer a buscar.
     * @return El objeto ProductoBuffer encontrado, o null si no existe o hay error.
     */
    public ProductoBuffer getBufferById(int idBuffer) {
        String sql = "SELECT * FROM productos_buffer WHERE id_buffer = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idBuffer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProductoBuffer(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener buffer por ID " + idBuffer + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza el estado y opcionalmente el mensaje de rechazo de una solicitud en el buffer.
     * Usado principalmente al aprobar o rechazar desde la lógica de ProductDAO.
     * @param idBuffer ID de la solicitud a actualizar.
     * @param nuevoEstado Nuevo estado ("APROBADO", "RECHAZADO").
     * @param mensajeRechazo Mensaje si se rechaza (puede ser null).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean updateBufferState(int idBuffer, String nuevoEstado, String mensajeRechazo) {
        // Necesitas la columna mensaje_rechazo en la tabla para que esto funcione
        String sql = "UPDATE productos_buffer SET estado = ?, mensaje_rechazo = ? WHERE id_buffer = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);

            if (mensajeRechazo != null && !mensajeRechazo.trim().isEmpty()) {
                pstmt.setString(2, mensajeRechazo.trim());
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }

            pstmt.setInt(3, idBuffer);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del buffer ID " + idBuffer + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina una entrada del buffer por su ID.
     * Podría usarse después de una aprobación exitosa para limpiar, o no usarse
     * y mantener el registro con estado "APROBADO"/"RECHAZADO".
     * @param idBuffer ID de la entrada a eliminar.
     * @return true si se eliminó con éxito.
     */
    public boolean deleteBufferEntry(int idBuffer) {
        String sql = "DELETE FROM productos_buffer WHERE id_buffer = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBuffer);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar entrada del buffer ID " + idBuffer + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Mapea una fila del ResultSet a un objeto ProductoBuffer.
     * @param rs El ResultSet posicionado en la fila a mapear.
     * @return Objeto ProductoBuffer poblado.
     * @throws SQLException Si ocurre un error al leer el ResultSet.
     */
    private ProductoBuffer mapResultSetToProductoBuffer(ResultSet rs) throws SQLException {
        ProductoBuffer pb = new ProductoBuffer();
        pb.setIdBuffer(rs.getInt("id_buffer"));
        pb.setNombre(rs.getString("nombre"));
        pb.setDescripcion(rs.getString("descripcion"));
        pb.setPrecio(rs.getBigDecimal("precio")); // Asumiendo columna REAL
        pb.setStock(rs.getInt("stock"));
        pb.setIdInventario(rs.getInt("id_inventario"));
        pb.setEstado(rs.getString("estado"));
        pb.setIdUsuarioSolicitud(rs.getInt("id_usuario_solicitud"));
        pb.setTimestampSolicitudFromString(rs.getString("timestamp_solicitud")); // Conversión

        // Mapear campos adicionales si existen en la tabla/ResultSet
        // Usar getObject para manejar NULLs de forma segura con tipos Integer
        pb.setIdProductoOriginal(rs.getObject("id_producto_original", Integer.class));
        pb.setMensajeRechazo(rs.getString("mensaje_rechazo"));

        return pb;
    }
}