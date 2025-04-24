<%--
  Archivo: indextablas.jsp (reconstruido desde cero)
  Descripción: Página de acceso intermedio a las tablas gestionables del sistema (productos, usuarios, etc).
  Dependencias: header.jsp, footer.jsp, style.css, Bootstrap
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Usuario" %>
<%
  Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
  String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Tablas - Inventario Fusionado</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
  <style>
    body {
      background-image: url('<%= contextPath %>/images/image_226ba8.jpg');
      background-size: cover;
      background-attachment: fixed;
      background-position: center;
      background-repeat: no-repeat;
      font-family: 'Georgia', serif;
    }
    .main-content {
      background-color: #fff;
      padding: 40px;
      margin-top: 60px;
      border-radius: 16px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      text-align: center;
    }
    .btn-anim {
      position: relative;
      overflow: hidden;
      background-color: #4b3e2e;
      border: none;
      color: #fff;
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .btn-anim:hover {
      transform: scale(1.05);
      box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15);
    }
    .btn-anim span {
      position: relative;
      z-index: 1;
      transition: color 0.4s ease-in-out;
    }
    .btn-anim:hover span {
      color: #ffd700;
    }
  </style>
</head>
<body>
<jsp:include page="includes/header.jsp" />

<div class="container">
  <div class="main-content">
    <% if (usuario != null) { %>
    <h2 class="mb-4">Tablas del Sistema</h2>
    <p class="lead">Selecciona una tabla para gestionarla:</p>
    <div class="d-grid gap-3 col-6 mx-auto">
      <a href="<%= contextPath %>/ruta?vista=productos" class="btn btn-lg btn-anim"><span>Ver Productos</span></a>
      <a href="<%= contextPath %>/ruta?vista=usuarios" class="btn btn-lg btn-anim"><span>Ver Usuarios</span></a>
      <a href="<%= contextPath %>/index.jsp" class="btn btn-outline-secondary btn-lg">Volver al Inicio</a>
    </div>
    <% } else { %>
    <div class="alert alert-warning mt-4">
      Debes iniciar sesión para acceder a esta sección.
    </div>
    <a href="<%= contextPath %>/login.jsp" class="btn btn-success">Ir al Login</a>
    <% } %>
  </div>
</div>

<jsp:include page="includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>
