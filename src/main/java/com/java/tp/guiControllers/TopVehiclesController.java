package com.java.tp.guiControllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.java.tp.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.java.tp.agency.Agency;
import com.java.tp.agency.travels.Travel;
import com.java.tp.agency.vehicles.Vehicles;

public class TopVehiclesController {

    @FXML
    private javafx.scene.control.ListView<String> list;
    
    @FXML
    private javafx.scene.control.Label totalVehicles;

    @FXML
    private void initialize() {
        try {
            HashMap<String, Travel> viajes = Agency.getInstancia().getViajes();
            HashMap<String, Vehicles> vehiculos = Agency.getInstancia().getVehiculos();
            
            if (viajes == null || viajes.isEmpty() || vehiculos == null || vehiculos.isEmpty()) {
                throw new Exception("No hay datos cargados");
            }
            
            float acumBusCC = 0, acumBusSC = 0, acumMiniBus = 0, acumCar = 0;
            
            for (Travel v : viajes.values()) {
                // Obtener el vehículo del viaje usando la patente como clave del HashMap
                Vehicles vehiculoDelViaje = vehiculos.get(v.getPatVehiculo());
                
                int pasajeros = vehiculoDelViaje.getCapacidad();
                
                switch (pasajeros) {
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
            vehicleReport.add("Auto: $" + acumCar);
            vehicleReport.add("Combi: $" + acumMiniBus);
            vehicleReport.add("Colectivo semi-cama: $" + acumBusSC);
            vehicleReport.add("Colectivo coche-cama: $" + acumBusCC);
            
            float total = acumCar + acumMiniBus + acumBusSC + acumBusCC;
            totalVehicles.setText(String.valueOf("$ " + total));
            
            ObservableList<String> items = FXCollections.observableArrayList(vehicleReport);
            list.setItems(items);
            
        } catch (Exception e) {
            // En caso de error, mostrar mensaje de error
            ObservableList<String> items = FXCollections.observableArrayList(
                List.of("No se pudieron cargar los vehículos")
            );
            list.setItems(items);
            totalVehicles.setText("0");
            System.out.println("Error al cargar vehículos: " + e.getMessage());
        }
    }
    


    private List<String> getVehiculosFormateados() {
        try {
            HashMap<String, Travel> viajes = Agency.getInstancia().getViajes();
            HashMap<String, Vehicles> vehiculos = Agency.getInstancia().getVehiculos();
            
            if (viajes == null || viajes.isEmpty() || vehiculos == null || vehiculos.isEmpty()) {
                return List.of("No se pudieron cargar los vehículos");
            }
            
            float acumBusCC = 0, acumBusSC = 0, acumMiniBus = 0, acumCar = 0;
            
            for (Travel v : viajes.values()) {
                Vehicles vehiculoDelViaje = vehiculos.get(v.getPatVehiculo());
                
                if (vehiculoDelViaje == null) {
                    continue;
                }
                
                int pasajeros = vehiculoDelViaje.getCapacidad();
                
                switch (pasajeros) {
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
            vehicleReport.add("Auto: $" + acumCar);
            vehicleReport.add("Combi: $" + acumMiniBus);
            vehicleReport.add("Colectivo semi-cama: $" + acumBusSC);
            vehicleReport.add("Colectivo coche-cama: $" + acumBusCC);
            
            return vehicleReport;
            
        } catch (Exception e) {
            return List.of("No se pudieron cargar los vehículos");
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