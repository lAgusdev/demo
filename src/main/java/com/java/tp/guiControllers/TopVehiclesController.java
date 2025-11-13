package com.java.tp.guiControllers;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.java.tp.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.java.tp.agency.Agency;
import com.java.tp.agency.vehicles.Vehicles;

public class TopVehiclesController {

    @FXML
    private javafx.scene.control.ListView<String> list;

    @FXML
    private void initialize() {
        // Obtener vehículos desde la agencia
        try {
            HashMap<String, Vehicles> veh = Agency.getInstancia().getVehiculos();
            List<String> lista;
            if (veh == null || veh.isEmpty()) {
                // fallback: lista por defecto si no hay vehículos cargados
                lista = List.of("Auto", "Camión", "Motocicleta", "Bicicleta", "Autobús");
            } else {
                lista = new ArrayList<>(veh.values().stream()
                        .map(v -> v.getPatente() + " - " + v.getClass().getSimpleName())
                        .sorted()
                        .collect(Collectors.toList()));
            }
            ObservableList<String> items = FXCollections.observableArrayList(lista);
            list.setItems(items);
        } catch (Exception e) {
            // en caso de error, usar la lista por defecto
            ObservableList<String> items = FXCollections.observableArrayList(
                    List.of("No se pudieron cargar los vehículos")
            );
            list.setItems(items);
            System.out.println("No se pudieron cargar los vehículos: " + e.getMessage());
        }
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }
    
    /** generador de reportes de vehiculos */
    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
