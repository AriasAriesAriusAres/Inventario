package Inventario_Fusionado.database; // Asegúrate que el paquete coincida

import java.io.BufferedReader;
// Removemos FileReader ya que leeremos como recurso
// import java.io.FileReader;
import java.io.File; // Importamos File
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    // Ruta al script SQL como recurso dentro del JAR/WAR
    private static final String SQL_SCRIPT_RESOURCE_PATH = "/sqlite/Inventario.sql"; // Ruta desde la raíz del classpath

    // Método para inicializar la base de datos ejecutando el script SQL
    public static void initializeDatabase() {
        // Verificar si la base de datos ya existe para no reinicializar siempre
        File dbFile = new File(DBManager.getDatabaseFilePath()); // Llama al método de DBManager
        boolean dbExists = dbFile.exists() && dbFile.length() > 0;

        // Solo inicializar si la base de datos NO existe o está vacía
        // Descomentar estas 3 líneas si NO quieres que se ejecute siempre al arrancar
        // if (!dbExists) {
        //    System.out.println("La base de datos no existe o está vacía. Inicializando...");
        // } else {
        //     System.out.println("La base de datos ya existe en: " + dbFile.getAbsolutePath() + ". No se reinicializa.");
        //     return; // Salir si ya existe
        // }

        System.out.println("Intentando inicializar base de datos desde script recurso: " + SQL_SCRIPT_RESOURCE_PATH);

        // Usar ClassLoader para leer el recurso desde el classpath
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(SQL_SCRIPT_RESOURCE_PATH);
             Connection connection = DBManager.getConnection(); // Obtener conexión
             Statement statement = connection.createStatement()) { // Crear statement aquí

            if (is == null) {
                System.err.println("FATAL: No se pudo encontrar el archivo de script SQL como recurso: " + SQL_SCRIPT_RESOURCE_PATH);
                System.err.println("Asegúrate de que el archivo Inventario.sql esté en src/main/resources/sqlite/");
                return; // Salir si no se encuentra el script
            }

            // Usar try-with-resources para los readers también
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8); // Especificar UTF-8
                 BufferedReader reader = new BufferedReader(isr)) {

                String line;
                StringBuilder sql = new StringBuilder();
                int commandCount = 0;

                // Leer línea por línea del archivo SQL
                while ((line = reader.readLine()) != null) {
                    // Ignorar comentarios SQL y líneas vacías
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("--") || trimmedLine.startsWith("/*") || trimmedLine.startsWith("LOCK TABLES") || trimmedLine.startsWith("UNLOCK TABLES") || trimmedLine.startsWith("/*!")) {
                        continue;
                    }

                    sql.append(line).append(" "); // Añadir espacio entre líneas

                    // Ejecutar una sentencia completa cuando termina con ";"
                    if (trimmedLine.endsWith(";")) {
                        String finalSql = sql.toString().trim();
                        // Quitar el punto y coma final para evitar errores en algunos drivers/modos
                        if (finalSql.endsWith(";")) {
                            finalSql = finalSql.substring(0, finalSql.length() - 1);
                        }

                        // --- Limpieza de Sintaxis MySQL para SQLite ---
                        finalSql = finalSql.replaceAll("ENGINE\\s*=\\s*InnoDB", "");
                        finalSql = finalSql.replaceAll("AUTO_INCREMENT\\s*=\\s*\\d+", "");
                        finalSql = finalSql.replaceAll("COLLATE\\s*=\\s*\\w+", "");
                        finalSql = finalSql.replaceAll("DEFAULT\\s+CHARSET\\s*=\\s*\\w+", "");
                        finalSql = finalSql.replaceAll("COMMENT\\s+'.*?'", "");
                        finalSql = finalSql.replaceAll("VISIBLE", "");
                        // SQLite maneja AUTOINCREMENT con INTEGER PRIMARY KEY, MySQL usa INT(11), cambiamos
                        finalSql = finalSql.replaceAll("INT\\(\\d+\\)", "INTEGER");
                        // --- Fin Limpieza ---

                        if (!finalSql.isEmpty()) {
                            try {
                                // System.out.println("Ejecutando SQL: " + finalSql); // Descomentar para depuración detallada
                                statement.execute(finalSql);
                                commandCount++;
                            } catch (SQLException sqle) {
                                // Ignorar errores comunes si la tabla/etc ya existe (puede pasar si no se borra el .db)
                                if (!sqle.getMessage().contains("already exists")) {
                                    System.err.println("Error ejecutando SQL: " + finalSql);
                                    System.err.println("SQLException: " + sqle.getMessage() + " (SQLState: " + sqle.getSQLState() + ", ErrorCode: " + sqle.getErrorCode() + ")");
                                    // Podrías decidir si continuar o detenerte ante un error
                                } else {
                                    System.out.println("INFO: Objeto ya existía (Ignorado): " + finalSql.substring(0, Math.min(finalSql.length(), 60)) + "...");
                                }
                            }
                        }
                        sql.setLength(0); // Vaciar el acumulador para la siguiente sentencia
                    }
                } // Fin while
                System.out.println("Inicialización de base de datos completada. Se intentaron ejecutar " + commandCount + " comandos.");
            } // Cierre automático de readers

        } catch (IOException e) {
            System.err.println("Error al leer el archivo SQL recurso: " + SQL_SCRIPT_RESOURCE_PATH);
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL durante la inicialización de la base de datos (conexión o statement): " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error Crítico: NullPointerException al intentar leer el recurso SQL. ¿Está el archivo en el classpath? " + SQL_SCRIPT_RESOURCE_PATH);
            e.printStackTrace();
        }
    }
}