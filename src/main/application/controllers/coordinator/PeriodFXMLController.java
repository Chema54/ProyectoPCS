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
import main.business.dto.PeriodoDTO;
import main.common.ExceptionHandler;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.PeriodoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.business.dto.ExperienciaEducativaDTO;
import main.database.dao.ExperienciaEducativaDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class PeriodFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(PeriodFXMLController.class);

    @FXML
    private TableView<PeriodoDTO> tvPeriods;
    @FXML
    private TableColumn<PeriodoDTO, String> colPeriodName;
    @FXML
    private TableColumn<PeriodoDTO, String> colStartDate;
    @FXML
    private TableColumn<PeriodoDTO, String> colEndDate;

    @FXML
    private TableView<ExperienciaEducativaDTO> tvEducationalExperiences;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, String> colEENrc;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, Integer> colEEPeriod;

    private final PeriodoDAO periodoDAO = new PeriodoDAO();
    private final ExperienciaEducativaDAO eeDAO = new ExperienciaEducativaDAO();

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

        // Educational Experiences
        colEENrc.setCellValueFactory(new PropertyValueFactory<>("nrc"));
        colEEPeriod.setCellValueFactory(new PropertyValueFactory<>("periodId"));
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


