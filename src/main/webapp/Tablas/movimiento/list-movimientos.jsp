<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Inventario_Fusionado.model.Movimiento" %>
<%
  List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos");
%>
<jsp:include page="../../includes/header.jsp" />
<div class="container mt-5 fade-in">
  <h2 class="mb-4">Historial de Movimientos</h2>
  <div class="table-responsive">
    <table class="table table-striped table-hover">
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
      <% if (movimientos != null && !movimientos.isEmpty()) {
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
      <% }
      } else { %>
      <tr>
        <td colspan="8">No hay movimientos registrados.</td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</div>
<jsp:include page="../../includes/footer.jsp" />
