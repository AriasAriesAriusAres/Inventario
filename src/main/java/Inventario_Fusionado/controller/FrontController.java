package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.ProductDAO;
import Inventario_Fusionado.dao.inventoryDAO;
import Inventario_Fusionado.dao.movimientoDAO;
import Inventario_Fusionado.dao.usuarioDAO;
import Inventario_Fusionado.model.Product;
import Inventario_Fusionado.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * FrontController mejora: carga datos para vistas antes de incluir JSP.
 */
@WebServlet("/ruta")
public class FrontController extends HttpServlet {
    private ProductDAO productDAO;
    private usuarioDAO usuarioDAO;
    private inventoryDAO inventoryDAO;
    private movimientoDAO movimientoDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        usuarioDAO = new usuarioDAO();
        inventoryDAO = new inventoryDAO();
        movimientoDAO = new movimientoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vista = request.getParameter("vista");
        if (vista == null || vista.trim().isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        // cargar datos según la vista
        switch (vista) {
            case "productos":
                List<Product> productos = productDAO.getAllProducts();
                request.setAttribute("productos", productos);
                break;
            case "usuarios":
                List<Usuario> usuarios = usuarioDAO.getAllUsuarios();
                request.setAttribute("usuarios", usuarios);
                break;
            case "inventarios":
                request.setAttribute("inventarios", inventoryDAO.getAllInventories());
                break;
            case "movimientos":
                request.setAttribute("movimientos", movimientoDAO.getAllMovimientos());
                break;
            // ya no usamos buffer
        }

        // manejar vista independiente
        if ("indextablas".equals(vista)) {
            request.getRequestDispatcher("/indextablas.jsp").forward(request, response);
            return;
        }

        request.setAttribute("vista", vista);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
