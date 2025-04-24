package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.Movimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la tabla 'movimientos'.
 * Responsable de todas las operaciones de base de datos (CRUD) relacionadas con los registros de movimientos (log/auditoría).
 *
 * --- Resumen de Interacción ---
 * Datos que Toma (Input):
 * - Objeto `Movimiento` para insertar (`addMovimiento`).
 * - `int` (id de movimiento) para eliminar (`deleteMovimiento`).
 * - `String` (estado) para filtrar (`getMovimientosByEstado`).
 * - `int` (id de movimiento) y `String` (nuevo estado) para actualizar estado (`updateEstado`).
 *
 * Datos que Da (Output):
 * - `boolean`: Resultado éxito/fallo para operaciones CUD (Create, Update, Delete).
 * - `List<Movimiento>`: Lista de objetos `Movimiento` para operaciones de lectura (`getAllMovimientos`, `getMovimientosByEstado`).
 *
 * Archivos con los que Interactúa:
 * - `Inventario_Fusionado.model.Movimiento`: Usa/Crea objetos de este modelo para representar los datos.
 * - `Inventario_Fusionado.database.DBManager`: Utiliza esta clase para obtener conexiones a la BD.
 * - Base de datos SQLite (`InventarioFusionado.db`): Accede específicamente a la tabla `movimientos`.
 *
 * Dependencias del Proyecto:
 * - Paquete `Inventario_Fusionado.database` (por `DBManager`).
 * - Paquete `Inventario_Fusionado.model` (por `Movimiento`).
 * - Librería `sqlite-jdbc` (implícitamente, a través de `DBManager` y `java.sql`).
 *
 * Utilizado por:
 * - `MovimientoController`: Llama a los métodos de este DAO para responder a las peticiones web.
 * - Posiblemente otros DAOs (ej. `ProductDAO`) si registran movimientos como parte de sus operaciones.
 */
public class movimientoDAO {

    /**
     * Obtiene una conexión a la base de datos SQLite utilizando DBManager.
     * @return Una conexión JDBC activa.
     * @throws SQLException Si ocurre un error al obtener la conexión.
     */
    private Connection getConnection() throws SQLException {
        // Llama al método estático de DBManager para obtener la conexión configurada.
        return DBManager.getConnection();
    }

