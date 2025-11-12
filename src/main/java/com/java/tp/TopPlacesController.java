package com.java.tp;

import java.io.IOException;

import javafx.fxml.FXML;

public class TopPlacesController {

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }

    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
