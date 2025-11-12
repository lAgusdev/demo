package com.java.tp;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void switchToCreateTravel() throws IOException {
        App.setRoot("newTravelMenu");
    }
    @FXML
    private void switchToReports() throws IOException {
        App.setRoot("reports");
    }
    @FXML
    private void switchToExit() throws IOException {
        App.setRoot("exit");
    }
}
