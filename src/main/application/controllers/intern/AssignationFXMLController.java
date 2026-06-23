package main.application.controllers.intern;

import java.io.File;
import java.nio.file.Files;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.business.dto.AssignmentDTO;
import main.business.dto.AcceptanceDocumentDTO;
import main.business.dto.InternDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.Session;
import main.database.dao.AssignmentDAO;
import main.database.dao.AcceptanceDocumentDAO;
import main.database.dao.InternDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AssignationFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(AssignationFXMLController.class);

    @FXML private Label labelProjectName;
    @FXML private Label labelInternName;
    @FXML private Label labelMatricula;
    @FXML private Label labelNRC;
    
    @FXML private TableView<AcceptanceDocumentDTO> tableViewDocuments;
    @FXML private TableColumn<AcceptanceDocumentDTO, String> columnName;
    @FXML private TableColumn<AcceptanceDocumentDTO, String> columnStatus;

    private AssignmentDTO currentAssignment;
    private AcceptanceDocumentDAO documentDAO = new AcceptanceDocumentDAO();
    private InternDTO currentPracticante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            InternDAO practicanteDAO = new InternDAO();
            currentPracticante = practicanteDAO.getByEnrollment(Session.getCurrentUser().getUsername());
            
            if (currentPracticante != null) {
                AssignmentDAO asignacionDAO = new AssignmentDAO();
                currentAssignment = asignacionDAO.getActiveAssignmentByIntern(currentPracticante.getInternId());
                
                if (currentAssignment != null) {
                    labelProjectName.setText(currentAssignment.getProjectName());
                    labelInternName.setText(currentAssignment.getPracticanteName());
                    labelMatricula.setText(currentAssignment.getPracticanteMatricula());
                    labelNRC.setText(currentAssignment.getNrc());
                    loadDocuments();
                } else {
                    Modal.displayInformation("Sin Asignación", "No cuentas con un proyecto asignado activo.");
                }
            }
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }    

    private void loadDocuments() throws UserDisplayableException {
        List<AcceptanceDocumentDTO> docs = documentDAO.getAllByAssignmentId(currentAssignment.getAssignmentId());
        ObservableList<AcceptanceDocumentDTO> observableDocs = FXCollections.observableArrayList(docs);
        columnName.setCellValueFactory(new PropertyValueFactory<>("deliverableName"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewDocuments.setItems(observableDocs);
    }

    @FXML
    private void downloadOficio(ActionEvent event) {
        if (currentAssignment == null) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Oficio de Asignación");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("Oficio_Asignacion_" + currentAssignment.getPracticanteMatricula() + ".txt");
        File file = fileChooser.showSaveDialog(null);
        
        if (file != null) {
            try {
                String content = "OFICIO DE ASIGNACIÓN\n\n" +
                        "Proyecto: " + currentAssignment.getProjectName() + "\n" +
                        "Practicante: " + currentAssignment.getPracticanteName() + "\n" +
                        "Matrícula: " + currentAssignment.getPracticanteMatricula() + "\n" +
                        "NRC: " + currentAssignment.getNrc() + "\n\n" +
                        "Estado: " + currentAssignment.getStatus();
                Files.write(file.toPath(), content.getBytes());
                Modal.displayInformation("Éxito", "Oficio generado y descargado exitosamente.");
            } catch (Exception e) {
                LOGGER.error("Error guardando oficio", e);
                Modal.displayError(new UserDisplayableException("Error", "Error de archivo", "No se pudo guardar el archivo."));
            }
        }
    }

    @FXML
    private void uploadDocument(ActionEvent event) {
        AcceptanceDocumentDTO selected = tableViewDocuments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Por favor, seleccione un documento de la tabla.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Documento PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                AcceptanceDocumentDTO updated = new AcceptanceDocumentDTO.AcceptanceDocumentBuilder()
                        .setAcceptanceDocumentId(selected.getAcceptanceDocumentId())
                        .setAssignmentId(selected.getAssignmentId())
                        .setDeliverableName(selected.getDeliverableName())
                        .setFile(fileBytes)
                        .setStatus("Entregado")
                        .build();
                documentDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "Se han enviado los documentos con éxito");
                loadDocuments();
            } catch (Exception e) {
                LOGGER.error("Error subiendo archivo", e);
                Modal.displayError(new UserDisplayableException("Error", "Error de archivo", "No se pudo cargar el archivo."));
            }
        }
    }
}
