package main.aplicacion.controladores.coordinador.practicante;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.negocio.dto.AsignacionDTO;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.negocio.dto.PracticanteDTO;
import main.negocio.dto.ProyectoDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioAsignacion;
import main.servicio.ServicioExperienciaEducativa;
import main.servicio.ServicioPracticante;
import main.servicio.ServicioProyecto;

public class GUIRegistrarAsignacion implements Initializable {

    @FXML
    private ComboBox<PracticanteDTO> comboBoxIntern;
    @FXML
    private ComboBox<ProyectoDTO> comboBoxProject;
    @FXML
    private ComboBox<ExperienciaEducativaDTO> comboBoxEducationalExperience;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<PracticanteDTO> practicantesSinAsignar = ServicioPracticante.getAllPracticantes().stream()
                    .filter(p -> !"Asignado".equals(p.getEstado()))
                    .collect(Collectors.toList());
            comboBoxIntern.setItems(FXCollections.observableArrayList(practicantesSinAsignar));

            List<ProyectoDTO> proyectosDisponibles = ServicioProyecto.getAllProyectos().stream()
                    .filter(p -> p.getEspaciosDisponibles() > 0)
                    .collect(Collectors.toList());
            comboBoxProject.setItems(FXCollections.observableArrayList(proyectosDisponibles));

            comboBoxEducationalExperience.setItems(FXCollections.observableArrayList(ServicioExperienciaEducativa.getAllExperiencias()));
            
            if (practicantesSinAsignar.isEmpty() || proyectosDisponibles.isEmpty()) {
                Modal.displayError(new ExcepcionMostrableUsuario(
                    "Registro Bloqueado", 
                    "Faltan elementos disponibles", 
                    "Actualmente no hay asignaciones posibles."
                ));
            }
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            cerrarVentana();
        }
    }

    @FXML
    private void registerAssignment(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            AsignacionDTO asignacion = new AsignacionDTO.AsignacionBuilder()
                    .setPracticanteId(comboBoxIntern.getValue().getPracticanteId())
                    .setProyectoId(comboBoxProject.getValue().getProyectoId())
                    .setExperienciaEducativaId(comboBoxEducationalExperience.getValue().getExperienciaEducativaId())
                    .setEstado("Activa")
                    .build();

            ServicioAsignacion.registrarNuevaAsignacion(asignacion);

            Modal.displayInformation("Asignación Exitosa", "La operación se ha realizado exitosamente");
            cerrarVentana();

        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        } catch (Exception e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Error del Sistema", 
                "No se pudo completar la asignación", 
                "Ocurrió un error con el sistema", 
                e
            ));
        }
    }

    private boolean validateFields() {
        if (comboBoxProject.getValue() == null){
            
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Campos Incompletos",
                "Faltan selecciones obligatorias",
                "No ha seleccionado un proyecto, seleccione uno para continuar"
            ));
            return false;
        }
        
        if (comboBoxIntern.getValue() == null) {
            
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Campos Incompletos",
                "Faltan selecciones obligatorias",
                "No ha seleccionado un practicante, seleccione uno para continuar"
            ));
            return false;
        }
        
        if (comboBoxEducationalExperience.getValue() == null) {
            
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Campos Incompletos",
                "Faltan selecciones obligatorias",
                "No ha seleccionado una experiencia educativa, seleccione una para continuar"
            ));
            return false;
        }
        
        return true;
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
