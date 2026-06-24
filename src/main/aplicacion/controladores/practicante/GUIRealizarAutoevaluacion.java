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

    @FXML private VBox vboxQuestions;

    private List<ToggleGroup> toggleGroups = new ArrayList<>();
    private String[] questions = {
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

    private AutoevaluacionDTO selfAssessment;
    private AsignacionDTO assignment;
    private PracticanteDTO intern;
    private GUIEntregarEntregables parentController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String q : questions) {
            Label labelQ = new Label(q);
            labelQ.setWrapText(true);
            labelQ.setPrefWidth(600);
            
            HBox options = new HBox(20);
            ToggleGroup group = new ToggleGroup();
            toggleGroups.add(group);
            
            for (int i = 1; i <= 5; i++) {
                RadioButton rbOption = new RadioButton(String.valueOf(i));
                rbOption.setUserData(i);
                rbOption.setToggleGroup(group);
                options.getChildren().add(rbOption);
            }
            vboxQuestions.getChildren().addAll(labelQ, options);
        }
    }

    public void initData(AutoevaluacionDTO selfAssessment, AsignacionDTO assignment, PracticanteDTO intern, GUIEntregarEntregables parent) {
        this.selfAssessment = selfAssessment;
        this.assignment = assignment;
        this.intern = intern;
        this.parentController = parent;
    }

    @FXML
    private void guardarYGenerar(ActionEvent event) {
        int totalScore = 0;
        List<Integer> answers = new ArrayList<>();

        for (int i = 0; i < toggleGroups.size(); i++) {
            ToggleGroup group = toggleGroups.get(i);
            if (group.getSelectedToggle() == null) {
                Modal.displayInformation("Incompleto", "Por favor responda la pregunta " + (i + 1));
                return;
            }
            int score = (int) group.getSelectedToggle().getUserData();
            totalScore += score;
            answers.add(score);
        }

        try {
            byte[] pdfBytes = generatePDF(totalScore, answers);
            
            AutoevaluacionDTO updated = new AutoevaluacionDTO.SelfAssessmentBuilder()
                    .setSelfAssessmentId(selfAssessment.getSelfAssessmentId())
                    .setAsignacionId(selfAssessment.getAsignacionId())
                    .setNombreEntregable(selfAssessment.getNombreEntregable())
                    .setArchivo(pdfBytes)
                    .setEstado("Entregado")
                    .setScore(new java.math.BigDecimal(totalScore))
                    .build();

            new AutoevaluacionDAO().updateOne(updated);
            
            Modal.displayInformation("Éxito", "La autoevaluación se ha registrado exitosamente. Puntuación Final: " + totalScore + "/50");
            parentController.loadData();
            cerrarVentana();
            
        } catch (Exception e) {
            LOGGER.error("Error generando/guardando autoevaluación", e);
            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error al procesar", e.getMessage()));
        }
    }

    private byte[] generatePDF(int totalScore, List<Integer> answers) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        
        Paragraph title = new Paragraph("FORMATO: EVALUACIÓN DEL ALUMNO\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        document.add(new Paragraph("Nombre del alumno: " + intern.getNombre() + " " + intern.getApellidoPaterno(), normalFont));
        document.add(new Paragraph("Matrícula: " + intern.getMatricula(), normalFont));
        document.add(new Paragraph("Proyecto: " + assignment.getNombreProyecto() + "\n\n", normalFont));
        
        document.add(new Paragraph("RESULTADOS DEL CUESTIONARIO:\n\n", boldFont));
        for (int i = 0; i < questions.length; i++) {
            document.add(new Paragraph(questions[i], normalFont));
            document.add(new Paragraph("Respuesta: " + answers.get(i) + " / 5\n", boldFont));
        }
        
        Paragraph scorePara = new Paragraph("\nPUNTUACIÓN FINAL: " + totalScore + " / 50", titleFont);
        scorePara.setAlignment(Element.ALIGN_RIGHT);
        document.add(scorePara);
        
        document.close();
        return baos.toByteArray();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) vboxQuestions.getScene().getWindow();
        stage.close();
    }
}
