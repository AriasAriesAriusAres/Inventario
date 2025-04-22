// Asegúrate que el nombre del paquete sea correcto
package Inventario_Fusionado.listener;

// Importa el inicializador de la BD y las clases de Listener/Servlet necesarias
import Inventario_Fusionado.database.DatabaseInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener; // Importa la anotación

// La anotación @WebListener registra este Listener automáticamente en Tomcat (Servlet 3.0+)
// Así no necesitas añadirlo manualmente en web.xml
@WebListener
public class WebAppInitializer implements ServletContextListener {

    /**
     * Este método se ejecuta cuando la aplicación web se inicia.
     * Es el lugar perfecto para inicializar la base de datos.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> [WebAppInitializer] Aplicación Web Iniciándose - Intentando inicializar Base de Datos SQLite...");
        try {
            // Llama al método estático de tu clase inicializadora
            DatabaseInitializer.initializeDatabase();
            System.out.println(">>> [WebAppInitializer] Inicialización de Base de Datos SQLite completada (o ya existía).");
        } catch (Exception e) {
            System.err.println(">>> [WebAppInitializer] ERROR GRAVE durante la inicialización de la BD:");
            e.printStackTrace();
            // Considera lanzar una RuntimeException si la BD es crítica para arrancar
            // throw new RuntimeException("Fallo al inicializar la base de datos", e);
        }
    }

    /**
     * Este método se ejecuta cuando la aplicación web se detiene.
     * Puedes usarlo para liberar recursos si es necesario.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> [WebAppInitializer] Aplicación Web Deteniéndose.");
        // Código de limpieza aquí si es necesario (ej. cerrar pool de conexiones si usaras uno)
    }
}