/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.aplicacion.controladores.coordinador;

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
import main.comun.ManejadorExcepciones;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.negocio.dto.AsignacionDTO;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.negocio.dto.ProyectoDTO;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.OrganizacionVinculadaDAO;
import main.basedatos.dao.ProyectoDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class GUIAdministrarProyectos implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIAdministrarProyectos.class);

    @FXML
    private TableView<ProyectoDTO> tvProjects;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectName;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectOrg;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectTitular;
    @FXML
    private TableColumn<ProyectoDTO, String> colProjectStatus;
    @FXML
    private TableColumn<ProyectoDTO, Integer> colProjectTotal;
    @FXML
    private TableColumn<ProyectoDTO, Integer> colProjectAvailable;

    @FXML
    private TableView<AsignacionDTO> tvAssignments;
    @FXML
    private TableColumn<AsignacionDTO, String> colAsigProject;
    @FXML
    private TableColumn<AsignacionDTO, String> colAsigMatricula;
    @FXML
    private TableColumn<AsignacionDTO, String> colAsigIntern;
    @FXML
    private TableColumn<AsignacionDTO, String> colAsigNrc;
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
    @FXML
    private TextField txtSearchProject;
    @FXML
    private ComboBox<?> cmbFilterProject;
    @FXML
    private Button btnModifyProject;
    @FXML
    private Button btnRegisterProject;
    @FXML
    private Button btnAssignProject;
    @FXML
    private ComboBox<?> cmbFilterAssignment;
    @FXML
    private TextField txtSearchIntern;
    @FXML
    private TextField txtSearchOrganization;
    @FXML
    private Button btnModifyOrganization;
    @FXML
    private Button btnRegisterOrganization;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTables();
        cargarDatosTabla();
    }

    private void initializeTables() {
        // Projects
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colProjectOrg.setCellValueFactory(new PropertyValueFactory<>("nombreOrganizacion"));
        colProjectTitular.setCellValueFactory(new PropertyValueFactory<>("nombreTitular"));
        colProjectStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colProjectTotal.setCellValueFactory(new PropertyValueFactory<>("cupoTotal"));
        colProjectAvailable.setCellValueFactory(new PropertyValueFactory<>("espaciosDisponibles"));

        // Assignments
        colAsigProject.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        colAsigMatricula.setCellValueFactory(new PropertyValueFactory<>("matriculaPracticante"));
        colAsigIntern.setCellValueFactory(new PropertyValueFactory<>("nombrePracticante"));
        colAsigNrc.setCellValueFactory(new PropertyValueFactory<>("nrc"));
        colAsigStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Organizations
        colOrgName.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colOrgLocation.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colOrgEmail.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colOrgPhone.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }

    private void cargarDatosTabla() {
        try {
            tvProjects.setItems(FXCollections.observableArrayList(proyectoDAO.getAll()));
            tvAssignments.setItems(FXCollections.observableArrayList(asignacionDAO.getAll()));
            tvOrganizations.setItems(FXCollections.observableArrayList(organizacionDAO.getAll()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void abrirRegistroProyecto(ActionEvent event) {
        openModalWindow("/main/aplicacion/vistas/coordinador/proyecto/GUIRegistrarProyecto.fxml", "Registrar Nuevo Proyecto");
        cargarDatosTabla();
    }

    @FXML
    private void handleModifyProject(ActionEvent event) {
        ProyectoDTO selected = tvProjects.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Selección requerida", "Debe seleccionar un proyecto de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        openModifyProjectWindow(selected);
    }

    @FXML
    private void handleAssignProject(ActionEvent event) {
        openModalWindow("/main/aplicacion/vistas/coordinador/practicante/GUIRegistrarAsignacion.fxml", "Asignar Proyecto a Practicante");
        cargarDatosTabla();
    }

    @FXML
    private void handleRegisterOrganization(ActionEvent event) {
        openModalWindow("/main/aplicacion/vistas/coordinador/organizacion/GUIRegistrarOrganizacion.fxml", "Registrar Organización Vinculada");
        cargarDatosTabla();
    }

    @FXML
    private void handleModifyOrganization(ActionEvent event) {
        OrganizacionVinculadaDTO selected = tvOrganizations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Selección requerida", "Debe seleccionar una organización de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        openModifyOrganizationWindow(selected);
    }

    private void openModifyProjectWindow(ProyectoDTO proyecto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/coordinador/proyecto/GUIRegistrarProyecto.fxml"));
            Parent root = loader.load();
            main.aplicacion.controladores.coordinador.proyecto.GUIRegistrarProyecto controller = loader.getController();
            controller.initUpdate(proyecto);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Proyecto: " + proyecto.getNombre());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarDatosTabla();
        } catch (IOException e) {
            Modal.displayError(ManejadorExcepciones.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana de modificación"));
        }
    }

    private void openModifyOrganizationWindow(OrganizacionVinculadaDTO organizacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/coordinador/organizacion/GUIRegistrarOrganizacion.fxml"));
            Parent root = loader.load();
            main.aplicacion.controladores.coordinador.organizacion.GUIRegistrarOrganizacion controller = loader.getController();
            controller.initUpdate(organizacion);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Organización: " + organizacion.getNombreEmpresa());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cargarDatosTabla();
        } catch (IOException e) {
            Modal.displayError(ManejadorExcepciones.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana de modificación"));
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
            Modal.displayError(ManejadorExcepciones.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana: " + title));
        }
    }
}

