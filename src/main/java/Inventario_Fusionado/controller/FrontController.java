package Inventario_Fusionado.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ruta")
public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vista = request.getParameter("vista");
        if (vista == null || vista.trim().isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Si es una vista independiente, no va a index.jsp
        if (vista.equals("indextablas")) {
            request.getRequestDispatcher("/indextablas.jsp").forward(request, response);
            return;
        }

        // Todas las demás vistas se resuelven desde index.jsp
        request.setAttribute("vista", vista);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
