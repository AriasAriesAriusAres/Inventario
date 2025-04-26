package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.usuarioDAO;
import Inventario_Fusionado.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "UsuarioController", urlPatterns = {"/usuario"})
public class UsuarioController extends HttpServlet {

    public static final String ATTR_MESSAGE = "message";

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
                RequestDispatcher rdNew = request.getRequestDispatcher("/Tablas/usuario/form-usuario.jsp");
                rdNew.forward(request, response);
                break;
            case "edit": {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=usuario&error=id_invalido");
                    return;
                }
                Usuario user = usuarioDAO.getUsuarioById(id);
                if (user != null) {
                    request.setAttribute("usuarioEdit", user);
                    RequestDispatcher rdEdit = request.getRequestDispatcher("/Tablas/usuario/form-usuario.jsp");
                    rdEdit.forward(request, response);
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
                List<Usuario> list = usuarioDAO.getAllUsuarios();
                if (list == null || list.isEmpty()) {
                    list = new ArrayList<>();
                    list.add(new Usuario(-1, "-Sin usuarios-", ""));
                    list.add(new Usuario(-2, "-Sin usuarios-", ""));
                    list.add(new Usuario(-3, "-Sin usuarios-", ""));
                }
                request.setAttribute("usuarios", list);
                RequestDispatcher rdList = request.getRequestDispatcher("/Tablas/usuario/list-usuarios.jsp");
                rdList.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "insert";
        String message = null;
        switch(action) {
            case "insert":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                    message = "Usuario y contraseña son requeridos.";
                } else {
                    Usuario nuevo = new Usuario();
                    nuevo.setUsername(username.trim());
                    nuevo.setPassword(password.trim());
                    boolean ok = usuarioDAO.registrarUsuario(nuevo);
                    message = ok ? "Usuario creado correctamente." : "Error al crear usuario.";
                }
                break;
            case "update":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String user2 = request.getParameter("username");
                    String pass2 = request.getParameter("password");
                    if (user2 == null || user2.trim().isEmpty() || pass2 == null || pass2.trim().isEmpty()) {
                        message = "Usuario y contraseña son requeridos.";
                    } else {
                        Usuario upd = new Usuario();
                        upd.setIdUsuario(id);
                        upd.setUsername(user2.trim());
                        upd.setPassword(pass2.trim());
                        boolean ok2 = usuarioDAO.updateUsuario(upd);
                        message = ok2 ? "Usuario actualizado correctamente." : "Error al actualizar usuario.";
                    }
                } catch(Exception e) {
                    message = "ID inválido.";
                }
                break;
            default:
                break;
        }
        // after CUD, show list
        List<Usuario> list = usuarioDAO.getAllUsuarios();
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new Usuario(-1, "-Sin usuarios-", ""));
            list.add(new Usuario(-2, "-Sin usuarios-", ""));
            list.add(new Usuario(-3, "-Sin usuarios-", ""));
        }
        request.setAttribute("usuarios", list);
        if (message != null) request.setAttribute(ATTR_MESSAGE, message);
        RequestDispatcher rd = request.getRequestDispatcher("/Tablas/usuario/list-usuarios.jsp");
        rd.forward(request, response);
    }

    private void deleteUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            request.setAttribute(ATTR_MESSAGE, "ID inválido para borrado.");
            doGet(request, response);
            return;
        }
        boolean success = usuarioDAO.eliminarUsuario(idUsuario);
        System.out.println("Eliminación Usuario ID " + idUsuario + ": " + success);
        request.setAttribute(ATTR_MESSAGE, success ? "Usuario eliminado correctamente." : "Error al eliminar usuario.");
        doGet(request, response);
    }
}
