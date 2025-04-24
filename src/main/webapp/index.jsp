<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Usuario" %>
<%
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio - Inventario Fusionado</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .main-content {
            background-color: #fff;
            padding: 40px;
            margin-top: 50px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align: center;
        }
        .main-content h1 {
            margin-bottom: 20px;
        }
        .main-content p {
            color: #6c757d;
            margin-bottom: 30px;
        }
        .action-buttons .btn {
            margin: 0 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="main-content fade-in">
                <h1>Gestión de Inventario</h1>

                <% if (usuario != null) { %>
                <p>Bienvenido, <strong><%= usuario.getUsername() %></strong>!</p>
                <div class="action-buttons">
                    <a href="inventario?action=list" class="btn btn-primary btn-lg">Ver Inventarios</a>
                    <a href="logout.jsp" class="btn btn-outline-danger btn-lg">Cerrar Sesión</a>
                </div>
                <% } else { %>
                <p>Una aplicación web simple para administrar inventarios y productos.</p>
                <div class="action-buttons">
                    <a href="login.jsp" class="btn btn-success btn-lg">Iniciar Sesión</a>
                </div>
                <% } %>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script src="js/Main.js"></script>
</body>
</html>
