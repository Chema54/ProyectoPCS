/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Optional;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class ExceptionHandler {
    private static final String SQL_UNKNOWN_ERROR = "Error desconocido al procesar la solicitud. Por favor, inténtelo más tarde.";

    public static UserDisplayableException handleUnexpectedException(Logger logger, Exception e, String message) {
        logger.error("Error Inesperado: " + e.getMessage(), e);
        String detail = message + " Error inesperado. Por favor, comuníquese con el desarrollador si el error persiste.";
        return new UserDisplayableException(
            "Error Inesperado", 
            "Ha ocurrido un error inesperado", 
            detail.trim(), 
            e
        );
    }

    private static UserDisplayableException handleXMLStreamException(Logger logger, IOException e) {
        logger.error("Error al Cargar la Interfaz gráfica: Error de análisis XML. Verifique el archivo FXML.", e);
        return new UserDisplayableException(
            "Error de Análisis XML", 
            "Problema al procesar archivo FXML", 
            "Error al procesar archivo de la interfaz gráfica. Por favor, comuníquese con el desarrollador si el error persiste.", 
            e
        );
    }

    public static UserDisplayableException handleGUILoadIOException(Logger logger, IOException e) {
        if (e.getCause() instanceof XMLStreamException) {
            return handleXMLStreamException(logger, e);
        }

        if (e.getCause() instanceof ClassNotFoundException) {
            logger.error("Error al cargar la interfaz gráfica: Clase no encontrada. Verifique el archivo FXML.", e);
            return new UserDisplayableException(
                "Clase no Encontrada", 
                "Error al cargar la interfaz gráfica", 
                "Por favor, comuníquese con el desarrollador si el error persiste.", 
                e
            );
        }

        return handleIOException(logger, e, "Error al cargar la interfaz gráfica");
    }

    public static UserDisplayableException handleIOException(Logger logger, IOException e, String message) {
        if (e instanceof FileNotFoundException) {
            return handleFileNotFoundExceptionMessage(logger, (FileNotFoundException) e, message);
        }

        if (e instanceof AccessDeniedException) {
            return handleAccessDeniedExceptionMessage(logger, (AccessDeniedException) e, message);
        }

        logger.error("Error de Entrada/Salida Desconocido: {}", e);
        return new UserDisplayableException(
            "Error de Entrada/Salida", 
            "Problema de lectura/escritura", 
            "Ocurrió un error con el sistema", 
            e
        );
    }

    private static UserDisplayableException handleFileNotFoundExceptionMessage(Logger logger, FileNotFoundException e, String message) {
        logger.error("Archivo no Encontrado (Verificar Ruta de Archivo): {}", e);
        return new UserDisplayableException(
            "Ruta de Archivo no encontrada", 
            "Archivo no disponible", 
            "Ocurrió un error con el sistema", 
            e
        );
    }

    private static UserDisplayableException handleAccessDeniedExceptionMessage(Logger logger, AccessDeniedException e, String message) {
        logger.error("Acceso Denegado (Verificar Permisos): {}", e);
        return new UserDisplayableException(
            "Permisos de Archivo Denegados", 
            "Acceso restringido", 
            "Ocurrió un error con el sistema", 
            e
        );
    }

    public static NotFoundException handleNotFoundException(Logger logger, String entity) {
        logger.warn("Recurso no Encontrado: {}", entity);
        return new NotFoundException("El " + entity + " no se encontró. Por favor, verifique la información ingresada.");
    }

    public static UserDisplayableException handleSQLException(Logger logger, SQLException e) {
        return handleSQLException(logger, e, "");
    }

    public static UserDisplayableException handleSQLException(Logger logger, SQLException e, String message) {
        logger.error("Error de SQL detectado. SQLState: {}", e.getSQLState(), e);
        return new UserDisplayableException(
            "Error de Base de Datos", 
            "Error de conexión", 
            "No se ha podido realizar La operación, debido a un error de conexión con la Base de datos", 
            e
        );
    }



    private static String getSQLConnectionErrorMessage(Logger logger, SQLException e, String state) {
        if ("08S01".equals(state)) {
            logger.error("Error de comunicación: {}", e.getSQLState());
            return "Error de comunicación con la base de datos. Por favor, inténtelo más tarde.";
        }

        logger.error("Error de conexión: {}", e.getSQLState());
        return "Error de conexión a la base de datos. Por favor, inténtelo más tarde.";
    }

    private static String getSQLIntegrityErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de integridad: {}", e.getSQLState());
        return "Error de integridad de datos. Por favor, revise la información ingresada.";
    }

    private static String getSQLAuthenticationErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de autenticación detectado. SQLState: {}", e.getSQLState());
        return "Usuario y/o Contraseña Incorrectos.";
    }

    private static String getSQLSyntaxErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de sintaxis: {}", e.getSQLState());
        return "Error de sintaxis en la consulta. Por favor, contacte al administrador del sistema.";
    }
}
