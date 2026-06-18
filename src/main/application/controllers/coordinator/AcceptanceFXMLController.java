/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application.controllers.coordinator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dto.DocumentoAceptacionDTO;
import main.common.Modal;
import main.common.UserDisplayableException;
import main.database.dao.DocumentoAceptacionDAO;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class AcceptanceFXMLController implements Initializable {

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
        colAsigId.setCellValueFactory(new PropertyValueFactory<>("assignmentId"));
        colFile.setCellValueFactory(new PropertyValueFactory<>("file"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    }

    private void loadDocuments() {
        try {
            tvAcceptance.setItems(FXCollections.observableArrayList(documentoDAO.getAll()));
        } catch (UserDisplayableException e) {
            Modal.displayError(e);
        }
    }
}

