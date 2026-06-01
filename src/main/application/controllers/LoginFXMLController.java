/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.application.controllers;

import java.io.IOException;
import java.net.URL;
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
import main.application.modal.Modal;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
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
    private void login() {
        String username = textFieldUsername.getText().trim();
        String password = passwordFieldPassword.getText();
        
        if(!verifyFields(username, password)){
            System.out.println("aqui deberias revisar el formato del texto");
            return;
        } 
        //toDo: agregar otro if validacion de caracteres 
        try {
            openMenuByRole(username);
        } catch (UserDisplayableException e) {
            Modal.displayError(e.getMessage());
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
    
    private void openMenuByRole(String rol) throws UserDisplayableException {
    switch (rol) {

        case "practicante"://aqui seria CASE INTERN
            openWindow("/main/application/views/intern/InternMenuFXML.fxml");
            break;

        case "coordinador"://aqui seria case COORDINATOR
            openWindow("/main/application/views/coordinator/CoordinatorMenuFXML.fxml");
            break;

        case "profesor"://aqui seria case COORDINATOR
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
