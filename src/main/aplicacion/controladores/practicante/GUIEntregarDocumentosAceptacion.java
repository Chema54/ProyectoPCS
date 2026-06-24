package main.aplicacion.controladores.practicante;

import java.io.File;
import java.nio.file.Files;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.negocio.dto.AsignacionDTO;
import main.negocio.dto.DocumentoAceptacionDTO;
import main.negocio.dto.PracticanteDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.Sesion;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.DocumentoAceptacionDAO;
import main.basedatos.dao.PracticanteDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.ResponsableProyectoDAO;
import main.basedatos.dao.OrganizacionVinculadaDAO;
import main.basedatos.dao.ExperienciaEducativaDAO;
import main.basedatos.dao.CoordinadorDAO;
import main.negocio.dto.ProyectoDTO;
import main.negocio.dto.ResponsableProyectoDTO;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.negocio.dto.CoordinadorDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIEntregarDocumentosAceptacion implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIEntregarDocumentosAceptacion.class);

    @FXML
    private Label labelProjectName;
    @FXML
    private Label labelMatricula;
    @FXML
    private Label labelOrganization;
    @FXML
    private Label labelManager;
    @FXML
    private Label labelEE;

    @FXML
    private TableView<DocumentoAceptacionDTO> tableViewDocuments;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> columnName;
    @FXML
    private TableColumn<DocumentoAceptacionDTO, String> columnStatus;

    private AsignacionDTO currentAssignment;
    private DocumentoAceptacionDAO documentDAO = new DocumentoAceptacionDAO();
    private PracticanteDTO currentPracticante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            PracticanteDAO practicanteDAO = new PracticanteDAO();
            currentPracticante = practicanteDAO.getByEnrollment(Sesion.getCurrentUser().getNombreUsuario());

            if (currentPracticante != null) {
                AsignacionDAO asignacionDAO = new AsignacionDAO();
                currentAssignment = asignacionDAO.getActiveAssignmentByIntern(currentPracticante.getPracticanteId());

                if (currentAssignment != null) {
                    labelProjectName.setText(currentAssignment.getNombreProyecto());
                    labelMatricula.setText(currentAssignment.getMatriculaPracticante());

                    try {
                        ProyectoDTO project = new ProyectoDAO().getOne(currentAssignment.getProyectoId());
                        ResponsableProyectoDTO manager = new ResponsableProyectoDAO().getOne(project.getTitularId());
                        OrganizacionVinculadaDTO org = new OrganizacionVinculadaDAO().getOne(manager.getOrganizacionId());
                        ExperienciaEducativaDTO ee = new ExperienciaEducativaDAO().getOne(currentAssignment.getExperienciaEducativaId());

                        labelOrganization.setText(org.getNombreEmpresa());
                        labelManager.setText(manager.getNombre());
                        labelEE.setText(ee.getNombre() + " (" + ee.getNrc() + ")");
                    } catch (ExcepcionMostrableUsuario e) {
                        LOGGER.error("Error al cargar datos adicionales del oficio", e);
                    }

                    loadDocuments();
                } else {
                    labelProjectName.setText("No asignado");
                    labelMatricula.setText("No asignado");
                    labelOrganization.setText("No asignado");
                    labelManager.setText("No asignado");
                    labelEE.setText("No asignado");
                    Modal.displayInformation("Atención", "No cuentas con un proyecto asignado activo.");
                }
            }
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
        }
    }

    private void loadDocuments() throws ExcepcionMostrableUsuario {
        List<DocumentoAceptacionDTO> docs = documentDAO.getAllByAssignmentId(currentAssignment.getAsignacionId());
        ObservableList<DocumentoAceptacionDTO> observableDocs = FXCollections.observableArrayList(docs);
        columnName.setCellValueFactory(new PropertyValueFactory<>("nombreEntregable"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tableViewDocuments.setItems(observableDocs);
    }

    @FXML
    private void generarOficioAsignacion(ActionEvent event) {
        if (currentAssignment == null) {
            Modal.displayError(new ExcepcionMostrableUsuario("Operación Inválida", "Sin asignación", "No tienes un proyecto asignado activo para generar el oficio."));
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Oficio de Asignación");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Oficio_Asignacion_" + currentAssignment.getMatriculaPracticante() + ".pdf");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Document document = new Document();
                document.setMargins(50, 50, 50, 50);
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

                SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
                String dateStr = sdf.format(new Date());
                Paragraph datePara = new Paragraph("Xalapa-Enríquez, Veracruz, a " + dateStr + "\n\n", normalFont);
                datePara.setAlignment(Element.ALIGN_RIGHT);
                document.add(datePara);

                ProyectoDTO project = new ProyectoDAO().getOne(currentAssignment.getProyectoId());
                ResponsableProyectoDTO manager = new ResponsableProyectoDAO().getOne(project.getTitularId());
                OrganizacionVinculadaDTO org = new OrganizacionVinculadaDAO().getOne(manager.getOrganizacionId());
                ExperienciaEducativaDTO ee = new ExperienciaEducativaDAO().getOne(currentAssignment.getExperienciaEducativaId());
                List<CoordinadorDTO> coords = new CoordinadorDAO().getAll();
                CoordinadorDTO coord = coords.isEmpty() ? null : coords.get(0);

                Paragraph destinatario = new Paragraph();
                destinatario.add(new Chunk(manager.getNombre().toUpperCase() + "\n", boldFont));
                destinatario.add(new Chunk("GESTOR DE PROYECTOS\n", boldFont));
                destinatario.add(new Chunk(org.getNombreEmpresa().toUpperCase() + "\n", boldFont));
                destinatario.add(new Chunk(org.getDireccion().toUpperCase() + "\n\n", boldFont));
                destinatario.setAlignment(Element.ALIGN_LEFT);
                document.add(destinatario);

                Paragraph p1 = new Paragraph();
                p1.setAlignment(Element.ALIGN_JUSTIFIED);
                p1.setFont(normalFont);

                String texto = String.format(
                        "El practicante %s con matrícula %s en la experiencia educativa %s (NRC: %s) fue asignado al proyecto \"%s\" de la organización %s el día %s. Se compromete en el periodo \"%s\" a cumplir 420 horas.\n\n"
                        + "Las prácticas profesionales de ingeniería de software en la Universidad Veracruzana se evalúan mediante una rúbrica de reportes y evaluaciones.\n\n\n\n",
                        currentAssignment.getNombrePracticante().toUpperCase(),
                        currentAssignment.getMatriculaPracticante().toUpperCase(),
                        ee.getNombre().toUpperCase(),
                        currentAssignment.getNrc(),
                        currentAssignment.getNombreProyecto(),
                        org.getNombreEmpresa(),
                        dateStr,
                        ee.getNombrePeriodo()
                );

                p1.add(new Chunk(texto));
                document.add(p1);

                String nombreFirma = coord != null ? (coord.getNombre() + " " + coord.getApellidoPaterno() + " " + coord.getApellidoMaterno()) : "COORDINADOR";
                Paragraph firma = new Paragraph();
                firma.setAlignment(Element.ALIGN_CENTER);
                firma.setFont(normalFont);
                firma.add(new Chunk("Atentamente\n\n\n\n___________________________________\n", normalFont));
                firma.add(new Chunk(nombreFirma.toUpperCase() + "\nCoordinador de Servicio Social y Prácticas Profesionales\nLicenciatura en Ingeniería de Software", normalFont));
                document.add(firma);

                document.close();
                Modal.displayInformation("Éxito", "Oficio generado y descargado exitosamente en formato PDF.");
            } catch (Exception e) {
                LOGGER.error("Error guardando oficio", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error al generar", "Fallo al guardar el PDF", "No se pudo guardar el documento. Verifique que el archivo no esté abierto en otro programa y que tenga permisos de escritura en la carpeta destino."));
            }
        }
    }

    @FXML
    private void entregarDocumento(ActionEvent event) {
        DocumentoAceptacionDTO selected = tableViewDocuments.getSelectionModel().getSelectedItem();

        try {
            ValidadorEntrega.validateDelivery(selected);
        } catch (ExcepcionMostrableUsuario e) {
            Modal.displayError(e);
            return;
        }

        if ("Entregado".equalsIgnoreCase(selected.getEstado())) {
            if (!Modal.displayConfirmation("Ya existe un archivo cargado. ¿Desea sobrescribirlo?")) {
                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Documento PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(null);

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
                DocumentoAceptacionDTO updated = new DocumentoAceptacionDTO.DocumentoAceptacionBuilder()
                        .setDocumentoAceptacionId(selected.getDocumentoAceptacionId())
                        .setAsignacionId(selected.getAsignacionId())
                        .setNombreEntregable(selected.getNombreEntregable())
                        .setArchivo(fileBytes)
                        .setEstado("Entregado")
                        .build();
                documentDAO.updateOne(updated);
                Modal.displayInformation("Éxito", "Se han enviado los documentos con éxito");
                loadDocuments();
            } catch (Exception e) {
                LOGGER.error("Error subiendo archivo", e);
                Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error de archivo", "No se pudo cargar el archivo."));
            }
        }
    }
}
