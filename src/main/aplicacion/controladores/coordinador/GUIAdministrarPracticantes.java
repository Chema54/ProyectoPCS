package main.aplicacion.controladores.coordinador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import main.negocio.dto.CoordinadorDTO;
import main.negocio.dto.PracticanteDTO;
import main.comun.ManejadorExcepciones;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.CoordinadorDAO;
import main.basedatos.dao.PracticanteDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.negocio.dto.ProfesorDTO;
import main.basedatos.dao.ProfesorDAO;

public class GUIAdministrarPracticantes implements Initializable {

    private static final Logger LOGGER
            = LogManager.getLogger(GUIAdministrarPracticantes.class);

    @FXML
    private Tab tabIntern;

    @FXML
    private TableView<PracticanteDTO> tvInterns;

    @FXML
    private TableColumn<PracticanteDTO, String> colInternUser;

    @FXML
    private TableColumn<PracticanteDTO, String> colInternEnrollment;

    @FXML
    private TableColumn<PracticanteDTO, String> colInternName;

    @FXML
    private TableColumn<PracticanteDTO, String> colInternStatus;

    @FXML
    private TextField txtSearchIntern;

    @FXML
    private ComboBox<String> cmbFilterIntern;

    @FXML
    private Button btnRegisterIntern;

    @FXML
    private Button btnModifyIntern;

    @FXML
    private Tab tabProfessor;

    @FXML
    private TableView<ProfesorDTO> tvProfessors;

    @FXML
    private ComboBox<String> cmbFilterProfessor;

    @FXML
    private TextField txtSearchProfessor;

    @FXML
    private Button btnRegisterProfessor;

    @FXML
    private Button btnModifyProfessor;

    @FXML
    private Tab tabCoordinator;

    @FXML
    private TableView<CoordinadorDTO> tableViewCoordinator;

    @FXML
    private TableColumn<CoordinadorDTO, String> columnUser;

    @FXML
    private TableColumn<CoordinadorDTO, String> columnAcademicNumber;

    @FXML
    private TableColumn<CoordinadorDTO, String> columnName;

    @FXML
    private ComboBox<String> cmbFilterCoordinator;

    @FXML
    private TextField txtSearchCoordinator;

    @FXML
    private Button btnRegisterCoordinator;

    private final CoordinadorDAO coordinatorDAO
            = new CoordinadorDAO();

    private final PracticanteDAO practicanteDAO
            = new PracticanteDAO();

    private final ProfesorDAO profesorDAO
            = new ProfesorDAO();

    private ObservableList<CoordinadorDTO> coordinatorList;

    private FilteredList<CoordinadorDTO> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initializeCoordinatorTable();
        initializeInternTable();
        initializeProfessorTable();

        loadCoordinators();
        loadInterns();
        loadProfessors();

        configureSearch();
    }

    private void initializeInternTable() {

        colInternUser.setCellValueFactory(
                new PropertyValueFactory<>("nombreUsuario")
        );

        colInternEnrollment.setCellValueFactory(
                new PropertyValueFactory<>("matricula")
        );

        colInternName.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto")
        );

        colInternStatus.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );
    }

    private void loadInterns() {

        try {
            tvInterns.setItems(FXCollections.observableArrayList(practicanteDAO.getAll()
                    )
            );

        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    private void initializeProfessorTable() {
        if (tvProfessors.getColumns().size() >= 3) {
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("numeroPersonal"));
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        }
    }

    private void loadProfessors() {
        try {
            tvProfessors.setItems(FXCollections.observableArrayList(profesorDAO.getAll()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    private void initializeCoordinatorTable() {

        columnUser.setCellValueFactory(
                new PropertyValueFactory<>("nombreUsuario")
        );

        columnAcademicNumber.setCellValueFactory(
                new PropertyValueFactory<>("numeroPersonal")
        );

        columnName.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

    }

    private void loadCoordinators() {

        try {
            coordinatorList= FXCollections.observableArrayList(coordinatorDAO.getAll());
            filteredList = new FilteredList<>(coordinatorList,p -> true);
            tableViewCoordinator.setItems(filteredList);
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }

    }

    private void configureSearch() {
        if (txtSearchCoordinator != null && filteredList != null) {
            txtSearchCoordinator.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(coordinator -> {
                    if (newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }
                    String filter = newValue.toLowerCase().trim();
                    return String.valueOf(coordinator.getNombreUsuario()).toLowerCase().contains(filter)
                            || String.valueOf(coordinator.getNumeroPersonal()).toLowerCase().contains(filter)
                            || String.valueOf(coordinator.getNombre()).toLowerCase().contains(filter);
                });
            });
        }

        if (tableViewCoordinator != null && filteredList != null) {
            SortedList<CoordinadorDTO> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableViewCoordinator.comparatorProperty());
            tableViewCoordinator.setItems(sortedList);
        }
    }

    @FXML
    private void handleRegisterIntern(ActionEvent event) {

        openModalWindow(
                "/main/aplicacion/vistas/coordinador/practicante/GUIRegistrarPracticante.fxml",
                "Registrar Nuevo Practicante"
        );
        loadInterns();
    }

    @FXML
    private void handleRegisterProfessor(ActionEvent event) {
        openModalWindow(
            "/main/aplicacion/vistas/coordinador/profesor/GUIDarAltaProfesor.fxml",
            "Registrar Nuevo Profesor"
        );
        loadProfessors();
    }

    @FXML
    private void handleModifyIntern(ActionEvent event) {
        PracticanteDTO selected = tvInterns.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Selección requerida", "Debe seleccionar un practicante de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        openModifyInternWindow(selected);
    }

    private void openModifyInternWindow(PracticanteDTO practicante) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/coordinador/practicante/GUIRegistrarPracticante.fxml"));
            Parent root = loader.load();
            main.aplicacion.controladores.coordinador.practicante.GUIRegistrarPracticante controller = loader.getController();
            controller.initUpdate(practicante);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modificar Practicante: " + practicante.getNombreCompleto());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadInterns();
        } catch (IOException e) {
            Modal.displayError(ManejadorExcepciones.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana de modificación"));
        }
    }

    @FXML
    private void handleModifyProfessor(ActionEvent event) {
        ProfesorDTO selected = tvProfessors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Selección requerida", "Debe seleccionar un profesor de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        Modal.displayInformation("Modificar", "Abrir ventana de modificación para: " + selected.getNombreCompleto());
    }

    @FXML
    private void handleRegisterCoordinator(ActionEvent event) {
        openModalWindow(
                "/main/aplicacion/vistas/coordinador/usuario/GUIRegistrarUsuarioCoordinador.fxml",
                "Registrar Nuevo Coordinador"
        );
    }

    private void openModalWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage= new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadCoordinators();
            loadInterns();
            loadProfessors();
        } catch (IOException e) {
            Modal.displayError(
                    ManejadorExcepciones
                            .handleGUILoadIOException(
                                    LOGGER,
                                    e
                            )
            );

        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "Error al abrir ventana: " + title));
        }

    }

}
