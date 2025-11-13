package com.java.tp;
import java.io.IOException;
import javafx.fxml.FXML;

public class ReportMenuController {

    @FXML
    private void switchToResponsableReport() throws IOException {
        App.setRoot("topResponsable");
    }
    @FXML
    private void switchToVehiculosReport() throws IOException {
        App.setRoot("topVehicles");
    }
    @FXML
    private void switchToPlacesReport() throws IOException {
        App.setRoot("topPlaces");
    }
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

}
