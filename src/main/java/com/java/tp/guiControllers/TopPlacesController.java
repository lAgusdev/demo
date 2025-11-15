package com.java.tp.guiControllers;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.java.tp.agency.reports.ReportPlaces;

public class TopPlacesController {

    @FXML
    private javafx.scene.control.ListView<String> list;
    
    @FXML
    private javafx.scene.control.Label totalPlaces;

    /**
     * Método auxiliar para obtener la lista de destinos formateada.
     * @return Una lista de Strings con los destinos formateados ("ID - KM km").
     */


    @FXML
    private void initialize() {
        try {
            List<String> lista = getDestinosFormateados();
            ObservableList<String> items = FXCollections.observableArrayList(lista);
            list.setItems(items);
            
            // Calcular el total de km recorridos
            HashMap<String, Travel> viajes = Agency.getInstancia().getViajes();
            float totalKm = 0;
            if (viajes != null && !viajes.isEmpty()) {
                for (Travel viaje : viajes.values()) {
                    totalKm += viaje.getKmRec();
                }
            }
            totalPlaces.setText(String.format("%.2f", totalKm));
            
        } catch (Exception e) {
            ObservableList<String> items = FXCollections.observableArrayList(
                List.of("No se pudieron cargar los destinos")
            );
            list.setItems(items);
            totalPlaces.setText("0.00");
            System.out.println("No se pudieron cargar los destinos: " + e.getMessage());
        }
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }

}