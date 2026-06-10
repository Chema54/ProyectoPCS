/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application.controllers.coordinator.user;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.common.Modal;
import main.business.dto.CoordinatorDTO;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.common.UserDisplayableException;
import main.database.dao.CoordinatorDAO;
import main.database.dao.UserDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class RegisterUserCoordinatorFXMLController implements Initializable {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldAcademicNumber;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

    private final UserDAO userDAO = new UserDAO();
    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void registerCoordinator(ActionEvent event) {
        try {
            validateFields();
            String name = textFieldName.getText().trim();
            String academicNumber = textFieldAcademicNumber.getText().trim();
            String username = textFieldUsername.getText().trim();
            String password = passwordFieldPassword.getText();

            UserDTO user = new UserDTO.UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRole(UserRole.COORDINADOR)
                .setAccess(true)
                .build();

            userDAO.createOne(user);
            UserDTO createdUser = userDAO.getOne(username);
            CoordinatorDTO coordinator = new CoordinatorDTO.CoordinatorBuilder()
                .setIDUser(createdUser.getUserID())
                .setAcademicNumber(academicNumber)
                .setName(name)
                .build();
            coordinatorDAO.createOne(coordinator);
            Modal.displayInformation(
                    "Registro exitoso",
                    "El coordinador fue registrado correctamente."
            );
            clearFields();

        } catch (UserDisplayableException exception) {
            Modal.displayError(exception);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        textFieldName.getScene().getWindow().hide();
    }

    private void validateFields() throws UserDisplayableException {

    }

    private void clearFields() {
        textFieldName.clear();
        textFieldAcademicNumber.clear();
        textFieldUsername.clear();
        passwordFieldPassword.clear();
    }
}