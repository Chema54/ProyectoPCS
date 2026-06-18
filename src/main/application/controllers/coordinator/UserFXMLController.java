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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.common.Modal;
import main.business.dto.CoordinatorDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.dao.CoordinatorDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class UserFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static final Logger LOGGER = LogManager.getLogger(UserFXMLController.class);
    //Seccion Practicantes
    @FXML
    private Tab tabIntern;
    
    //Seccion Profesores
    @FXML
    private Tab tabProfessor;
   
    
    //Seccion Coordinadores
    @FXML
    private Tab tabCoordinator;
    
    @FXML
    private TextField textFieldSearchCoordinators;
    
    @FXML
    private TableView<CoordinatorDTO> tableViewCoordinator;

    @FXML
    private TableColumn<CoordinatorDTO, Integer> columnUser;

    @FXML
    private TableColumn<CoordinatorDTO, String> columnAcademicNumber;

    @FXML
    private TableColumn<CoordinatorDTO, String> columnName;

    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();
    
    private ObservableList<CoordinatorDTO> coordinatorList;
    private FilteredList<CoordinatorDTO> filteredList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCoordinatorTable();
        loadCoordinators();
        configureSearch();
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
            coordinatorList = FXCollections.observableArrayList(coordinatorDAO.getAll());
            filteredList = new FilteredList<>(coordinatorList, p -> true);
            tableViewCoordinator.setItems(filteredList);
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }
    
    private void configureSearch() {
        textFieldSearchCoordinators.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(coordinator -> {
                if (newValue == null || newValue.trim().isEmpty())
                    return true;
                String filter = newValue.toLowerCase().trim();
                return coordinator.getUsername().toLowerCase().contains(filter)
                || coordinator.getAcademicNumber().toLowerCase().contains(filter)
                || coordinator.getNombre().toLowerCase().contains(filter);
            });
        });
        SortedList<CoordinatorDTO> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableViewCoordinator.comparatorProperty());
        tableViewCoordinator.setItems(sortedList);
    }
    
    @FXML
    private void handleRegisterIntern(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/intern/RegisterInternFXML.fxml", "Registrar Nuevo Practicante");
    }

    @FXML
    private void handleRegisterProfessor(ActionEvent event) {
        Modal.displayAlert("Información", "Módulo en construcción", "El registro de profesores estará disponible próximamente.");
    }

    @FXML
    private void handleRegisterCoordinator(ActionEvent event) {
        openModalWindow("/main/application/views/coordinator/user/RegisterUserCoordinatorFXML.fxml", "Registrar Nuevo Coordinador");
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
            loadCoordinators(); // Refresh table
        } catch (IOException e) {
            Modal.displayError(ExceptionHandler.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al abrir la ventana: " + title));
        }
    }

    @FXML
    @Deprecated
    private void goToRegisterCoordinator(ActionEvent event) throws UserDisplayableException{
        handleRegisterCoordinator(event);
    }
}

