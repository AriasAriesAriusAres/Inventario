<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
  <%-- El título cambia si estamos editando o creando --%>
  <title><c:choose><c:when test="${inventory != null}">Editar</c:when><c:otherwise>Añadir</c:otherwise></c:choose> Inventario</title>
  <%-- Aquí podrías enlazar CSS --%>
</head>
<body>

<h1><c:choose><c:when test="${inventory != null}">Editar</c:when><c:otherwise>Añadir Nuevo</c:otherwise></c:choose> Inventario</h1>

<%-- Mensaje de error si viene de una redirección (opcional) --%>
<c:if test="${not empty param.error}">
  <p style="color:red;">Error: <c:out value="${param.error}" /></p>
</c:if>

<%--
  El formulario envía los datos por POST al servlet InventoryController.
  La acción (insert o update) se determina con un campo oculto.
--%>
<form action="${pageContext.request.contextPath}/inventario" method="post">

  <%-- Campo oculto para la acción (insertar o actualizar) --%>
  <input type="hidden" name="action" value="${inventory != null ? 'update' : 'insert'}" />

  <%-- Campo oculto para el ID SOLO si estamos editando --%>
  <c:if test="${inventory != null}">
    <input type="hidden" name="idInventario" value="<c:out value='${inventory.idInventario}' />" />
  </c:if>

  <%-- Campos del formulario --%>
  <table>
    <tr>
      <td><label for="nombre">Nombre:</label></td>
      <%-- El valor se rellena si estamos editando --%>
      <td><input type="text" id="nombre" name="nombre" value="<c:out value='${inventory.nombre}' />" required size="50"/></td>
    </tr>
    <tr>
      <td><label for="descripcion">Descripción:</label></td>
      <td><textarea id="descripcion" name="descripcion" rows="4" cols="50"><c:out value='${inventory.descripcion}' /></textarea></td>
    </tr>
    <tr>
      <td colspan="2" style="text-align:center;">
        <input type="submit" value="Guardar Cambios" />
      </td>
    </tr>
  </table>
</form>

<br/>
<a href="${pageContext.request.contextPath}/inventario?action=list">Volver a la Lista</a>

</body>
</html>