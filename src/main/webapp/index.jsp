<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Usuario" %>
<%
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
    String vista = (String) request.getAttribute("vista");
    String jspDestino = "";

    if (vista != null) {
        switch (vista) {
            case "inventarios":
                jspDestino = "Tablas/Inventario/list-inventarios.jsp";
                break;
            case "productos":
                jspDestino = "Tablas/producto/product-list.jsp";
                break;
            case "movimientos":
                jspDestino = "Tablas/movimiento/list-movimientos.jsp";
                break;
            case "usuarios":
                jspDestino = "Tablas/usuario/list-usuarios.jsp";
                break;
            default:
                jspDestino = "";
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventario Fusionado</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<jsp:include page="includes/header.jsp" />
<div class="container mt-4">
    <% if (usuario == null) { %>
    <div class="text-center mt-5">
        <p class="lead">Una aplicación web simple para administrar inventarios y productos.</p>
        <a href="login.jsp" class="btn btn-success btn-lg">Iniciar Sesión</a>
    </div>
    <% } else if (jspDestino != null && !jspDestino.isEmpty()) { %>
    <jsp:include page="<%= jspDestino %>" />
    <% } else { %>
    <div class="text-center mt-5">
        <h2>Bienvenido, <%= usuario.getUsername() %></h2>
        <p class="lead">Selecciona una opción en el menú para comenzar.</p>
        <div class="d-grid gap-2 col-6 mx-auto">
            <a href="ruta?vista=productos" class="btn btn-outline-primary btn-lg btn-anim"><span>Productos</span></a>
            <a href="ruta?vista=movimientos" class="btn btn-outline-warning btn-lg btn-anim"><span>Movimientos</span></a>
            <a href="ruta?vista=usuarios" class="btn btn-outline-success btn-lg btn-anim"><span>Usuarios</span></a>
        </div>
    </div>
    <% } %>
</div>
<jsp:include page="includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/Main.js"></script>
</body>
</html>
