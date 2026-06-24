package main.aplicacion.controladores.profesor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.negocio.dto.*;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIAdministrarEntregables implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIAdministrarEntregables.class);

    @FXML private Label labelTitle;
    @FXML private TableView<ReporteDTO> tableReportes;
    @FXML private TableColumn<ReporteDTO, String> colRepName;
    @FXML private TableColumn<ReporteDTO, String> colRepStatus;

    // We use a generic representation or just EvaluacionOVDTO for the table since it has the same getters
    @FXML private TableView<EvaluacionOVDTO> tableEvals;
    @FXML private TableColumn<EvaluacionOVDTO, String> colEvalName;
    @FXML private TableColumn<EvaluacionOVDTO, String> colEvalStatus;

    private ExperienciaEducativaDTO experiencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colRepName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        colRepStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        colEvalName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        colEvalStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    public void setExperiencia(ExperienciaEducativaDTO experiencia) {
        this.experiencia = experiencia;
        labelTitle.setText("Entregables de: " + experiencia.getNombre());
        loadData();
    }

    public void loadData() {
        try {
            ReporteDAO repDao = new ReporteDAO();
            tableReportes.setItems(FXCollections.observableArrayList(repDao.getUniqueEntregablesByExperiencia(experiencia.getExperienciaEducativaId())));

            EvaluacionOVDAO evalDao = new EvaluacionOVDAO();
            AutoevaluacionDAO autoDao = new AutoevaluacionDAO();
            
            List<EvaluacionOVDTO> allEvals = evalDao.getUniqueEntregablesByExperiencia(experiencia.getExperienciaEducativaId());
            // Add autoevaluaciones casted as EvaluacionOVDTO just for the table display since they share properties
            List<AutoevaluacionDTO> autos = autoDao.getUniqueEntregablesByExperiencia(experiencia.getExperienciaEducativaId());
            for (AutoevaluacionDTO a : autos) {
                allEvals.add(new EvaluacionOVDTO.LinkedOrganizationEvaluationBuilder().setNombreEntregable(a.getNombreEntregable()).setEstado(a.getEstado()).build());
            }
            tableEvals.setItems(FXCollections.observableArrayList(allEvals));
            
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/profesor/GUIExperienciasProfesor.fxml"));
            Parent view = loader.load();
            ((javafx.scene.layout.AnchorPane) labelTitle.getParent()).getChildren().setAll(view);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @FXML
    private void configurarReporte(ActionEvent event) {
        ReporteDTO selected = tableReportes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Seleccione un reporte de la tabla.");
            return;
        }
        if (!"Inhabilitado".equalsIgnoreCase(selected.getEstado())) {
            Modal.displayInformation("Atención", "El reporte ya está habilitado o cerrado.");
            return;
        }
        abrirModal(selected.getNombreEntregable(), "REPORTE");
    }

    @FXML
    private void configurarEvaluacion(ActionEvent event) {
        EvaluacionOVDTO selected = tableEvals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Seleccione una evaluación de la tabla.");
            return;
        }
        if (!"Inhabilitado".equalsIgnoreCase(selected.getEstado())) {
            Modal.displayInformation("Atención", "El entregable ya está habilitado o cerrado.");
            return;
        }
        String tipo = selected.getNombreEntregable().toLowerCase().contains("auto") ? "AUTOEVALUACION" : "EVALUACIONOV";
        abrirModal(selected.getNombreEntregable(), tipo);
    }

    private void abrirModal(String nombreDoc, String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/profesor/GUIHabilitarEntrega.fxml"));
            Parent root = loader.load();
            GUIHabilitarEntrega controller = loader.getController();
            controller.initData(nombreDoc, tipo, experiencia.getExperienciaEducativaId(), this);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Configurar Entrega");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Error abriendo modal", e);
        }
    }
}
