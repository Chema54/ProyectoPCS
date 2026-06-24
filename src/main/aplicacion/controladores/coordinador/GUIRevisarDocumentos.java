/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.negocio.dto.DocumentoAceptacionDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.DocumentoAceptacionDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class GUIRevisarDocumentos implements Initializable {

    @FXML
    private TableView<DocumentoAceptacionDTO> tvAcceptance;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, Integer> colAsigId;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colFile;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colStatus;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> colDate;

    private final DocumentoAceptacionDAO documentoDAO = new DocumentoAceptacionDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
        loadDocuments();
    }

    private void initializeTable() {
        colAsigId.setCellValueFactory(new PropertyValueFactory<>("asignacionId"));
        colFile.setCellValueFactory(new PropertyValueFactory<>("archivo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
    }

    private void loadDocuments() {
        try {
            tvAcceptance.setItems(FXCollections.observableArrayList(documentoDAO.getAll()));
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    @FXML
    private void habilitarDocumento(ActionEvent event) {
        DocumentoAceptacionDTO selected = tvAcceptance.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Selección Requerida", "Por favor seleccione un documento de la tabla.");
            return;
        }
        
        try {
            DocumentoAceptacionDTO updated = new DocumentoAceptacionDTO.AcceptanceDocumentBuilder()
                .setDocumentoAceptacionId(selected.getDocumentoAceptacionId())
                .setAsignacionId(selected.getAsignacionId())
                .setNombreEntregable(selected.getNombreEntregable())
                .setArchivo(selected.getArchivo())
                .setEstado("Pendiente")
                .setFechaLimite(selected.getFechaLimite())
                .build();
            documentoDAO.updateOne(updated);
            Modal.displayInformation("Éxito", "El documento ha sido habilitado para entrega.");
            loadDocuments();
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

