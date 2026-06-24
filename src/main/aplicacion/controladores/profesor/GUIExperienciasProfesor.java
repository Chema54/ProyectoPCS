package main.aplicacion.controladores.profesor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.comun.Modal;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.Sesion;
import main.basedatos.dao.ExperienciaEducativaDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIExperienciasProfesor implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(GUIExperienciasProfesor.class);

    @FXML private TableView<ExperienciaEducativaDTO> tableViewExperiencias;
    @FXML private TableColumn<ExperienciaEducativaDTO, String> colNRC;
    @FXML private TableColumn<ExperienciaEducativaDTO, String> colName;
    @FXML private TableColumn<ExperienciaEducativaDTO, String> colPeriod;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ExperienciaEducativaDAO dao = new ExperienciaEducativaDAO();
            List<ExperienciaEducativaDTO> exp = dao.getExperienciasByUserId(Sesion.getCurrentUser().getUserID());
            
            colNRC.setCellValueFactory(new PropertyValueFactory<>("nrc"));
            colName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPeriod.setCellValueFactory(new PropertyValueFactory<>("periodName"));
            
            tableViewExperiencias.setItems(FXCollections.observableArrayList(exp));
        } catch (ExcepcionMostrableUsuario ex) {
            Modal.displayError(ex);
        }
    }

    @FXML
    private void administrarEntregas(ActionEvent event) {
        ExperienciaEducativaDTO selected = tableViewExperiencias.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Modal.displayInformation("Atención", "Por favor, seleccione una Experiencia Educativa de la tabla.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/aplicacion/vistas/profesor/GUIAdministrarEntregables.fxml"));
            Parent view = loader.load();
            GUIAdministrarEntregables controller = loader.getController();
            controller.setExperiencia(selected);
            
            // To replace central pane, we can get parent of parent or use static method if needed.
            // A simpler way: Find the menu controller from scene or pass it.
            // For now, replacing the scene's root is too drastic, let's just get the anchor pane:
            tableViewExperiencias.getScene().lookup("#anchorPaneCentral"); // Just an idea, but cleaner to use the node.
            
            // We just set the children of the parent AnchorPane
            ((javafx.scene.layout.AnchorPane) tableViewExperiencias.getParent()).getChildren().setAll(view);
            
        } catch (IOException ex) {
            LOGGER.error("Error al cargar Administrar Entregas", ex);
        }
    }
}
