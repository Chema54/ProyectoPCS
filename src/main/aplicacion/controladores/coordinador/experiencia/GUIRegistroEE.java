package main.aplicacion.controladores.coordinador.experiencia;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.ManejadorExcepciones;
import main.comun.Modal;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.negocio.dto.PeriodoDTO;
import main.negocio.dto.ProfesorDTO;
import main.servicio.ServicioExperienciaEducativa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIRegistroEE implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIRegistroEE.class);

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldNrc;

    @FXML
    private ComboBox<PeriodoDTO> comboBoxPeriod;

    @FXML
    private ComboBox<ProfesorDTO> comboBoxProfessor;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorNrc;

    @FXML
    private Label labelErrorPeriod;

    @FXML
    private Label labelErrorProfessor;

    @FXML
    private Button btnFinalizar;

    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearErrorLabels();
        loadPeriodsAndProfessors();
    }

    private void loadPeriodsAndProfessors() {
        try {
            comboBoxPeriod.setItems(
                    FXCollections.observableArrayList(
                            ServicioExperienciaEducativa.getPeriodosSinAbrir()
                    )
            );

            comboBoxProfessor.setItems(
                    FXCollections.observableArrayList(
                            ServicioExperienciaEducativa.getProfesoresDisponibles()
                    )
            );

            if (comboBoxPeriod.getItems().isEmpty()) {
                labelErrorPeriod.setText("No hay periodos sin abrir registrados");
            }

            if (comboBoxProfessor.getItems().isEmpty()) {
                labelErrorProfessor.setText("No hay profesores disponibles");
            }
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void registerEducationalExperience(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        boolean isConfirmed = Modal.displayConfirmation(
                "¿Está seguro que desea registrar esta Experiencia Educativa?"
        );

        if (!isConfirmed) {
            return;
        }

        try {
            PeriodoDTO selectedPeriod = comboBoxPeriod.getSelectionModel().getSelectedItem();
            ProfesorDTO selectedProfessor = comboBoxProfessor.getSelectionModel().getSelectedItem();

            ExperienciaEducativaDTO experiencia = new ExperienciaEducativaDTO.ExperienciaEducativaBuilder()
                    .setNombre(textFieldName.getText().trim())
                    .setNrc(textFieldNrc.getText().trim())
                    .setPeriodoId(selectedPeriod.getPeriodoId())
                    .build();

            ServicioExperienciaEducativa.registrarNuevaExperienciaEducativa(
                    experiencia,
                    selectedProfessor
            );

            Modal.displayInformation(
                    "Acción realizada",
                    "La experiencia educativa se ha registrado exitosamente."
            );

            closeWindow();

        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                    "Error del Sistema",
                    "No se pudo completar el registro",
                    "Ocurrió un error inesperado con el sistema.",
                    e
            ));
        }
    }

    @FXML
    private void openRegisterPeriod(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/main/aplicacion/vistas/coordinador/periodo/GUIRegistrarPeriodo.fxml"
            ));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Abrir un Nuevo Periodo");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            clearErrorLabels();
            loadPeriodsAndProfessors();

        } catch (IOException e) {
            Modal.displayError(ManejadorExcepciones.handleGUILoadIOException(LOGGER, e));
        } catch (Exception e) {
            Modal.displayError(ManejadorExcepciones.handleUnexpectedException(
                    LOGGER,
                    e,
                    "Error al abrir la ventana de periodo"
            ));
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateFields() {
        clearErrorLabels();
        boolean isValid = true;

        String name = textFieldName.getText() != null ? textFieldName.getText().trim() : "";
        String nrc = textFieldNrc.getText() != null ? textFieldNrc.getText().trim() : "";

        if (name.isEmpty()) {
            labelErrorName.setText("El campo Nombre es obligatorio");
            isValid = false;
        } else if (!name.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\.\\-]+$")) {
            labelErrorName.setText("El campo Nombre contiene valores inválidos");
            isValid = false;
        }

        if (nrc.isEmpty()) {
            labelErrorNrc.setText("El campo NRC es obligatorio");
            isValid = false;
        } else if (!nrc.matches("^\\d{5,10}$")) {
            labelErrorNrc.setText("El campo NRC debe ser numérico, entre 5 y 10 dígitos");
            isValid = false;
        }

        if (comboBoxPeriod.getSelectionModel().getSelectedItem() == null) {
            labelErrorPeriod.setText("Debe seleccionar un periodo sin abrir");
            isValid = false;
        }

        if (comboBoxProfessor.getSelectionModel().getSelectedItem() == null) {
            labelErrorProfessor.setText("Debe seleccionar un profesor disponible");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrorLabels() {
        labelErrorName.setText("");
        labelErrorNrc.setText("");
        labelErrorPeriod.setText("");
        labelErrorProfessor.setText("");
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
