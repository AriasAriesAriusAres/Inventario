package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class inventoryDAO {

    // Obtener todos los inventarios
    public List<Inventory> getAllInventories() {
        List<Inventory> inventarios = new ArrayList<>();
        String sql = "SELECT id, nombre_producto, cantidad FROM inventarios";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inv = new Inventory(
                        rs.getInt("id"),
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad")
                );
                inventarios.add(inv);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener inventarios: " + e.getMessage());
            e.printStackTrace();
        }
        return inventarios;
    }

    // Obtener inventario por ID
    public Inventory getInventoryById(int id) {
        String sql = "SELECT id, nombre_producto, cantidad FROM inventarios WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Inventory(
                        rs.getInt("id"),
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar inventario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Insertar nuevo inventario
    public boolean addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventarios (nombre_producto, cantidad) VALUES (?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, inventory.getNombreProducto());
            pstmt.setInt(2, inventory.getCantidad());
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    inventory.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar inventario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Actualizar inventario existente
    public boolean updateInventory(Inventory inventory) {
        String sql = "UPDATE inventarios SET nombre_producto = ?, cantidad = ? WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inventory.getNombreProducto());
            pstmt.setInt(2, inventory.getCantidad());
            pstmt.setInt(3, inventory.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar inventario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Eliminar inventario por ID
    public boolean deleteInventory(int id) {
        String sql = "DELETE FROM inventarios WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar inventario: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
