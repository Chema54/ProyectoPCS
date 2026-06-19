package main.application.controllers.coordinator.intern;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.business.dto.PracticanteDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.service.PracticanteService;

public class RegisterInternFXMLController implements Initializable {

    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldPaternalSurname;
    @FXML
    private TextField textFieldMaternalSurname;
    @FXML
    private TextField textFieldEnrollment;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxStatus.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        comboBoxStatus.getSelectionModel().selectFirst();
    }

    @FXML
    private void registerIntern(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            PracticanteDTO practicante = new PracticanteDTO.PracticanteBuilder()
                    .setName(textFieldName.getText().trim())
                    .setPaternalSurname(textFieldPaternalSurname.getText().trim())
                    .setMaternalSurname(textFieldMaternalSurname.getText().trim())
                    .setEnrollment(textFieldEnrollment.getText().trim())
                    .setEmail(textFieldEmail.getText().trim())
                    .setStatus(comboBoxStatus.getValue())
                    .build();

            PracticanteService.registrarNuevoPracticante(practicante);

            Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            closeWindow();

        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new UserDisplayableException(
                "Error del Sistema", 
                "No se pudo completar el registro", 
                "Ocurrió un error con el sistema", 
                e
            ));
        }
    }

    private boolean validateFields() {
        if (textFieldName.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Nombre es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldPaternalSurname.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Apellido Paterno es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldEnrollment.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Matrícula es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldEmail.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Correo Electrónico es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        return true;
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
