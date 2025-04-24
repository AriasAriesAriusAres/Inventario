<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
  List<Inventory> inventarios = (List<Inventory>) request.getAttribute("inventarios");
  String contextPath = request.getContextPath();
  String errorMsg = request.getParameter("error");
  String displayError = "";
  if (errorMsg != null) {
    switch (errorMsg) {
      case "id_invalido": displayError = "El ID proporcionado no es válido."; break;
      case "no_encontrado": displayError = "No se encontró el registro solicitado."; break;
      default: displayError = "Ocurrió un error desconocido."; break;
    }
  }
%>
<jsp:include page="/includes/header.jsp" />
<div class="container fade-in mt-5">
  <h1 class="mb-4 text-center">Inventario Registrado</h1>

  <% if (!displayError.isEmpty()) { %>
  <div class="alert alert-danger text-center">
    <%= displayError %>
  </div>
  <% } %>

  <div class="d-flex justify-content-end mb-3">
    <a href="<%= contextPath %>/inventario?action=new" class="btn btn-success me-2">Añadir Nuevo</a>
    <a href="ruta?vista=inventarios" class="btn btn-outline-secondary">Vista Tabla</a>
  </div>

  <% if (inventarios != null && !inventarios.isEmpty()) { %>
  <% for (Inventory inv : inventarios) { %>
  <div class="card shadow-sm mb-3 p-4 rounded-4 border-0 bg-white position-relative">
    <h4><%= inv.getNombreProducto() %>
      <small class="text-muted" style="font-size: 0.8em;">(ID: <%= inv.getId() %>)</small>
    </h4>
    <p><strong>Cantidad:</strong> <%= inv.getCantidad() %></p>
    <div class="position-absolute top-0 end-0 m-3">
      <a href="<%= contextPath %>/inventario?action=edit&id=<%= inv.getId() %>" class="btn btn-sm btn-warning me-2">Editar</a>
      <a href="<%= contextPath %>/inventario?action=delete&id=<%= inv.getId() %>" class="btn btn-sm btn-danger"
         onclick="return confirm('¿Eliminar el inventario <%= inv.getNombreProducto() %>?');">Eliminar</a>
    </div>
  </div>
  <% } %>
  <% } else { %>
  <p class="text-center text-muted">No hay inventarios registrados.</p>
  <% } %>
</div>
<jsp:include page="/includes/footer.jsp" />
<script src="<%= contextPath %>/js/main.js"></script>
