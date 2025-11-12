package com.java.tp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class NewTravelController {

    @FXML
    private ComboBox<String> opcionesComboBox;

    @FXML
    private void initialize() {
        opcionesComboBox.getItems().setAll("Opción 1", "Opción 2", "Opción 3");
    }

    @FXML
    private void switchToSecondInstanceCreateTravel() throws IOException {
        App.setRoot("secondInstanceCreateTravel");
    }
}
