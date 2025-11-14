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

    /**
     * Método auxiliar para obtener la lista de vehículos formateada, ordenada
     * y con el número de puesto (ranking) al inicio.
     * @return Una lista de Strings con los vehículos formateados ("#Puesto - Patente - TipoVehiculo - Vel/H: X").
     */
    private List<String> getVehiculosFormateados() {
        HashMap<String, Vehicles> veh = Agency.getInstancia().getVehiculos();
        
        if (veh == null || veh.isEmpty()) {
            return List.of("No hay vehículos cargados");
        } else {
            // 1. Obtener la lista de vehículos formateada y ordenada (base)
            // Se utiliza getClass().getSimpleName() para obtener el tipo de vehículo.
            List<String> vehiculosOrdenados = veh.values().stream()
                .map(v -> v.getPatente() + " - " + v.getClass().getSimpleName() + " - Vel/H: " + v.getVelPerH())
                .sorted() 
                .collect(Collectors.toList());

            List<String> listaConPuesto = new ArrayList<>();
            
            // 2. Iterar sobre la lista ordenada para añadir el número de puesto (ranking)
            for (int i = 0; i < vehiculosOrdenados.size(); i++) {
                // Se añade el número de posición (i + 1). Ejemplo: "#1 - ABC123 - Car - Vel/H: 100"
                String linea = String.format("#%d - %s", (i + 1), vehiculosOrdenados.get(i)); 
                listaConPuesto.add(linea);
            }
            
            return listaConPuesto;
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

        // 1. Añadir encabezado al reporte
        lineasReporte.add(0, "--- REPORTE DE VEHÍCULOS DE LA AGENCIA ---");
        
        // 2. Generar nombre de archivo único con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "Reporte_Vehiculos_" + timestamp + ".txt";
        
        // 3. Definir la ruta del archivo (se guardará en el directorio de ejecución del programa)
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