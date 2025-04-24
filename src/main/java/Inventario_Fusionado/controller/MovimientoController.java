package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.movimientoDAO;
import Inventario_Fusionado.model.Movimiento;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MovimientoController", urlPatterns = {"/movimiento"})
public class MovimientoController extends HttpServlet {

    private movimientoDAO movimientoDAO;

    @Override
    public void init() {
        movimientoDAO = new movimientoDAO();
        System.out.println("MovimientoController inicializado.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch(action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteMovimiento(request, response);
                break;
            case "list":
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch(action) {
            case "insert":
                insertMovimiento(request, response);
                break;
            case "update_estado":
                updateMovimientoEstado(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
                break;
        }
    }

    private void listMovimientos(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/Tablas/movimiento/form-movimiento.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idMovimiento = Integer.parseInt(request.getParameter("id"));
        Movimiento movimiento = movimientoDAO.getMovimientoById(idMovimiento);
        // En una implementación real, sería mejor pasar los datos por sesión o una estructura compartida
        response.sendRedirect(request.getContextPath() + "/Tablas/movimiento/form-movimiento.jsp?id=" + idMovimiento);
    }

    private void insertMovimiento(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Movimiento nuevo = new Movimiento();
            nuevo.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
            nuevo.setFechaHora(LocalDateTime.parse(request.getParameter("fecha_hora")));
            nuevo.setTablaAfectada(request.getParameter("tabla_afectada"));
            nuevo.setIdRegistroAfectado(Integer.parseInt(request.getParameter("id_registro_afectado")));
            nuevo.setAccion(request.getParameter("accion"));
            nuevo.setEstado(request.getParameter("estado"));
            nuevo.setDetallesCambio(request.getParameter("detalles_cambio"));
            boolean success = movimientoDAO.addMovimiento(nuevo);
            System.out.println("Inserción Movimiento: " + success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
    }

    private void updateMovimientoEstado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idMovimiento = Integer.parseInt(request.getParameter("id"));
        String nuevoEstado = request.getParameter("estado");
        boolean success = movimientoDAO.updateEstado(idMovimiento, nuevoEstado);
        System.out.println("Actualización estado Movimiento ID " + idMovimiento + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
    }

    private void deleteMovimiento(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idMovimiento;
        try {
            idMovimiento = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos&error=id_invalido");
            return;
        }
        boolean success = movimientoDAO.deleteMovimiento(idMovimiento);
        System.out.println("Eliminación Movimiento ID " + idMovimiento + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=movimientos");
    }
}
