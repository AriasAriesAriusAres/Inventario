<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Inventario_Fusionado.model.Inventory" %>
<%
  // Obtener lista de inventarios del request
  List<Inventory> inventarios = (List<Inventory>) request.getAttribute("inventarios");
  String contextPath = request.getContextPath(); // Context path para URLs

  // Obtener posible mensaje de error desde redirect
  String errorMsg = request.getParameter("error");
  String displayError = "";
  if (errorMsg != null) {
    switch (errorMsg) {
      case "id_invalido": displayError = "El ID proporcionado no es válido."; break;
      case "no_encontrado": displayError = "No se encontró el registro solicitado."; break;
      default: displayError = "Ocurrió un error desconocido."; break;
    }
  }
  // O obtener error desde un forward (si el controlador usa setAttribute)
  // String errorAttr = (String) request.getAttribute("errorMessage");
  // if (errorAttr != null) { displayError = errorAttr; }
%>
<html>
<head>
  <title>Visualizar Inventarios</title>
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css"> <%-- Context Path --%>
  <style>
    /* Estilos específicos de esta página */
    .card {
      max-width: 600px;
      margin-bottom: 20px;
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      position: relative; /* Para posicionar botones */
    }
    .card h2 {
      margin-top: 0;
      margin-bottom: 15px; /* Más espacio bajo el título */
      font-size: 1.4em;
      color: #4a3f35; /* Marrón oscuro */
    }
    .card p {
      margin-bottom: 8px; /* Menos espacio entre párrafos */
      font-size: 1.1em;
    }
    .card p strong {
      display: inline-block;
      min-width: 80px; /* Alineación mínima */
    }
    body {
      background-color: #f7f0e8; /* Crema claro */
      font-family: 'Georgia', serif; /* Coincidir con style.css */
      color: #333;
    }
    .container {
      margin: 40px auto;
      max-width: 700px;
      padding: 0 15px; /* Padding lateral */
    }
    h1 {
      text-align: center;
      margin-bottom: 30px; /* Menos espacio */
      color: #4a3f35;
    }
    .action-buttons {
      position: absolute;
      top: 15px;
      right: 15px;
    }
    .action-buttons .btn-sm { /* Estilo para botones pequeños */
      padding: 0.25rem 0.5rem;
      font-size: 0.875rem;
      line-height: 1.5;
      border-radius: 0.2rem;
      margin-left: 5px; /* Espacio entre botones */
    }
    .page-actions {
      text-align: right;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
<div class="container fade-in">
  <h1>Inventario Registrado</h1>

  <%-- Mostrar mensaje de error si existe --%>
  <% if (!displayError.isEmpty()) { %>
  <div style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 15px; background-color: #ffeeee; text-align: center;">
    <%= displayError %>
  </div>
  <% } %>

  <%-- Botón para añadir nuevo inventario --%>
  <div class="page-actions">
    <a href="<%= contextPath %>/inventario?action=new" class="btn">Añadir Nuevo</a>
    <%-- Enlace para volver al índice (opcional) --%>
    <a href="<%= contextPath %>/index.jsp" class="btn btn-secondary" style="margin-left: 10px;">Volver al Inicio</a>
    <%-- Enlace a la vista de tabla (opcional, si coexisten) --%>
    <%-- <a href="<%= contextPath %>/inventario?action=list" class="btn btn-info" style="margin-left: 10px;">Vista Tabla</a> --%>
  </div>

  <%-- Verificar si la lista de inventarios no es nula ni vacía --%>
  <% if (inventarios != null && !inventarios.isEmpty()) { %>
  <%-- Iterar sobre cada inventario --%>
  <% for (Inventory inv : inventarios) { %>
  <div class="card">
    <%-- Mostrar ID como título o subtítulo --%>
    <h2><%= inv.getNombreProducto() %> <span style="font-size: 0.7em; color: #888;">(ID: <%= inv.getId() %>)</span></h2>
    <%-- Mostrar la cantidad --%>
    <p><strong>Cantidad:</strong> <%= inv.getCantidad() %></p>

    <%-- Botones de acción para cada tarjeta --%>
    <div class="action-buttons">
      <a href="<%= contextPath %>/inventario?action=edit&id=<%= inv.getId() %>" class="btn btn-sm btn-warning">Editar</a>
      <a href="<%= contextPath %>/inventario?action=delete&id=<%= inv.getId() %>" class="btn btn-sm btn-danger" onclick="return confirm('¿Estás seguro de eliminar el inventario \'<%= inv.getNombreProducto() %>\' (ID: <%= inv.getId() %>)?');">Eliminar</a>
    </div>
  </div>
  <% } %>
  <% } else { %>
  <%-- Mensaje si no hay inventarios --%>
  <p style="text-align:center; font-size: 1.2em; color: #6c757d;">No hay inventarios registrados.</p>
  <% } %>
</div>

<script src="<%= contextPath %>/js/main.js"></script> <%-- Context Path --%>
</body>
</html>