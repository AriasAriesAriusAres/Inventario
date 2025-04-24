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
            case "producto_buffer":
                jspDestino = "Tablas/producto_buffer/list-productos_buffer.jsp";
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

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .btn-lg {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .btn-lg:hover {
            transform: scale(1.05);
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15);
        }
        .btn-anim {
            position: relative;
            overflow: hidden;
            color: #fff;
        }
        .btn-anim::before {
            content: '';
            position: absolute;
            width: 0;
            height: 100%;
            top: 0;
            left: 0;
            background: transparent;
            z-index: 0;
            transition: width 0.4s ease-in-out;
        }
        .btn-anim:hover::before {
            width: 100%;
        }
        .btn-anim span {
            position: relative;
            z-index: 1;
            transition: color 0.4s ease-in-out;
        }
        .btn-anim:hover span {
            color: #ffd700; /* dorado */
        }
    </style>
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
            <a href="indextablas.jsp" class="btn btn-outline-primary btn-lg btn-anim"><span>Ver Tablas</span></a>
            <a href="ruta?vista=movimientos" class="btn btn-outline-warning btn-lg btn-anim"><span>Ver Movimientos</span></a>
            <a href="ruta?vista=producto_buffer" class="btn btn-outline-secondary btn-lg btn-anim"><span>Ver Buffer</span></a>
        </div>
    </div>
    <% } %>
</div>
<jsp:include page="includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="<%= request.getContextPath() %>/js/Main.js"></script>
</body>
</html>
