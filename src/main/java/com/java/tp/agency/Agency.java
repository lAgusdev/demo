package com.java.tp.agency;
import Agencia.dominio.Vehiculos.Vehiculo;
import Agencia.dominio.Viajes.CortaDis;
import Agencia.dominio.Viajes.LargaDis;
import Agencia.dominio.Viajes.Viaje;
import Agencia.dominio.enums.Disponibilidad;
import Agencia.dominio.Exceptions.*;

import java.util.*;
public class Agency {
    private  static  Agency instancia;
    private HashMap<String,Viaje> viajes = new HashMap<>();    //ordenado por id hashmap
    private TreeMap<String,Place>destinos=new TreeMap<>();    //treemap por que el enunciado pide que esten ordenados alfabeticamente
    private HashMap<String,Vehiculo>vehiculos=new HashMap<>(); //ordenado por patente
    private HashMap<String,Responsable>responsables=new HashMap<>(); //ordenado por dni
    private HashMap<String,Integer>cantViajes= new HashMap<>();
    private HashMap<String,Float>resKm= new HashMap<>(); //Hashmap para acumular, List para mostrar

    private Agency() {
        System.out.println("Se creo la instancia Singleton de agencia");
    }

    public static Agency getInstancia(){
        if (instancia==null)
            instancia=new Agency();
        return instancia;
    }

    public void ActualizaResKm(String dniResponsableABordo, float kmRecorridos){
        kmRecorridos+= resKm.getOrDefault(dniResponsableABordo, 0.0f);
        resKm.put(dniResponsableABordo, kmRecorridos); //remplaza el viejo por el nuevo
    }

    public ArrayList<ResponsableKM> obtenerRankingPorKm(HashMap<String, Float> resKm) {
        ArrayList<ResponsableKM> Ranking = new ArrayList<>();
        for (HashMap.Entry<String,Float> objeto : resKm.entrySet()) {
            String dni = objeto.getKey();
            Float km = objeto.getValue();
            ResponsableKM resumen = new ResponsableKM(dni, km);
            Ranking.add(resumen);
        }
        Ranking.sort(Comparator.comparing(ResponsableKM::getKmRecorridos).reversed());
        return Ranking;
    }

    //getters
    public HashMap<String,Responsable> getResponsables() {return responsables;}
    public HashMap<String, Integer> getCantViajes() {return cantViajes;}
    public HashMap<String, Viaje> getViajes() {return viajes;}
    public TreeMap<String,Place> getDestinos(){return destinos;}
    public HashMap<String,Vehiculo> getVehiculos(){return vehiculos;}
    //fin getter

    public HashMap<String, Vehiculo> VehiculosDisponibles(){ //devuelve una lista con los vehiculos disponibles
        HashMap<String, Vehiculo> aux=new HashMap<>();
        for(Vehiculo v: vehiculos.values()){
            if(v.getEstado()== Disponibilidad.DISPONIBLE){
                aux.put(v.getPatente(),v);
            }
        }
        if (aux.isEmpty()) {
            throw new SinVehiculosDisponiblesException("No hay vehículos disponibles");
        }
        return aux;

    }
    public HashMap<String, Responsable> ResponsablesDisponibles(){ //devuelve una lista con los responsables disponibles
        HashMap<String, Responsable> aux=new HashMap<>();
        for(Responsable r: responsables.values()){
            if(r.getEstado()== Disponibilidad.DISPONIBLE){
                aux.put(r.getDni(),r);
            }
        }
        if (aux.isEmpty()) {
            throw new SinResponsablesDisponiblesException("No hay responsables disponibles");
        }
        return aux;
    }
    public void muestraViajes(){
        for(Viaje v: viajes.values()){
            System.out.println(v.getId()+" || "+v.getIdDestino() + " || " + v.getEstado());
        }
    }
    public void crearViaje(String destino,String patVeh ,int pasajeros,float kmRec, TreeSet <String>res){
        Viaje viajenuevo;
        Place desAct=destinos.get(destino);
        String id=creaIdViaje(destino);
        System.out.println("=== DEBUG crearViaje ===");
        if(desAct.getKm()<100){//viaje corta
            viajenuevo =new CortaDis(id,patVeh,destino,pasajeros,kmRec);
            System.out.println("=== viaje corta ===");
        }else{
            if(res.isEmpty()){
                throw new SinResLargaDisException("la lista de responsables esta vacia");
            }
            viajenuevo =new LargaDis(id,patVeh,destino,pasajeros,kmRec,res);
        }

        viajes.put(id,viajenuevo);
        Vehiculo v=vehiculos.get(patVeh);
        v.setEstado(Disponibilidad.OCUPADO);
        if (res != null) {
            for (String r : res) {
                responsables.get(r).setEstado(Disponibilidad.OCUPADO);
            }
        }
    }
    
    public void inciaCV(){// el contador de destinos empieza en 1 osea "Mardelplata-1" "Mardelplata-2"...
        String destinoaux;
        for(Viaje v: viajes.values()){
            destinoaux=v.getId();
            if(!cantViajes.containsKey(destinoaux)){ //no contiene el destino
                cantViajes.put(destinoaux,1);
                System.out.println("se creo el destino :"+destinoaux);
            }else{ //contiene el destino
                cantViajes.put(destinoaux, cantViajes.get(destinoaux) + 1);
            }
        }
    }

    public String creaIdViaje(String destino){
        String id,numid;
        if (cantViajes.containsKey(destino)){
            cantViajes.replace(destino,cantViajes.get(destino) + 1);
            numid= String.valueOf(cantViajes.get(destino));
        }else{
            cantViajes.put(destino,1);
            numid ="1";
        }
        id= destino +"-"+ numid;
        return id;
    }
}