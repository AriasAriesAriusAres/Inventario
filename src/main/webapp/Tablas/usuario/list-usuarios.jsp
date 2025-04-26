<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, Inventario_Fusionado.model.Usuario" %>
<%
  // Obtener o inicializar la lista de usuarios
  List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
  if (usuarios == null) {
    usuarios = new ArrayList<>();
  }
  boolean vacio = usuarios.isEmpty();
  String contextPath = request.getContextPath();
  // flash message
  String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Gestión de Usuarios</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="container mt-5 fade-in">
  <% if (message != null) { %>
  <div class="alert alert-success" role="alert">
    <%= message %>
  </div>
  <% } %>

  <h2 class="mb-4">Gestión de Usuarios</h2>
  <div class="mb-3">
    <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#newUserForm">
      Nuevo Usuario
    </button>
  </div>

  <div class="collapse mb-4" id="newUserForm">
    <form action="<%=contextPath%>/usuario" method="post" class="row g-3">
      <input type="hidden" name="action" value="insert" />
      <div class="col-md-4"><input name="username" class="form-control" placeholder="Nombre de usuario" required/></div>
      <div class="col-md-4"><input name="password" type="password" class="form-control" placeholder="Contraseña" required/></div>
      <div class="col-md-2"><button class="btn btn-primary">Crear</button></div>
    </form>
  </div>

  <div class="table-container">
    <table class="styled-table table table-striped">
      <thead>
      <tr>
        <th>ID</th>
        <th>Usuario</th>
        <th>Acciones</th>
      </tr>
      </thead>
      <tbody>
      <%
        if (!vacio) {
          for (Usuario u : usuarios) {
      %>
      <tr>
        <td><%= u.getIdUsuario() %></td>
        <form id="editForm_<%=u.getIdUsuario()%>" action="<%=contextPath%>/usuario" method="post">
          <input type="hidden" name="action" value="edit" />
          <input type="hidden" name="id" value="<%= u.getIdUsuario() %>" />
          <td><input class="form-control" name="username" value="<%= u.getUsername() %>" form="editForm_<%=u.getIdUsuario()%>"/></td>
          <td>
            <button class="btn btn-sm btn-primary">Guardar</button>
            <button type="button" class="btn btn-sm btn-danger"
                    onclick="if(confirm('¿Eliminar usuario <%=u.getUsername()%>?')) { document.getElementById('del_<%=u.getIdUsuario()%>').submit(); }">
              Borrar
            </button>
        </form>
        <form id="del_<%=u.getIdUsuario()%>" action="<%=contextPath%>/usuario" method="post">
          <input type="hidden" name="action" value="delete" />
          <input type="hidden" name="id" value="<%= u.getIdUsuario() %>" />
        </form>
        </td>
      </tr>
      <%    }
      } else {
        // tres filas dummy
        for(int i=1;i<=3;i++) {
      %>
      <tr class="text-muted">
        <td>-<%=i%></td><td>-Sin usuarios-</td><td></td>
      </tr>
      <%  }
      }
      %>
      </tbody>
    </table>
  </div>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="<%= contextPath %>/js/Main.js"></script>
</body>
</html>


<%--
  Problema: Esta página estaba cargando dos cabeceras superpuestas (el header global de style.css y el header local de la vista).
  Solución: eliminamos la inclusión del estilo global aquí para que solo aparezca el header específico de “Gestión de Usuarios”.

  Tampoco funciona, el puñetero header de style.css no se puede eliminar especificamente y cuando defino defines dos “flags” (atributos en la petición) que controlan cada header:

showGlobalHeader → cuando es true, se renderiza el header global (el de header.jsp).

showLocalHeader → cuando es true, se renderiza el header específico de list-usuarios.jsp.

Y en cada JSP envuelves su header en un <c:if> que comprueba su flag. Así, si uno está activo, el otro puede permanecer oculto. Por ejemplo:

jsp
Copiar
Editar
<!-- header.jsp -->
<c:if test="${showGlobalHeader}">
  <!-- tu navbar global -->
</c:if>
jsp
Copiar
Editar
<!-- list-usuarios.jsp -->
<c:if test="${showLocalHeader}">
  <!-- tu barra verde local -->
</c:if>
Y en el controlador, antes de hacer forward:

java
Copiar
Editar
// Para la vista inicial (index.jsp)
request.setAttribute("showGlobalHeader", true);
request.setAttribute("showLocalHeader", false);

// Para list-usuarios.jsp tras /usuario
request.setAttribute("showGlobalHeader", false);
request.setAttribute("showLocalHeader", true);

De este modo “se comunican”: nunca verás los dos a la vez, pero siempre habrá uno visible según el escenario
pero funciona? y una polla que va a funcionar.

dejo los dos y ya lo arreglare mas adelante.
--%>
