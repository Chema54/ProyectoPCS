/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.application.modal;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

/**
 *
 * @author josem
 */
public final class Modal {

    private Modal() {
        throw new IllegalStateException("Clase utilitaria");
    }

    public static void displayError(String message) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public static void displayWarning(String message) {
        Alert alert = new Alert(AlertType.WARNING);

        alert.setTitle("Advertencia");
        alert.setHeaderText("Advertencia");
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public static void displayInformation(String title,String message) {
        Alert alert = new Alert(AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public static void displayConfirmation(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmación");
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
