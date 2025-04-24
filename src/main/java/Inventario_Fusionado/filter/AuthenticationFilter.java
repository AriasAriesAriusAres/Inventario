package Inventario_Fusionado.filter;

import Inventario_Fusionado.model.Usuario; // Asegúrate que la importación del modelo Usuario sea correcta
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Filtro de Autenticación para el proyecto Inventario_Fusionado.
 * Intercepta solicitudes a rutas protegidas y verifica si el usuario
 * tiene una sesión activa. Si no, redirige a la página de login.
 * Permite el acceso a rutas y recursos públicos definidos.
 */
// Define a qué patrones de URL se aplicará este filtro.
// ¡¡IMPORTANTE!! Ajusta estos patrones según las rutas que REALMENTE necesitas proteger.
// NO incluyas aquí /login, /logout.jsp, /login.jsp ni los recursos estáticos.
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/inventario/*",     // Protege todo bajo /inventario (CRUD Inventarios)
        "/visualizar-inventarios.jsp", // Protege la vista de cascada si es necesario
        // "/list-inventarios.jsp", // Descomenta si también quieres proteger la vista de tabla
        // "/inventario-form.jsp", // Descomenta si quieres proteger el acceso directo al form
        "/producto/*",       // Protege todo bajo /producto (Futuro)
        "/admin/*"           // Protege todo bajo /admin (Futuro)
        // Añade otras rutas específicas o patrones que necesiten login
})
public class AuthenticationFilter implements Filter {

    // Conjunto de rutas EXACTAS que son PÚBLICAS (no requieren login)
    // El context path (ej: /Inventario_Fusionado) se maneja automáticamente por el getPathInfo.
    private final Set<String> publicPaths = new HashSet<>(Arrays.asList(
            "/login",       // Acción POST del LoginController
            "/logout.jsp",  // Página/Acción de Logout
            "/login.jsp"    // Página de Login
            // "/index.jsp" // No es necesario filtrar index.jsp si ya maneja ambos estados
            // Añadir aquí otras rutas públicas, ej: "/registro", "/registro.jsp"
    ));

    // Prefijos de rutas para RECURSOS ESTÁTICOS públicos (CSS, JS, Imágenes)
    private final Set<String> publicResourcePrefixes = new HashSet<>(Arrays.asList(
            "/css/",
            "/js/",
            "/images/"
            // Añadir otros prefijos si es necesario, ej: "/fonts/"
    ));

    /**
     * Método de inicialización del filtro (opcional).
     * @param filterConfig Configuración del filtro.
     * @throws ServletException Si ocurre un error.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthenticationFilter inicializado y listo para proteger rutas.");
        // Código de inicialización si fuera necesario
    }

    /**
     * Lógica principal del filtro que se ejecuta para cada solicitud coincidente.
     * @param request Objeto ServletRequest.
     * @param response Objeto ServletResponse.
     * @param chain Cadena de filtros.
     * @throws IOException Si ocurre un error de IO.
     * @throws ServletException Si ocurre un error de Servlet.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Obtener sesión SIN crear una nueva

        String contextPath = httpRequest.getContextPath();
        String requestURI = httpRequest.getRequestURI();
        // Obtiene la ruta relativa a la aplicación (ej: /login, /inventario, /css/style.css)
        // NOTA: Si tu servlet está mapeado a /inventario/*, getServletPath() podría ser /inventario
        // y getPathInfo() podría ser /list, /edit etc. Usar requestURI es más general.
        String path = requestURI.substring(contextPath.length());

        // Verificar si el usuario está logueado (buscando el atributo "usuario")
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        // Verificar si la ruta es pública
        boolean isPublicPath = publicPaths.contains(path);

        // Verificar si es un recurso estático público
        boolean isPublicResource = publicResourcePrefixes.stream().anyMatch(prefix -> path.startsWith(prefix));

        // ----- Lógica de decisión -----
        if (loggedIn || isPublicPath || isPublicResource) {
            // CASO 1: Usuario logueado -> Permitir acceso
            // CASO 2: La ruta es pública (login, logout) -> Permitir acceso
            // CASO 3: Es un recurso estático público (CSS, JS, IMG) -> Permitir acceso
            // System.out.println("AuthFilter: Acceso PERMITIDO a " + path);
            chain.doFilter(request, response); // Continuar con la cadena (al servlet o siguiente filtro)
        } else {
            // CASO 4: Usuario NO logueado y la ruta NO es pública/recurso -> DENEGAR
            System.out.println("AuthFilter: Acceso DENEGADO a " + path + " (no logueado). Redirigiendo a login.");

            // Opcional: Guardar URL solicitada para redirigir después del login
            // HttpSession tempSession = httpRequest.getSession(true); // Crear sesión si es necesario aquí
            // String queryString = httpRequest.getQueryString();
            // String requestedUrl = requestURI + (queryString != null ? "?" + queryString : "");
            // tempSession.setAttribute("requestedUrl", requestedUrl);
            // System.out.println("AuthFilter: Guardando requestedUrl: " + requestedUrl);

            // Redirigir a la página de login
            httpResponse.sendRedirect(contextPath + "/login.jsp");
        }
    }

    /**
     * Método de destrucción del filtro (opcional).
     */
    @Override
    public void destroy() {
        System.out.println("AuthenticationFilter destruido.");
        // Código de limpieza si fuera necesario
    }
}