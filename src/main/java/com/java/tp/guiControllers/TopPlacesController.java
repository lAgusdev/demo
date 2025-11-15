package com.java.tp.guiControllers;

import java.io.IOException;
import java.nio.file.Files; // Para manejo de archivos
import java.nio.file.Path; // Para definir la ruta
import java.time.LocalDateTime; // Para el timestamp
import java.time.format.DateTimeFormatter; // Para formatear el timestamp
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import com.java.tp.App;
import com.java.tp.agency.Agency;
import com.java.tp.agency.places.Place;
import com.java.tp.agency.travels.Travel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class TopPlacesController {

    @FXML
    private javafx.scene.control.ListView<String> list;
    
    @FXML
    private javafx.scene.control.Label totalPlaces;

    /**
     * Método auxiliar para obtener la lista de destinos formateada.
     * @return Una lista de Strings con los destinos formateados ("ID - KM km").
     */
    private List<String> getDestinosFormateados() {
        TreeMap<String, Place> destinos = Agency.getInstancia().getDestinos();
        HashMap<String, Travel> viajes = Agency.getInstancia().getViajes();

        if (destinos == null || destinos.isEmpty()) {
            return List.of("No se pudieron cargar los destinos");
        }
        
        if (viajes == null || viajes.isEmpty()) {
            return List.of("No hay viajes cargados para evaluar destinos");
        }
        
        // HashMap para acumular km por destino
        HashMap<String, Float> kmPorDestino = new HashMap<>();
        
        // Recorrer todos los destinos del TreeMap
        for (String destinoId : destinos.keySet()) {
            float kmAcumulados = 0;
            
            // Recorrer todos los viajes
            for (Travel viaje : viajes.values()) {
                String viajeId = viaje.getId(); // Ej: "destino-1"
                
                // Extraer la parte del destino del ID del viaje (antes del guion)
                String destinoDelViaje = viajeId.substring(0, viajeId.lastIndexOf('-'));
                
                // Si el viaje corresponde a este destino, sumar los km
                if (destinoDelViaje.equals(destinoId)) {
                    kmAcumulados += viaje.getKmRec();
                }
            }
            
            // Almacenar el acumulado para este destino
            kmPorDestino.put(destinoId, kmAcumulados);
        }
        
        // Convertir a lista sin ordenar
        List<String> destinosFormateados = kmPorDestino.entrySet().stream()
            .map(entry -> entry.getKey() + " - " + String.format("$ %.2f", entry.getValue()))
            .collect(Collectors.toList());
        
        return destinosFormateados;
    }

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

    /** generador de reportes de destinos */
    @FXML
    private void generateReport() throws IOException {
        List<String> lineasReporte = getDestinosFormateados();
        
        // Verifica si el reporte contiene el mensaje de error
        if (lineasReporte.size() == 1 && lineasReporte.get(0).equals("No se pudieron cargar los destinos")) {
            System.out.println("No se puede generar el reporte: No hay datos de destinos cargados.");
            return;
        }

        // 1. Añadir encabezado al reporte
        lineasReporte.add(0, "--- REPORTE DE DESTINOS DE LA AGENCIA ---");
        
        // 2. Generar nombre de archivo único con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "Reporte_Destinos_" + timestamp + ".txt";
        
        // 3. Definir la ruta del archivo (se guardará en el directorio de ejecución del programa)
        Path rutaArchivo = Path.of("reports/" + nombreArchivo);
        
        try {
            // Escribir todas las líneas en el archivo. Esto lo crea si no existe.
            Files.write(rutaArchivo, lineasReporte);
            
            System.out.println("Reporte de destinos generado con éxito en: " + rutaArchivo.toAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte de destinos: " + e.getMessage());
            // Manejo de errores de escritura
        }
    }
}