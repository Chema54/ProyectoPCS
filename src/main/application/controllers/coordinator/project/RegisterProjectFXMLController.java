package main.application.controllers.coordinator.project;

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
import main.business.dto.OrganizacionVinculadaDTO;
import main.business.dto.ProyectoDTO;
import main.business.dto.TitularProyectoDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.service.OrganizacionService;
import main.service.ProyectoService;
import main.service.TitularService;

public class RegisterProjectFXMLController implements Initializable {

    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<OrganizacionVinculadaDTO> comboBoxOrganization; 
    @FXML
    private ComboBox<TitularProyectoDTO> comboBoxTitular; 
    @FXML
    private TextField textFieldTotalCapacity;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxStatus.setItems(FXCollections.observableArrayList("Sin asignar", "Asignado", "Concluido"));
        comboBoxStatus.getSelectionModel().selectFirst();
        
        try {
            // Llenado dinámico sin datos hardcodeados. El combobox mostrará el nombre por el toString() del DTO.
            comboBoxOrganization.setItems(FXCollections.observableArrayList(OrganizacionService.getAllOrganizaciones()));
            comboBoxTitular.setItems(FXCollections.observableArrayList(TitularService.getAllTitulares()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void registerProject(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            int capacity = Integer.parseInt(textFieldTotalCapacity.getText().trim());

            // Usando el ID extraído transparentemente desde la selección del ComboBox
            ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                    .setName(textFieldName.getText().trim())
                    .setTitularId(comboBoxTitular.getValue().getTitularId())
                    .setTotalCapacity(capacity)
                    .setAvailableSpaces(capacity)
                    .setStatus(comboBoxStatus.getValue())
                    .build();

            ProyectoService.registrarNuevoProyecto(proyecto);

            Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            closeWindow();

        } catch (NumberFormatException e) {
            Modal.displayError(new UserDisplayableException(
                "Formato Inválido",
                "Cupo Total debe ser numérico",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            ));
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
        if (textFieldName.getText().trim().isEmpty() ||
            textFieldTotalCapacity.getText().trim().isEmpty() ||
            comboBoxOrganization.getValue() == null ||
            comboBoxTitular.getValue() == null) {
            
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
