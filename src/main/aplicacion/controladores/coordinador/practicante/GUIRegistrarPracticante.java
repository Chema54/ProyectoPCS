package main.aplicacion.controladores.coordinador.practicante;

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
import main.negocio.dto.PracticanteDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioPracticante;

public class GUIRegistrarPracticante implements Initializable {

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

    private PracticanteDTO practicanteToUpdate = null;

    public void initUpdate(PracticanteDTO practicante) {
        this.practicanteToUpdate = practicante;
        textFieldName.setText(practicante.getNombre());
        textFieldPaternalSurname.setText(practicante.getApellidoPaterno());
        textFieldMaternalSurname.setText(practicante.getApellidoMaterno());
        textFieldEnrollment.setText(practicante.getMatricula());
        textFieldEmail.setText(practicante.getCorreo());
        comboBoxStatus.setValue(practicante.getEstado());
        btnGuardar.setText("Actualizar");
    }

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
            PracticanteDTO practicante = new PracticanteDTO.InternBuilder()
                    .setPracticanteId(practicanteToUpdate != null ? practicanteToUpdate.getPracticanteId() : 0)
                    .setNombre(textFieldName.getText().trim())
                    .setApellidoPaterno(textFieldPaternalSurname.getText().trim())
                    .setApellidoMaterno(textFieldMaternalSurname.getText().trim())
                    .setMatricula(textFieldEnrollment.getText().trim())
                    .setCorreo(textFieldEmail.getText().trim())
                    .setEstado(comboBoxStatus.getValue())
                    .build();

            if (practicanteToUpdate != null) {
                ServicioPracticante.actualizarPracticante(practicante);
                Modal.displayInformation("Actualización Exitosa", "La operación se ha realizado exitosamente");
            } else {
                ServicioPracticante.registrarNuevoPracticante(practicante);
                Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            }
            cerrarVentana();

        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Error del Sistema", 
                "No se pudo completar el registro", 
                "Ocurrió un error con el sistema", 
                e
            ));
        }
    }

    private boolean validateFields() {
        if (textFieldName.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Nombre es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldPaternalSurname.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Apellido Paterno es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldEnrollment.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Matrícula es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldEmail.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Correo Electrónico es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
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
