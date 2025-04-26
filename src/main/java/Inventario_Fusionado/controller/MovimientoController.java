package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.movimientoDAO;
import Inventario_Fusionado.model.Movimiento;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador para manejar las acciones CRUD de Movimientos
 * Ruta base: /movimiento
 */
@WebServlet(name = "MovimientoController", urlPatterns = {"/movimiento"})
public class MovimientoController extends HttpServlet {

    private movimientoDAO movimientoDAO;
    public static final String ATTR_MESSAGE = "message";

    @Override
    public void init() {
        movimientoDAO = new movimientoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
            default:
                List<Movimiento> lista = movimientoDAO.getAllMovimientos();
                // dummy si lista vacía
                if (lista == null || lista.isEmpty()) {
                    lista = new ArrayList<>();
                    lista.add(new Movimiento()); // objeto vacío como dummy
                }
                request.setAttribute("movimientos", lista);
                String msg = (String) request.getAttribute(ATTR_MESSAGE);
                if (msg != null) {
                    request.setAttribute(ATTR_MESSAGE, msg);
                }
                RequestDispatcher rd = request.getRequestDispatcher("/Tablas/movimiento/list-movimientos.jsp");
                rd.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "insert";
        String message = null;

        switch (action) {
            case "insert":
                insertMovimiento(request);
                message = "Movimiento creado correctamente.";
                break;
            case "edit":
                updateMovimientoEstado(request);
                message = "Movimiento actualizado correctamente.";
                break;
            case "delete":
                deleteMovimiento(request);
                message = "Movimiento eliminado correctamente.";
                break;
            default:
                break;
        }

        // recargar lista y mostrar
        List<Movimiento> lista = movimientoDAO.getAllMovimientos();
        if (lista == null || lista.isEmpty()) {
            lista = new ArrayList<>();
            lista.add(new Movimiento());
        }
        request.setAttribute("movimientos", lista);
        if (message != null) {
            request.setAttribute(ATTR_MESSAGE, message);
        }
        RequestDispatcher rd = request.getRequestDispatcher("/Tablas/movimiento/list-movimientos.jsp");
        rd.forward(request, response);
    }

    // helpers
    private void insertMovimiento(HttpServletRequest request) {
        try {
            Movimiento nuevo = new Movimiento();
            nuevo.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
            // Si deseas parsear fecha de otra manera, ajusta aquí
            nuevo.setFechaHoraFromString(request.getParameter("fecha_hora"));
            nuevo.setTablaAfectada(request.getParameter("tabla_afectada"));
            nuevo.setIdRegistroAfectado(Integer.parseInt(request.getParameter("id_registro_afectado")));
            nuevo.setAccion(request.getParameter("accion"));
            nuevo.setEstado(request.getParameter("estado"));
            nuevo.setDetallesCambio(request.getParameter("detalles_cambio"));
            movimientoDAO.addMovimiento(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMovimientoEstado(HttpServletRequest request) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nuevoEstado = request.getParameter("estado");
            movimientoDAO.updateEstado(id, nuevoEstado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMovimiento(HttpServletRequest request) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            movimientoDAO.deleteMovimiento(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
