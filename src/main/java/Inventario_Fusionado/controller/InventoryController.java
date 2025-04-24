package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.inventoryDAO;
import Inventario_Fusionado.model.Inventory;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InventoryController", urlPatterns = {"/inventario"})
public class InventoryController extends HttpServlet {

    private inventoryDAO inventoryDAO;

    @Override
    public void init() {
        inventoryDAO = new inventoryDAO();
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
                case "viewCascade":
                case "list":
                default:
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=inventarios");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Error en GET", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade");
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
                    response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade");
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Error en POST", e);
        }
    }

    private void listInventarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/ruta?vista=inventarios");
    }

    private void visualizarInventariosEnCascada(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/ruta?vista=inventarios");
    }

    private void showNewInventoryForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando showNewInventoryForm...");
        response.sendRedirect(request.getContextPath() + "/Tablas/Inventario/inventario-form.jsp");
    }

    private void showEditInventoryForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando showEditInventoryForm...");
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("ERROR: ID inválido o nulo para editar: '" + request.getParameter("id") + "'");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido");
            return;
        }

        Inventory existingInventory = inventoryDAO.getInventoryById(id);
        if (existingInventory != null) {
            response.sendRedirect(request.getContextPath() + "/Tablas/Inventario/inventario-form.jsp?id=" + id);
        } else {
            System.err.println("WARN: No se encontró inventario ID=" + id + " para editar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=no_encontrado");
        }
    }

    private void insertInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando insertInventory...");
        String nombre = request.getParameter("nombre");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad = 0;

        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre de inventario vacío.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=nombre_vacio");
            return;
        }

        try {
            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadStr.trim());
                if (cantidad < 0) {
                    System.err.println("ERROR: Cantidad no puede ser negativa.");
                    response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=cantidad_negativa");
                    return;
                }
            } else {
                System.err.println("ERROR: Cantidad vacía.");
                response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=cantidad_vacia");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Cantidad inválida (no es número).");
            response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=cantidad_invalida");
            return;
        }

        Inventory newInventory = new Inventory();
        newInventory.setNombreProducto(nombre.trim());
        newInventory.setCantidad(cantidad);

        boolean success = inventoryDAO.addInventory(newInventory);
        System.out.println("Resultado de inserción: " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade");
    }

    private void updateInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando updateInventory...");
        int id;
        int cantidad = 0;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("ERROR: ID inválido o nulo para actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido");
            return;
        }

        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre vacío al actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=nombre_vacio");
            return;
        }

        String cantidadStr = request.getParameter("cantidad");
        try {
            if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                cantidad = Integer.parseInt(cantidadStr.trim());
                if (cantidad < 0) {
                    System.err.println("ERROR: Cantidad no puede ser negativa.");
                    response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=cantidad_negativa");
                    return;
                }
            } else {
                System.err.println("ERROR: Cantidad vacía.");
                response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=cantidad_vacia");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Cantidad inválida (no es número).");
            response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=cantidad_invalida");
            return;
        }

        Inventory inventory = new Inventory(id, nombre.trim(), cantidad);
        boolean success = inventoryDAO.updateInventory(inventory);
        System.out.println("Resultado de actualización (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade");
    }

    private void deleteInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando deleteInventory...");
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("ERROR: ID de inventario inválido o nulo para borrar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido");
            return;
        }
        boolean success = inventoryDAO.deleteInventory(id);
        System.out.println("Resultado de borrado (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message, Exception e) throws ServletException, IOException {
        System.err.println("ERROR en InventoryController (" + message + "): " + e.getClass().getName() + " - " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("errorMessage", "Ocurrió un error inesperado procesando su solicitud. Por favor, intente más tarde.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
        try {
            if (dispatcher != null && !response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                dispatcher.forward(request, response);
            } else if (!response.isCommitted()){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error interno.");
            } else {
                System.err.println("WARN: La respuesta ya estaba comprometida. No se puede mostrar la página de error.");
            }
        } catch(Exception ex) {
            System.err.println("ERROR adicional al intentar mostrar la página de error: " + ex.getMessage());
            if(!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error interno grave.");
            }
        }
    }
}
