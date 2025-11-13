package com.java.tp.guiControllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import com.java.tp.App;
import com.java.tp.agency.Agency;
import com.java.tp.agency.places.Place;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TopPlacesController {

    @FXML
    private javafx.scene.control.ListView<String> list;

    @FXML
    private void initialize() {
        try {
            TreeMap<String, Place> destinos = Agency.getInstancia().getDestinos();
            List<String> lista;
            if (destinos == null || destinos.isEmpty()) {
                lista = List.of("No se pudieron cargar los destinos");
            } else {
                lista = new ArrayList<>(destinos.values().stream()
                        .map(p -> p.getId() + " - " + p.getKm() + " km")
                        .sorted()
                        .collect(Collectors.toList()));
            }
            ObservableList<String> items = FXCollections.observableArrayList(lista);
            list.setItems(items);
        } catch (Exception e) {
            ObservableList<String> items = FXCollections.observableArrayList(
                    List.of("No se pudieron cargar los destinos")
            );
            list.setItems(items);
            System.out.println("No se pudieron cargar los destinos: " + e.getMessage());
        }
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }

    /** generador de reportes de destinos */
    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
