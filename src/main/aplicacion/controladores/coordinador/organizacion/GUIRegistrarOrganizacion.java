package main.aplicacion.controladores.coordinador.organizacion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioOrganizacionVinculada;

public class GUIRegistrarOrganizacion implements Initializable {

    @FXML
    private TextField textFieldBusinessName;
    @FXML
    private TextField textFieldLocation;
    @FXML
    private TextField textFieldPhoneNumber;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    private OrganizacionVinculadaDTO organizacionToUpdate = null;

    public void initUpdate(OrganizacionVinculadaDTO organizacion) {
        this.organizacionToUpdate = organizacion;
        textFieldBusinessName.setText(organizacion.getNombreEmpresa());
        textFieldLocation.setText(organizacion.getDireccion());
        textFieldPhoneNumber.setText(organizacion.getTelefono());
        textFieldEmail.setText(organizacion.getCorreo());
        btnGuardar.setText("Actualizar");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void registerOrganization(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            OrganizacionVinculadaDTO organizacion = new OrganizacionVinculadaDTO.LinkedOrganizationBuilder()
                    .setOrganizacionId(organizacionToUpdate != null ? organizacionToUpdate.getOrganizacionId() : 0)
                    .setNombreEmpresa(textFieldBusinessName.getText().trim())
                    .setDireccion(textFieldLocation.getText().trim())
                    .setTelefono(textFieldPhoneNumber.getText().trim())
                    .setCorreo(textFieldEmail.getText().trim())
                    .build();

            if (organizacionToUpdate != null) {
                ServicioOrganizacionVinculada.actualizarOrganizacion(organizacion);
                Modal.displayInformation("Actualización Exitosa", "La operación se ha realizado exitosamente");
            } else {
                ServicioOrganizacionVinculada.registrarNuevaOrganizacion(organizacion);
                Modal.displayInformation("Registro Exitoso", "La operación se ha realizado exitosamente");
            }
            cerrarVentana();

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
        if (textFieldBusinessName.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Nombre de Organización es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldLocation.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Dirección es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
            return false;
        }
        if (textFieldEmail.getText().trim().isEmpty()) {
            Modal.displayError(new ExcepcionMostrableUsuario("Campos Incompletos", "El campo Correo es obligatorio", "Dato asignado tiene un valor invalido, debe seguir un formato asignado"));
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
