package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.Usuario;

import java.sql.*;

public class UsuarioDAO {

    /**
     * Busca un usuario en la base de datos por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return Un objeto Usuario si se encuentra, o null si no existe.
     */
    public Usuario obtenerPorUsername(String username) {
        String sql = "SELECT id_usuario, username, password FROM usuarios WHERE username = ?";
        Usuario usuario = null;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por username '" + username + "': " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }

    /**
     * Valida si el nombre de usuario y la contraseña proporcionados coinciden
     * con un registro en la base de datos.
     * ¡¡ADVERTENCIA!! Comparación de contraseña en texto plano.
     * @return true si las credenciales coinciden.
     */
    public boolean validarUsuario(String username, String password) {
        Usuario usuario = obtenerPorUsername(username);
        if (usuario != null && usuario.getPassword().equals(password)) {
            System.out.println("Usuario validado exitosamente: " + username);
            return true;
        } else {
            System.out.println("Validación fallida para usuario: " + username);
            return false;
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un usuario por su ID.
     */
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
