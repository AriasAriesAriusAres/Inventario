package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.usuarioDAO;
import Inventario_Fusionado.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UsuarioController", urlPatterns = {"/usuario"})
public class UsuarioController extends HttpServlet {

    private usuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new usuarioDAO();
        System.out.println("UsuarioController inicializado.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch(action) {
            case "new":
                response.sendRedirect(request.getContextPath() + "/Tablas/usuario/form-usuario.jsp");
                break;
            case "edit": {
                int idUsuario;
                try {
                    idUsuario = Integer.parseInt(request.getParameter("id"));
                } catch(NumberFormatException | NullPointerException e) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=id_invalido");
                    return;
                }
                Usuario existente = usuarioDAO.getUsuarioById(idUsuario);
                if (existente != null) {
                    response.sendRedirect(request.getContextPath() + "/Tablas/usuario/form-usuario.jsp?id=" + idUsuario);
                } else {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=no_encontrado");
                }
                break;
            }
            case "delete":
                deleteUsuario(request, response);
                break;
            case "list":
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario");
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
                insertUsuario(request, response);
                break;
            case "update":
                updateUsuario(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario");
                break;
        }
    }

    private void insertUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=campos_requeridos");
            return;
        }
        Usuario nuevo = new Usuario();
        nuevo.setUsername(username.trim());
        nuevo.setPassword(password.trim());
        boolean success = usuarioDAO.registrarUsuario(nuevo);
        System.out.println("Registro Usuario exitoso: " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario");
    }

    private void updateUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=id_invalido");
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=campos_requeridos&id=" + idUsuario);
            return;
        }
        Usuario actualizado = new Usuario();
        actualizado.setIdUsuario(idUsuario);
        actualizado.setUsername(username.trim());
        actualizado.setPassword(password.trim());
        boolean success = usuarioDAO.updateUsuario(actualizado);
        System.out.println("Actualización Usuario ID " + idUsuario + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario");
    }

    private void deleteUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=id_invalido");
            return;
        }
        boolean success = usuarioDAO.eliminarUsuario(idUsuario);
        System.out.println("Eliminación Usuario ID " + idUsuario + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario");
    }
}
