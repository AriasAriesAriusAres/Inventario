<%--
  PLANTILLA JSP ESTÁNDAR - Inventario Fusionado

  Descripción:
  Esta plantilla define la estructura base reutilizable para páginas JSP del proyecto Inventario Fusionado.
  Incluye encabezado, pie de página, estilo visual unificado (color, márgenes, imagen de fondo), botones animados y zona principal central para contenido dinámico.

  Dependencias:
  - includes/header.jsp: barra de navegación superior.
  - includes/footer.jsp: pie de página con derechos u otra info.
  - css/style.css: estilos personalizados adicionales.
  - Bootstrap 5: diseño responsivo y componentes UI.
  - js/Main.js: script para animaciones o interactividad opcional.

  Variables JSP:
  - usuario: recuperado desde sesión. Controla acceso y personalización.
  - contextPath: ruta base de la aplicación (para imágenes, css, js, links).

  Contenido reutilizable:
  - .main-content: contenedor blanco centrado con sombreado y bordes redondeados.
  - botones con clase .btn-anim que cambian color del texto al pasar el cursor.

  Diagrama simplificado de relación de vistas:
  [index.jsp] ---> menú principal
      |---> [indextablas.jsp] ---> tablas: productos, usuarios, etc.
      |---> [ruta?vista=movimientos] ---> vista de movimientos
      |---> [ruta?vista=producto_buffer] ---> solicitudes de buffer

  Esta plantilla se puede clonar y adaptar para cualquier nueva página.
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
  <title>Plantilla - Inventario Fusionado</title>

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
    }
    .btn-anim {
      background-color: #4b3e2e;
      color: white;
      border: none;
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .btn-anim:hover {
      transform: scale(1.05);
      box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15);
    }
    .btn-anim span {
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
  <div class="main-content text-center">
    <h2 class="mb-4">Título de la Página</h2>
    <p class="lead">Aquí puedes colocar contenido dinámico o botones.</p>
    <!-- Contenido reutilizable -->
    <div class="d-grid gap-2 col-6 mx-auto">
      <a href="#" class="btn btn-lg btn-anim"><span>Acción 1</span></a>
      <a href="#" class="btn btn-lg btn-anim"><span>Acción 2</span></a>
    </div>
  </div>
</div>

<jsp:include page="includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>
