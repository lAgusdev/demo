package com.java.tp;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TopResponsableController {

    @FXML
    private javafx.scene.control.ListView<String> list;

    @FXML
    private void initialize() {
        List<String> lista = List.of("Alejandro", "Alfonso", "Maria", "Gonzalez", "Pedro", "Luis", "Martinez", "Ana", "Lucia", "Rodriguez", "Carlos", "Javier", "Lopez", "Sofia", "Elena");
        ObservableList<String> items = FXCollections.observableArrayList(lista);
        list.setItems(items);
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }

    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
