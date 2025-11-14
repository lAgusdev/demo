package com.java.tp.guiControllers;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import com.java.tp.agency.Agency;
import com.java.tp.agency.places.Place;
import com.java.tp.agency.dataController.DataController;
import com.java.tp.agency.responsables.Responsable;
import com.java.tp.agency.vehicles.Vehicles;
import com.java.tp.App;
import java.util.TreeSet;
public class NewTravelController {

    // Componentes de la UI inyectados desde FXML
    @FXML private ComboBox<String> destinoComboBox;
    @FXML private ComboBox<String> vehiculoComboBox; 
    @FXML private Spinner<Integer> pasajerosSpinner;
    @FXML private Spinner<Integer> kmHechosSpinner; 
    
    // Componentes de Responsables (Doble ListView)
    @FXML private HBox responsablesHBox;
    @FXML private ListView<String> responsablesDisponiblesListView;
    @FXML private ListView<String> responsablesSeleccionadosListView;
    @FXML private Button agregarResponsableButton;
    @FXML private Button quitarResponsableButton;
    @FXML private Label responsablesSectionLabel; 

    @FXML private final static Map<String, Integer> DISTANCIAS;

    private final static int UMBRAL_LARGA_DISTANCIA = 100;

    private ObservableList<String> disponibles = FXCollections.observableArrayList(
        "Responsable A", "Responsable B", "Responsable C", "Responsable D"
    );
    private ObservableList<String> seleccionados = FXCollections.observableArrayList();

    static {
        DISTANCIAS = getDistanciasTreeMap();
    }

    private static TreeMap<String, Integer> getDistanciasTreeMap() {
        List<Place> destinos = Agency.getInstancia().getDestinos().values().stream().collect(Collectors.toList());
        TreeMap<String, Integer> distanciasMap = new TreeMap<>();
                
        for (Object item : destinos) {
            try {

                Place destino = (Place) item;
                
                String nombreDestino = destino.getId(); 
                float kmDestino = destino.getKm();      
                distanciasMap.put(nombreDestino, (int) kmDestino);
            } catch (ClassCastException e) {
                // Manejar el error si el objeto retornado no es del tipo esperado
                System.err.println("Error de casting en getDistanciasTreeMap: Asegúrate de que los objetos devueltos por getDestinos() son del tipo correcto y tienen getId() y getKm().");
                // Podrías lanzar una excepción o simplemente ignorar este objeto.
            } catch (Exception e) {
                System.err.println("Error al obtener datos de destino: " + e.getMessage());
            }
        }
        

        return distanciasMap;
    }


    @FXML
    // Definición de las capacidades conocidas y sus tipos
    private final static int CAPACIDAD_AUTO = 4;
    private final static int CAPACIDAD_MINIBUS = 16;
    private final static int CAPACIDAD_SEMICAMA = 40;
    private final static int CAPACIDAD_COCHECAMA = 32;

    // Mapa para clasificar los vehículos que pueden hacer Larga Distancia
    // Basado en el ejemplo anterior ("Coche Cama", "Semi-Cama", "Combi" o Minibus)
    private final static List<Integer> CAPACIDADES_LARGA = List.of(
        CAPACIDAD_MINIBUS,     
        CAPACIDAD_SEMICAMA,    
        CAPACIDAD_COCHECAMA    
    );
    
    // Mapa para clasificar los vehículos que pueden hacer Corta Distancia
    private final static List<Integer> CAPACIDADES_CORTA = List.of(
        CAPACIDAD_AUTO,        
        CAPACIDAD_MINIBUS,     
        CAPACIDAD_SEMICAMA     
    );

    // Listas finales llenadas con PATENTES
    private final static ObservableList<String> VEHICULOS_LARGA; 
    private final static ObservableList<String> VEHICULOS_CORTA; 

