<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Usuario" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
  Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
  Inventory inventario = (Inventory) request.getAttribute("inventario");
  boolean editar = (inventario != null && inventario.getId() > 0);
  String contextPath = request.getContextPath();
  String errorMsg = request.getParameter("error");
  String displayError = "";
  if (errorMsg != null) {
    switch (errorMsg) {
      case "nombre_vacio": displayError = "El nombre del producto no puede estar vacío."; break;
      case "cantidad_vacia": displayError = "La cantidad no puede estar vacía."; break;
      case "cantidad_negativa": displayError = "La cantidad no puede ser negativa."; break;
      case "cantidad_invalida": displayError = "La cantidad debe ser un número entero."; break;
      default: displayError = "Error desconocido en los datos."; break;
    }
  }
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= editar ? "Editar" : "Nuevo" %> Inventario</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-8">
      <div class="bg-white p-5 shadow rounded-4">
        <h1 class="text-center mb-4"><%= editar ? "Editar Inventario" : "Nuevo Inventario" %></h1>
        <p class="text-muted mb-4">
          En este formulario puedes registrar un nuevo producto en el inventario o actualizar uno existente. Asegúrate de que los campos estén correctamente completados antes de guardar.
        </p>
        <% if (!displayError.isEmpty()) { %>
        <div class="alert alert-danger"><%= displayError %></div>
        <% } %>
        <form action="<%= contextPath %>/inventario" method="post">
          <input type="hidden" name="action" value="<%= editar ? "update" : "insert" %>" />
          <% if (editar) { %>
          <input type="hidden" name="id" value="<%= inventario.getId() %>" />
          <% } %>
          <div class="mb-3">
            <label for="nombre" class="form-label">Nombre del Producto:</label>
            <input type="text" id="nombre" name="nombre" class="form-control" value="<%= editar ? (inventario.getNombreProducto() != null ? inventario.getNombreProducto() : "") : "" %>" required />
          </div>
          <div class="mb-3">
            <label for="cantidad" class="form-label">Cantidad:</label>
            <input type="number" id="cantidad" name="cantidad" class="form-control" min="0" step="1" value="<%= editar ? inventario.getCantidad() : "0" %>" required />
          </div>
          <div class="d-flex justify-content-between">
            <a href="<%= contextPath %>/inventario?action=viewCascade" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-primary"><%= editar ? "Actualizar" : "Guardar" %></button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/includes/footer.jsp" />
<script src="<%= contextPath %>/js/main.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
