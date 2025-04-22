package Inventario_Fusionado.dao; // Ajusta el paquete si es diferente

import Inventario_Fusionado.database.DBManager; // Importa el nuevo gestor
import Inventario_Fusionado.model.Inventory; // Asume que Inventory.java está en model

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // Método para obtener todos los inventarios (adaptado para DBManager)
    public List<Inventory> GetAllInventories() {
        List<Inventory> inventarios = new ArrayList<>();
        // Consulta SQL estándar - ¡Verifica nombres de columna en Inventario.sql!
        String sql = "SELECT id_inventario, nombre, descripcion FROM inventarios";

        try (Connection conn = DBManager.getConnection(); // Usa el nuevo DBManager
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inv = new Inventory(
                        rs.getInt("id_inventario"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                inventarios.add(inv);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener inventarios desde SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return inventarios;
    }

    // Agregar Inventario (adaptado para DBManager)
    public boolean addInventory(Inventory inventory) {
        // SQLite usa INTEGER PRIMARY KEY AUTOINCREMENT por defecto para IDs
        // El nombre de columna en el SQL era 'nombre', 'descripcion'
        String sql = "INSERT INTO inventarios(nombre, descripcion) VALUES(?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, inventory.getNombre());
            pstmt.setString(2, inventory.getDescripcion());

            int affectedRows = pstmt.executeUpdate();

            // Obtener el ID generado por SQLite (ROWID)
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // En SQLite, la columna es a menudo implícita o ROWID
                        // Si tu tabla define explícitamente 'id_inventario', usa eso.
                        // Si no, getInt(1) podría funcionar para el ROWID.
                        inventory.setIdInventario(generatedKeys.getInt(1));
                    }
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar inventario a SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Borrar Inventario (adaptado para DBManager)
    public boolean deleteInventory(int id) {
        // El nombre de columna en el SQL era 'id_inventario'
        String sql = "DELETE FROM inventarios WHERE id_inventario = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al borrar inventario de SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar Inventario (adaptado para DBManager)
    public boolean updateInventory(Inventory inventory) {
        // Nombres de columna en SQL: nombre, descripcion, id_inventario
        String sql = "UPDATE inventarios SET nombre = ?, descripcion = ? WHERE id_inventario = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inventory.getNombre());
            pstmt.setString(2, inventory.getDescripcion());
            pstmt.setInt(3, inventory.getIdInventario());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar inventario en SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Obtener un inventario por ID (adaptado para DBManager)
    public Inventory getInventoryById(int id) {
        // Nombres de columna en SQL: id_inventario, nombre, descripcion
        String sql = "SELECT id_inventario, nombre, descripcion FROM inventarios WHERE id_inventario = ?";
        Inventory inventory = null;
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    inventory = new Inventory(
                            rs.getInt("id_inventario"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener inventario por ID desde SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return inventory;
    }
}