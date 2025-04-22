package Inventario_Fusionado.database; // Asegúrate que el paquete coincida

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DBManager {
    // Define la ruta relativa a la raíz del proyecto o una absoluta
    // Usaremos una carpeta 'database' en la raíz del proyecto por ahora
    // NOTA: La ruta real dependerá desde dónde se ejecute Tomcat. Podríamos necesitar ajustarla.
    private static final String DB_FOLDER = "database";
    private static final String DB_FILENAME = "InventarioFusionado.db";
    // Construye la URL JDBC para SQLite
    private static final String DATABASE_URL = "jdbc:sqlite:" + DB_FOLDER + File.separator + DB_FILENAME;

    // Cargar el driver SQLite al iniciar la clase
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver SQLite JDBC cargado exitosamente.");
            // Crear la carpeta si no existe (puede ayudar con permisos/rutas relativas)
            File dbDir = new File(DB_FOLDER);
            if (!dbDir.exists()) {
                if(dbDir.mkdirs()) {
                    System.out.println("Carpeta de base de datos creada en: " + dbDir.getAbsolutePath());
                } else {
                    System.err.println("WARN: No se pudo crear la carpeta de base de datos en: " + dbDir.getAbsolutePath());
                }
            } else {
                System.out.println("Carpeta de base de datos ya existe en: " + dbDir.getAbsolutePath());
            }
            System.out.println("Intentando conectar a: " + DATABASE_URL);
            // Intentar una conexión temprana para verificar la ruta/driver
            testConnection();

        } catch (ClassNotFoundException e) {
            System.err.println("FATAL: No se encontró el driver SQLite JDBC. Asegúrate de que la dependencia sqlite-jdbc esté en el pom.xml.");
            e.printStackTrace();
            // Podríamos lanzar una RuntimeException aquí para detener la carga si el driver es esencial.
            // throw new RuntimeException("No se pudo cargar el driver SQLite JDBC", e);
        }
    }

    // Obtener conexión a la Base de Datos
    public static Connection getConnection() throws SQLException {
        // Asegura que la carpeta exista antes de intentar conectar
        File dbDir = new File(DB_FOLDER);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Probar la conexión a la Base de Datos (solo para el bloque static inicial)
    private static void testConnection() { // Cambiado a private
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            System.out.println("Conexión de prueba inicial a SQLite exitosa: " + DATABASE_URL);
        } catch (SQLException e) {
            System.err.println("ERROR en conexión de prueba inicial a SQLite ("+ DATABASE_URL +"): " + e.getMessage());
            // Imprimir más detalles puede ser útil
            // e.printStackTrace(); // Quizás comentar esto si es muy verboso al inicio
        }
    }

    // Método para obtener la ruta completa al archivo DB (útil para inicializador)
    public static String getDatabaseFilePath() {
        // Devuelve la ruta relativa que usa la URL JDBC
        return DB_FOLDER + File.separator + DB_FILENAME;
    }

    // Método para obtener solo la URL JDBC
    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }
}