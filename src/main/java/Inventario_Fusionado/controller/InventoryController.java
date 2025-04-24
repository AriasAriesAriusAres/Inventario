package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.InventoryDAO;
import Inventario_Fusionado.model.Inventory;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InventoryController", urlPatterns = {"/inventario"})
public class InventoryController extends HttpServlet {

    private InventoryDAO inventoryDAO;

    @Override
    public void init() {
        inventoryDAO = new InventoryDAO();
        System.out.println("InventoryController inicializado.");

        // --- Código para insertar inventario de prueba al iniciar (si está vacío) ---
        /* // Descomentar si se desea esta funcionalidad de prueba
        List<Inventory> existentes = inventoryDAO.getAllInventories();
        if (existentes == null || existentes.isEmpty()) {
            Inventory inventarioTest = new Inventory();
            // Asumiendo que el modelo Inventory tiene setId, setNombreProducto, setCantidad
            // inventarioTest.setId(0); // Opcional, si el ID es autoincremental
            inventarioTest.setNombreProducto("Inventario de Prueba Inicial");
            inventarioTest.setCantidad(100);
            inventoryDAO.addInventory(inventarioTest);
            System.out.println("Inventario de prueba insertado al iniciar.");
        } else {
            System.out.println("Inventarios ya existentes: " + existentes.size());
        }
        */
        // --- Fin código de prueba ---
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Acción por defecto
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
                case "viewCascade": // Acción para la nueva vista
                    visualizarInventariosEnCascada(request, response);
                    break;
                case "list": // Acción original para la tabla (puede coexistir o eliminarse)
                    listInventarios(request, response);
                    // O redirigir a la nueva vista por defecto:
                    // visualizarInventariosEnCascada(request, response);
                    break;
                default: // Acción desconocida, redirigir a la vista por defecto
                    visualizarInventariosEnCascada(request, response);
                    // O listInventarios(request, response);
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
            // Si no hay acción en POST, redirigir a la lista (o vista por defecto)
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade"); // O action=list
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
                default: // Acción POST desconocida
                    response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade"); // O action=list
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Error en POST", e);
        }
    }

    // Método para la vista de tabla original
    private void listInventarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando listInventarios...");
        List<Inventory> listaInventarios = inventoryDAO.getAllInventories();
        if (listaInventarios == null) {
            System.err.println("WARN: inventoryDAO.getAllInventories() devolvió null.");
            listaInventarios = new ArrayList<>(); // Evitar NullPointerException en JSP
        } else {
            System.out.println("Inventarios obtenidos para tabla: " + listaInventarios.size());
        }
        request.setAttribute("inventarios", listaInventarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list-inventarios.jsp");
        dispatcher.forward(request, response);
    }

    // Método para la nueva vista en cascada
    private void visualizarInventariosEnCascada(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando visualizarInventariosEnCascada...");
        List<Inventory> listaInventarios = inventoryDAO.getAllInventories();
        if (listaInventarios == null) {
            System.err.println("WARN: visualizarInventariosEnCascada: listaInventarios es null.");
            listaInventarios = new ArrayList<>(); // Evitar NullPointerException en JSP
        } else {
            System.out.println("visualizarInventariosEnCascada: cantidad de inventarios = " + listaInventarios.size());
        }
        request.setAttribute("inventarios", listaInventarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("visualizar-inventarios.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando showNewInventoryForm...");
        // No es necesario pasar un objeto Inventory vacío si el JSP lo maneja
        // request.setAttribute("inventario", new Inventory());
        RequestDispatcher dispatcher = request.getRequestDispatcher("inventario-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Ejecutando showEditInventoryForm...");
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) { // Capturar NullPointer si 'id' es null
            System.err.println("ERROR: ID inválido o nulo para editar: '" + request.getParameter("id") + "'");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido"); // O action=list
            return;
        }

        Inventory existingInventory = inventoryDAO.getInventoryById(id);
        if (existingInventory != null) {
            request.setAttribute("inventario", existingInventory); // Pasar el objeto al JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("inventario-form.jsp");
            dispatcher.forward(request, response);
        } else {
            System.err.println("WARN: No se encontró inventario ID=" + id + " para editar.");
            // Redirigir con mensaje de error (opcional)
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=no_encontrado"); // O action=list
        }
    }

    private void insertInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando insertInventory...");
        // !! Ajustar aquí si cambias el name en el JSP a 'nombre_producto' !!
        String nombre = request.getParameter("nombre");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad = 0; // Valor por defecto

        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre de inventario vacío.");
            // Considera reenviar al formulario con error en lugar de redirigir directamente
            // request.setAttribute("errorNombre", "El nombre no puede estar vacío");
            // showNewInventoryForm(request, response); // Requeriría cambio en firma o manejo de error en JSP
            response.sendRedirect(request.getContextPath() + "/inventario?action=new&error=nombre_vacio"); // Simple redirect por ahora
            return;
        }

        // Validar cantidad
        try {
            if (cantidadStr != null && !cantidadStr.trim().isEmpty()){
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
        newInventory.setNombreProducto(nombre.trim()); // Usa el método correcto del modelo
        newInventory.setCantidad(cantidad); // Usa el método correcto del modelo

        boolean success = inventoryDAO.addInventory(newInventory);
        System.out.println("Resultado de inserción: " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade"); // O action=list
    }

    private void updateInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando updateInventory...");
        int id;
        int cantidad = 0; // Valor por defecto

        // Validar ID
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("ERROR: ID inválido o nulo para actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido"); // O action=list
            return;
        }

        // Validar nombre
        // !! Ajustar aquí si cambias el name en el JSP a 'nombre_producto' !!
        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("ERROR: Nombre vacío al actualizar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=edit&id=" + id + "&error=nombre_vacio");
            return;
        }

        // Validar cantidad
        String cantidadStr = request.getParameter("cantidad");
        try {
            if (cantidadStr != null && !cantidadStr.trim().isEmpty()){
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

        // Usar constructor que incluye ID o crear objeto y setear ID
        Inventory inventory = new Inventory(id, nombre.trim(), cantidad);
        // Opcionalmente: verificar si el inventario existe antes de actualizar
        // Inventory existing = inventoryDAO.getInventoryById(id);
        // if (existing == null) { ... manejar error ... }

        boolean success = inventoryDAO.updateInventory(inventory);
        System.out.println("Resultado de actualización (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade"); // O action=list
    }

    private void deleteInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Ejecutando deleteInventory...");
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("ERROR: ID de inventario inválido o nulo para borrar.");
            response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade&error=id_invalido"); // O action=list
            return;
        }
        // Opcionalmente: verificar dependencias (ej: productos asociados) antes de borrar
        boolean success = inventoryDAO.deleteInventory(id);
        System.out.println("Resultado de borrado (ID: "+id+"): " + success);
        response.sendRedirect(request.getContextPath() + "/inventario?action=viewCascade"); // O action=list
    }

    // Manejador de errores genérico
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message, Exception e) throws ServletException, IOException {
        System.err.println("ERROR en InventoryController (" + message + "): " + e.getClass().getName() + " - " + e.getMessage());
        // Loggear el stack trace completo para diagnóstico
        e.printStackTrace();
        request.setAttribute("errorMessage", "Ocurrió un error inesperado procesando su solicitud. Por favor, intente más tarde.");
        // Intentar reenviar a una página de error genérica si existe
        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp"); // Asegúrate que error.jsp exista
        try {
            if (dispatcher != null && !response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500
                dispatcher.forward(request, response);
            } else if (!response.isCommitted()){
                // Si no hay error.jsp, enviar error HTTP genérico
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error interno.");
            } else {
                System.err.println("WARN: La respuesta ya estaba comprometida. No se puede mostrar la página de error.");
            }
        } catch(Exception ex) {
            // Error al intentar mostrar la página de error
            System.err.println("ERROR adicional al intentar mostrar la página de error: " + ex.getMessage());
            if(!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error interno grave.");
            }
        }
    }
}