    /**
     * Inserta un nuevo registro de movimiento en la base de datos.
     * Asigna el ID generado automáticamente al objeto Movimiento proporcionado si la inserción es exitosa.
     *
     * @param movimiento El objeto Movimiento con los datos a insertar (excepto idMovimiento que se autogenera).
     * @return `true` si el movimiento se insertó correctamente, `false` en caso contrario.
     */
    public boolean addMovimiento(Movimiento movimiento) {
        // Define la sentencia SQL para insertar un nuevo movimiento.
        String sql = "INSERT INTO movimientos (id_usuario, fecha_hora, tabla_afectada, " +
                "id_registro_afectado, accion, estado, detalles_cambio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Usa try-with-resources para asegurar el cierre automático de Connection y PreparedStatement.
        try (Connection conn = getConnection();
             // Prepara la sentencia SQL, pidiendo que devuelva las claves generadas (el ID autoincremental).
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asigna los valores del objeto Movimiento a los parámetros (?) de la sentencia SQL.
            pstmt.setInt(1, movimiento.getIdUsuario());
            // Formatea la fecha/hora de Java (LocalDateTime) al formato de texto ISO para SQLite.
            pstmt.setString(2, movimiento.getFechaHora().format(Movimiento.SQLITE_DATETIME_FORMATTER));
            pstmt.setString(3, movimiento.getTablaAfectada());
            pstmt.setInt(4, movimiento.getIdRegistroAfectado());
            pstmt.setString(5, movimiento.getAccion());
            pstmt.setString(6, movimiento.getEstado());

            // Maneja el campo opcional detalles_cambio (puede ser null).
            if (movimiento.getDetallesCambio() != null) {
                pstmt.setString(7, movimiento.getDetallesCambio());
            } else {
                // Si es null en el objeto, inserta NULL en la base de datos.
                pstmt.setNull(7, Types.VARCHAR);
            }

            // Ejecuta la sentencia INSERT. affectedRows contiene el número de filas insertadas (debería ser 1).
            int affectedRows = pstmt.executeUpdate();

            // Verifica si la inserción fue exitosa.
            if (affectedRows > 0) {
                // Intenta obtener el ID autogenerado por la base de datos.
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Asigna el ID generado de vuelta al objeto Movimiento original.
                        movimiento.setIdMovimiento(generatedKeys.getInt(1));
                    }
                }
                // Devuelve true indicando éxito.
                return true;
            }
        } catch (SQLException e) {
            // Imprime un mensaje de error y la traza si ocurre una excepción SQL.
            System.err.println("Error al insertar movimiento: " + e.getMessage());
            e.printStackTrace();
        }
        // Devuelve false si la inserción falló o hubo una excepción.
        return false;
    }

    /**
     * Obtiene todos los registros de movimientos de la base de datos, ordenados por fecha descendente.
     *
     * @return Una lista de objetos `Movimiento`. La lista estará vacía si no hay movimientos o si ocurre un error.
     */
    public List<Movimiento> getAllMovimientos() {
        // Inicializa una lista vacía para almacenar los movimientos.
        List<Movimiento> movimientos = new ArrayList<>();
        // Define la consulta SQL para seleccionar todos los movimientos.
        String sql = "SELECT * FROM movimientos ORDER BY fecha_hora DESC";

        // Usa try-with-resources para Connection, Statement y ResultSet.
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre cada fila del resultado de la consulta.
            while (rs.next()) {
                // Mapea la fila actual a un objeto Movimiento y lo añade a la lista.
                movimientos.add(mapResultSetToMovimiento(rs));
            }
        } catch (SQLException e) {
            // Imprime error si falla la consulta.
            System.err.println("Error al obtener todos los movimientos: " + e.getMessage());
            e.printStackTrace();
            // Devuelve la lista (posiblemente vacía) en caso de error.
        }
        // Devuelve la lista de movimientos recuperados.
        return movimientos;
    }

    /**
     * Obtiene los registros de movimientos que coinciden con un estado específico, ordenados por fecha descendente.
     *
     * @param estado El estado por el cual filtrar los movimientos (ej. "APROBADO", "PENDIENTE").
     * @return Una lista de objetos `Movimiento` que coinciden con el estado. Lista vacía si no hay coincidencias o si ocurre un error.
     */
    public List<Movimiento> getMovimientosByEstado(String estado) {
        // Inicializa lista vacía.
        List<Movimiento> movimientos = new ArrayList<>();
        // Define la consulta SQL con un parámetro (?) para el estado.
        String sql = "SELECT * FROM movimientos WHERE estado = ? ORDER BY fecha_hora DESC";

        // Usa try-with-resources para Connection y PreparedStatement.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asigna el valor del parámetro 'estado'.
            pstmt.setString(1, estado);
            // Ejecuta la consulta y obtiene el ResultSet (dentro de otro try-with-resources).
            try (ResultSet rs = pstmt.executeQuery()) {
                // Itera y mapea los resultados.
                while (rs.next()) {
                    movimientos.add(mapResultSetToMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            // Imprime error si falla la consulta.
            System.err.println("Error al filtrar movimientos: " + e.getMessage());
            e.printStackTrace();
        }
        // Devuelve la lista filtrada.
        return movimientos;
    }

    /**
     * Actualiza el estado de un registro de movimiento específico en la base de datos.
     * (Este método fue añadido para corregir un error de compilación previo).
     *
     * @param idMovimiento El ID del movimiento cuyo estado se actualizará.
     * @param nuevoEstado El nuevo valor para la columna 'estado'.
     * @return `true` si la actualización fue exitosa (al menos una fila afectada), `false` en caso contrario.
     */
    public boolean updateEstado(int idMovimiento, String nuevoEstado) {
        // Define la sentencia SQL para actualizar el estado de un movimiento específico.
        String sql = "UPDATE movimientos SET estado = ? WHERE id_movimiento = ?";

        // Usa try-with-resources para Connection y PreparedStatement.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asigna los parámetros: el nuevo estado y el ID del movimiento a actualizar.
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idMovimiento);

            // Ejecuta la sentencia UPDATE. affectedRows indica cuántas filas se modificaron.
            int affectedRows = pstmt.executeUpdate();
            // Devuelve true si se actualizó exactamente una fila (o más, aunque no debería si el ID es único).
            return affectedRows > 0;

        } catch (SQLException e) {
            // Imprime un mensaje de error si ocurre una excepción SQL durante la actualización.
            System.err.println("Error al actualizar estado del movimiento ID " + idMovimiento + ": " + e.getMessage());
            e.printStackTrace();
        }
        // Devuelve false si hubo una excepción o no se actualizó ninguna fila.
        return false;
    }


    /**
     * Elimina un registro de movimiento de la base de datos basado en su ID.
     *
     * @param id El ID del movimiento a eliminar.
     * @return `true` si el movimiento se eliminó correctamente (al menos una fila afectada), `false` en caso contrario.
     */
    public boolean deleteMovimiento(int id) {
        // Define la consulta SQL para eliminar por ID.
        String sql = "DELETE FROM movimientos WHERE id_movimiento = ?";

        // Usa try-with-resources.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Asigna el ID al parámetro de la consulta.
            pstmt.setInt(1, id);
            // Ejecuta DELETE y devuelve true si alguna fila fue eliminada.
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Imprime error si falla la eliminación.
            System.err.println("Error al eliminar movimiento: " + e.getMessage());
            e.printStackTrace();
        }
        // Devuelve false si hubo error o no se eliminó nada.
        return false;
    }

    /**
     * Mapea una fila del ResultSet actual a un objeto Movimiento.
     * Este es un método de ayuda interno utilizado por los métodos de consulta.
     *
     * @param rs El ResultSet posicionado en la fila que se va a mapear.
     * @return Un objeto `Movimiento` con sus atributos poblados desde la fila actual del ResultSet.
     * @throws SQLException Si ocurre un error al leer datos del ResultSet.
     */
    private Movimiento mapResultSetToMovimiento(ResultSet rs) throws SQLException {
        // Crea una nueva instancia del modelo Movimiento.
        Movimiento mov = new Movimiento();
        // Lee cada columna del ResultSet por su nombre y la asigna al atributo correspondiente del objeto.
        mov.setIdMovimiento(rs.getInt("id_movimiento"));
        mov.setIdUsuario(rs.getInt("id_usuario"));
        // Convierte el String de fecha/hora de la BD a LocalDateTime usando el método del modelo.
        mov.setFechaHoraFromString(rs.getString("fecha_hora"));
        mov.setTablaAfectada(rs.getString("tabla_afectada"));
        mov.setIdRegistroAfectado(rs.getInt("id_registro_afectado"));
        mov.setAccion(rs.getString("accion"));
        mov.setEstado(rs.getString("estado"));
        mov.setDetallesCambio(rs.getString("detalles_cambio")); // Puede ser null si la columna es null
        // Devuelve el objeto Movimiento poblado.
        return mov;
    }

    /**
     * Obtiene un registro de movimiento específico por su ID.
     *
     * @param idMovimiento El ID del movimiento a buscar.
     * @return El objeto `Movimiento` encontrado, o `null` si no se encuentra o ocurre un error.
     */
    public Movimiento getMovimientoById(int idMovimiento) {
        // Define la consulta SQL para buscar por ID.
        String sql = "SELECT * FROM movimientos WHERE id_movimiento = ?";
        Movimiento movimiento = null; // Inicializa a null

        // Usa try-with-resources para Connection y PreparedStatement.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asigna el ID al parámetro de la consulta.
            pstmt.setInt(1, idMovimiento);

            // Ejecuta la consulta y obtiene el ResultSet.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encontró una fila...
                if (rs.next()) {
                    // ...mapea la fila a un objeto Movimiento usando el método existente.
                    movimiento = mapResultSetToMovimiento(rs);
                }
            }
        } catch (SQLException e) {
            // Imprime error si falla la consulta.
            System.err.println("Error al obtener movimiento por ID " + idMovimiento + ": " + e.getMessage());
            e.printStackTrace();
            // movimiento permanece null en caso de error.
        }
        // Devuelve el objeto encontrado o null si no se encontró/hubo error.
        return movimiento;
    }
}