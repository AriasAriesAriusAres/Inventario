<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
    List<Inventory> inventarios = (List<Inventory>) request.getAttribute("inventarios");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Inventarios</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container fade-in">
    <h1>Inventario Actual</h1>
    <div class="table-container">
        <table class="styled-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Cantidad</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <% if (inventarios != null) {
                for (Inventory inv : inventarios) { %>
            <tr>
                <td><%= inv.getId() %></td>
                <td><%= inv.getNombreProducto() %></td>
                <td><%= inv.getCantidad() %></td>
                <td>
                    <a href="inventario?action=edit&id=<%= inv.getId() %>">Editar</a> |
                    <a href="inventario?action=delete&id=<%= inv.getId() %>" onclick="return confirm('¿Estás seguro de eliminar este inventario?');">Eliminar</a>
                </td>
            </tr>
            <% }
            } else { %>
            <tr>
                <td colspan="4">No hay inventarios registrados.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
