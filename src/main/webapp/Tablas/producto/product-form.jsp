<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Product" %>
<%
  Product producto = (Product) request.getAttribute("producto");
  boolean esNuevo = (producto == null || producto.getIdProducto() == 0);
%>
<jsp:include page="../../includes/header.jsp" />
<div class="container mt-5 fade-in">
  <h2 class="mb-4"><%= esNuevo ? "Agregar Producto" : "Editar Producto" %></h2>
  <form method="post" action="producto">
    <% if (!esNuevo) { %>
    <input type="hidden" name="id" value="<%= producto.getIdProducto() %>" />
    <% } %>
    <input type="hidden" name="action" value="<%= esNuevo ? "insert" : "update" %>">

    <div class="mb-3">
      <label for="nombre" class="form-label">Nombre:</label>
      <input type="text" class="form-control" id="nombre" name="nombre" value="<%= producto != null ? producto.getNombre() : "" %>" required>
    </div>

    <div class="mb-3">
      <label for="descripcion" class="form-label">Descripción:</label>
      <textarea class="form-control" id="descripcion" name="descripcion"><%= producto != null ? producto.getDescripcion() : "" %></textarea>
    </div>

    <div class="mb-3">
      <label for="precio" class="form-label">Precio:</label>
      <input type="number" step="0.01" class="form-control" id="precio" name="precio" value="<%= producto != null ? producto.getPrecio() : "" %>">
    </div>

    <div class="mb-3">
      <label for="stock" class="form-label">Stock:</label>
      <input type="number" class="form-control" id="stock" name="stock" value="<%= producto != null ? producto.getStock() : "" %>">
    </div>

    <div class="mb-3">
      <label for="idInventario" class="form-label">ID Inventario:</label>
      <input type="number" class="form-control" id="idInventario" name="idInventario" value="<%= producto != null ? producto.getIdInventario() : "" %>">
    </div>

    <button type="submit" class="btn btn-primary">Guardar</button>
    <a href="ruta?vista=productos" class="btn btn-secondary">Cancelar</a>
  </form>
</div>
<jsp:include page="../../includes/footer.jsp" />
