package com.java.tp.agency.reports;

import java.io.IOException;
import java.nio.file.Files; // Para manejo de archivos
import java.nio.file.Path; // Para definir la ruta
import java.time.LocalDateTime; // Para el timestamp
import java.time.format.DateTimeFormatter; // Para formatear el timestamp
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import com.java.tp.agency.Agency;
import com.java.tp.agency.places.Place;
import com.java.tp.agency.travels.Travel;

public class ReportPlaces {

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

// Clase para generar reportes de destinos
private void generateReport() throws IOException {
    List<String> lineasReporte = getDestinosFormateados();
        
    // Verifica si el reporte contiene el mensaje de error
    if (lineasReporte.size() == 1 && lineasReporte.get(0).equals("No se pudieron cargar los destinos")) {
        System.out.println("No se puede generar el reporte: No hay datos de destinos cargados.");
        return;
    }

    // Añadir encabezado al reporte
        lineasReporte.add(0, "--- REPORTE DE DESTINOS DE LA AGENCIA ---");
        
        // Generar nombre de archivo único con timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "Reporte_Destinos_" + timestamp + ".txt";
        
        // Definir la ruta del archivo (se guardará en el directorio de ejecución del programa)
        Path rutaArchivo = Path.of("reports/" + nombreArchivo);
        
        try {
            // Escribir todas las líneas en el archivo. Esto lo crea si no existe.
            Files.write(rutaArchivo, lineasReporte);
            
            System.out.println("Reporte de destinos generado con éxito en: " + rutaArchivo.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte de destinos: " + e.getMessage());
        }
    }
}       // Manejo de errores de escritura