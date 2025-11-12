package Agencia.dominio.Viajes;
import Agencia.dominio.Destino;
import Agencia.dominio.Agencia;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;
import Agencia.dominio.enums.EstadoViaje;

import java.util.*;


public  abstract class Viaje {
    private String id;//nombre; //formato "Ciudad -4
    private String patVehiculo;
    private String idDestino; //se cambio para no duplicar los datos
    private int cPasajeros; //cantidad de pasajeros a bordo del viaje
    private EstadoViaje estado;
    private float kmRec;
    public  Viaje(){ //constructor predef para json
    }

    public Viaje(String inId,String inpatVehiculo ,String inDestino, int inCPasajeros,float inkmRec){
        id = inId;
        patVehiculo=inpatVehiculo;
        idDestino = inDestino;
        cPasajeros = inCPasajeros;
        kmRec = inkmRec;
        estado=actualizaEstadoViaje(inDestino);
    }
    //getters
    public float getKmRec() {
        return kmRec;
    }
    public String getId() {
        return id;
    }
    //
    public EstadoViaje actualizaEstadoViaje(String idDestino){
        TreeMap<String,Destino> aux= Agencia.getInstancia().getDestinos();//traigo el hashmap
        Destino destinoaux=aux.get(idDestino);
        if(destinoaux.getKm()== kmRec){
            return  EstadoViaje.FINALIZADO;
        } else if (destinoaux.getKm()==0) {
            return EstadoViaje.PENDIENTE;
        }else {
            return EstadoViaje.EN_CURSO;
        }

    }

    public  TreeSet<String> getPerResponsables() {// Devuelve un TreeSet vacío
        return new TreeSet<>();
    }



    public abstract float devuelveValorCalculado(Vehiculo vehiculo, Destino destino, HashMap<String,ResponsableABordo> responsableABordo, int cantPas, int cantCamas);
}
