package main.aplicacion.controladores.practicante;

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
import main.negocio.dto.*;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.Sesion;
import main.basedatos.dao.*;
import main.negocio.servicio.ValidadorEntrega;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIEntregarEntregables implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIEntregarEntregables.class);

    @FXML private TableView<ReporteDTO> tableViewReports;
    @FXML private TableColumn<ReporteDTO, String> colReportName;
    @FXML private TableColumn<ReporteDTO, String> colReportStatus;

    @FXML private TableView<EvaluacionOVDTO> tableViewEvaluations;
    @FXML private TableColumn<EvaluacionOVDTO, String> colEvalName;
    @FXML private TableColumn<EvaluacionOVDTO, String> colEvalStatus;

    @FXML private TableView<AutoevaluacionDTO> tableViewSelfAssessments;
    @FXML private TableColumn<AutoevaluacionDTO, String> colSelfName;
    @FXML private TableColumn<AutoevaluacionDTO, String> colSelfStatus;

    private AsignacionDTO currentAssignment;
    private ReporteDAO reporteDAO = new ReporteDAO();
    private EvaluacionOVDAO evalDAO = new EvaluacionOVDAO();
    private AutoevaluacionDAO selfDAO = new AutoevaluacionDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            PracticanteDAO practicanteDAO = new PracticanteDAO();
            PracticanteDTO currentPracticante = practicanteDAO.getByEnrollment(Sesion.getCurrentUser().getNombreUsuario());
            
            if (currentPracticante != null) {
                AsignacionDAO asignacionDAO = new AsignacionDAO();
                currentAssignment = asignacionDAO.getActiveAssignmentByIntern(currentPracticante.getPracticanteId());
                if (currentAssignment != null) {
                    loadData();
                }
            }
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }    

    void loadData() throws ExcepcionMostrableUsuario {
        // Reports
        colReportName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        colReportStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tableViewReports.setItems(FXCollections.observableArrayList(reporteDAO.getAllByAssignmentId(currentAssignment.getAsignacionId())));

        // Evaluations
        colEvalName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        colEvalStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tableViewEvaluations.setItems(FXCollections.observableArrayList(evalDAO.getAllByAssignmentId(currentAssignment.getAsignacionId())));

        // Self Assessments
        colSelfName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        colSelfStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tableViewSelfAssessments.setItems(FXCollections.observableArrayList(selfDAO.getAllByAssignmentId(currentAssignment.getAsignacionId())));
    }

    private File pickPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void entregarReporte(ActionEvent event) {
        ReporteDTO selected = tableViewReports.getSelectionModel().getSelectedItem();
        
        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }
        
        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            if (!Modal.displayConfirmation("Ya existe un archivo cargado. ¿Desea sobrescribirlo?")) {
                return;
            }
        }
        
        File file = pickPDF();
        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                Modal.displayError(new ExcepcionMostrableUsuario("Formato incorrecto", "Documento inválido", "El archivo debe tener el formato PDF."));
                return;
            }
            if (!Modal.displayConfirmation("¿Desea entregar el archivo: " + file.getName() + "?")) {
                return;
            }
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                ReporteDTO updated = new ReporteDTO.ReporteBuilder()
                        .setMonthlyReportId(selected.getMonthlyReportId())
                        .setAsignacionId(selected.getAsignacionId())
                        .setNombreEntregable(selected.getNombreEntregable())
                        .setArchivo(fileBytes)
                        .setEstado("Entregado")
                        .build();
                reporteDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "Se ha enviado el Reporte con éxito");
                loadData();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void entregarEvaluacion(ActionEvent event) {
        EvaluacionOVDTO selected = tableViewEvaluations.getSelectionModel().getSelectedItem();
        
        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }
        
        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            if (!Modal.displayConfirmation("Ya existe un archivo cargado. ¿Desea sobrescribirlo?")) {
                return;
            }
        }
        
        File file = pickPDF();
        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                Modal.displayError(new ExcepcionMostrableUsuario("Formato incorrecto", "Documento inválido", "El archivo debe tener el formato PDF."));
                return;
            }
            if (!Modal.displayConfirmation("¿Desea entregar el archivo: " + file.getName() + "?")) {
                return;
            }
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                EvaluacionOVDTO updated = new EvaluacionOVDTO.LinkedOrganizationEvaluationBuilder()
                        .setLinkedOrganizationEvaluationId(selected.getLinkedOrganizationEvaluationId())
                        .setAsignacionId(selected.getAsignacionId())
                        .setNombreEntregable(selected.getNombreEntregable())
                        .setArchivo(fileBytes)
                        .setEstado("Entregado")
                        .build();
                evalDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "La evaluación ha sido enviada con éxito");
                loadData();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void uploadSelfAssessment(ActionEvent event) {
        AutoevaluacionDTO selected = tableViewSelfAssessments.getSelectionModel().getSelectedItem();
        
        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }
        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Autoevaluación Completada");
            alert.setHeaderText("Autoevaluación ya realizada");
            alert.setContentText("El documento ya se encuentra registrado en el sistema.");
            
            javafx.scene.control.ButtonType btnGuardar = new javafx.scene.control.ButtonType("Guardar PDF", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            javafx.scene.control.ButtonType btnCerrar = new javafx.scene.control.ButtonType("Cerrar", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnGuardar, btnCerrar);
            
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnGuardar) {
                if (selected.getArchivo() != null) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Guardar Autoevaluación PDF");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    fileChooser.setInitialFileName("Autoevaluacion_" + currentAssignment.getMatriculaPracticante() + ".pdf");
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                            fos.write(selected.getArchivo());
                            Modal.displayInformation("Éxito", "Documento guardado correctamente.");
                        } catch (Exception e) {
                            LOGGER.error("Error guardando autoevaluacion pdf", e);
                            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error al guardar", "No se pudo guardar el archivo."));
                        }
                    }
                } else {
                    Modal.displayInformation("Atención", "El archivo PDF no se encuentra disponible.");
                }
            }
            return;
        }
        
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main/aplicacion/vistas/practicante/GUIRealizarAutoevaluacion.fxml"));
            javafx.scene.Parent root = loader.load();
            GUIRealizarAutoevaluacion controller = loader.getController();
            
            PracticanteDAO practicanteDAO = new PracticanteDAO();
            PracticanteDTO currentPracticante = practicanteDAO.getByEnrollment(Sesion.getCurrentUser().getNombreUsuario());
            
            controller.initData(selected, currentAssignment, currentPracticante, this);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Autoevaluación");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.error("Error abriendo formulario autoevaluacion", e);
            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error GUI", "No se pudo abrir el formulario."));
        }
    }
}
