package com.java.tp.guiControllers;

import java.io.IOException;
import java.nio.file.Files; // Para manejo de archivos
import java.nio.file.Path; // Para definir la ruta
import java.time.LocalDateTime; // Para el timestamp
import java.time.format.DateTimeFormatter; // Para formatear el timestamp
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

    /**
     * Método auxiliar para obtener la lista de destinos formateada, ordenada
     * y con el número de puesto (ranking) al inicio.
     * @return Una lista de Strings con los destinos formateados ("#Puesto - ID - KM km").
     */
    private List<String> getDestinosFormateados() {
        TreeMap<String, Place> destinos = Agency.getInstancia().getDestinos();
        
        if (destinos == null || destinos.isEmpty()) {
            return List.of("No se pudieron cargar los destinos");
        } else {
            // 1. Obtener la lista de destinos formateada y ordenada (base)
            List<String> destinosOrdenados = destinos.values().stream()
                .map(p -> p.getId() + " - " + p.getKm() + " km")
                .sorted() 
                .collect(Collectors.toList());

            List<String> listaConPuesto = new ArrayList<>();
            
            // 2. Iterar sobre la lista ordenada para añadir el número de puesto (ranking)
            for (int i = 0; i < destinosOrdenados.size(); i++) {
                // Se añade el número de posición (i + 1). Ejemplo: "#1 - Buenos Aires - 1200 km"
                String linea = String.format("#%d - %s", (i + 1), destinosOrdenados.get(i)); 
                listaConPuesto.add(linea);
            }
            
            return listaConPuesto;
        }
    }

    @FXML
    private void initialize() {
        try {
            List<String> lista = getDestinosFormateados();
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