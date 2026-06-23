/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application.controllers.coordinator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.business.dto.PeriodDTO;
import main.common.ExceptionHandler;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.PeriodDAO;
import main.service.PeriodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.business.dto.EducationalExperienceDTO;
import main.database.dao.EducationalExperienceDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class PeriodFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(PeriodFXMLController.class);

    @FXML
    private TableView<PeriodDTO> tvPeriods;
    @FXML
    private TableColumn<PeriodDTO, String> colPeriodName;
    @FXML
    private TableColumn<PeriodDTO, String> colStartDate;
    @FXML
    private TableColumn<PeriodDTO, String> colEndDate;
    @FXML
    private TableColumn<PeriodDTO, String> colPeriodStatus;

    @FXML
    private TableView<EducationalExperienceDTO> tvEducationalExperiences;
    @FXML
    private TableColumn<EducationalExperienceDTO, String> colEENrc;
    @FXML
    private TableColumn<EducationalExperienceDTO, String> colEEName;
    @FXML
    private TableColumn<EducationalExperienceDTO, String> colEEPeriod;
    @FXML
    private TableColumn<EducationalExperienceDTO, String> colEEProfessor;

    private final PeriodDAO periodoDAO = new PeriodDAO();
    private final EducationalExperienceDAO eeDAO = new EducationalExperienceDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTables();
        loadAllData();
    }

    private void initializeTables() {
        // Periods
        colPeriodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colPeriodStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Educational Experiences
        colEENrc.setCellValueFactory(new PropertyValueFactory<>("nrc"));
        colEEName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEEPeriod.setCellValueFactory(new PropertyValueFactory<>("periodName"));
        colEEProfessor.setCellValueFactory(new PropertyValueFactory<>("professorName"));
    }

    private void loadAllData() {
        try {
            tvPeriods.setItems(FXCollections.observableArrayList(periodoDAO.getAll()));
            tvEducationalExperiences.setItems(FXCollections.observableArrayList(eeDAO.getAll()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    private void loadPeriods() {
        loadAllData();
    }

    @FXML
    private void handleRegisterPeriod(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/period/RegisterPeriodFXML.fxml", "Abrir Periodo Escolar");
        loadAllData();
    }

    @FXML
    private void handleOpenPeriod(ActionEvent event) {
        PeriodDTO selected = tvPeriods.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new UserDisplayableException("Selección requerida", "No se ha seleccionado ningún periodo", "Por favor seleccione un periodo de la tabla para abrirlo."));
            return;
        }
        
        boolean confirm = Modal.displayConfirmation("¿Está seguro que desea abrir el periodo " + selected.getName() + "?\nEsto activará los proyectos y generará los documentos de asignación.");
        if (!confirm) {
            return;
        }
        
        try {
            PeriodService.openPeriod(selected.getPeriodId());
            Modal.displayInformation("Éxito", "El periodo escolar se ha abierto exitosamente.");
            loadAllData();
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new UserDisplayableException("Error del Sistema", "No se pudo abrir el periodo", "Ocurrió un error inesperado", e));
        }
    }

    private void openModalWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            Modal.displayError(ExceptionHandler.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana: " + title));
        }
    }
}


