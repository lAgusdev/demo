package com.java.tp.guiControllers;
import java.io.IOException;
import java.util.List;

import com.java.tp.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TopVehiclesController {

    @FXML
    private javafx.scene.control.ListView<String> list;

    @FXML
    private void initialize() {
        List<String> lista = List.of("Auto", "Camión", "Motocicleta", "Bicicleta", "Autobús");
        ObservableList<String> items = FXCollections.observableArrayList(lista);
        list.setItems(items);
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }
    
    /** generador de reportes de vehiculos */
    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
