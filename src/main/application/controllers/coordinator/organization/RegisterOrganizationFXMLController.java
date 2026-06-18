package main.application.controllers.coordinator.organization;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.business.dto.OrganizacionVinculadaDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.service.OrganizacionService;

public class RegisterOrganizationFXMLController implements Initializable {

    @FXML
    private TextField textFieldBusinessName;
    @FXML
    private TextField textFieldLocation;
    @FXML
    private TextField textFieldPhoneNumber;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void registerOrganization(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            OrganizacionVinculadaDTO organizacion = new OrganizacionVinculadaDTO.OrganizacionVinculadaBuilder()
                    .setBusinessName(textFieldBusinessName.getText().trim())
                    .setLocation(textFieldLocation.getText().trim())
                    .setPhoneNumber(textFieldPhoneNumber.getText().trim())
                    .setEmail(textFieldEmail.getText().trim())
                    .build();

            OrganizacionService.registrarNuevaOrganizacion(organizacion);

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
        if (textFieldBusinessName.getText().trim().isEmpty() ||
            textFieldLocation.getText().trim().isEmpty() ||
            textFieldEmail.getText().trim().isEmpty()) {
            
            Modal.displayError(new UserDisplayableException(
                "Campos Incompletos",
                "Faltan datos obligatorios",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            ));
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
