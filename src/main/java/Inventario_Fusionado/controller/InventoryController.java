package Inventario_Fusionado.controller; // Ajusta el paquete si es necesario

import Inventario_Fusionado.dao.InventoryDAO;
import Inventario_Fusionado.dao.ProductDAO;
import Inventario_Fusionado.model.Inventory;
import Inventario_Fusionado.model.Product;

// Imports de Jakarta EE para Servlets
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
// import java.math.BigDecimal; // No es necesario aquí si no se usa directamente
import java.sql.SQLException; // <--- ¡IMPORT AÑADIDO! (Aunque eliminaremos los throws)
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InventoryController", urlPatterns = {"/inventario"})
public class InventoryController extends HttpServlet {

    private InventoryDAO inventoryDAO;
    // Advertencia: productDAO no se usa aún, se puede eliminar o usar más tarde.
    private ProductDAO productDAO;

    @Override
    public void init() {
        inventoryDAO = new InventoryDAO();
        productDAO = new ProductDAO();
        System.out.println("InventoryController inicializado.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        System.out.println("[GET] Action: " + action);

        try {
            switch (action) {
                case "new":
                    showNewInventoryForm(request, response);
                    break;
                case "edit":
                    showEditInventoryForm(request, response);
                    break;
                case "delete":
                    deleteInventory(request, response);
                    break;
                case "list":
                default:
                    listInventarios(request, response);
                    break;
            }
            // No capturamos SQLException aquí porque los DAOs ya lo hacen (imprimen stacktrace)
        } catch (Exception e) {
            handleError(request, response, "Error en GET", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/inventario?action=list");
            return;
        }
        System.out.println("[POST] Action: " + action);

        try {
            switch (action) {
                case "insert":
                    insertInventory(request, response);
                    break;
                case "update":
                    updateInventory(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/inventario?action=list");
                    break;
            }
            // No capturamos SQLException aquí
        } catch (Exception e) {
            handleError(request, response, "Error en POST", e);
        }
    }

    // --- Métodos de Acción (SIN throws SQLException) ---

    // Ya no necesita throws SQLException porque el DAO lo maneja internamente
    private void listInventarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando listInventarios...");
        List<Inventory> listaInventarios = inventoryDAO.GetAllInventories();
        if (listaInventarios == null) {
            System.err.println("WARN: inventoryDAO.GetAllInventories() devolvió null.");
            listaInventarios = new ArrayList<>();
        } else {
            System.out.println("Inventarios obtenidos: " + listaInventarios.size());
        }
        request.setAttribute("listaInventarios", listaInventarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-inventarios.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando showNewInventoryForm...");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/inventario-form.jsp");
        dispatcher.forward(request, response);
    }

    // Ya no necesita throws SQLException
    private void showEditInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando showEditInventoryForm...");
        int id = 0; // Inicialización redundante eliminada si se asigna en try
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID inválido para editar: " + request.getParameter("id"));
            response.sendRedirect(request.getContextPath() + "/inventario?action=list&error=id_invalido");
            return;
        }

        Inventory existingInventory = inventoryDAO.getInventoryById(id);
        if (existingInventory != null) {
            request.setAttribute("inventory", existingInventory);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/inventario-form.jsp");
            dispatcher.forward(request, response);
        } else {
            System.err.println("WARN: No se encontró inventario ID=" + id + " para editar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=list");
        }
    }

    // Ya no necesita throws SQLException
    private void insertInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando insertInventory...");
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre de inventario vacío.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=nombre_vacio");
            return;
        }

        Inventory newInventory = new Inventory();
        newInventory.setNombre(nombre.trim());
        newInventory.setDescripcion(descripcion != null ? descripcion.trim() : "");

        boolean success = inventoryDAO.addInventory(newInventory);
        System.out.println("Resultado de inserción: " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=list");
    }

    // Ya no necesita throws SQLException
    private void updateInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando updateInventory...");
        int id = 0;
        try {
            // Asegúrate que el input se llame 'idInventario' en el form JSP
            id = Integer.parseInt(request.getParameter("idInventario"));
        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID de inventario inválido o faltante para actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=list&error=id_invalido");
            return;
        }

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre de inventario vacío al actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=nombre_vacio");
            return;
        }

        Inventory inventory = new Inventory(id, nombre.trim(), descripcion != null ? descripcion.trim() : "");
        boolean success = inventoryDAO.updateInventory(inventory);
        System.out.println("Resultado de actualización (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=list");
    }

    // Ya no necesita throws SQLException
    private void deleteInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando deleteInventory...");
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID de inventario inválido para borrar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=list&error=id_invalido");
            return;
        }
        boolean success = inventoryDAO.deleteInventory(id);
        System.out.println("Resultado de borrado (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=list");
    }

    // Método centralizado para manejo de errores (opcional)
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message, Exception e) throws ServletException, IOException {
        System.err.println("ERROR en InventoryController (" + message + "): " + e.getClass().getName() + " - " + e.getMessage());
        // e.printStackTrace(); // Mantenlo si quieres ver el stack trace completo en logs
        request.setAttribute("errorMessage", "Ocurrió un error inesperado: " + e.getMessage());
        // Asegúrate de tener una página error.jsp en /src/main/webapp/
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        if (!response.isCommitted()) { // Evita error si ya se envió respuesta
            dispatcher.forward(request, response);
        } else {
            System.err.println("WARN: La respuesta ya estaba comprometida. No se puede redirigir a la página de error.");
        }
    }
}