package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.productbufferDAO;
import Inventario_Fusionado.dao.ProductDAO;
import Inventario_Fusionado.dao.movimientoDAO;
import Inventario_Fusionado.model.ProductoBuffer;
import Inventario_Fusionado.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para manejar las acciones relacionadas con productos
 * mediante flujo de aprobación. Ruta base: /producto/*
 */
@WebServlet(name = "ProductController", urlPatterns = {"/producto"})
public class ProductController extends HttpServlet {

    private productbufferDAO productBufferDAO;
    private ProductDAO productDAO;
    private movimientoDAO movimientoDAO;

    @Override
    public void init() {
        productBufferDAO = new productbufferDAO();
        productDAO = new ProductDAO();
        movimientoDAO = new movimientoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "form":
                response.sendRedirect(request.getContextPath() + "/Tablas/producto/product-form.jsp");
                break;
            case "list":
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=productos");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Capturar datos del formulario
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precio = request.getParameter("precio");
        String stock = request.getParameter("stock");
        String idInventario = request.getParameter("id_inventario");

        // Obtener el usuario en sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        ProductoBuffer nuevo = new ProductoBuffer();
        nuevo.setNombre(nombre);
        nuevo.setDescripcion(descripcion);
        nuevo.setPrecio(new java.math.BigDecimal(precio));
        nuevo.setStock(Integer.parseInt(stock));
        nuevo.setIdInventario(Integer.parseInt(idInventario));
        nuevo.setEstado("PENDIENTE_CREAR");
        nuevo.setIdUsuarioSolicitud(usuario.getIdUsuario());

        productBufferDAO.addProductoBuffer(nuevo);

        response.sendRedirect(request.getContextPath() + "/ruta?vista=productos");
    }
}
