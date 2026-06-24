package main.aplicacion.controladores.coordinador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileOutputStream;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.negocio.dto.DocumentoAceptacionDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.servicio.ServicioDocumento;

public class GUIRevisarDocumentos implements Initializable {

    @FXML
    private TableView<DocumentoAceptacionDTO> tvAcceptance;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colPracticante;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colProyecto;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colDocumento;

    private final ServicioDocumento servicioDocumento = new ServicioDocumento();
    @FXML
    private Button btnDescargar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
        loadDocuments();
    }

    private void initializeTable() {
        colPracticante.setCellValueFactory(new PropertyValueFactory<>("nombrePracticante"));
        colProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
    }

    private void loadDocuments() {
        try {
            tvAcceptance.setItems(FXCollections.observableArrayList(servicioDocumento.obtenerDocumentosEntregados()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void descargarDocumento(ActionEvent event) {
        DocumentoAceptacionDTO selected = tvAcceptance.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Selección Requerida", "Por favor seleccione un documento de la tabla.");
            return;
        }

        if (selected.getArchivo() == null || selected.getArchivo().length == 0) {
            Modal.displayInformation("Sin Archivo", "Este registro no tiene ningún archivo PDF entregado aún.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Documento PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(selected.getNombreEntregable() + ".pdf");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(selected.getArchivo());
                Modal.displayInformation("Éxito", "Documento descargado exitosamente.");
            } catch (Exception e) {
                Modal.displayError(new ExcepcionMostrableUsuario("Error de archivo", "No se pudo guardar", "Verifique que el archivo no esté en uso."));
            }
        }
    }
}
