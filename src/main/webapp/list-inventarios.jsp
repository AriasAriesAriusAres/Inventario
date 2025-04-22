<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Importa la librería JSTL core para usar c:forEach, c:url, etc. --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
  <title>Lista de Inventarios</title>
  <%-- Aquí podrías enlazar un CSS para estilos --%>
  <style>
    table, th, td {
      border: 1px solid black;
      border-collapse: collapse;
      padding: 5px;
    }
    th {
      background-color: #f2f2f2;
    }
  </style>
</head>
<body>

<h1>Lista de Inventarios</h1>

<%-- Enlace para ir al formulario de creación --%>
<p>
  <a href="${pageContext.request.contextPath}/inventario?action=new">Añadir Nuevo Inventario</a>
</p>

<%-- Tabla para mostrar los inventarios --%>
<table>
  <thead>
  <tr>
    <th>ID</th>
    <th>Nombre</th>
    <th>Descripción</th>
    <th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <%-- Itera sobre la lista de inventarios pasada por el Controller ("listaInventarios") --%>
  <c:forEach var="inv" items="${listaInventarios}">
    <tr>
      <td><c:out value="${inv.idInventario}" /></td>
      <td><c:out value="${inv.nombre}" /></td>
      <td><c:out value="${inv.descripcion}" /></td>
      <td>
          <%-- Enlace para editar (pasa el ID como parámetro) --%>
        <a href="${pageContext.request.contextPath}/inventario?action=edit&id=${inv.idInventario}">Editar</a>
        &nbsp;|&nbsp;
          <%-- Enlace para borrar (pasa el ID como parámetro) - Añadir confirmación JS sería bueno --%>
        <a href="${pageContext.request.contextPath}/inventario?action=delete&id=${inv.idInventario}" onclick="return confirm('¿Estás seguro de que quieres borrar este inventario?');">Borrar</a>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<%-- Mensaje si la lista está vacía --%>
<c:if test="${empty listaInventarios}">
  <p>No hay inventarios para mostrar.</p>
</c:if>

<br/>
<a href="${pageContext.request.contextPath}/index.jsp">Volver al Inicio</a>

</body>
</html>