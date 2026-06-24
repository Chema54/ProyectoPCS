package main.aplicacion.controladores.coordinador.proyecto;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.negocio.dto.ProyectoDTO;
import main.negocio.dto.ResponsableProyectoDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioOrganizacionVinculada;
import main.servicio.ServicioProyecto;
import main.servicio.ServicioProyecto;

public class GUIRegistrarProyecto implements Initializable {

    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<OrganizacionVinculadaDTO> comboBoxOrganization; 
    @FXML
    private TextField textFieldTitularName; 
    @FXML
    private TextField textFieldTitularPersonalNumber; 
    @FXML
    private TextField textFieldTotalCapacity;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private ProyectoDTO proyectoToUpdate = null;
    private ResponsableProyectoDTO titularToUpdate = null;

    public void initUpdate(ProyectoDTO proyecto) {
        this.proyectoToUpdate = proyecto;
        textFieldName.setText(proyecto.getNombre());
        textFieldTotalCapacity.setText(String.valueOf(proyecto.getCupoTotal()));
        
        try {
            main.basedatos.dao.ResponsableProyectoDAO titularDAO = new main.basedatos.dao.ResponsableProyectoDAO();
            if (proyecto.getTitularId() != null) {
                this.titularToUpdate = titularDAO.getOne(proyecto.getTitularId());
                if (this.titularToUpdate != null) {
                    textFieldTitularName.setText(this.titularToUpdate.getNombre());
                    textFieldTitularPersonalNumber.setText(this.titularToUpdate.getNumeroPersonal());
                    for (OrganizacionVinculadaDTO org : comboBoxOrganization.getItems()) {
                        if (org.getOrganizacionId() == this.titularToUpdate.getOrganizacionId()) {
                            comboBoxOrganization.setValue(org);
                            break;
                        }
                    }
                }
            }
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
        
        btnGuardar.setText("Actualizar");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboBoxOrganization.setItems(FXCollections.observableArrayList(ServicioOrganizacionVinculada.getAllOrganizaciones()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void registerProject(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            int capacity = Integer.parseInt(textFieldTotalCapacity.getText().trim());

            ResponsableProyectoDTO titular = new ResponsableProyectoDTO.TitularBuilder()
                    .setTitularId(titularToUpdate != null ? titularToUpdate.getTitularId() : 0)
                    .setNombre(textFieldTitularName.getText().trim())
                    .setNumeroPersonal(textFieldTitularPersonalNumber.getText().trim())
                    .setOrganizacionId(comboBoxOrganization.getValue().getOrganizacionId())
                    .build();

            ProyectoDTO proyecto = new ProyectoDTO.ProjectBuilder()
                    .setProyectoId(proyectoToUpdate != null ? proyectoToUpdate.getProyectoId() : 0)
                    .setNombre(textFieldName.getText().trim())
                    .setCupoTotal(capacity)
                    .setEspaciosDisponibles(proyectoToUpdate != null ? proyectoToUpdate.getEspaciosDisponibles() : capacity)
                    .setEstado(proyectoToUpdate != null ? proyectoToUpdate.getEstado() : "Sin asignar")
                    .build();

            if (proyectoToUpdate != null) {
                ServicioProyecto.actualizarProyecto(proyecto, titular);
                Modal.displayInformation("Actualización Exitosa", "La operación se ha realizado exitosamente");
            } else {
                ServicioProyecto.registrarNuevoProyecto(proyecto, titular);
                Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            }
            cerrarVentana();

        } catch (NumberFormatException e) {
            Modal.displayError(new ExcepcionMostrableUsuario(
                "Formato Inválido",
                "Cupo Total debe ser numérico",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            ));
        } catch (ExcepcionMostrableUsuario e) {
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
        if (textFieldName.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Nombre es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTotalCapacity.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Cupo Total es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTitularName.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Nombre del Titular es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldTitularPersonalNumber.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo No. Personal del Titular es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (comboBoxOrganization.getValue() == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Organización es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
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
