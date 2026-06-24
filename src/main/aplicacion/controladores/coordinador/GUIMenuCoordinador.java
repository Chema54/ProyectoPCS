/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.aplicacion.controladores.coordinador;

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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.comun.Modal;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class GUIMenuCoordinador implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static final Logger LOGGER = LogManager.getLogger(GUIMenuCoordinador.class);

    @FXML
    private Button buttonCloseSesion;

    @FXML
    private Label labelTitleMenu;

    @FXML
    private Label labelAcademicName;

    @FXML
    private ToggleButton toggleButtonProject;

    @FXML
    private ToggleButton toggleButtonPeriod;

    @FXML
    private ToggleButton toggleButtonUsers;
    
    @FXML
    private ToggleButton toggleButtonAcceptanceDocuments;

    @FXML
    private AnchorPane anchorPaneCentral;

    private final ToggleGroup toggleGroupMenu = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelAcademicName.setText("¡Bienvenido de vuelta, Coordinador!");
        try {
        configureToggleGroup();
        toggleButtonProject.setSelected(true);
        } catch (ExcepcionMostrableUsuario e) {
            LOGGER.error(e.getMessage(), e);
            Modal.displayError(e);
        }
    }

    private void configureToggleGroup() throws ExcepcionMostrableUsuario{
        toggleButtonProject.setToggleGroup(toggleGroupMenu);
        toggleButtonPeriod.setToggleGroup(toggleGroupMenu);
        toggleButtonUsers.setToggleGroup(toggleGroupMenu);
        toggleButtonAcceptanceDocuments.setToggleGroup(toggleGroupMenu);

        toggleGroupMenu.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
                if (newToggle == null) {
                    if (oldToggle != null) {
                        oldToggle.setSelected(true);
                    }
                    return;
                }
                try {
                    loadSelectedView(newToggle);
                } catch (ExcepcionMostrableUsuario ex) {
                    LOGGER.error(ex.getMessage(), ex);
                    Modal.displayError(ex);
                }
            }
        );
    }

    private void loadSelectedView(Toggle toggle) throws ExcepcionMostrableUsuario {
        if (toggle == toggleButtonProject) {
            changeSubView("/main/aplicacion/vistas/coordinador/GUIAdministrarProyectos.fxml");
        } else if (toggle == toggleButtonPeriod) {
            changeSubView("/main/aplicacion/vistas/coordinador/GUIAbrirPeriodoEscolar.fxml");
        } else if (toggle == toggleButtonUsers) {
            changeSubView("/main/aplicacion/vistas/coordinador/GUIAdministrarPracticantes.fxml");
        } else if (toggle == toggleButtonAcceptanceDocuments) {
            changeSubView("/main/aplicacion/vistas/coordinador/GUIRevisarDocumentos.fxml");
        }
    }

    private void changeSubView(String routeFXML)throws ExcepcionMostrableUsuario {
        try {
            anchorPaneCentral.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(routeFXML));
            Parent subView = loader.load();
            AnchorPane.setTopAnchor(subView, 0.0);
            AnchorPane.setBottomAnchor(subView, 0.0);
            AnchorPane.setLeftAnchor(subView, 0.0);
            AnchorPane.setRightAnchor(subView, 0.0);
            anchorPaneCentral.getChildren().add(subView);
        } catch (IOException e) {
            throw ManejadorExcepciones.handleGUILoadIOException(LOGGER, e);
        } catch (NullPointerException e) {
            throw ManejadorExcepciones.handleUnexpectedException(LOGGER, e, "No se encontró la vista: " + routeFXML);
        } catch (Exception e) {
            throw ManejadorExcepciones.handleUnexpectedException(LOGGER, e,"Error al cargar la vista: " + routeFXML);
        }
    }

    @FXML
    private void handleCloseSesion(ActionEvent event) {
        try {
            ConexionBD.deleteConfiguration();
            returnToLogin();
        } catch (ExcepcionMostrableUsuario e) {
            LOGGER.error(e.getMessage(), e); 
            Modal.displayError(e);
        }
    }

    private void returnToLogin() throws ExcepcionMostrableUsuario {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/main/aplicacion/vistas/GUIIniciarSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonCloseSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Portal de Prácticas Profesionales");
            stage.show();
        } catch (IOException e) {
            throw ManejadorExcepciones.handleGUILoadIOException(LOGGER, e);
        }
    }
}
