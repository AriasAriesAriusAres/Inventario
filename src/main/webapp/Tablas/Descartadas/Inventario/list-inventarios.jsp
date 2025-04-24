<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
    List<Inventory> inventarios = (List<Inventory>) request.getAttribute("inventarios");
    String contextPath = request.getContextPath();
%>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <div class="card shadow rounded-4 p-4">
                <h2 class="text-center mb-4">Listado de Inventarios</h2>

                <div class="d-flex justify-content-end mb-3">
                    <a href="<%= contextPath %>/inventario?action=new" class="btn btn-success">Nuevo Inventario</a>
                </div>

                <table class="table table-bordered table-striped table-hover align-middle text-center">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Nombre del Producto</th>
                        <th>Cantidad</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (inventarios != null && !inventarios.isEmpty()) {
                        for (Inventory inv : inventarios) { %>
                    <tr>
                        <td><%= inv.getId() %></td>
                        <td><%= inv.getNombreProducto() %></td>
                        <td><%= inv.getCantidad() %></td>
                        <td>
                            <a href="<%= contextPath %>/inventario?action=edit&id=<%= inv.getId() %>" class="btn btn-warning btn-sm">Editar</a>
                            <a href="<%= contextPath %>/inventario?action=delete&id=<%= inv.getId() %>" class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Estás seguro de eliminar este inventario?');">Eliminar</a>
                        </td>
                    </tr>
                    <%   }
                    } else { %>
                    <tr>
                        <td colspan="4" class="text-center text-muted">No hay inventarios registrados.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
