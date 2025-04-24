<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Inventario_Fusionado.model.Usuario" %>
<%
    Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="ruta?vista=inventarios">InventarioFusionado</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <% if (usuario != null) { %>
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ruta?vista=indextablas">Sección Tablas</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link disabled" href="#">Usuario: <strong><%= usuario.getUsername() %></strong></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="logout.jsp">Cerrar Sesión</a>
                </li>
                <% } else { %>
                <li class="nav-item">
                    <a class="nav-link" href="login.jsp">Iniciar Sesión</a>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
