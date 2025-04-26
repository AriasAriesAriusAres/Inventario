package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.ProductDAO;
import Inventario_Fusionado.dao.movimientoDAO;
import Inventario_Fusionado.model.Product;
import Inventario_Fusionado.model.Movimiento;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Controlador para manejar las acciones relacionadas con productos.
 * Ruta base: /producto
 */
@WebServlet(name = "ProductController", urlPatterns = {"/producto"})
public class ProductController extends HttpServlet {

    public static final String ATTR_MESSAGE = "message";

    private ProductDAO productDAO;
    private movimientoDAO movimientoDAO;

    @Override
    public void init() {
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
                RequestDispatcher rdForm = request.getRequestDispatcher("/Tablas/producto/product-form.jsp");
                rdForm.forward(request, response);
                break;
            case "list":
            default:
                List<Product> lista = productDAO.getAllProducts();
                request.setAttribute("productos", lista != null ? lista : new ArrayList<>());
                RequestDispatcher rdList = request.getRequestDispatcher("/Tablas/producto/product-list.jsp");
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
        switch (action) {
            case "insert":
                try {
                    Product newProduct = new Product();
                    newProduct.setNombre(request.getParameter("nombre"));
                    newProduct.setDescripcion(request.getParameter("descripcion"));
                    newProduct.setPrecio(new BigDecimal(request.getParameter("precio")));
                    newProduct.setStock(Integer.parseInt(request.getParameter("stock")));
                    newProduct.setIdInventario(Integer.parseInt(request.getParameter("id_inventario")));

                    boolean success = productDAO.addProduct(newProduct);
                    message = success ? "Producto creado correctamente." : "Error al crear el producto.";

                    // registrar movimiento con el DAO existente
                    Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                    Movimiento mov = new Movimiento();
                    mov.setIdUsuario(usuario.getIdUsuario());
                    mov.setFechaHora(LocalDateTime.now());
                    mov.setTablaAfectada("productos");
                    mov.setIdRegistroAfectado(newProduct.getIdProducto());
                    mov.setAccion("INSERT");
                    mov.setEstado(message);
                    movimientoDAO.addMovimiento(mov);
                } catch (Exception e) {
                    message = "Error al crear el producto: " + e.getMessage();
                }
                break;
            case "edit":
                try {
                    Product p = new Product();
                    p.setIdProducto(Integer.parseInt(request.getParameter("id")));
                    p.setNombre(request.getParameter("nombre"));
                    p.setDescripcion(request.getParameter("descripcion"));
                    p.setPrecio(new BigDecimal(request.getParameter("precio")));
                    p.setStock(Integer.parseInt(request.getParameter("stock")));
                    p.setIdInventario(Integer.parseInt(request.getParameter("id_inventario")));

                    boolean success = productDAO.updateProduct(p);
                    message = success ? "Producto actualizado correctamente." : "Error al actualizar el producto.";

                    Usuario usuarioU = (Usuario) request.getSession().getAttribute("usuario");
                    Movimiento movU = new Movimiento();
                    movU.setIdUsuario(usuarioU.getIdUsuario());
                    movU.setFechaHora(LocalDateTime.now());
                    movU.setTablaAfectada("productos");
                    movU.setIdRegistroAfectado(p.getIdProducto());
                    movU.setAccion("UPDATE");
                    movU.setEstado(message);
                    movimientoDAO.addMovimiento(movU);
                } catch (Exception e) {
                    message = "Error al actualizar el producto: " + e.getMessage();
                }
                break;
            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean success = productDAO.deleteProduct(id);
                    message = success ? "Producto eliminado correctamente." : "Error al eliminar el producto.";

                    Usuario usuarioD = (Usuario) request.getSession().getAttribute("usuario");
                    Movimiento movD = new Movimiento();
                    movD.setIdUsuario(usuarioD.getIdUsuario());
                    movD.setFechaHora(LocalDateTime.now());
                    movD.setTablaAfectada("productos");
                    movD.setIdRegistroAfectado(id);
                    movD.setAccion("DELETE");
                    movD.setEstado(message);
                    movimientoDAO.addMovimiento(movD);
                } catch (Exception e) {
                    message = "Error al eliminar el producto: " + e.getMessage();
                }
                break;
            default:
                break;
        }

        List<Product> lista = productDAO.getAllProducts();
        request.setAttribute("productos", lista != null ? lista : new ArrayList<>());
        if (message != null) request.setAttribute(ATTR_MESSAGE, message);
        RequestDispatcher rd = request.getRequestDispatcher("/Tablas/producto/product-list.jsp");
        rd.forward(request, response);
    }
}
