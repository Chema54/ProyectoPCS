package main.aplicacion.controladores.coordinador.periodo;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import main.negocio.dto.PeriodoDTO;
import main.negocio.dto.ProyectoDTO;
import main.basedatos.dao.ProyectoDAO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioPeriodo;

public class GUIRegistrarPeriodo implements Initializable {

    @FXML
    private TextField textFieldName;
    @FXML
    private DatePicker datePickerStartDate;
    @FXML
    private DatePicker datePickerEndDate;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorStartDate;
    @FXML
    private Label labelErrorEndDate;
    @FXML
    private Label labelErrorProjects;
    @FXML
    private ListView<ProyectoDTO> listViewProjects;
    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearErrorLabels();
        listViewProjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            listViewProjects.setItems(FXCollections.observableArrayList(proyectoDAO.getAll()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    private void clearErrorLabels() {
        labelErrorName.setText("");
        labelErrorStartDate.setText("");
        labelErrorEndDate.setText("");
        if (labelErrorProjects != null) labelErrorProjects.setText("");
    }

    @FXML
    private void registerPeriod(ActionEvent event) {
        if (!validateFields()) {
            return; // Regresa al paso 2 del flujo normal (FA 3.1 y FA 3.2)
        }

        // 4. Despliega ventana de confirmación (Paso 4 y 5)
        boolean isConfirmed = Modal.displayConfirmation("¿Está seguro que desea registrar este Periodo?");
        
        if (!isConfirmed) {
            return; // FA 5.1 Rechazar Confirmación: Regresa al paso 3 (se queda en la ventana)
        }

        try {
            PeriodoDTO periodo = new PeriodoDTO.PeriodoBuilder()
                    .setNombre(textFieldName.getText().trim())
                    .setFechaInicio(java.sql.Date.valueOf(datePickerStartDate.getValue()))
                    .setFechaFin(java.sql.Date.valueOf(datePickerEndDate.getValue()))
                    .build();

            ServicioPeriodo.registerNewPeriod(periodo);

            // 8. Despliega ventana de Acción Realizada
            Modal.displayInformation("Registro Exitoso", "El Periodo ha sido registrado exitosamente.");
            cerrarVentana(); // 10. Cierra la ventana

        } catch (ExcepcionMostrableUsuario e) {
            // EX1 - Error de conexión u otros errores manejados por el Service
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Error del Sistema", 
                "No se pudo completar el registro", 
                "Ocurrió un error con el sistema", 
                e
            ));
        }
    }

    private boolean validateFields() {
        clearErrorLabels();
        boolean isValid = true;

        String name = textFieldName.getText().trim();
        LocalDate startDate = datePickerStartDate.getValue();
        LocalDate endDate = datePickerEndDate.getValue();

        // FA 3.1 Campos Vacíos de registro
        if (name.isEmpty()) {
            labelErrorName.setText("El campo Nombre del Periodo es obligatorio");
            isValid = false;
        }
        if (startDate == null) {
            labelErrorStartDate.setText("El campo Fecha de Inicio es obligatorio");
            isValid = false;
        }
        if (endDate == null) {
            labelErrorEndDate.setText("El campo Fecha de Fin es obligatorio");
            isValid = false;
        }
        if (listViewProjects.getSelectionModel().getSelectedItems().isEmpty()) {
            labelErrorProjects.setText("Debe seleccionar al menos un proyecto");
            isValid = false;
        }

        // FA 3.2 Campos con valores incorrectos (Lógica superficial, la profunda está en el Service)
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            labelErrorEndDate.setText("El campo Fecha de Fin contiene valores invalidos (No puede ser antes del inicio)");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    private void cancel(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
