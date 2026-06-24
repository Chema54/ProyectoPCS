package main.aplicacion.controladores.practicante;

import java.io.File;
import java.nio.file.Files;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.negocio.dto.*;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.Sesion;
import main.basedatos.dao.*;
import main.negocio.servicio.ValidadorEntrega;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIEntregarEntregables implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIEntregarEntregables.class);

    @FXML
    private TableView<ReporteDTO> tablaReportes;
    @FXML
    private TableColumn<ReporteDTO, String> columnaNombreReporte;
    @FXML
    private TableColumn<ReporteDTO, String> columnaEstadoReporte;

    @FXML
    private TableView<EvaluacionOVDTO> tablaEvaluacionesOV;
    @FXML
    private TableColumn<EvaluacionOVDTO, String> columnaNombreEvaluacionOV;
    @FXML
    private TableColumn<EvaluacionOVDTO, String> columnaEstadoEvaluacionOV;

    @FXML
    private TableView<AutoevaluacionDTO> tablaAutoevaluaciones;
    @FXML
    private TableColumn<AutoevaluacionDTO, String> columnaNombreAutoevaluacion;
    @FXML
    private TableColumn<AutoevaluacionDTO, String> columnaEstadoAutoevaluacion;

    private AsignacionDTO asignacionActual;
    private ReporteDAO reporteDAO = new ReporteDAO();
    private EvaluacionOVDAO evaluacionDAO = new EvaluacionOVDAO();
    private AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            PracticanteDAO practicanteDAO = new PracticanteDAO();
            PracticanteDTO practicanteActual = practicanteDAO.getByEnrollment(Sesion.getCurrentUser().getNombreUsuario());

            AsignacionDAO asignacionDAO = new AsignacionDAO();
            AsignacionDTO assignment = asignacionDAO.getActiveAssignmentByIntern(practicanteActual.getPracticanteId());

            if (assignment != null) {
                asignacionActual = assignment;

                columnaNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
                columnaEstadoReporte.setCellValueFactory(new PropertyValueFactory<>("estado"));

                columnaNombreEvaluacionOV.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
                columnaEstadoEvaluacionOV.setCellValueFactory(new PropertyValueFactory<>("estado"));

                columnaNombreAutoevaluacion.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
                columnaEstadoAutoevaluacion.setCellValueFactory(new PropertyValueFactory<>("estado"));

                loadDeliverables();
            } else {
                Modal.displayInformation("Sin Proyecto", "No tiene un proyecto activo asignado.");
            }
        } catch (ExcepcionMostrableUsuario ex) {
            LOGGER.error(ex.getMessage(), ex);
            Modal.displayError(ex);
        }
    }

    public void loadDeliverables() {
        try {
            if (asignacionActual != null) {
                List<ReporteDTO> reportes = reporteDAO.getAllByAssignmentId(asignacionActual.getAsignacionId());
                tablaReportes.setItems(FXCollections.observableArrayList(reportes));

                List<EvaluacionOVDTO> evaluaciones = evaluacionDAO.getAllByAssignmentId(asignacionActual.getAsignacionId());
                tablaEvaluacionesOV.setItems(FXCollections.observableArrayList(evaluaciones));

                List<AutoevaluacionDTO> autoevaluaciones = autoevaluacionDAO.getAllByAssignmentId(asignacionActual.getAsignacionId());
                tablaAutoevaluaciones.setItems(FXCollections.observableArrayList(autoevaluaciones));
            }
        } catch (ExcepcionMostrableUsuario ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private File pickPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void entregarReporte(ActionEvent event) {
        ReporteDTO selected = tablaReportes.getSelectionModel().getSelectedItem();

        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }

        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            if (!Modal.displayConfirmation("Ya existe un archivo cargado. ¿Desea sobrescribirlo?")) {
                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(tablaReportes.getScene().getWindow());

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                Modal.displayError(new ExcepcionMostrableUsuario("Formato incorrecto", "Documento inválido", "El archivo debe tener el formato PDF."));
                return;
            }
            if (!Modal.displayConfirmation("¿Desea entregar el archivo: " + file.getName() + "?")) {
                return;
            }
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                ReporteDTO updated = new ReporteDTO.ReporteBuilder()
                        .setReporteId(selected.getReporteId())
                        .setAsignacionId(selected.getAsignacionId())
                        .setNombreEntregable(selected.getNombreEntregable())
                        .setArchivo(fileBytes)
                        .setEstado("Entregado")
                        .setPuntaje(selected.getPuntaje())
                        .setFechaLimite(selected.getFechaLimite())
                        .build();
                reporteDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "El reporte se ha entregado exitosamente.");
                loadDeliverables();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void entregarEvaluacion(ActionEvent event) {
        EvaluacionOVDTO selected = tablaEvaluacionesOV.getSelectionModel().getSelectedItem();

        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }

        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            if (!Modal.displayConfirmation("Ya existe un archivo cargado. ¿Desea sobrescribirlo?")) {
                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(tablaEvaluacionesOV.getScene().getWindow());

        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                Modal.displayError(new ExcepcionMostrableUsuario("Formato incorrecto", "Documento inválido", "El archivo debe tener el formato PDF."));
                return;
            }
            if (!Modal.displayConfirmation("¿Desea entregar el archivo: " + file.getName() + "?")) {
                return;
            }
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                EvaluacionOVDTO updated = new EvaluacionOVDTO.EvaluacionOVBuilder()
                        .setEvaluacionId(selected.getEvaluacionId())
                        .setAsignacionId(selected.getAsignacionId())
                        .setNombreEntregable(selected.getNombreEntregable())
                        .setArchivo(fileBytes)
                        .setEstado("Entregado")
                        .setPuntaje(selected.getPuntaje())
                        .setFechaLimite(selected.getFechaLimite())
                        .build();
                evaluacionDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "La evaluación se ha entregado exitosamente.");
                loadDeliverables();
            } catch (Exception e) {
                LOGGER.error("Error", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error de archivo", e.getMessage()));
            }
        }
    }

    @FXML
    private void realizarAutoevaluacion(ActionEvent event) {
        AutoevaluacionDTO selected = tablaAutoevaluaciones.getSelectionModel().getSelectedItem();

        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (main.comun.ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }
        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Autoevaluación Completada");
            alert.setHeaderText("Autoevaluación ya realizada");
            alert.setContentText("El documento ya se encuentra registrado en el sistema.");

            javafx.scene.control.ButtonType btnGuardar = new javafx.scene.control.ButtonType("Guardar PDF", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            javafx.scene.control.ButtonType btnCerrar = new javafx.scene.control.ButtonType("Cerrar", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnGuardar, btnCerrar);

            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnGuardar) {
                if (selected.getArchivo() != null) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Guardar Autoevaluación PDF");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                    fileChooser.setInitialFileName("Autoevaluacion_" + asignacionActual.getMatriculaPracticante() + ".pdf");
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                            fos.write(selected.getArchivo());
                            Modal.displayInformation("Éxito", "Documento guardado correctamente.");
                        } catch (Exception e) {
                            LOGGER.error("Error guardando autoevaluacion pdf", e);
                            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error al guardar", "No se pudo guardar el archivo."));
                        }
                    }
                } else {
                    Modal.displayInformation("Atención", "El archivo PDF no se encuentra disponible.");
                }
            }
            return;
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main/aplicacion/vistas/practicante/GUIRealizarAutoevaluacion.fxml"));
            javafx.scene.Parent root = loader.load();

            GUIRealizarAutoevaluacion controller = loader.getController();

            PracticanteDAO practicanteDAO = new PracticanteDAO();
            PracticanteDTO practicanteActual = practicanteDAO.getByEnrollment(Sesion.getCurrentUser().getNombreUsuario());

            controller.initData(selected, asignacionActual, practicanteActual, this);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Formulario de Autoevaluación");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            LOGGER.error("Error abriendo formulario autoevaluacion", e);
            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error GUI", "No se pudo abrir el formulario."));
        }
    }
}
