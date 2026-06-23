package main.application.controllers.intern;

import java.io.File;
import java.nio.file.Files;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.business.dto.*;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.Session;
import main.database.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProgressFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ProgressFXMLController.class);

    @FXML private TableView<ReportDTO> tableViewReports;
    @FXML private TableColumn<ReportDTO, String> colReportName;
    @FXML private TableColumn<ReportDTO, String> colReportStatus;

    @FXML private TableView<LinkedOrganizationEvaluationDTO> tableViewEvaluations;
    @FXML private TableColumn<LinkedOrganizationEvaluationDTO, String> colEvalName;
    @FXML private TableColumn<LinkedOrganizationEvaluationDTO, String> colEvalStatus;

    @FXML private TableView<SelfAssessmentDTO> tableViewSelfAssessments;
    @FXML private TableColumn<SelfAssessmentDTO, String> colSelfName;
    @FXML private TableColumn<SelfAssessmentDTO, String> colSelfStatus;

    private AssignmentDTO currentAssignment;
    private ReportDAO reporteDAO = new ReportDAO();
    private LinkedOrganizationEvaluationDAO evalDAO = new LinkedOrganizationEvaluationDAO();
    private SelfAssessmentDAO selfDAO = new SelfAssessmentDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            InternDAO practicanteDAO = new InternDAO();
            InternDTO currentPracticante = practicanteDAO.getByEnrollment(Session.getCurrentUser().getUsername());
            
            if (currentPracticante != null) {
                AssignmentDAO asignacionDAO = new AssignmentDAO();
                currentAssignment = asignacionDAO.getActiveAssignmentByIntern(currentPracticante.getInternId());
                if (currentAssignment != null) {
                    loadData();
                }
            }
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }    

    private void loadData() throws UserDisplayableException {
        // Reports
        colReportName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        colReportStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewReports.setItems(FXCollections.observableArrayList(reporteDAO.getAllByAssignmentId(currentAssignment.getAssignmentId())));

        // Evaluations
        colEvalName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        colEvalStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewEvaluations.setItems(FXCollections.observableArrayList(evalDAO.getAllByAssignmentId(currentAssignment.getAssignmentId())));

        // Self Assessments
        colSelfName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        colSelfStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewSelfAssessments.setItems(FXCollections.observableArrayList(selfDAO.getAllByAssignmentId(currentAssignment.getAssignmentId())));
    }

    private File pickPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void uploadReport(ActionEvent event) {
        ReportDTO selected = tableViewReports.getSelectionModel().getSelectedItem();
        if (selected == null) { Modal.displayInformation("Atención", "Seleccione un reporte."); return; }
        
        File file = pickPDF();
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                ReportDTO updated = new ReportDTO.ReportBuilder()
                        .setMonthlyReportId(selected.getMonthlyReportId())
                        .setAssignmentId(selected.getAssignmentId())
                        .setDeliverableName(selected.getDeliverableName())
                        .setFile(fileBytes)
                        .setStatus("Entregado")
                        .build();
                reporteDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "Se ha enviado el Reporte con éxito");
                loadData();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new UserDisplayableException("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void uploadEvaluation(ActionEvent event) {
        LinkedOrganizationEvaluationDTO selected = tableViewEvaluations.getSelectionModel().getSelectedItem();
        if (selected == null) { Modal.displayInformation("Atención", "Seleccione una evaluación."); return; }
        
        File file = pickPDF();
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                LinkedOrganizationEvaluationDTO updated = new LinkedOrganizationEvaluationDTO.LinkedOrganizationEvaluationBuilder()
                        .setLinkedOrganizationEvaluationId(selected.getLinkedOrganizationEvaluationId())
                        .setAssignmentId(selected.getAssignmentId())
                        .setDeliverableName(selected.getDeliverableName())
                        .setFile(fileBytes)
                        .setStatus("Entregado")
                        .build();
                evalDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "La evaluación ha sido enviada con éxito");
                loadData();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new UserDisplayableException("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void uploadSelfAssessment(ActionEvent event) {
        SelfAssessmentDTO selected = tableViewSelfAssessments.getSelectionModel().getSelectedItem();
        if (selected == null) { Modal.displayInformation("Atención", "Seleccione una autoevaluación."); return; }
        
        File file = pickPDF();
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                SelfAssessmentDTO updated = new SelfAssessmentDTO.SelfAssessmentBuilder()
                        .setSelfAssessmentId(selected.getSelfAssessmentId())
                        .setAssignmentId(selected.getAssignmentId())
                        .setDeliverableName(selected.getDeliverableName())
                        .setFile(fileBytes)
                        .setStatus("Entregado")
                        .build();
                selfDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "La autoevaluación se ha registrado exitosamente.");
                loadData();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new UserDisplayableException("Error", "Error de archivo", e.getMessage()));
            }
        }
    }
}
