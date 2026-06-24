package main.basedatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConexionBD {

    private static final Logger LOGGER = LogManager.getLogger(ConexionBD.class);

    private static final String PROPERTIES_FILE = "database.properties";

    private static ConexionBD instance;

    private final String url;
    private final String username;
    private final String password;

    private ConexionBD(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static synchronized ConexionBD getInstance(
            String url,
            String username,
            String password) {

        instance = new ConexionBD(url, username, password);
        instance.saveProperties();

        return instance;
    }

    public static synchronized ConexionBD getInstance() throws ExcepcionMostrableUsuario {
        if (instance == null) {
            throw new ExcepcionMostrableUsuario(
                    "Error de Configuración",
                    "Conexión no inicializada",
                    "No se ha configurado la conexión a la base de datos."
            );
        }
        return instance;
    }

    public Connection getConnection() throws ExcepcionMostrableUsuario {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se pudo establecer la conexión con la base de datos."
            );
        }
    }

    public void saveProperties() {
        Properties properties = new Properties();

        properties.setProperty("db.url", url);
        properties.setProperty("db.usuario", username);
        properties.setProperty("db.password", password);

        try (FileOutputStream output
                = new FileOutputStream(PROPERTIES_FILE)) {

            properties.store(output,
                    "Configuracion temporal de base de datos");

        } catch (IOException e) {
            LOGGER.error("Error al guardar archivo properties", e);
        }
    }

    public static void loadConfiguration() throws IOException {

        Properties properties = new Properties();

        try (FileInputStream input
                = new FileInputStream(PROPERTIES_FILE)) {

            properties.load(input);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.usuario");
        String password = properties.getProperty("db.password");

        instance = new ConexionBD(url, user, password);
    }

    public static boolean configurationExists() {
        return new File(PROPERTIES_FILE).exists();
    }

    public static void deleteConfiguration() {

        File file = new File(PROPERTIES_FILE);

        if (file.exists() && !file.delete()) {
            LOGGER.warn("No fue posible eliminar {}", PROPERTIES_FILE);
        }
    }
}
