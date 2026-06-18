/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application.controllers.coordinator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.common.ExceptionHandler;
import main.common.Modal;
import main.common.UserDisplayableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dto.AsignacionDTO;
import main.business.dto.OrganizacionVinculadaDTO;
import main.business.dto.ProyectoDTO;
import main.database.dao.AsignacionDAO;
import main.database.dao.OrganizacionVinculadaDAO;
import main.database.dao.ProyectoDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class ProjectFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ProjectFXMLController.class);

    @FXML
    private TableView<ProyectoDTO> tvProjects;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectName;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectStatus;
    @FXML
    private TableColumn<ProyectoDTO, Integer> colProjectAvailable;
    @FXML
    private TableColumn<ProyectoDTO, Integer> colProjectTotal;

    @FXML
    private TableView<AsignacionDTO> tvAssignments;
    @FXML
    private TableColumn<AsignacionDTO, Integer> colAsigProject;
    @FXML
    private TableColumn<AsignacionDTO, Integer> colAsigIntern;
    @FXML
    private TableColumn<AsignacionDTO, String> colAsigStatus;

    @FXML
    private TableView<OrganizacionVinculadaDTO> tvOrganizations;
    @FXML
    private TableColumn<OrganizacionVinculadaDTO, String> colOrgName;
    @FXML
    private TableColumn<OrganizacionVinculadaDTO, String> colOrgLocation;
    @FXML
    private TableColumn<OrganizacionVinculadaDTO, String> colOrgEmail;
    @FXML
    private TableColumn<OrganizacionVinculadaDTO, String> colOrgPhone;

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();
    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    private final OrganizacionVinculadaDAO organizacionDAO = new OrganizacionVinculadaDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTables();
        loadAllData();
    }

    private void initializeTables() {
        // Projects
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProjectStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colProjectAvailable.setCellValueFactory(new PropertyValueFactory<>("availableSpaces"));
        colProjectTotal.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));

        // Assignments
        colAsigProject.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        colAsigIntern.setCellValueFactory(new PropertyValueFactory<>("internId"));
        colAsigStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Organizations
        colOrgName.setCellValueFactory(new PropertyValueFactory<>("businessName"));
        colOrgLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colOrgEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colOrgPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    private void loadAllData() {
        try {
            tvProjects.setItems(FXCollections.observableArrayList(proyectoDAO.getAll()));
            tvAssignments.setItems(FXCollections.observableArrayList(asignacionDAO.getAll()));
            tvOrganizations.setItems(FXCollections.observableArrayList(organizacionDAO.getAll()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void handleRegisterProject(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/project/RegisterProjectFXML.fxml", "Registrar Nuevo Proyecto");
        loadAllData();
    }

    @FXML
    private void handleAssignProject(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/intern/RegisterAssignmentFXML.fxml", "Asignar Proyecto a Practicante");
        loadAllData();
    }

    @FXML
    private void handleRegisterOrganization(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/organization/RegisterOrganizationFXML.fxml", "Registrar Organización Vinculada");
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