    // Bloque de inicialización estático
    static {
        ObservableList<String> largaTemp = FXCollections.observableArrayList();
        ObservableList<String> cortaTemp = FXCollections.observableArrayList();

        // 1. Obtener todos los vehículos disponibles
        // Asumo que getVehiculosDisponibles() devuelve List<Vehiculo>
        List<Vehicles> vehiculos = Agency.getInstancia().VehiculosDisponibles().values().stream().collect(Collectors.toList()); 

        // 2. Iterar y clasificar por capacidad
        for (Vehicles vehiculo : vehiculos) {
            String patente = vehiculo.getPatente(); // Necesitas getPatente()
            int capacidad = vehiculo.getCapacidad(); // Necesitas getCapacidad()

            // Clasificación por Larga Distancia
            if (CAPACIDADES_LARGA.contains(capacidad)) {
                largaTemp.add(patente);
            }
            
            // Clasificación por Corta Distancia
            if (CAPACIDADES_CORTA.contains(capacidad)) {
                cortaTemp.add(patente);
            }
        }

        // Asignación final
        VEHICULOS_LARGA = largaTemp;
        VEHICULOS_CORTA = cortaTemp;
    }
    @FXML
    private List<String> obtenerResponsablesDisponibles() {
        
        // 1. Obtener la colección de objetos Responsable del HashMap (usando .values()).
        java.util.Collection<Responsable> responsables = 
            Agency.getInstancia().ResponsablesDisponibles().values();
        
        // 2. Usar Streams para mapear cada objeto a un String formateado y colectarlos en una List<String>.
        List<String> listaEstandar = responsables.stream()
            // Mapea cada objeto Responsable a un String "Nombre (DNI)"
            .map(r -> String.format("%s (%s)", r.getNombre(), r.getDni()))
            // Colecta los Strings resultantes en una List<String>
            .collect(Collectors.toList());
        
        return listaEstandar;
    }
    @FXML
    public void initialize() {
        // --- CONFIGURACIÓN DE DESTINO ---
        destinoComboBox.setItems(FXCollections.observableArrayList(DISTANCIAS.keySet()));
        destinoComboBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> actualizarOpcionesDeViaje(newValue)
        );

