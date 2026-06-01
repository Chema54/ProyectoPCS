/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public static synchronized DBConnector getInstance(String url, String username, String password) {
        instance = new DBConnector(url, username, password);
        return instance;
    }

    /**
     * Obtiene la instancia configurada.
     */
    public static synchronized DBConnector getInstance() throws UserDisplayableException {

        if (instance == null) {
            throw new UserDisplayableException("No se ha configurado la conexión a la base de datos.");
        }
        return instance;
    }

    /**
     * Obtiene una nueva conexión usando las credenciales configuradas.
     */
    public Connection getConnection()throws UserDisplayableException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e);
        }
    }
}