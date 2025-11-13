package com.java.tp.agency.dataController;
import com.java.tp.agency.Agency;
import com.java.tp.agency.places.Place;
import com.java.tp.agency.exceptions.*;
import com.java.tp.agency.responsables.Responsable;
import com.java.tp.agency.vehicles.*;
import com.java.tp.agency.travels.*;

import java.io.*;
import java.util.*;

import jakarta.xml.bind.*;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class DataController {
    private Agency agency; // atributo que guarda la instancia del dominio

    public DataController() {
        this.agency = Agency.getInstancia(); // singleton
    }

    public void crearViaje(String destino, String patVeh, int pasajeros, float kmRec, TreeSet<String> res) throws SinResLargaDisException {
        agency.crearViaje(destino, patVeh, pasajeros, kmRec, res);
    }

    public HashMap<String, Vehicles> VehiculosDisponibles() {
        return Agency.getInstancia().VehiculosDisponibles();
    }

    public HashMap<String, Responsable> ResponsablesDisponibles() {
        return Agency.getInstancia().ResponsablesDisponibles();
    }

    public void iniciaxml(){
        deserealizaResponsables();
        deserealizaDestinos();
        deserializaVehiculos();
        deserializaViajes();
    }
    private void deserealizaResponsables() {
        HashMap<String, Responsable> res = agency.getResponsables();
        try {
            JAXBContext contexto = JAXBContext.newInstance(Responsable.class); //crea el contexto para pasar de xml a obj
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/responsables.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && reader.getLocalName().equals("responsable")) {
                    try {
                        Responsable r = (Responsable) unmarshaller.unmarshal(reader);
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
            System.out.println("------ Carga completa, se cargaron: " + res.size() + " Responsables------");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }

    private void deserealizaDestinos() {
        TreeMap<String, Place> des = agency.getDestinos();
        try {
            JAXBContext contexto = JAXBContext.newInstance(Place.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/destinos.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && reader.getLocalName().equals("destino")) {
                    try {
                        Place d = (Place) unmarshaller.unmarshal(reader);
                        if (d.getId().isEmpty()) {
                            throw new DestinoInvalidoException("Id Vacio");
                        }
                        if (d.getKm() <= 0) {
                            throw new DestinoInvalidoException("Destino km negativo");
                        }

                        if (des.containsKey(d.getId())) {
                            throw new DestinoInvalidoException("Id repetido");
                        }
                        des.put(d.getId(), d);
                    } catch (Exception e) {
                        System.out.println("Destino inválido, " + e.getMessage());
                    }
                }
                reader.next();
            }
            reader.close();
            System.out.println("------ Carga completa, se cargaron: " + des.size() + " Destinos ------");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }

    private void deserializaVehiculos() {
        HashMap<String, Vehicles> veh = agency.getVehiculos();
        try {
            JAXBContext contexto = JAXBContext.newInstance(Car.class, MiniBus.class, BusSC.class, BusCC.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/vehiculos.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && (reader.getLocalName().equals("auto")||reader.getLocalName().equals("combi")||reader.getLocalName().equals("colectivoSC")||reader.getLocalName().equals("colectivoCC"))) {
                    try {
                        String tag = reader.getLocalName();
                        Vehicles v = null;
                        switch (tag) {
                            case "auto": v = (Car) unmarshaller.unmarshal(reader);break;
                            case "combi": v = (MiniBus) unmarshaller.unmarshal(reader);break;
                            case "colectivoSC": v = (BusSC) unmarshaller.unmarshal(reader);break;
                            case "colectivoCC": v = (BusCC) unmarshaller.unmarshal(reader);break;
                        }
                            if (v.getPatente().isEmpty()) {
                                throw new VehiculoInvalidoException("Patente vacía");
                            }
                            if (v.getVelPerH() <= 0) {
                                throw new VehiculoInvalidoException("Velocidad negativa o nula");
                            }
                            if (veh.containsKey(v.getPatente())) {
                                throw new VehiculoInvalidoException("Patente repetida");
                            }
                        switch (tag) {
                            case "auto" -> {
                                Car a = (Car) v;
                                if (a.getValKm() <= 0) {
                                    throw new VehiculoInvalidoException("Auto con valor por Km inválido");
                                }
                                if(a.getValBase()<=0){
                                    throw new VehiculoInvalidoException("Auto con valor base inválido");
                                }
                            }
                            case "combi" -> {
                                MiniBus c = (MiniBus) v;
                                if (c.getValPasajero() <= 0) {
                                    throw new VehiculoInvalidoException("Combi con valor por pasajero inválida");
                                }
                                if (c.getValBase()<=0){
                                    throw new VehiculoInvalidoException("Combi con valor base invalida");
                                }
                            }
                            case "colectivoSC" -> {
                                BusSC sc= (BusSC) v;
                                if (sc.getValPasajero()<= 0) {
                                    throw new VehiculoInvalidoException("Colectivo con valor por Pasajero inválido");
                                }
                            }
                            case "colectivoCC" ->{
                                BusCC cc=(BusCC) v;
                                if(cc.getValPasajeroAsiento()<=0){
                                    throw new VehiculoInvalidoException("Colectivo con valor por asiento invalido");
                                }
                                if (cc.getValPasajeroCama()<=0){
                                    throw new VehiculoInvalidoException("Colectivo con valor por cama invalido");
                                }
                            }

                        }
                        veh.put(v.getPatente(), v);
                    }catch (Exception e) {
                        System.out.println("vehiculo inválido, " + e.getMessage());
                    }

                }else{
                    reader.next();
                }
            }
            reader.close();
            System.out.println("------ Carga completa, se cargaron: " + veh.size() + " vehiculos ------");
        } catch (Exception e) {
            System.out.println("Error general al leer XML: " + e.getMessage());
        }
    }

    public void deserializaViajes(){
        HashMap<String, Travel> via = agency.getViajes();
        try {
            JAXBContext contexto = JAXBContext.newInstance(LongDistance.class,ShortDistance.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/viajes.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) { //while !eof
                if (reader.isStartElement() && (reader.getLocalName().equals("largaDis")|| reader.getLocalName().equals("cortaDis"))){
                    String tag = reader.getLocalName();
                    Travel v = null;
                    switch (tag){
                        case "largaDis":v = (LongDis) unmarshaller.unmarshal(reader);break;
                        case "cortaDis":v = (ShortDis) unmarshaller.unmarshal(reader);break;
                    }
                    v.setId(Agency.getInstancia().creaIdViaje(v.getIdDestino()));
                    v.setEstado(v.actualizaEstadoViaje(v.getIdDestino()));
                    via.put(v.getId(), v);
                }
                reader.next();
            }
            reader.close();
            System.out.println("------ Carga completa, se cargaron: " + via.size() + " Viajes ------");
        }catch (Exception e){
            System.out.println("Error general al leer XML:" + e.getMessage());
        }
    }
    public void muestraviajes(){
        agency.muestraViajes();
    }
    public void serializaViajes() {
        List<Travel> listaViajes = new ArrayList<>(agency.getViajes().values());
        try {
            JAXBContext contexto = JAXBContext.newInstance(Travel.class, LongDis.class, ShortDis.class);
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            sw.write("<viajes>\n");
            for (Travel viaje : listaViajes) {
                StringWriter viajeWriter = new StringWriter();
                marshaller.marshal(viaje, viajeWriter);
                String viajeXml = viajeWriter.toString();
                String contenido = viajeXml.substring(viajeXml.indexOf("?>") + 2).trim();
                sw.write("    " + contenido + "\n");
            }
            sw.write("</viajes>");
            File archivo = new File("src/data/viajes.xml");
            try (FileWriter fw = new FileWriter(archivo)) {
                fw.write(sw.toString());
            }
            System.out.println("------ Viajes serializados: " + listaViajes.size() + " viajes ------");
            System.out.println("Guardado en: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error al serializar viajes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}