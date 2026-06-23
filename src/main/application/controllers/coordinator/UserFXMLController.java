package main.application.controllers.coordinator;

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

import main.business.dto.CoordinatorDTO;
import main.business.dto.PracticanteDTO;
import main.common.ExceptionHandler;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.CoordinatorDAO;
import main.database.dao.PracticanteDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.business.dto.ProfesorDTO;
import main.database.dao.ProfesorDAO;

public class UserFXMLController implements Initializable {

    private static final Logger LOGGER
            = LogManager.getLogger(UserFXMLController.class);

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
    private TableView<CoordinatorDTO> tableViewCoordinator;

    @FXML
    private TableColumn<CoordinatorDTO, String> columnUser;

    @FXML
    private TableColumn<CoordinatorDTO, String> columnAcademicNumber;

    @FXML
    private TableColumn<CoordinatorDTO, String> columnName;

    @FXML
    private ComboBox<String> cmbFilterCoordinator;

    @FXML
    private TextField txtSearchCoordinator;

    @FXML
    private Button btnRegisterCoordinator;

    private final CoordinatorDAO coordinatorDAO
            = new CoordinatorDAO();

    private final PracticanteDAO practicanteDAO
            = new PracticanteDAO();

    private final ProfesorDAO profesorDAO
            = new ProfesorDAO();

    private ObservableList<CoordinatorDTO> coordinatorList;

    private FilteredList<CoordinatorDTO> filteredList;

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
                new PropertyValueFactory<>("username")
        );

        colInternEnrollment.setCellValueFactory(
                new PropertyValueFactory<>("enrollment")
        );

        colInternName.setCellValueFactory(
                new PropertyValueFactory<>("fullName")
        );

        colInternStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
    }

    private void loadInterns() {

        try {

            tvInterns.setItems(
                    FXCollections.observableArrayList(
                            practicanteDAO.getAll()
                    )
            );

        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    private void initializeProfessorTable() {
        if (tvProfessors.getColumns().size() >= 3) {
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("username"));
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("personalNumber"));
            ((TableColumn<ProfesorDTO, String>) tvProfessors.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("fullName"));
        }
    }

    private void loadProfessors() {
        try {
            tvProfessors.setItems(FXCollections.observableArrayList(profesorDAO.getAll()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }

    private void initializeCoordinatorTable() {

        columnUser.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );

        columnAcademicNumber.setCellValueFactory(
                new PropertyValueFactory<>("academicNumber")
        );

        columnName.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

    }

    private void loadCoordinators() {

        try {

            coordinatorList
                    = FXCollections.observableArrayList(
                            coordinatorDAO.getAll()
                    );

            filteredList
                    = new FilteredList<>(
                            coordinatorList,
                            p -> true
                    );

            tableViewCoordinator.setItems(filteredList);

        } catch (UserDisplayableException e) {

            Modal.displayError(e);

        }

    }

    private void configureSearch() {

        txtSearchCoordinator.textProperty()
                .addListener((observable, oldValue, newValue) -> {

                    filteredList.setPredicate(coordinator -> {

                        if (newValue == null || newValue.trim().isEmpty()) {

                            return true;

                        }

                        String filter
                                = newValue.toLowerCase().trim();

                        return String.valueOf(
                                coordinator.getUsername()
                        )
                                .toLowerCase()
                                .contains(filter)
                                || String.valueOf(
                                        coordinator.getAcademicNumber()
                                )
                                        .toLowerCase()
                                        .contains(filter)
                                || String.valueOf(
                                        coordinator.getNombre()
                                )
                                        .toLowerCase()
                                        .contains(filter);

                    });

                });

        SortedList<CoordinatorDTO> sortedList
                = new SortedList<>(filteredList);

        sortedList.comparatorProperty()
                .bind(
                        tableViewCoordinator.comparatorProperty()
                );

        tableViewCoordinator.setItems(sortedList);

    }

    @FXML
    private void handleRegisterIntern(ActionEvent event) {

        openModalWindow(
                "/main/application/views/coordinator/intern/RegisterInternFXML.fxml",
                "Registrar Nuevo Practicante"
        );

    }

    @FXML
    private void handleRegisterProfessor(ActionEvent event) {

        // Próximamente
    }

    @FXML
    private void handleModifyIntern(ActionEvent event) {
        PracticanteDTO selected = tvInterns.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new UserDisplayableException("Selección requerida", "Debe seleccionar un practicante de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        Modal.displayInformation("Modificar", "Abrir ventana de modificación para: " + selected.getFullName());
    }

    @FXML
    private void handleModifyProfessor(ActionEvent event) {
        ProfesorDTO selected = tvProfessors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new UserDisplayableException("Selección requerida", "Debe seleccionar un profesor de la tabla", "No se ha seleccionado ningún elemento para modificar."));
            return;
        }
        Modal.displayInformation("Modificar", "Abrir ventana de modificación para: " + selected.getFullName());
    }

    @FXML
    private void handleRegisterCoordinator(ActionEvent event) {

        openModalWindow(
                "/main/application/views/coordinator/user/RegisterUserCoordinatorFXML.fxml",
                "Registrar Nuevo Coordinador"
        );

    }

    private void openModalWindow(String fxmlPath, String title) {

        try {

            FXMLLoader loader
                    = new FXMLLoader(
                            getClass().getResource(fxmlPath)
                    );

            Parent root
                    = loader.load();

            Stage stage
                    = new Stage();

            stage.initModality(
                    Modality.APPLICATION_MODAL
            );

            stage.setTitle(title);

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            loadCoordinators();

        } catch (IOException e) {

            Modal.displayError(
                    ExceptionHandler
                            .handleGUILoadIOException(
                                    LOGGER,
                                    e
                            )
            );

        } catch (Exception e) {
            Modal.displayError(ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al abrir ventana: " + title));
        }

    }

}
