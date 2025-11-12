package com.java.tp;

import java.io.IOException;
import javafx.fxml.FXML;

public class NewTravelController {

    @FXML
    private void switchToSecondInstanceCreateTravel() throws IOException {
        App.setRoot("secondInstanceCreateTravel");
    }
}
