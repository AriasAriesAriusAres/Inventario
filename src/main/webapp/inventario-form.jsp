<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
  // Obtener inventario para edición
  Inventory inventario = (Inventory) request.getAttribute("inventario");
  // Determinar si estamos editando (un ID válido usualmente es > 0)
  boolean editar = (inventario != null && inventario.getId() > 0);
  String contextPath = request.getContextPath(); // Context path para URLs relativas

  // Obtener posible mensaje de error desde redirect (si aplica)
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
  // O obtener error desde un forward (si el controlador usa setAttribute)
  // String errorAttr = (String) request.getAttribute("errorMessage");
  // if (errorAttr != null) { displayError = errorAttr; }

%>
<!DOCTYPE html>
<html>
<head>
  <title><%= editar ? "Editar" : "Nuevo" %> Inventario</title>
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css"> <%-- Context Path --%>
</head>
<body>
<div class="container fade-in">
  <h1><%= editar ? "Editar Inventario" : "Nuevo Inventario" %></h1>

  <%-- Mostrar mensaje de error si existe --%>
  <% if (!displayError.isEmpty()) { %>
  <div style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px; background-color: #ffeeee;">
    <%= displayError %>
  </div>
  <% } %>

  <%-- Formulario apunta al servlet 'inventario' --%>
  <form action="<%= contextPath %>/inventario" method="post">
    <%-- Campo oculto para la acción (insert o update) --%>
    <input type="hidden" name="action" value="<%= editar ? "update" : "insert" %>" />
    <%-- Campo oculto para el ID solo si estamos editando --%>
    <% if (editar) { %>
    <input type="hidden" name="id" value="<%= inventario.getId() %>" />
    <% } %>

    <%-- Campo Nombre Producto --%>
    <%-- !! USA name="nombre". Si lo cambias a "nombre_producto", ajusta InventoryController !! --%>
    <div class="form-group" style="margin-bottom: 20px;">
      <label for="nombre">Nombre del Producto:</label><br>
      <input type="text" id="nombre" name="nombre" class="form-control"
             value="<%= editar ? (inventario.getNombreProducto() != null ? inventario.getNombreProducto() : "") : "" %>" required />
      <%-- El value usa el getter correcto del modelo refactorizado --%>
    </div>

    <%-- Campo Cantidad --%>
    <div class="form-group" style="margin-bottom: 20px;">
      <label for="cantidad">Cantidad:</label><br>
      <input type="number" id="cantidad" name="cantidad" class="form-control" min="0" step="1"
             value="<%= editar ? inventario.getCantidad() : "0" %>" required />
      <%-- El value usa el getter correcto del modelo refactorizado --%>
    </div>

    <%-- Botones de acción --%>
    <div style="text-align: center;">
      <button type="submit" class="btn"><%= editar ? "Actualizar" : "Guardar" %></button>
      <%-- Botón Cancelar lleva a la vista por defecto del inventario --%>
      <a href="<%= contextPath %>/inventario?action=viewCascade" class="btn btn-secondary">Cancelar</a>
    </div>
  </form>
</div>

<script src="<%= contextPath %>/js/main.js"></script> <%-- Context Path --%>
</body>
</html>