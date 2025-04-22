package Inventario_Fusionado.controller; // Ajusta el paquete

import Inventario_Fusionado.dao.InventoryDAO;
import Inventario_Fusionado.dao.ProductDAO; // Necesitaremos ProductDAO también
import Inventario_Fusionado.model.Inventory;
import Inventario_Fusionado.model.Product;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*; // Importa para anotaciones
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

// Mapea este servlet a la URL /inventario (o la que prefieras)
@WebServlet(name = "InventoryController", urlPatterns = {"/inventario"})
public class InventoryController extends HttpServlet {

    private InventoryDAO inventoryDAO;
    private ProductDAO productDAO; // Añade DAO de producto si lo necesitas aquí

    // Inicializa los DAOs cuando el servlet se carga por primera vez
    @Override
    public void init() {
        inventoryDAO = new InventoryDAO();
        productDAO = new ProductDAO(); // Inicializa también productDAO
        System.out.println("InventoryController inicializado con DAOs."); // Log simple
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Acción por defecto: listar inventarios
        }
        System.out.println("Acción GET recibida: " + action); // Log

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
        } catch (Exception e) {
            System.err.println("Error en doGet de InventoryController: " + e.getMessage());
            e.printStackTrace();
            // Podrías redirigir a una página de error
            request.setAttribute("errorMessage", "Ocurrió un error: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp"); // Necesitas crear error.jsp
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            // Si no hay acción en POST, quizás redirigir a la lista o mostrar error
            listInventarios(request, response);
            return;
        }
        System.out.println("Acción POST recibida: " + action); // Log

        try {
            switch (action) {
                case "insert":
                    insertInventory(request, response);
                    break;
                case "update":
                    updateInventory(request, response);
                    break;
                default:
                    listInventarios(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error en doPost de InventoryController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Ocurrió un error procesando el formulario: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    // --- Métodos de Acción ---

    private void listInventarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Intentando listar inventarios..."); // Log
        List<Inventory> listaInventarios = inventoryDAO.GetAllInventories();
        if (listaInventarios == null) {
            System.err.println("GetAllInventories devolvió null!");
            listaInventarios = new ArrayList<>(); // Evitar NullPointerException en JSP
        } else {
            System.out.println("Se obtuvieron " + listaInventarios.size() + " inventarios.");
        }
        request.setAttribute("listaInventarios", listaInventarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list-inventarios.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Mostrando formulario para nuevo inventario..."); // Log
        RequestDispatcher dispatcher = request.getRequestDispatcher("inventario-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditInventoryForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Mostrando formulario para editar inventario..."); // Log
        int id = Integer.parseInt(request.getParameter("id"));
        Inventory existingInventory = inventoryDAO.getInventoryById(id);
        if (existingInventory != null) {
            request.setAttribute("inventory", existingInventory);
            RequestDispatcher dispatcher = request.getRequestDispatcher("inventario-form.jsp");
            dispatcher.forward(request, response);
        } else {
            System.err.println("No se encontró inventario con ID: " + id + " para editar.");
            response.sendRedirect("inventario?action=list"); // Redirige si no existe
        }
    }

    private void insertInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Intentando insertar inventario..."); // Log
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        Inventory newInventory = new Inventory();
        newInventory.setNombre(nombre);
        newInventory.setDescripcion(descripcion);

        boolean success = inventoryDAO.addInventory(newInventory);
        if (success) {
            System.out.println("Inventario insertado con éxito.");
        } else {
            System.err.println("Fallo al insertar inventario.");
        }
        response.sendRedirect("inventario?action=list"); // Redirige a la lista después de insertar
    }

    private void updateInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Intentando actualizar inventario..."); // Log
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        Inventory inventory = new Inventory(id, nombre, descripcion);
        boolean success = inventoryDAO.updateInventory(inventory);
        if (success) {
            System.out.println("Inventario actualizado con éxito (ID: " + id + ")");
        } else {
            System.err.println("Fallo al actualizar inventario (ID: " + id + ")");
        }
        response.sendRedirect("inventario?action=list"); // Redirige a la lista
    }

    private void deleteInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Intentando borrar inventario..."); // Log
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = inventoryDAO.deleteInventory(id);
        if (success) {
            System.out.println("Inventario borrado con éxito (ID: " + id + ")");
        } else {
            System.err.println("Fallo al borrar inventario (ID: " + id + ")");
        }
        response.sendRedirect("inventario?action=list"); // Redirige a la lista
    }
}