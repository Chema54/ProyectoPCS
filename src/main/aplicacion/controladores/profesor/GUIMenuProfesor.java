package main.aplicacion.controladores.profesor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.comun.Modal;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.Sesion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIMenuProfesor implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIMenuProfesor.class);

    @FXML private Button buttonCloseSesion;
    @FXML private Label labelProfessorName;
    @FXML private ToggleButton toggleButtonExperiencias;
    @FXML private AnchorPane anchorPaneCentral;

    private final ToggleGroup toggleGroupMenu = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            labelProfessorName.setText("¡Bienvenido, " + Sesion.getCurrentUser().getNombreUsuario() + "!");
            configureToggleGroup();
            toggleButtonExperiencias.setSelected(true); // Loads default
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Modal.displayError(new ExcepcionMostrableUsuario("Error", "Error al inicializar", e.getMessage()));
        }
    }

    private void configureToggleGroup() {
        toggleButtonExperiencias.setToggleGroup(toggleGroupMenu);

        toggleGroupMenu.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    if (oldToggle != null) {
                        oldToggle.setSelected(true);
                    }
                    return;
                }
                if (newToggle == toggleButtonExperiencias) {
                    loadView("/main/aplicacion/vistas/profesor/GUIExperienciasProfesor.fxml");
                }
            }
        );
    }

    public void loadView(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane view = loader.load();
            anchorPaneCentral.getChildren().setAll(view);
        } catch (IOException ex) {
            LOGGER.error("Error al cargar vista: " + path, ex);
        }
    }

    public void setCentralView(Parent view) {
        anchorPaneCentral.getChildren().setAll(view);
    }

    @FXML
    private void handleCloseSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/GUIIniciarSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonCloseSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
