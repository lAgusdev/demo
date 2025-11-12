package com.java.tp;

import java.io.IOException;

import javafx.fxml.FXML;

public class TopVehiclesController {

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }
}
