<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, Inventario_Fusionado.model.Product" %>
<%
  // obtener o inicializar la lista
  List<Product> productos = (List<Product>) request.getAttribute("productos");
  if (productos == null) {
    productos = new ArrayList<>();
  }
  boolean vacio = productos.isEmpty();
  String contextPath = request.getContextPath();
  // flash message
  String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Listado de Productos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-4">
  <% if (message != null) { %>
  <div class="alert alert-success" role="alert">
    <%= message %>
  </div>
  <% } %>

  <div class="table-container">
    <table class="styled-table table table-striped">
      <thead>
      <tr>
        <th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Stock</th><th>Inventario</th><th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <%
        if (!vacio) {
          for (Product p : productos) {
      %>
      <tr>
        <td><%= p.getIdProducto() %></td>
        <td><input class="form-control" name="nombre" value="<%= p.getNombre() %>" form="editForm_<%=p.getIdProducto()%>"/></td>
        <td><input class="form-control" name="descripcion" value="<%= p.getDescripcion() %>" form="editForm_<%=p.getIdProducto()%>"/></td>
        <td><input class="form-control" name="precio" value="<%= p.getPrecio() %>" form="editForm_<%=p.getIdProducto()%>"/></td>
        <td><input class="form-control" name="stock" value="<%= p.getStock() %>" form="editForm_<%=p.getIdProducto()%>"/></td>
        <td><input class="form-control" name="id_inventario" value="<%= p.getIdInventario() %>" form="editForm_<%=p.getIdProducto()%>"/></td>
        <td>
          <form id="editForm_<%=p.getIdProducto()%>" action="<%=contextPath%>/producto" method="post" style="display:inline;">
            <input type="hidden" name="action" value="edit" />
            <input type="hidden" name="id" value="<%= p.getIdProducto() %>" />
            <button class="btn btn-sm btn-primary">Guardar</button>
          </form>
          <form action="<%=contextPath%>/producto" method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar producto ID <%=p.getIdProducto()%>?');">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="id" value="<%= p.getIdProducto() %>" />
            <button class="btn btn-sm btn-danger">Borrar</button>
          </form>
        </td>
      </tr>
      <%    }
      } else {
        // tres filas dummy con valores negativos
        for (int i = 1; i <= 3; i++) {
      %>
      <tr class="text-muted">
        <td>-<%=i%></td>
        <td>-Sin datos-</td>
        <td>-Sin datos-</td>
        <td>-0.00-</td>
        <td>-0-</td>
        <td>-0-</td>
        <td></td>
      </tr>
      <%  }
      }
      %>
      </tbody>
    </table>
  </div>

  <div class="page-actions">
    <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#newProductForm">Nuevo Producto</button>
  </div>
  <div class="collapse" id="newProductForm">
    <form action="<%=contextPath%>/producto" method="post" class="row g-3">
      <input type="hidden" name="action" value="insert" />
      <div class="col-md-2"><input name="nombre" class="form-control" placeholder="Nombre" required/></div>
      <div class="col-md-3"><input name="descripcion" class="form-control" placeholder="Descripción"/></div>
      <div class="col-md-1"><input name="precio" type="number" step="0.01" class="form-control" placeholder="Precio" required/></div>
      <div class="col-md-1"><input name="stock" type="number" class="form-control" placeholder="Stock" required/></div>
      <div class="col-md-2"><input name="id_inventario" type="number" class="form-control" placeholder="ID Inventario" required/></div>
      <div class="col-md-1"><button class="btn btn-primary">Crear</button></div>
    </form>
  </div>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>
