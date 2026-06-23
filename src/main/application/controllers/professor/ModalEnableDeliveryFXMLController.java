package main.application.controllers.professor;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModalEnableDeliveryFXMLController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ModalEnableDeliveryFXMLController.class);

    @FXML private Label labelDocName;
    @FXML private DatePicker datePickerLimite;

    private String nombreDoc;
    private String tipo;
    private int idExperiencia;
    private ManageDeliverablesFXMLController parentController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePickerLimite.setValue(LocalDate.now().plusDays(7));
    }

    public void initData(String nombreDoc, String tipo, int idExperiencia, ManageDeliverablesFXMLController parent) {
        this.nombreDoc = nombreDoc;
        this.tipo = tipo;
        this.idExperiencia = idExperiencia;
        this.parentController = parent;
        labelDocName.setText("Habilitar: " + nombreDoc);
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (datePickerLimite.getValue() == null) {
            Modal.displayInformation("Campos Vacíos", "El campo Fecha Límite es obligatorio");
            return;
        }

        java.sql.Date fechaSql = java.sql.Date.valueOf(datePickerLimite.getValue());

        try {
            if ("REPORTE".equals(tipo)) {
                new ReportDAO().enableDeliverablesMasive(nombreDoc, idExperiencia, fechaSql);
            } else if ("EVALUACIONOV".equals(tipo)) {
                new LinkedOrganizationEvaluationDAO().enableDeliverablesMasive(nombreDoc, idExperiencia, fechaSql);
            } else if ("AUTOEVALUACION".equals(tipo)) {
                new SelfAssessmentDAO().enableDeliverablesMasive(nombreDoc, idExperiencia, fechaSql);
            }

            Modal.displayInformation("Éxito", "El entregable ha sido habilitado correctamente para toda la Experiencia Educativa.");
            parentController.loadData();
            cerrarModal();
            
        } catch (UserDisplayableException ex) {
            Modal.displayError(ex);
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarModal();
    }

    private void cerrarModal() {
        Stage stage = (Stage) labelDocName.getScene().getWindow();
        stage.close();
    }
}
