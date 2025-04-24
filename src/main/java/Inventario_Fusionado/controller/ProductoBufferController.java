package Inventario_Fusionado.controller;

import Inventario_Fusionado.dao.productbufferDAO;
import Inventario_Fusionado.model.ProductoBuffer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductoBufferController", urlPatterns = {"/producto_buffer"})
public class ProductoBufferController extends HttpServlet {

    private productbufferDAO bufferDAO;

    @Override
    public void init() {
        bufferDAO = new productbufferDAO();
        System.out.println("ProductoBufferController inicializado.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        switch(action) {
            case "new":
                response.sendRedirect(request.getContextPath() + "/Tablas/producto_buffer/form-producto_buffer.jsp");
                break;
            case "edit": {
                int idBuffer;
                try {
                    idBuffer = Integer.parseInt(request.getParameter("id"));
                } catch(NumberFormatException | NullPointerException e) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=id_invalido");
                    return;
                }
                ProductoBuffer existente = bufferDAO.getBufferById(idBuffer);
                if (existente != null) {
                    response.sendRedirect(request.getContextPath() + "/Tablas/producto_buffer/form-producto_buffer.jsp?id=" + idBuffer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=no_encontrado");
                }
                break;
            }
            case "delete":
                deleteProductoBuffer(request, response);
                break;
            case "list":
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer");
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
                insertProductoBuffer(request, response);
                break;
            case "update":
                updateProductoBuffer(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer");
                break;
        }
    }

    private void insertProductoBuffer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");
        String idInventarioStr = request.getParameter("idInventario");
        String estado = request.getParameter("estado");
        String idUsuarioSolicitudStr = request.getParameter("idUsuarioSolicitud");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=nombre_vacio");
            return;
        }

        BigDecimal precio = BigDecimal.ZERO;
        try {
            if (precioStr != null && !precioStr.trim().isEmpty()) {
                precio = new BigDecimal(precioStr.trim());
                if (precio.compareTo(BigDecimal.ZERO) < 0) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=precio_negativo");
                    return;
                }
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=precio_invalido");
            return;
        }

        int stock = 0;
        try {
            if (stockStr != null && !stockStr.trim().isEmpty()) {
                stock = Integer.parseInt(stockStr.trim());
                if (stock < 0) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=stock_negativo");
                    return;
                }
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=stock_invalido");
            return;
        }

        int idInventario = 0;
        try {
            if (idInventarioStr != null && !idInventarioStr.trim().isEmpty()) {
                idInventario = Integer.parseInt(idInventarioStr.trim());
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=idInventario_invalido");
            return;
        }

        int idUsuarioSolicitud = 0;
        try {
            if (idUsuarioSolicitudStr != null && !idUsuarioSolicitudStr.trim().isEmpty()) {
                idUsuarioSolicitud = Integer.parseInt(idUsuarioSolicitudStr.trim());
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=idUsuario_invalido");
            return;
        }

        if (estado == null || estado.trim().isEmpty()) {
            estado = "PENDIENTE_CREAR";
        }

        ProductoBuffer nuevo = new ProductoBuffer();
        nuevo.setNombre(nombre.trim());
        nuevo.setDescripcion(descripcion != null ? descripcion.trim() : "");
        nuevo.setPrecio(precio);
        nuevo.setStock(stock);
        nuevo.setIdInventario(idInventario);
        nuevo.setEstado(estado.trim());
        nuevo.setIdUsuarioSolicitud(idUsuarioSolicitud);
        nuevo.setTimestampSolicitud(LocalDateTime.now());

        int newId = bufferDAO.addProductoBuffer(nuevo);
        System.out.println("Resultado inserción ProductoBuffer ID: " + newId);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer");
    }

    private void updateProductoBuffer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idBuffer;
        try {
            idBuffer = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=id_invalido");
            return;
        }

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");
        String idInventarioStr = request.getParameter("idInventario");
        String estado = request.getParameter("estado");
        String idUsuarioSolicitudStr = request.getParameter("idUsuarioSolicitud");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=nombre_vacio");
            return;
        }

        BigDecimal precio = BigDecimal.ZERO;
        try {
            if (precioStr != null && !precioStr.trim().isEmpty()) {
                precio = new BigDecimal(precioStr.trim());
                if (precio.compareTo(BigDecimal.ZERO) < 0) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=precio_negativo");
                    return;
                }
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=precio_invalido");
            return;
        }

        int stock = 0;
        try {
            if (stockStr != null && !stockStr.trim().isEmpty()) {
                stock = Integer.parseInt(stockStr.trim());
                if (stock < 0) {
                    response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=stock_negativo");
                    return;
                }
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=stock_invalido");
            return;
        }

        int idInventario = 0;
        try {
            if (idInventarioStr != null && !idInventarioStr.trim().isEmpty()) {
                idInventario = Integer.parseInt(idInventarioStr.trim());
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=idInventario_invalido");
            return;
        }

        int idUsuarioSolicitud = 0;
        try {
            if (idUsuarioSolicitudStr != null && !idUsuarioSolicitudStr.trim().isEmpty()) {
                idUsuarioSolicitud = Integer.parseInt(idUsuarioSolicitudStr.trim());
            }
        } catch(NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=idUsuario_invalido");
            return;
        }

        if (estado == null || estado.trim().isEmpty()) {
            estado = "PENDIENTE_CREAR";
        }

        ProductoBuffer actualizado = new ProductoBuffer();
        actualizado.setIdBuffer(idBuffer);
        actualizado.setNombre(nombre.trim());
        actualizado.setDescripcion(descripcion != null ? descripcion.trim() : "");
        actualizado.setPrecio(precio);
        actualizado.setStock(stock);
        actualizado.setIdInventario(idInventario);
        actualizado.setEstado(estado.trim());
        actualizado.setIdUsuarioSolicitud(idUsuarioSolicitud);

        boolean success = bufferDAO.updateBufferState(idBuffer, actualizado.getEstado(),
                actualizado.getEstado().equalsIgnoreCase("RECHAZADO") ? actualizado.getDescripcion() : null);
        System.out.println("Actualización ProductoBuffer ID " + idBuffer + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer");
    }

    private void deleteProductoBuffer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idBuffer;
        try {
            idBuffer = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer&error=id_invalido");
            return;
        }
        boolean success = bufferDAO.deleteBufferEntry(idBuffer);
        System.out.println("Eliminación ProductoBuffer ID " + idBuffer + ": " + success);
        response.sendRedirect(request.getContextPath() + "/ruta?vista=producto_buffer");
    }
}
