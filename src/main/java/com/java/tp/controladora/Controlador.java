package Agencia.controladora;
import Agencia.dominio.Agencia;
import Agencia.dominio.Destino;
import Agencia.dominio.Exceptions.ResponsableInvalidoException;
import Agencia.dominio.Exceptions.SinResLargaDisException;
import Agencia.dominio.Exceptions.DestinoInvalidoException;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.*;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;


public class Controlador {
    private Agencia agencia; // atributo que guarda la instancia del dominio

    public Controlador() {
        this.agencia = Agencia.getInstancia(); // singleton
    }

    public void crearViaje(String destino, String patVeh, int pasajeros, float kmRec, TreeSet<String> res) throws SinResLargaDisException {
        agencia.crearViaje(destino, patVeh, pasajeros, kmRec, res);
    }

    public HashMap<String, Vehiculo> VehiculosDisponibles() {
        return Agencia.getInstancia().VehiculosDisponibles();
    }

    public HashMap<String, ResponsableABordo> ResponsablesDisponibles() {
        return Agencia.getInstancia().ResponsablesDisponibles();
    }

    public void deserealizaResponsables() {
        HashMap<String, ResponsableABordo> res = agencia.getResponsables();
        try {
            JAXBContext contexto = JAXBContext.newInstance(ResponsableABordo.class); //crea el contexto para pasar de xml a obj
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/responsables.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && reader.getLocalName().equals("responsable")) {
                    try {
                        ResponsableABordo r = (ResponsableABordo) unmarshaller.unmarshal(reader);
                        if (r.getNombre().isEmpty()) {
                            throw new ResponsableInvalidoException("Nombre Vacio");
                        }
                        if (!r.getDni().matches("[0-9]{8}")) {
                            throw new ResponsableInvalidoException("Dni Invalido");
                        }
                        if (r.getSalario() < 0) {
                            throw new ResponsableInvalidoException("Salario negativo");
                        }
                        if (res.containsKey(r.getDni())) {
                            throw new ResponsableInvalidoException("Dni repetido");
                        }
                        res.put(r.getDni(), r);
                    } catch (Exception e) {
                        System.out.println("Responsable inválido," + e.getMessage());
                    }
                }
                reader.next();
            }
            reader.close();
            System.out.println("Carga completa, se cargaron: " + res.size() + " datos");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }

    public void deserealizaDestinos() {
        TreeMap<String, Destino> res = agencia.getDestinos();
        int contador = 0;
        try {
            JAXBContext contexto = JAXBContext.newInstance(Destino.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();

            InputStream is = getClass().getClassLoader().getResourceAsStream("data/destinos.xml");
            if (is == null) {
                System.out.println("Error: No se encontró el archivo data/destinos.xml");
                return;
            }
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext() && !reader.isStartElement()) {
                reader.next();
            }
            if (reader.isStartElement() && reader.getLocalName().equals("destinos")) {
                reader.next();
            }
            while (reader.hasNext()) {
                if (reader.isStartElement() && reader.getLocalName().equals("destino")) {
                    try {
                        Destino r = (Destino) unmarshaller.unmarshal(reader);
                        if (r.getNombre() == null || r.getNombre().trim().isEmpty()) {
                            throw new DestinoInvalidoException("Nombre Vacío");
                        }
                        if (r.getKm() < 0) {
                            throw new DestinoInvalidoException("Distancia negativa: " + r.getKm());
                        }
                        if (res.containsKey(r.getId())) {
                            throw new DestinoInvalidoException("ID repetido: " + r.getId());
                        }
                        res.put(r.getId(), r);
                        contador++;
                    } catch (Exception e) {
                        System.out.println("Destino inválido: " + e.getMessage());
                        while (reader.hasNext() && !(reader.isEndElement() && reader.getLocalName().equals("destino"))) {
                            reader.next();
                        }
                        if (reader.hasNext()) {
                            reader.next();
                        }
                    }
                } else {
                    reader.next();
                }
            }

            reader.close();
            is.close();
            System.out.println("Carga completa. Se cargaron: " + contador + " destinos válidos.");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }

    private void deserializaVehiculos() {
        HashMap<String, Vehiculo> res = agencia.getVehiculos();
        try {
            JAXBContext contexto = JAXBContext.newInstance(Auto.class, Combi.class, ColectivoSC.class, ColectivoCC.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();

            //verifica archivo
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/vehiculos.xml");
            if (is == null) {
                System.out.println("Error: No se encontró el archivo vehiculos.xml");
                return;
            }

            //lee el archivo
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && reader.getLocalName().equals("vehiculo")) { //o "combi" || "auto" || ...
                    try {
                        Vehiculo v = (Vehiculo) unmarshaller.unmarshal(reader);
                        if (v.getPatente() == null || v.getPatente().trim().isEmpty()) {
                            throw new DestinoInvalidoException("Patente Vacía");
                        }
                        //por si hace algo especifico por cada uno, si detecta que es combi o auto por ej hacer if
                       /* switch (v) {
                            case Auto a -> {
                                agencia.agregarVehiculo(a); //CREAR FUNCION DE AGREGARVEHICULO
                            }
                            default -> System.out.println("Tipo de vehículo desconocido");
                        }*/
                    } catch (Exception e) {
                        System.out.println("Vehiculo inválido: " + e.getMessage());
                    }
                }
                reader.next();
            }
            reader.close();
            System.out.println("Carga completa, se cargaron: " + res.size() + " datos");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }
}