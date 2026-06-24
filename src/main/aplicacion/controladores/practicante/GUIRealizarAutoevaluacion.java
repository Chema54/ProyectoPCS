package main.aplicacion.controladores.practicante;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import main.negocio.dto.AutoevaluacionDTO;
import main.negocio.dto.AsignacionDTO;
import main.negocio.dto.PracticanteDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.dao.AutoevaluacionDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIRealizarAutoevaluacion implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIRealizarAutoevaluacion.class);

    @FXML private VBox vboxPreguntas;

    private List<ToggleGroup> gruposRespuestas = new ArrayList<>();
    private String[] preguntas = {
        "1. Mi participación en la Organización Vinculada fue productiva.",
        "2. Logré la aplicación de los conocimientos teórico-prácticos adquiridos en la Licenciatura.",
        "3. Me sentí seguro al realizar las actividades encomendadas.",
        "4. Las actividades encomendadas despertaron mi interés.",
        "5. La Organización Vinculada me proporcionó la información y facilidades adecuados.",
        "6. La Organización Vinculada me dio a conocer las reglas internas que debía seguir.",
        "7. El Responsable del Proyecto me orientó correctamente para el desarrollo de mis actividades.",
        "8. El Responsable del Proyecto realizó un seguimiento efectivo de mis actividades.",
        "9. El proyecto es congruente con la formación de mi carrera.",
        "10. Considero que las prácticas son importantes para mi formación profesional."
    };

    private AutoevaluacionDTO autoevaluacion;
    private AsignacionDTO asignacion;
    private PracticanteDTO practicante;
    private GUIEntregarEntregables controladorPadre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String q : preguntas) {
            Label labelPregunta = new Label(q);
            labelPregunta.setWrapText(true);
            labelPregunta.setPrefWidth(600);
            
            HBox opciones = new HBox(20);
            ToggleGroup grupo = new ToggleGroup();
            gruposRespuestas.add(grupo);
            
            for (int i = 1; i <= 5; i++) {
                RadioButton rbOpcion = new RadioButton(String.valueOf(i));
                rbOpcion.setUserData(i);
                rbOpcion.setToggleGroup(grupo);
                opciones.getChildren().add(rbOpcion);
            }
            vboxPreguntas.getChildren().addAll(labelPregunta, opciones);
        }
    }

    public void initData(AutoevaluacionDTO autoevaluacion, AsignacionDTO asignacion, PracticanteDTO practicante, GUIEntregarEntregables padre) {
        this.autoevaluacion = autoevaluacion;
        this.asignacion = asignacion;
        this.practicante = practicante;
        this.controladorPadre = padre;
    }

    @FXML
    private void guardarYGenerar(ActionEvent event) {
        int puntajeTotal = 0;
        List<Integer> respuestas = new ArrayList<>();

        for (int i = 0; i < gruposRespuestas.size(); i++) {
            ToggleGroup grupo = gruposRespuestas.get(i);
            if (grupo.getSelectedToggle() == null) {
                Modal.displayInformation("Incompleto", "Por favor responda la pregunta " + (i + 1));
                return;
            }
            int puntaje = (int) grupo.getSelectedToggle().getUserData();
            puntajeTotal += puntaje;
            respuestas.add(puntaje);
        }

        try {
            byte[] bytesPDF = generarPDF(puntajeTotal, respuestas);
            
            AutoevaluacionDTO actualizada = new AutoevaluacionDTO.AutoevaluacionBuilder()
                    .setAutoevaluacionId(autoevaluacion.getAutoevaluacionId())
                    .setAsignacionId(autoevaluacion.getAsignacionId())
                    .setNombreEntregable(autoevaluacion.getNombreEntregable())
                    .setArchivo(bytesPDF)
                    .setEstado("Entregado")
                    .setPuntaje(new java.math.BigDecimal(puntajeTotal))
                    .build();

            new AutoevaluacionDAO().updateOne(actualizada);
            
            Modal.displayInformation("Éxito", "La autoevaluación se ha registrado exitosamente. Puntuación Final: " + puntajeTotal + "/50");
            controladorPadre.loadDeliverables();
            cerrarVentana();
            
        } catch (Exception e) {
            LOGGER.error("Error generando/guardando autoevaluación", e);
            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error al procesar", e.getMessage()));
        }
    }

    private byte[] generarPDF(int puntajeTotal, List<Integer> respuestas) throws Exception {
        ByteArrayOutputStream salidaBytes = new ByteArrayOutputStream();
        Document documento = new Document();
        PdfWriter.getInstance(documento, salidaBytes);
        documento.open();
        
        Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        
        Paragraph titulo = new Paragraph("FORMATO: EVALUACIÓN DEL ALUMNO\n\n", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        
        documento.add(new Paragraph("Nombre del alumno: " + practicante.getNombre() + " " + practicante.getApellidoPaterno(), fuenteNormal));
        documento.add(new Paragraph("Matrícula: " + practicante.getMatricula(), fuenteNormal));
        documento.add(new Paragraph("Proyecto: " + asignacion.getNombreProyecto() + "\n\n", fuenteNormal));
        
        documento.add(new Paragraph("RESULTADOS DEL CUESTIONARIO:\n\n", fuenteNegrita));
        for (int i = 0; i < preguntas.length; i++) {
            documento.add(new Paragraph(preguntas[i], fuenteNormal));
            documento.add(new Paragraph("Respuesta: " + respuestas.get(i) + " / 5\n", fuenteNegrita));
        }
        
        Paragraph parrafoPuntaje = new Paragraph("\nPUNTUACIÓN FINAL: " + puntajeTotal + " / 50", fuenteTitulo);
        parrafoPuntaje.setAlignment(Element.ALIGN_RIGHT);
        documento.add(parrafoPuntaje);
        
        documento.close();
        return salidaBytes.toByteArray();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) vboxPreguntas.getScene().getWindow();
        stage.close();
    }
}
