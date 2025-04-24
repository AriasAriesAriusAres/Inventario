package Inventario_Fusionado.dao;

import Inventario_Fusionado.database.DBManager;
import Inventario_Fusionado.model.Product;
import Inventario_Fusionado.model.ProductoBuffer;
import Inventario_Fusionado.model.Movimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_inventario FROM productos";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        rs.getInt("id_inventario")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos desde SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByInventoryId(int inventoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_inventario FROM productos WHERE id_inventario = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inventoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock"),
                            rs.getInt("id_inventario")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por inventario desde SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO productos(nombre, descripcion, precio, stock, id_inventario) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getNombre());
            pstmt.setString(2, product.getDescripcion());
            pstmt.setBigDecimal(3, product.getPrecio());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5, product.getIdInventario());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setIdProducto(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar producto a SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al borrar producto de SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, id_inventario = ? WHERE id_producto = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getNombre());
            pstmt.setString(2, product.getDescripcion());
            pstmt.setBigDecimal(3, product.getPrecio());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5, product.getIdInventario());
            pstmt.setInt(6, product.getIdProducto());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto en SQLite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, id_inventario FROM productos WHERE id_producto = ?";
        Product product = null;
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock"),
                            rs.getInt("id_inventario")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID desde SQLite: " + e.getMessage());
            e.printStackTrace();
        }
        return product;
    }

    public boolean approveChange(ProductoBuffer pb) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, id_inventario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pb.getNombre());
            pstmt.setString(2, pb.getDescripcion());
            pstmt.setBigDecimal(3, pb.getPrecio());
            pstmt.setInt(4, pb.getStock());
            pstmt.setInt(5, pb.getIdInventario());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al aprobar solicitud de producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectChange(int idProducto, String motivo) {
        System.out.println("Solicitud de cambio rechazada para producto ID: " + idProducto + ". Motivo: " + motivo);
        return true;
    }
}