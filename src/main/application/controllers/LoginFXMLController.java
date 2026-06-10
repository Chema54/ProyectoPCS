/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.application.controllers;

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
import main.common.Modal;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import main.database.Session;
import main.database.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class LoginFXMLController implements Initializable{
    private static final Logger LOGGER = LogManager.getLogger(LoginFXMLController.class);
    
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
    private void login() throws SQLException {
        String username = textFieldUsername.getText().trim();
        String password = passwordFieldPassword.getText();
        
        if(!verifyFields(username, password)){
            System.out.println("aqui deberias revisar el formato del texto");
            return;
        } 
        try {
            createConnection();
            UserDAO userDAO = new UserDAO();
            UserDTO user = userDAO.getOne(username);
            if (user == null || !Objects.equals(user.getPassword(), password)) {
                throw new UserDisplayableException(
                    "Error de Autenticación",
                    "Inicio de sesión fallido",
                    "Usuario y/o contraseña incorrectos.",
                    null
                );
            }
            Session.setCurrentUser(user);
            System.out.println("Usuario: " + user.getUsername());
            System.out.println("Rol: " + user.getRole());
            System.out.println("Rol name: " + user.getRole().name());
            System.out.println("Abriendo menú para: " + user.getRole());
            openMenuByRole(user.getRole());
        } catch (UserDisplayableException e) {
            LOGGER.error("{} - {}", e.getTitle(), e.getHeader());
            Modal.displayError(e);
        }
    }
    
    private boolean verifyFields(String username, String password) {
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
    
    private void createConnection() throws UserDisplayableException {
        String url = "jdbc:mysql://100.119.92.93/practicas_profesionales";
        String dbUser = textFieldUsername.getText().trim();
        String dbPassword = passwordFieldPassword.getText();

        DBConnector connector = DBConnector.getInstance(url, dbUser, dbPassword);

        try (Connection connection = connector.getConnection()) {
            if (!connection.isClosed()) {
                connector.saveProperties();
            }
        } catch(SQLException e){
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se puede conectar a la base de datos.");
        }
    }
    
    private void openMenuByRole(UserRole rol) throws UserDisplayableException {
    switch (rol) {

        case INTERN://aqui seria CASE INTERN
            openWindow("/main/application/views/intern/InternMenuFXML.fxml");
            break;

        case COORDINADOR://aqui seria case COORDINATOR
            openWindow("/main/application/views/coordinator/CoordinatorMenuFXML.fxml");
            break;

        case PROFESSOR://aqui seria case PROFESOR
            openWindow("/main/application/views/professor/ProfessorMenuFXML.fxml");
            break;
            
        default:
            Modal.displayInformation("rol de Usuario no encontrado","No se ha encontrado un rol definido para este usuario");
            break;
    }
}
    
    private void openWindow(String fxmlPath) throws UserDisplayableException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Portal de Prácticas Profesionales");
            stage.show();
        } catch (IOException ex) {
            throw ExceptionHandler.handleGUILoadIOException(LOGGER, ex);
        }
    }
}
