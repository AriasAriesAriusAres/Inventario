<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio - Gestión de Inventario</title>
</head>
<body>
<h2>Gestión de Inventario</h2>
<p>Bienvenido a la aplicación de gestión de inventario.</p>
<p>
    <%-- Enlace que llama al InventoryController con la acción 'list' --%>
    <a href="${pageContext.request.contextPath}/inventario?action=list">Ver Lista de Inventarios</a>
    <%-- Alternativamente, podrías enlazar directamente al JSP, pero es mejor pasar por el Controller --%>
    <%-- <a href="list-inventarios.jsp">Ver Lista de Inventarios (Directo - No recomendado)</a> --%>
</p>
</body>
</html>