/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.aplicacion.controladores.coordinador.usuario;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.comun.Modal;
import main.negocio.dto.CoordinadorDTO;
import main.negocio.dto.UsuarioDTO;
import main.negocio.dto.enumeracion.RolUsuario;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.CoordinadorDAO;
import main.basedatos.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class GUIRegistrarUsuarioCoordinador implements Initializable {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldAcademicNumber;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

    private final UsuarioDAO userDAO = new UsuarioDAO();
    private final CoordinadorDAO coordinatorDAO = new CoordinadorDAO();

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

            UsuarioDTO user = new UsuarioDTO.UsuarioBuilder()
                .setNombreUsuario(username)
                .setContrasenia(password)
                .setRol(RolUsuario.COORDINADOR)
                .setAcceso(true)
                .build();

            userDAO.createOne(user);
            UsuarioDTO createdUser = userDAO.getOne(username);
            CoordinadorDTO coordinator = new CoordinadorDTO.CoordinadorBuilder()
                .setUsuarioId(createdUser.getUsuarioId())
                .setNumeroPersonal(academicNumber)
                .setNombre(name)
                .build();
            coordinatorDAO.createOne(coordinator);
            Modal.displayInformation(
                    "Registro exitoso",
                    "El coordinador fue registrado correctamente."
            );
            clearFields();

        } catch (ExcepcionMostrableUsuario exception) {
            Modal.displayError(exception);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        textFieldName.getScene().getWindow().hide();
    }

    private void validateFields() throws ExcepcionMostrableUsuario {
    }

    private void clearFields() {
        textFieldName.clear();
        textFieldAcademicNumber.clear();
        textFieldUsername.clear();
        passwordFieldPassword.clear();
    }
}