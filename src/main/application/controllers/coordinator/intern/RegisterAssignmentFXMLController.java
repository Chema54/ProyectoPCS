package main.application.controllers.coordinator.intern;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.business.dto.AsignacionDTO;
import main.business.dto.ExperienciaEducativaDTO;
import main.business.dto.PracticanteDTO;
import main.business.dto.ProyectoDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.service.AsignacionService;
import main.service.ExperienciaEducativaService;
import main.service.PracticanteService;
import main.service.ProyectoService;

public class RegisterAssignmentFXMLController implements Initializable {

    @FXML
    private ComboBox<PracticanteDTO> comboBoxIntern;
    @FXML
    private ComboBox<ProyectoDTO> comboBoxProject;
    @FXML
    private ComboBox<ExperienciaEducativaDTO> comboBoxEducationalExperience;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboBoxIntern.setItems(FXCollections.observableArrayList(PracticanteService.getAllPracticantes()));
            comboBoxProject.setItems(FXCollections.observableArrayList(ProyectoService.getAllProyectos()));
            comboBoxEducationalExperience.setItems(FXCollections.observableArrayList(ExperienciaEducativaService.getAllExperiencias()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void registerAssignment(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            AsignacionDTO asignacion = new AsignacionDTO.AsignacionBuilder()
                    .setInternId(comboBoxIntern.getValue().getInternId())
                    .setProjectId(comboBoxProject.getValue().getProjectId())
                    .setEducationalExperienceId(comboBoxEducationalExperience.getValue().getEducationalExperienceId())
                    .setStatus("Activa")
                    .build();

            AsignacionService.registrarNuevaAsignacion(asignacion);

            Modal.displayInformation("Asignación Exitosa", "La operación se ha realizado exitosamente");
            closeWindow();

        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new UserDisplayableException(
                "Error del Sistema", 
                "No se pudo completar la asignación", 
                "Ocurrió un error con el sistema", 
                e
            ));
        }
    }

    private boolean validateFields() {
        if (comboBoxIntern.getValue() == null ||
            comboBoxProject.getValue() == null ||
            comboBoxEducationalExperience.getValue() == null) {
            
            Modal.displayError(new UserDisplayableException(
                "Campos Incompletos",
                "Faltan selecciones obligatorias",
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
