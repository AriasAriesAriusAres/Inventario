<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../includes/header.jsp" />

<div class="container mt-5 fade-in">
  <h2 class="mb-4">
    <c:out value="${empty productoBuffer ? 'Nueva Solicitud de Producto (Buffer)' : 'Editar Solicitud de Producto (Buffer)'}"/>
  </h2>

  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">
      Ocurrió un error: ${param.error}
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/producto_buffer" method="post" class="needs-validation" novalidate>
    <input type="hidden" name="action" value="<c:out value='${empty productoBuffer ? "insert" : "update"}'/>" />
    <c:if test="${not empty productoBuffer}">
      <input type="hidden" name="id" value="${productoBuffer.idBuffer}" />
    </c:if>

    <div class="mb-3">
      <label for="nombre" class="form-label">Nombre:</label>
      <input type="text" name="nombre" id="nombre" class="form-control" value="${empty productoBuffer ? '' : productoBuffer.nombre}" required>
    </div>

    <div class="mb-3">
      <label for="descripcion" class="form-label">Descripción:</label>
      <textarea name="descripcion" id="descripcion" class="form-control">${empty productoBuffer ? '' : productoBuffer.descripcion}</textarea>
    </div>

    <div class="mb-3">
      <label for="precio" class="form-label">Precio:</label>
      <input type="text" name="precio" id="precio" class="form-control" value="${empty productoBuffer ? '' : productoBuffer.precio}">
    </div>

    <div class="mb-3">
      <label for="stock" class="form-label">Stock:</label>
      <input type="number" name="stock" id="stock" class="form-control" value="${empty productoBuffer ? '' : productoBuffer.stock}">
    </div>

    <div class="mb-3">
      <label for="idInventario" class="form-label">ID Inventario:</label>
      <input type="number" name="idInventario" id="idInventario" class="form-control" value="${empty productoBuffer ? '' : productoBuffer.idInventario}">
    </div>

    <div class="mb-3">
      <label for="estado" class="form-label">Estado:</label>
      <input type="text" name="estado" id="estado" class="form-control" value="${empty productoBuffer ? 'PENDIENTE_CREAR' : productoBuffer.estado}">
    </div>

    <div class="mb-3">
      <label for="idUsuarioSolicitud" class="form-label">ID Usuario Solicitante:</label>
      <input type="number" name="idUsuarioSolicitud" id="idUsuarioSolicitud" class="form-control" value="${empty productoBuffer ? '' : productoBuffer.idUsuarioSolicitud}">
    </div>

    <button type="submit" class="btn btn-success">Guardar</button>
    <a href="${pageContext.request.contextPath}/producto_buffer?action=list" class="btn btn-secondary">Cancelar</a>
  </form>
</div>

<jsp:include page="../../includes/footer.jsp" />
