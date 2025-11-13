package com.java.tp.guiControllers;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.stream.Collectors;

import com.java.tp.App;

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

    // --- Configuración Estática y Datos ---
    private final static java.util.Map<String, Integer> DISTANCIAS = java.util.Map.of(
        "Buenos Aires", 50,
        "Mar del Plata", 400,
        "Rosario", 350,
        "Pinamar", 90
    );

    private final static int UMBRAL_LARGA_DISTANCIA = 100;
    private final static ObservableList<String> VEHICULOS_LARGA = FXCollections.observableArrayList("Coche Cama", "Semi-Cama", "Combi");
    private final static ObservableList<String> VEHICULOS_CORTA = FXCollections.observableArrayList("Semi-Cama", "Auto", "Combi");

    private ObservableList<String> disponibles = FXCollections.observableArrayList(
        "Responsable A", "Responsable B", "Responsable C", "Responsable D"
    );
    private ObservableList<String> seleccionados = FXCollections.observableArrayList();


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
    private void crearViaje() throws IOException {
        String destino = destinoComboBox.getValue();
        String vehiculo = vehiculoComboBox.getValue();
        int pasajeros = pasajerosSpinner.getValue();
        int kmHechos = kmHechosSpinner.getValue();
        List<String> responsablesFinales = seleccionados.stream().collect(Collectors.toList()); 

        // 1. Validaciones
        if (destino == null || destino.isEmpty() || vehiculo == null || vehiculo.isEmpty()) {
            mostrarAlerta("Error de Selección", "Por favor, selecciona Destino y Vehículo.");
            return;
        }
        if (responsablesHBox.isManaged() && responsablesFinales.isEmpty()) {
            mostrarAlerta("Error de Selección", "Para viajes de larga distancia, debes seleccionar al menos un responsable.");
            return;
        }
        
        // 2. Resumen y Lógica de Negocio
        System.out.println("--- RESUMEN DEL VIAJE CREADO ---");
        System.out.println("Destino: " + destino);
        System.out.println("Pasajeros: " + pasajeros);
        System.out.println("Vehículo: " + vehiculo);
        System.out.println("Kilómetros Hechos: " + kmHechos);
        
        if (responsablesHBox.isManaged()) {
            System.out.println("Responsables: " + (responsablesFinales.isEmpty() ? "NINGUNO SELECCIONADO" : responsablesFinales));
        } else {
            System.out.println("Responsables: No Aplica (Corta Distancia)");
        }
        App.setRoot("menuPostNewTravel");
        mostrarAlerta("Viaje Creado", "¡El viaje a " + destino + " ha sido creado con éxito!");
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