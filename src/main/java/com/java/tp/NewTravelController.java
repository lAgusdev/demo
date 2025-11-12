package com.java.tp;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class NewTravelController {

    @FXML
    private ComboBox<String> opcionesComboBox;

    @FXML
    private void initialize() {
        List<String> lista = List.of("Opción 1", "Opción 2", "Opción 3");
        opcionesComboBox.setItems(FXCollections.observableArrayList(lista));    
    }

    @FXML
    private void switchToSecondInstanceCreateTravel() throws IOException {
        App.setRoot("secondInstanceCreateTravel");
    }
}
