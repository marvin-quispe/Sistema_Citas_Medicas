package util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static Connection connection = null;

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        Properties props = new Properties();
        String ruta = "config.properties";

        // Busca el archivo en varias ubicaciones posibles
        String[] rutas = {
            ruta,
            "src/" + ruta,
            "../" + ruta,
            System.getProperty("user.dir") + "/" + ruta,
            System.getProperty("user.dir") + "/src/" + ruta
        };

        boolean cargado = false;
        for (String path : rutas) {
            try (InputStream input = new FileInputStream(path)) {
                props.load(input);
                cargado = true;
                break;
            } catch (Exception ignored) {
            }
        }

        if (!cargado) {
            // Valores por defecto si no encuentra el archivo
            URL = "jdbc:mysql://localhost:3306/sistema_citas_medicas?useSSL=false&serverTimezone=UTC";
            USER = "root";
            PASSWORD = "root";
            return;
        }

        String host = props.getProperty("db.host", "localhost");
        String port = props.getProperty("db.port", "3306");
        String dbName = props.getProperty("db.name", "sistema_citas_medicas");
        USER = props.getProperty("db.user", "root");
        PASSWORD = props.getProperty("db.password", "root");

        URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                + "?useSSL=false&serverTimezone=UTC";
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver JDBC de MySQL. " +
                    "Agrega mysql-connector-java.jar al classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos MySQL.\n" +
                    "Verifica que MySQL esté ejecutándose en localhost:3306\n" +
                    "y que la base de datos 'sistema_citas_medicas' exista.", e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
