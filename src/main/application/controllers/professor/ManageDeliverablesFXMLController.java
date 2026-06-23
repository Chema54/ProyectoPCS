package main.application.controllers.professor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.business.dto.*;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManageDeliverablesFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ManageDeliverablesFXMLController.class);

    @FXML private Label labelTitle;
    @FXML private TableView<ReportDTO> tableReportes;
    @FXML private TableColumn<ReportDTO, String> colRepName;
    @FXML private TableColumn<ReportDTO, String> colRepStatus;

    // We use a generic representation or just LinkedOrganizationEvaluationDTO for the table since it has the same getters
    @FXML private TableView<LinkedOrganizationEvaluationDTO> tableEvals;
    @FXML private TableColumn<LinkedOrganizationEvaluationDTO, String> colEvalName;
    @FXML private TableColumn<LinkedOrganizationEvaluationDTO, String> colEvalStatus;

    private EducationalExperienceDTO experiencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colRepName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        colRepStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colEvalName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        colEvalStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setExperiencia(EducationalExperienceDTO experiencia) {
        this.experiencia = experiencia;
        labelTitle.setText("Entregables de: " + experiencia.getName());
        loadData();
    }

    public void loadData() {
        try {
            ReportDAO repDao = new ReportDAO();
            tableReportes.setItems(FXCollections.observableArrayList(repDao.getUniqueDeliverablesByExperiencia(experiencia.getEducationalExperienceId())));

            LinkedOrganizationEvaluationDAO evalDao = new LinkedOrganizationEvaluationDAO();
            SelfAssessmentDAO autoDao = new SelfAssessmentDAO();
            
            List<LinkedOrganizationEvaluationDTO> allEvals = evalDao.getUniqueDeliverablesByExperiencia(experiencia.getEducationalExperienceId());
            // Add autoevaluaciones casted as LinkedOrganizationEvaluationDTO just for the table display since they share properties
            List<SelfAssessmentDTO> autos = autoDao.getUniqueDeliverablesByExperiencia(experiencia.getEducationalExperienceId());
            for (SelfAssessmentDTO a : autos) {
                allEvals.add(new LinkedOrganizationEvaluationDTO.LinkedOrganizationEvaluationBuilder().setDeliverableName(a.getDeliverableName()).setStatus(a.getStatus()).build());
            }
            tableEvals.setItems(FXCollections.observableArrayList(allEvals));
            
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/application/views/professor/ProfessorExperiencesFXML.fxml"));
            Parent view = loader.load();
            ((javafx.scene.layout.AnchorPane) labelTitle.getParent()).getChildren().setAll(view);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @FXML
    private void configurarReporte(ActionEvent event) {
        ReportDTO selected = tableReportes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Seleccione un reporte de la tabla.");
            return;
        }
        if (!"Inhabilitado".equalsIgnoreCase(selected.getStatus())) {
            Modal.displayInformation("Atención", "El reporte ya está habilitado o cerrado.");
            return;
        }
        abrirModal(selected.getDeliverableName(), "REPORTE");
    }

    @FXML
    private void configurarEvaluacion(ActionEvent event) {
        LinkedOrganizationEvaluationDTO selected = tableEvals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Seleccione una evaluación de la tabla.");
            return;
        }
        if (!"Inhabilitado".equalsIgnoreCase(selected.getStatus())) {
            Modal.displayInformation("Atención", "El entregable ya está habilitado o cerrado.");
            return;
        }
        String tipo = selected.getDeliverableName().toLowerCase().contains("auto") ? "AUTOEVALUACION" : "EVALUACIONOV";
        abrirModal(selected.getDeliverableName(), tipo);
    }

    private void abrirModal(String nombreDoc, String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/application/views/professor/ModalEnableDeliveryFXML.fxml"));
            Parent root = loader.load();
            ModalEnableDeliveryFXMLController controller = loader.getController();
            controller.initData(nombreDoc, tipo, experiencia.getEducationalExperienceId(), this);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Configurar Entrega");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Error abriendo modal", e);
        }
    }
}
