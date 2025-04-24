<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../includes/header.jsp" />

<style>
  body {
    background-color: #f4f1e6;
    font-family: 'Georgia', serif;
  }
  .form-container {
    max-width: 600px;
    margin: 60px auto;
    background-color: #ffffff;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 6px 18px rgba(0,0,0,0.08);
  }
  h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #4b3e2e;
  }
  .btn-volver {
    background-color: #7a6954;
    color: white;
    border: none;
  }
  .btn-volver:hover {
    background-color: #665847;
  }
  .form-control {
    border-radius: 8px;
  }
</style>

<div class="form-container fade-in">
  <h2><c:out value="${empty usuario ? 'Registrar Nuevo Usuario' : 'Editar Usuario'}"/></h2>

  <form action="${pageContext.request.contextPath}/usuario" method="post">
    <input type="hidden" name="action" value="<c:out value='${empty usuario ? "insert" : "update"}'/>">
    <c:if test="${not empty usuario}">
      <input type="hidden" name="id" value="${usuario.idUsuario}"/>
    </c:if>

    <div class="mb-3">
      <label for="username" class="form-label">Nombre de Usuario:</label>
      <input type="text" class="form-control" name="username" id="username" value="${empty usuario ? '' : usuario.username}" required>
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Contraseña:</label>
      <input type="password" class="form-control" name="password" id="password" value="${empty usuario ? '' : usuario.password}" required>
    </div>

    <div class="d-flex justify-content-between">
      <a href="${pageContext.request.contextPath}/usuario?action=list" class="btn btn-volver">Cancelar</a>
      <button type="submit" class="btn btn-success">Guardar</button>
    </div>
  </form>
</div>

<jsp:include page="../../includes/footer.jsp" />
