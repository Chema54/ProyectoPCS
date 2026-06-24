package main.aplicacion.controladores.coordinador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.negocio.dto.PeriodoDTO;
import main.comun.ManejadorExcepciones;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.PeriodoDAO;
import main.servicio.ServicioPeriodo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.negocio.dto.ExperienciaEducativaDTO;
import main.basedatos.dao.ExperienciaEducativaDAO;

public class GUIAbrirPeriodoEscolar implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIAbrirPeriodoEscolar.class);

    @FXML
    private TableView<PeriodoDTO> tvPeriods;
    @FXML
    private TableColumn<PeriodoDTO, String> colPeriodName;
    @FXML
    private TableColumn<PeriodoDTO, String> colStartDate;
    @FXML
    private TableColumn<PeriodoDTO, String> colEndDate;
    @FXML
    private TableColumn<PeriodoDTO, String> colPeriodStatus;

    @FXML
    private TableView<ExperienciaEducativaDTO> tvEducationalExperiences;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, String> colEENrc;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, String> colEEName;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, String> colEEPeriod;
    @FXML
    private TableColumn<ExperienciaEducativaDTO, String> colEEProfessor;

    private final PeriodoDAO periodoDAO = new PeriodoDAO();
    private final ExperienciaEducativaDAO eeDAO = new ExperienciaEducativaDAO();
    @FXML
    private TextField txtSearchPeriod;
    @FXML
    private ComboBox<?> cmbFilterPeriod;
    @FXML
    private Button btnOpenPeriod;
    @FXML
    private Button btnRegisterPeriod;
    @FXML
    private TextField txtSearchNRC;
    @FXML
    private ComboBox<?> cmbFilterEE;
    @FXML
    private Button btnRegisterEE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTables();
        cargarDatosTabla();
    }

    private void initializeTables() {
        colPeriodName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colPeriodStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colEENrc.setCellValueFactory(new PropertyValueFactory<>("nrc"));
        colEEName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEEPeriod.setCellValueFactory(new PropertyValueFactory<>("nombrePeriodo"));
        colEEProfessor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
    }

    private void cargarDatosTabla() {
        try {
            tvPeriods.setItems(FXCollections.observableArrayList(periodoDAO.getAll()));
            tvEducationalExperiences.setItems(FXCollections.observableArrayList(eeDAO.getAll()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    private void loadPeriods() {
        cargarDatosTabla();
    }

    @FXML
    private void handleRegisterPeriod(ActionEvent event) {
        openModalWindow("/main/aplicacion/vistas/coordinador/periodo/GUIRegistrarPeriodo.fxml", "Abrir Periodo Escolar");
        cargarDatosTabla();
    }
    
    @FXML
    private void handleRegisterEE(ActionEvent event) {
        openModalWindow(
                "/main/aplicacion/vistas/coordinador/experiencia/GUIRegistroEE.fxml",
                "Registrar Experiencia Educativa"
        );

        cargarDatosTabla();
    }

    @FXML
    private void handleOpenPeriod(ActionEvent event) {
        PeriodoDTO selected = tvPeriods.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Selección requerida", "No se ha seleccionado ningún periodo", "Por favor seleccione un periodo de la tabla para abrirlo."));
            return;
        }
        
        boolean confirm = Modal.displayConfirmation("¿Está seguro que desea abrir el periodo " + selected.getNombre() + "?\nEsto activará los proyectos y generará los documentos de asignación.");
        if (!confirm) {
            return;
        }
        
        try {
            ServicioPeriodo.openPeriod(selected.getPeriodoId());
            Modal.displayInformation("Éxito", "El periodo escolar se ha abierto exitosamente.");
            cargarDatosTabla();
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario("Error del Sistema", "No se pudo abrir el periodo", "Ocurrió un error inesperado", e));
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


