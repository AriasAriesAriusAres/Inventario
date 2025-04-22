// Asegúrate que el nombre del paquete sea correcto
package Inventario_Fusionado.database;

import java.io.BufferedReader;
import java.io.File;
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
        // File dbFile = new File(DBManager.getDatabaseFilePath()); // Llama al método de DBManager
        // boolean dbExists = dbFile.exists() && dbFile.length() > 0;

        // >>> DESCOMENTAR LAS SIGUIENTES LÍNEAS SI NO QUIERES REINICIALIZAR LA BD CADA VEZ <<<
        // if (!dbExists) { 
        //    System.out.println("La base de datos no existe o está vacía. Inicializando...");
        // } else {
        //     System.out.println("La base de datos ya existe. No se reinicializa.");
        //     return; // Salir si ya existe
        // }
        // >>> FIN BLOQUE PARA DESCOMENTAR <<<

        System.out.println("Intentando inicializar base de datos desde script recurso: " + SQL_SCRIPT_RESOURCE_PATH);

        // Usar ClassLoader para leer el recurso desde el classpath
        // Usamos try-with-resources para Connection, Statement, InputStream, InputStreamReader y BufferedReader
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(SQL_SCRIPT_RESOURCE_PATH)) {

            if (is == null) {
                System.err.println("FATAL: No se pudo encontrar el archivo de script SQL como recurso: " + SQL_SCRIPT_RESOURCE_PATH);
                System.err.println("Asegúrate de que el archivo Inventario.sql esté en src/main/resources/sqlite/");
                return; // Salir si no se encuentra el script
            }

            try (Connection connection = DBManager.getConnection(); // Obtener conexión aquí
                 Statement statement = connection.createStatement(); // Crear statement aquí
                 InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8); // Especificar UTF-8
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
                        finalSql = finalSql.replaceAll("(?i)ON\\s+UPDATE\\s+CURRENT_TIMESTAMP", ""); // Quitar ON UPDATE
                        finalSql = finalSql.replaceAll("(?i)ON\\s+DELETE\\s+CASCADE", ""); // Quitar ON DELETE CASCADE si da problemas, aunque SQLite lo soporta con PRAGMA foreign_keys=ON;
                        finalSql = finalSql.replaceAll("(?i)DATETIME", "TEXT"); // Convertir DATETIME a TEXT (formato común en SQLite para fechas)
                        finalSql = finalSql.replaceAll("(?i)DECIMAL\\(\\d+,\\d+\\)", "REAL"); // Convertir DECIMAL a REAL
                        // SQLite maneja AUTOINCREMENT con INTEGER PRIMARY KEY, MySQL usa INT(11), cambiamos
                        finalSql = finalSql.replaceAll("INT\\(\\d+\\)", "INTEGER");
                        // --- Fin Limpieza ---

                        if (!finalSql.isEmpty()) {
                            try {
                                // System.out.println("Ejecutando SQL: " + finalSql); // Descomentar para depuración detallada
                                statement.execute(finalSql);
                                commandCount++;
                            } catch (SQLException sqle) {
                                // Ignorar errores comunes si la tabla/etc ya existe
                                if (!sqle.getMessage().contains("already exists") && !sqle.getMessage().contains("duplicate column name")) {
                                    System.err.println("Error ejecutando SQL: " + finalSql);
                                    System.err.println("SQLException: " + sqle.getMessage() + " (SQLState: " + sqle.getSQLState() + ", ErrorCode: " + sqle.getErrorCode() + ")");
                                } else {
                                    // System.out.println("INFO: Objeto ya existía (Ignorado): " + finalSql.substring(0, Math.min(finalSql.length(), 60)) + "...");
                                }
                            }
                        }
                        sql.setLength(0); // Vaciar el acumulador para la siguiente sentencia
                    }
                } // Fin while
                System.out.println("Inicialización de base de datos completada. Se intentaron ejecutar " + commandCount + " comandos.");

            } // Cierre automático de readers, statement, connection (si se obtuvo)

        } catch (IOException e) {
            System.err.println("Error al leer el archivo SQL recurso: " + SQL_SCRIPT_RESOURCE_PATH);
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL durante la inicialización (puede ser al obtener conexión): " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error Crítico: NullPointerException al intentar leer el recurso SQL. ¿Está el archivo en el classpath? " + SQL_SCRIPT_RESOURCE_PATH);
            e.printStackTrace();
        }
    }
}