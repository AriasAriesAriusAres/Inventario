<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Iniciar sesión</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container fade-in">
  <h1 style="text-align:center">Login de Usuario</h1>

  <% String error = (String) request.getAttribute("error"); %>
  <% if (error != null) { %>
  <div style="color: red; text-align: center; margin-bottom: 1em;">
    <%= error %>
  </div>
  <% } %>

  <form action="login" method="post" style="max-width: 400px; margin: auto;">
    <div style="margin-bottom: 15px;">
      <label for="username">Usuario:</label><br>
      <input type="text" id="username" name="username" required class="form-control" style="width: 100%; padding: 8px;">
    </div>

    <div style="margin-bottom: 15px;">
      <label for="password">Contraseña:</label><br>
      <input type="password" id="password" name="password" required class="form-control" style="width: 100%; padding: 8px;">
    </div>

    <div style="text-align: center;">
      <button type="submit" class="btn">Entrar</button>
    </div>
  </form>
</div>
<script src="js/main.js"></script>
</body>
</html>
