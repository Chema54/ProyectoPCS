/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */

public class DBConnector {

    private static final Logger LOGGER = LogManager.getLogger(DBConnector.class);

    private static final String PROPERTIES_FILE = "database.properties";

    private static DBConnector instance;

    private final String url;
    private final String username;
    private final String password;

    private DBConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Configura o reemplaza la conexión actual.
     */
    public static synchronized DBConnector getInstance(
            String url,
            String username,
            String password) {

        instance = new DBConnector(url, username, password);
        instance.saveProperties();

        return instance;
    }

    /**
     * Obtiene la instancia configurada.
     */
    public static synchronized DBConnector getInstance() throws UserDisplayableException {
        if (instance == null) {
            throw new UserDisplayableException(
                "Error de Configuración",
                "Conexión no inicializada",
                "No se ha configurado la conexión a la base de datos."
            );
        }
        return instance;
    }

    /**
     * Obtiene una nueva conexión usando las credenciales configuradas.
     */
    public Connection getConnection() throws UserDisplayableException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                LOGGER,
                e,
                "No se pudo establecer la conexión con la base de datos."
            );
        }
    }

    /**
     * Guarda la configuración actual en un archivo properties.
     */
    public void saveProperties() {
        Properties properties = new Properties();

        properties.setProperty("db.url", url);
        properties.setProperty("db.user", username);
        properties.setProperty("db.password", password);

        try (FileOutputStream output =
                     new FileOutputStream(PROPERTIES_FILE)) {

            properties.store(output,
                    "Configuracion temporal de base de datos");

        } catch (IOException e) {
            LOGGER.error("Error al guardar archivo properties", e);
        }
    }

    /**
     * Carga la configuración desde el archivo properties.
     */
    public static void loadConfiguration() throws IOException {

        Properties properties = new Properties();

        try (FileInputStream input =
                     new FileInputStream(PROPERTIES_FILE)) {

            properties.load(input);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        instance = new DBConnector(url, user, password);
    }

    /**
     * Verifica si existe el archivo de configuración.
     */
    public static boolean configurationExists() {
        return new File(PROPERTIES_FILE).exists();
    }

    /**
     * Elimina el archivo de configuración.
     */
    public static void deleteConfiguration() {

        File file = new File(PROPERTIES_FILE);

        if (file.exists() && !file.delete()) {
            LOGGER.warn("No fue posible eliminar {}", PROPERTIES_FILE);
        }
    }
}