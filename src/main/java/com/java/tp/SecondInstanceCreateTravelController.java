package com.java.tp;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class SecondInstanceCreateTravelController {

    @FXML
    private void switchToSecondary() throws IOException {
        Platform.exit();
    }
}
