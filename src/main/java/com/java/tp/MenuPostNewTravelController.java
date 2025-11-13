package com.java.tp;
import java.io.IOException;
import javafx.fxml.FXML;

public class MenuPostNewTravelController {

    @FXML
    private void switchToCreateTravel() throws IOException {
        App.setRoot("newTravelMenu");
    }
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}