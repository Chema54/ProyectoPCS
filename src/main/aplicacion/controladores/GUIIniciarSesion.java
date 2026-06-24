/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.aplicacion.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.comun.Modal;
import main.negocio.dto.UsuarioDTO;
import main.negocio.dto.enumeracion.RolUsuario;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import main.basedatos.Sesion;
import main.basedatos.dao.UsuarioDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class GUIIniciarSesion implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(GUIIniciarSesion.class);
    
    @FXML 
    private Button buttonLogin;
    
    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

    @FXML
    private Label labelErrorUsername;

    @FXML
    private Label labelErrorPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
    }

    @FXML
    private void iniciarSesion() throws SQLException {
        String username = textFieldUsername.getText().trim();
        String password = passwordFieldPassword.getText();
        
        if(!validarCampos(username, password)){
            return;
        } 
        try {
            createConnection();
            UsuarioDAO userDAO = new UsuarioDAO();
            UsuarioDTO user = userDAO.getOne(username);
            if (user == null || !Objects.equals(user.getContrasenia(), password)) {
                throw new ExcepcionMostrableUsuario(
                    "Error de Autenticación",
                    "Inicio de sesión fallido",
                    "Usuario y/o contraseña incorrectos.",
                    null
                );
            }
            if (!user.tieneAcceso()) {
                throw new ExcepcionMostrableUsuario(
                    "Usuario Inactivo",
                    "Acceso Denegado",
                    "Su cuenta ha sido deshabilitada. Por favor, comuníquese con el coordinador para más información.",
                    null
                );
            }
            Sesion.setCurrentUser(user);
            openMenuByRole(user.getRol());
        } catch (ExcepcionMostrableUsuario e) {
            LOGGER.error("{} - {}", e.getTitle(), e.getHeader());
            Modal.displayError(e);
        }
    }
    
    private boolean validarCampos(String username, String password) {
        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
        boolean emptyFields = true;
        if (username.isEmpty()) {
            labelErrorUsername.setText("Ingrese un usuario.");
            emptyFields = false;
        }
        if (password.isEmpty()) {
            labelErrorPassword.setText("Ingrese una contraseña.");
            emptyFields = false;
        }
        return emptyFields;

    }
    
    private void createConnection() throws ExcepcionMostrableUsuario {
        String url = "jdbc:mysql://100.119.92.93/practicas_profesionales";
        String dbUser = textFieldUsername.getText().trim();
        String dbPassword = passwordFieldPassword.getText();

        ConexionBD connector = ConexionBD.getInstance(url, dbUser, dbPassword);

        try (Connection connection = connector.getConnection()) {
            if (!connection.isClosed()) {
                connector.saveProperties();
            }
        } catch(SQLException e){
            if (e.getErrorCode() == 1045 || "28000".equals(e.getSQLState())) {
                throw new ExcepcionMostrableUsuario("Acceso Denegado", "Usuario o contraseña incorrectos", "El usuario no existe o la contraseña es errónea.");
            } else if (e.getSQLState() != null && e.getSQLState().startsWith("08")) {
                throw new ExcepcionMostrableUsuario("Error de Conexión", "No hay conexión al servidor", "El servidor de base de datos no está respondiendo. Verifique su conexión a red.");
            }
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se puede conectar a la base de datos.");
        }
    }
    
    private void openMenuByRole(RolUsuario rol) throws ExcepcionMostrableUsuario {
    switch (rol) {

        case INTERN://aqui seria CASE INTERN
            openWindow("/main/aplicacion/vistas/practicante/GUIMenuPracticante.fxml");
            break;

        case COORDINADOR://aqui seria case COORDINATOR
            openWindow("/main/aplicacion/vistas/coordinador/GUIMenuCoordinador.fxml");
            break;

        case PROFESSOR://aqui seria case PROFESOR
            openWindow("/main/aplicacion/vistas/profesor/GUIMenuProfesor.fxml");
            break;
            
        default:
            Modal.displayInformation("rol de Usuario no encontrado","No se ha encontrado un rol definido para este usuario");
            break;
    }
}
    
    private void openWindow(String fxmlPath) throws ExcepcionMostrableUsuario {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Portal de Prácticas Profesionales");
            stage.show();
        } catch (IOException ex) {
            throw ManejadorExcepciones.handleGUILoadIOException(LOGGER, ex);
        }
    }
}