        // --- CONFIGURACIÓN DE PASAJEROS ---
        pasajerosSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));
        
        // --- CONFIGURACIÓN DE KILÓMETROS (CON EDICIÓN POR TECLADO) ---
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0);
        
        kmHechosSpinner.setValueFactory(factory);
        kmHechosSpinner.setEditable(true); // Habilitar la edición por teclado
        
        // Listener para validar y aplicar la entrada del teclado
        kmHechosSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value >= factory.getMin() && value <= factory.getMax()) {
                        factory.setValue(value);
                    } else {
                        // Si está fuera de rango, revertimos o limpiamos
                        if (!newValue.isEmpty()) kmHechosSpinner.getEditor().setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    // Si el texto está vacío, lo ignoramos. Si es inválido, revertimos.
                    if (!newValue.isEmpty()) {
                        kmHechosSpinner.getEditor().setText(oldValue);
                    }
                }
            } else {
                // Si la entrada contiene caracteres no numéricos, revertimos
                kmHechosSpinner.getEditor().setText(oldValue);
            }
        });
        
        // --- CONFIGURACIÓN DE VEHÍCULOS (ComboBox) ---
        vehiculoComboBox.setItems(VEHICULOS_CORTA);
        vehiculoComboBox.getSelectionModel().select("Auto");
        
        
        // --- CONFIGURACIÓN DE RESPONSABLES ---
        List<String> responsablesEstandar = obtenerResponsablesDisponibles();
        
        disponibles = FXCollections.observableArrayList(responsablesEstandar);
        responsablesDisponiblesListView.setItems(disponibles);
        responsablesSeleccionadosListView.setItems(seleccionados);
        responsablesDisponiblesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        responsablesSeleccionadosListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Ocultar la sección de responsables al inicio
        responsablesHBox.setVisible(false);
        responsablesHBox.setManaged(false);
        if (responsablesSectionLabel != null) {
            responsablesSectionLabel.setVisible(false);
            responsablesSectionLabel.setManaged(false);
        }
    }
    
    /**
     * Lógica de Distancia: Oculta/Muestra Responsables y actualiza Vehículos
     */
    private void actualizarOpcionesDeViaje(String destino) {
        if (destino == null) return;
        
        int distancia = DISTANCIAS.getOrDefault(destino, 0);
        boolean esLargaDistancia = distancia >= UMBRAL_LARGA_DISTANCIA;

        // 1. LÓGICA DE RESPONSABLES (Ocultar/Mostrar el HBox completo)
        responsablesHBox.setVisible(esLargaDistancia);
        responsablesHBox.setManaged(esLargaDistancia);
        if (responsablesSectionLabel != null) {
            responsablesSectionLabel.setVisible(esLargaDistancia);
            responsablesSectionLabel.setManaged(esLargaDistancia);
        }

        // 2. LÓGICA DE VEHÍCULOS (Actualiza las opciones del ComboBox)
        actualizarVehiculos(esLargaDistancia);
    }
    
    /**
     * Configura el ComboBox de Vehículos basado en la distancia
     */
    private void actualizarVehiculos(boolean esLargaDistancia) {
        ObservableList<String> opcionesVehiculos = esLargaDistancia ? VEHICULOS_LARGA : VEHICULOS_CORTA;
        vehiculoComboBox.setItems(opcionesVehiculos);
        
        // Mantiene la selección si es posible, sino elige el primero
        String currentSelection = vehiculoComboBox.getValue();
        if (currentSelection != null && opcionesVehiculos.contains(currentSelection)) {
            vehiculoComboBox.getSelectionModel().select(currentSelection);
        } else {
            vehiculoComboBox.getSelectionModel().selectFirst();
        }
    }

    // --- Métodos para mover responsables entre listas ---

    @FXML
    private void agregarResponsable() {
        ObservableList<String> selected = responsablesDisponiblesListView.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty()) {
            seleccionados.addAll(selected);
            disponibles.removeAll(selected);
            responsablesDisponiblesListView.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void quitarResponsable() {
        ObservableList<String> selected = responsablesSeleccionadosListView.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty()) {
            disponibles.addAll(selected);
            seleccionados.removeAll(selected);
            responsablesSeleccionadosListView.getSelectionModel().clearSelection();
        }
    }

    // --- Método de Acción principal ---

    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    public static int getCapacidadDeVehiculoSeleccionado(String patenteSeleccionada) {
        if (patenteSeleccionada == null || patenteSeleccionada.isEmpty()) {
            return -1;
        }   
        List<Vehicles> vehiculos = Agency.getInstancia().VehiculosDisponibles().values().stream().collect(Collectors.toList());
        for (Vehicles vehiculo : vehiculos) {
            if (patenteSeleccionada.equals(vehiculo.getPatente())) {
                return vehiculo.getCapacidad(); 
            }
        }
        return -1; 
    }
    @FXML private final DataController dataController = new DataController();
    @FXML
    private void crearViaje() throws IOException {
        String destino = destinoComboBox.getValue();
        String vehiculo = vehiculoComboBox.getValue();
        int pasajeros = pasajerosSpinner.getValue();
        int kmHechos = kmHechosSpinner.getValue();
        
        // El Stream convierte los Strings "Nombre (DNI)" a un TreeSet de solo los DNIs
        TreeSet<String> responsablesDNI = seleccionados.stream()
            .map(r -> r.substring(r.indexOf('(') + 1, r.indexOf(')'))) 
            .collect(Collectors.toCollection(TreeSet::new));

        // 1. Validaciones
        if (destino == null || destino.isEmpty() || vehiculo == null || vehiculo.isEmpty()) {
            mostrarAlerta("Error de Selección", "Por favor, selecciona Destino y Vehículo.");
            return;
        }
        if(getCapacidadDeVehiculoSeleccionado(vehiculo)< pasajeros){
            mostrarAlerta("Error de Capacidad", "El vehículo seleccionado no tiene capacidad suficiente para los pasajeros.");
            return;
        }
        if (responsablesHBox.isManaged() && responsablesDNI.isEmpty()) { // Uso responsablesDNI
            mostrarAlerta("Error de Selección", "Para viajes de larga distancia, debes seleccionar al menos un responsable.");
            return;
        }
        
        // 2. Lógica de Negocio y Persistencia
        
        // Crear el viaje en la Agency
        try {
            Agency.getInstancia().crearViaje(destino, vehiculo, pasajeros, (float) kmHechos, responsablesDNI);
            
            // 3. SERIALIZAR LOS VIAJES CREADOS
            dataController.serializaViajes(); // <-- LLAMADA PARA GUARDAR EL CAMBIO
            
            App.setRoot("menuPostNewTravel");
            mostrarAlerta("Viaje Creado", "¡El viaje a " + destino + " ha sido creado con éxito!");

        } catch (com.java.tp.agency.exceptions.SinResLargaDisException e) {
            mostrarAlerta("Error de Negocio", "Error al crear viaje: " + e.getMessage());
            // O manejar la excepción si Agency.crearViaje lanza otras (ej. VehiculoNoDisponibleException)
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado al guardar el viaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}