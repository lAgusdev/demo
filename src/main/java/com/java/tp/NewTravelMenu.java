package com.java.tp;

import java.io.IOException;
import javafx.fxml.FXML;

public class NewTravelMenu {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("exit");
    }
}
