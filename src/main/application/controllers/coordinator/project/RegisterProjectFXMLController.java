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
import main.service.ProyectoService;

public class RegisterProjectFXMLController implements Initializable {

    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<OrganizacionVinculadaDTO> comboBoxOrganization; 
    @FXML
    private TextField textFieldTitularName; 
    @FXML
    private TextField textFieldTitularPersonalNumber; 
    @FXML
    private TextField textFieldTotalCapacity;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private ProyectoDTO proyectoToUpdate = null;
    private TitularProyectoDTO titularToUpdate = null;

    public void initUpdate(ProyectoDTO proyecto) {
        this.proyectoToUpdate = proyecto;
        textFieldName.setText(proyecto.getName());
        textFieldTotalCapacity.setText(String.valueOf(proyecto.getTotalCapacity()));
        
        try {
            main.database.dao.TitularProyectoDAO titularDAO = new main.database.dao.TitularProyectoDAO();
            if (proyecto.getTitularId() != null) {
                this.titularToUpdate = titularDAO.getOne(proyecto.getTitularId());
                if (this.titularToUpdate != null) {
                    textFieldTitularName.setText(this.titularToUpdate.getName());
                    textFieldTitularPersonalNumber.setText(this.titularToUpdate.getNumeroPersonal());
                    for (OrganizacionVinculadaDTO org : comboBoxOrganization.getItems()) {
                        if (org.getOrganizationId() == this.titularToUpdate.getOrganizationId()) {
                            comboBoxOrganization.setValue(org);
                            break;
                        }
                    }
                }
            }
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
        
        btnGuardar.setText("Actualizar");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboBoxOrganization.setItems(FXCollections.observableArrayList(OrganizacionService.getAllOrganizaciones()));
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

            TitularProyectoDTO titular = new TitularProyectoDTO.TitularBuilder()
                    .setTitularId(titularToUpdate != null ? titularToUpdate.getTitularId() : 0)
                    .setName(textFieldTitularName.getText().trim())
                    .setNumeroPersonal(textFieldTitularPersonalNumber.getText().trim())
                    .setOrganizationId(comboBoxOrganization.getValue().getOrganizationId())
                    .build();

            ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                    .setProjectId(proyectoToUpdate != null ? proyectoToUpdate.getProjectId() : 0)
                    .setName(textFieldName.getText().trim())
                    .setTotalCapacity(capacity)
                    .setAvailableSpaces(proyectoToUpdate != null ? proyectoToUpdate.getAvailableSpaces() : capacity)
                    .setStatus(proyectoToUpdate != null ? proyectoToUpdate.getStatus() : "Sin asignar")
                    .build();

            if (proyectoToUpdate != null) {
                ProyectoService.actualizarProyecto(proyecto, titular);
                Modal.displayInformation("Actualización Exitosa", "La operación se ha realizado exitosamente");
            } else {
                ProyectoService.registrarNuevoProyecto(proyecto, titular);
                Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            }
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
        if (textFieldName.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Nombre es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTotalCapacity.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Cupo Total es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTitularName.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Nombre del Titular es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTitularPersonalNumber.getText().trim().isEmpty()) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo No. Personal del Titular es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (comboBoxOrganization.getValue() == null) {
            Modal.displayError(new UserDisplayableException("Campos Incompletos", "El campo Organización es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
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
