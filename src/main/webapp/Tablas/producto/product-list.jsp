<%--
  ===========================================================================
  product-list.jsp v3.21
  Función: Listar productos pendientes y enlace a edición
  ===========================================================================
--%>

<%-- ==========================================================================
     MACRO BLOQUE: PLANTILLA (genérico y reutilizable)
  ========================================================================== --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, Inventario_Fusionado.model.ProductoBuffer" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Productos Pendientes - Inventario Fusionado v3.21</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>

<%-- ==========================================================================
     MACRO BLOQUE: PROPÓSITO (específico de listados de productos)
  ========================================================================== --%>
<body>
<jsp:include page="../../includes/header.jsp" />

<div class="container mt-5 fade-in">
  <div class="card d-flex flex-column align-items-center text-center">

    <h2 class="mb-4">Productos Pendientes</h2>
    <div class="d-flex justify-content-center mb-4">
      <a href="<%= contextPath %>/producto?action=form" class="btn btn-success">Nueva Solicitud</a>
    </div>

    <div class="table-container w-100 d-flex justify-content-center">
      <table class="styled-table">
        <thead class="table-dark">
        <tr>
          <th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio</th>
          <th>Stock</th><th>Inventario</th><th>Estado</th><th>Solicitado por</th><th>Fecha</th>
        </tr>
        </thead>
        <tbody>
        <%
          List<ProductoBuffer> pendientes = (List<ProductoBuffer>) request.getAttribute("pendientes");
          if (pendientes != null && !pendientes.isEmpty()) {
            for (ProductoBuffer p : pendientes) {
        %>
        <tr>
          <td><%= p.getIdBuffer() %></td>
          <td><%= p.getNombre() %></td>
          <td><%= p.getDescripcion() %></td>
          <td><%= p.getPrecio() %></td>
          <td><%= p.getStock() %></td>
          <td><%= p.getIdInventario() %></td>
          <td><%= p.getEstado() %></td>
          <td><%= p.getIdUsuarioSolicitud() %></td>
          <td><%= p.getTimestampSolicitudString() %></td>
        </tr>
        <%
          }
        } else {
        %>
        <tr><td colspan="9" class="text-center text-muted">No hay productos pendientes.</td></tr>
        <% } %>
        </tbody>
      </table>
    </div>

    <div class="text-center text-muted small mt-3">Versión 3.21</div>
  </div>
</div>

<jsp:include page="../../includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>
