<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../includes/header.jsp" />

<div class="container mt-5 fade-in">
  <h2 class="mb-4">Solicitudes de Productos (Buffer)</h2>

  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">
      Ocurrió un error: ${param.error}
    </div>
  </c:if>

  <table class="table table-striped table-hover">
    <thead class="table-dark">
    <tr>
      <th>ID</th>
      <th>Nombre</th>
      <th>Estado</th>
      <th>Stock</th>
      <th>ID Inventario</th>
      <th>ID Usuario Sol.</th>
      <th>Fecha Solicitud</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="pb" items="${productosBuffer}">
      <tr>
        <td>${pb.idBuffer}</td>
        <td>${pb.nombre}</td>
        <td>${pb.estado}</td>
        <td>${pb.stock}</td>
        <td>${pb.idInventario}</td>
        <td>${pb.idUsuarioSolicitud}</td>
        <td><c:out value="${pb.timestampSolicitudString}"/></td>
        <td>
          <a href="${pageContext.request.contextPath}/producto_buffer?action=edit&id=${pb.idBuffer}" class="btn btn-sm btn-warning">Editar</a>
          <a href="${pageContext.request.contextPath}/producto_buffer?action=delete&id=${pb.idBuffer}" class="btn btn-sm btn-danger" onclick="return confirm('¿Eliminar registro ${pb.idBuffer}?');">Eliminar</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <a href="${pageContext.request.contextPath}/producto_buffer?action=new" class="btn btn-primary">Agregar nueva Solicitud</a>
</div>

<jsp:include page="../../includes/footer.jsp" />
