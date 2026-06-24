package main.aplicacion.controladores.coordinador.profesor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.Modal;
import main.negocio.dto.ProfesorDTO;
import main.servicio.ServicioProfesor;

public class GUIDarAltaProfesor implements Initializable {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPaternalSurname;

    @FXML
    private TextField textFieldMaternalSurname;

    @FXML
    private TextField textFieldStaffNumber;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

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
    private void registerProfessor(ActionEvent event) {
        if (!validateRequiredFields()) {
            return;
        }

        try {
            ProfesorDTO profesor = new ProfesorDTO.ProfesorBuilder()
                .setNumeroPersonal(textFieldStaffNumber.getText().trim())
                .setNombre(textFieldName.getText().trim())
                .setApellidoPaterno(textFieldPaternalSurname.getText().trim())
                .setApellidoMaterno(textFieldMaternalSurname.getText().trim())
                .setCorreo(textFieldEmail.getText().trim())
                .setEstado(comboBoxStatus.getValue())
                .build();

            ServicioProfesor.registrarNuevoProfesor(
                profesor,
                textFieldUsername.getText().trim(),
                passwordFieldPassword.getText()
            );

            Modal.displayInformation(
                "Registro Exitoso",
                "El profesor y su usuario SQL fueron registrados correctamente."
            );

            cerrarVentana();

        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Error del Sistema",
                "No se pudo completar el registro del profesor",
                "Ocurrió un error inesperado con el sistema.",
                e
            ));
        }
    }

    private boolean validateRequiredFields() {
        if (textFieldName.getText().trim().isEmpty()
                || textFieldPaternalSurname.getText().trim().isEmpty()
                || textFieldMaternalSurname.getText().trim().isEmpty()
                || textFieldStaffNumber.getText().trim().isEmpty()
                || textFieldEmail.getText().trim().isEmpty()
                || textFieldUsername.getText().trim().isEmpty()
                || passwordFieldPassword.getText().trim().isEmpty()
                || comboBoxStatus.getValue() == null) {

            Modal.displayError(new ExcepcionMostrableUsuario(
                "Campos Incompletos",
                "Todos los campos son obligatorios",
                "Complete la información del profesor y del usuario."
            ));
            return false;
        }
        return true;
    }

    @FXML
    private void cancel(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
