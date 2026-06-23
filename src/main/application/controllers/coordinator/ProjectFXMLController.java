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
import main.business.dto.AssignmentDTO;
import main.business.dto.LinkedOrganizationDTO;
import main.business.dto.ProjectDTO;
import main.database.dao.AssignmentDAO;
import main.database.dao.LinkedOrganizationDAO;
import main.database.dao.ProjectDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class ProjectFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ProjectFXMLController.class);

    @FXML
    private TableView<ProjectDTO> tvProjects;
    @FXML
    private TableColumn<ProjectDTO, String> colProjectName;
    @FXML
    private TableColumn<ProjectDTO, String> colProjectOrg;
    @FXML
    private TableColumn<ProjectDTO, String> colProjectTitular;
    @FXML
    private TableColumn<ProjectDTO, String> colProjectStatus;
    @FXML
    private TableColumn<ProjectDTO, Integer> colProjectAvailable;
    @FXML
    private TableColumn<ProjectDTO, Integer> colProjectTotal;

    @FXML
    private TableView<AssignmentDTO> tvAssignments;
    @FXML
    private TableColumn<AssignmentDTO, String> colAsigProject;
    @FXML
    private TableColumn<AssignmentDTO, String> colAsigMatricula;
    @FXML
    private TableColumn<AssignmentDTO, String> colAsigIntern;
    @FXML
    private TableColumn<AssignmentDTO, String> colAsigNrc;
    @FXML
    private TableColumn<AssignmentDTO, String> colAsigStatus;

    @FXML
    private TableView<LinkedOrganizationDTO> tvOrganizations;
    @FXML
    private TableColumn<LinkedOrganizationDTO, String> colOrgName;
    @FXML
    private TableColumn<LinkedOrganizationDTO, String> colOrgLocation;
    @FXML
    private TableColumn<LinkedOrganizationDTO, String> colOrgEmail;
    @FXML
    private TableColumn<LinkedOrganizationDTO, String> colOrgPhone;

    private final ProjectDAO proyectoDAO = new ProjectDAO();
    private final AssignmentDAO asignacionDAO = new AssignmentDAO();
    private final LinkedOrganizationDAO organizacionDAO = new LinkedOrganizationDAO();

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
        colProjectOrg.setCellValueFactory(new PropertyValueFactory<>("organizationName"));
        colProjectTitular.setCellValueFactory(new PropertyValueFactory<>("titularDisplay"));
        colProjectStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colProjectAvailable.setCellValueFactory(new PropertyValueFactory<>("availableSpaces"));
        colProjectTotal.setCellValueFactory(new PropertyValueFactory<>("totalCapacity"));

        // Assignments
        colAsigProject.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colAsigMatricula.setCellValueFactory(new PropertyValueFactory<>("practicanteMatricula"));
        colAsigIntern.setCellValueFactory(new PropertyValueFactory<>("practicanteName"));
        colAsigNrc.setCellValueFactory(new PropertyValueFactory<>("nrc"));
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
    private void handleModifyProject(ActionEvent event) {
        ProjectDTO selected = tvProjects.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new UserDisplayableException("Selección requerida", "Debe seleccionar un proyecto de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        openModifyProjectWindow(selected);
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

    @FXML
    private void handleModifyOrganization(ActionEvent event) {
        LinkedOrganizationDTO selected = tvOrganizations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new UserDisplayableException("Selección requerida", "Debe seleccionar una organización de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        openModifyOrganizationWindow(selected);
    }

    private void openModifyProjectWindow(ProjectDTO proyecto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/application/views/coordinator/project/RegisterProjectFXML.fxml"));
            Parent root = loader.load();
            main.application.controllers.coordinator.project.RegisterProjectFXMLController controller = loader.getController();
            controller.initUpdate(proyecto);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Proyecto: " + proyecto.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadAllData();
        } catch (IOException e) {
            Modal.displayError(ExceptionHandler.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana de modificación"));
        }
    }

    private void openModifyOrganizationWindow(LinkedOrganizationDTO organizacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/application/views/coordinator/organization/RegisterOrganizationFXML.fxml"));
            Parent root = loader.load();
            main.application.controllers.coordinator.organization.RegisterOrganizationFXMLController controller = loader.getController();
            controller.initUpdate(organizacion);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Organización: " + organizacion.getBusinessName());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadAllData();
        } catch (IOException e) {
            Modal.displayError(ExceptionHandler.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana de modificación"));
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

