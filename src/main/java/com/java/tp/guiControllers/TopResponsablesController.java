package com.java.tp.guiControllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.java.tp.App;
import com.java.tp.agency.Agency;
import com.java.tp.agency.responsables.Responsable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TopResponsablesController {

    @FXML
    private javafx.scene.control.ListView<String> list;

    @FXML
    private void initialize() {
        try {
            HashMap<String, Responsable> res = Agency.getInstancia().getResponsables();
            List<String> lista;
            if (res == null || res.isEmpty()) {
                lista = List.of("No hay responsables cargados");
            } else {
                lista = new ArrayList<>(res.values().stream()
                        .map(r -> r.getDni() + " - " + r.getNombre() + " - $" + r.getSalario())
                        .sorted()
                        .collect(Collectors.toList()));
            }
            ObservableList<String> items = FXCollections.observableArrayList(lista);
            list.setItems(items);
        } catch (Exception e) {
            ObservableList<String> items = FXCollections.observableArrayList(
                    List.of("No se pudieron cargar los responsables")
            );
            list.setItems(items);
            System.out.println("No se pudieron cargar los responsables: " + e.getMessage());
        }
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("reportMenu");
    }

    /** generador de reportes de responsables */
    @FXML
    private void generateReport() throws IOException {
        // Lógica para generar el reporte de destinos
    }
}
