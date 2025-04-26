<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, Inventario_Fusionado.model.Movimiento" %>
<%
  // Obtener o inicializar la lista de movimientos
  List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos");
  if (movimientos == null) {
    movimientos = new ArrayList<>();
  }
  boolean vacio = movimientos.isEmpty();
  String contextPath = request.getContextPath();
  // flash message
  String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Historial de Movimientos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="container mt-5 fade-in">
  <% if (message != null) { %>
  <div class="alert alert-success" role="alert">
    <%= message %>
  </div>
  <% } %>

  <h2 class="mb-4">Historial de Movimientos</h2>
  <div class="table-responsive">
    <table class="styled-table table table-striped">
      <thead class="table-dark">
      <tr>
        <th>ID</th>
        <th>Usuario</th>
        <th>Fecha y Hora</th>
        <th>Tabla Afectada</th>
        <th>ID Registro</th>
        <th>Acción</th>
        <th>Estado</th>
        <th>Detalles</th>
      </tr>
      </thead>
      <tbody>
      <% if (!vacio) {
        for (Movimiento m : movimientos) { %>
      <tr>
        <td><%= m.getIdMovimiento() %></td>
        <td><%= m.getIdUsuario() %></td>
        <td><%= m.getFechaHoraString() %></td>
        <td><%= m.getTablaAfectada() %></td>
        <td><%= m.getIdRegistroAfectado() %></td>
        <td><%= m.getAccion() %></td>
        <td><%= m.getEstado() %></td>
        <td><%= m.getDetallesCambio() %></td>
      </tr>
      <%    }
      } else {
        // tres filas dummy con valores negativos
        for (int i = 1; i <= 3; i++) { %>
      <tr class="text-muted">
        <td>-<%= i %></td>
        <td>-Sin datos-</td>
        <td>-Sin datos-</td>
        <td>-Sin datos-</td>
        <td>-0-</td>
        <td>-Sin datos-</td>
        <td>-Sin datos-</td>
        <td></td>
      </tr>
      <%    }
      } %>
      </tbody>
    </table>
  </div>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>
