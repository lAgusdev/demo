package com.java.tp.guiControllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private List<String> getVehiculosFormateados() {
        HashMap<String, Vehicles> veh = Agency.getInstancia().getVehiculos();
        
        if (veh == null || veh.isEmpty()) {
            return List.of("No hay vehículos cargados");
        } else {
            int acumBusCC = 0, acumBusSC = 0, acumMiniBus = 0, acumCar = 0;
            for (Vehicles v : veh.values()) {
                switch (v.getCapacidad()) {
                    case 32:
                        acumBusCC += 1;
                        break;
                    case 40:
                        acumBusSC += 1;
                        break;
                    case 16:
                        acumMiniBus += 1;
                        break;
                    case 4:
                        acumCar += 1;
                        break;
                }
            }
            List<String> vehicleReport = new ArrayList<>();
            vehicleReport.add("Colectivo coche-cama: " + acumBusCC);
            vehicleReport.add("Colectivo semi-cama: " + acumBusSC);
            vehicleReport.add("Combi: " + acumMiniBus);
            vehicleReport.add("Auto: " + acumCar);
            return vehicleReport;
        }
    }

    @FXML
    private void initialize() {
        // Obtener vehículos desde la agencia
        try {
            List<String> lista = getVehiculosFormateados();
            
            // Si el resultado es la lista de error, se usa una ObservableList temporal para mostrar el error.
            if (lista.size() == 1 && lista.get(0).equals("No hay vehículos cargados")) {
                 ObservableList<String> items = FXCollections.observableArrayList(
                    List.of("No se pudieron cargar los vehículos")
                );
                list.setItems(items);
            } else {
                ObservableList<String> items = FXCollections.observableArrayList(lista);
                list.setItems(items);
            }
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
        List<String> lineasReporte = getVehiculosFormateados();
        
        // Si la única línea es el mensaje de error, no se genera el archivo.
        if (lineasReporte.size() == 1 && lineasReporte.get(0).equals("No hay vehículos cargados")) {
            System.out.println("No se puede generar el reporte: No hay datos de vehículos cargados.");
            return;
        }

        // Añadir encabezado al reporte
        lineasReporte.add(0, "--- REPORTE DE VEHÍCULOS DE LA AGENCIA ---");
        
        // Generar nombre de archivo único con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "Reporte_Vehiculos_" + timestamp + ".txt";
        
        // Definir la ruta del archivo (se guardará en el directorio de ejecución del programa)
        Path rutaArchivo = Path.of("reports/" + nombreArchivo);
        
        try {
            // Escribir todas las líneas en el archivo. Esto lo crea si no existe.
            Files.write(rutaArchivo, lineasReporte);
            
            System.out.println("Reporte de vehículos generado con éxito en: " + rutaArchivo.toAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte de vehículos: " + e.getMessage());
        }
    }
}