package com.java.tp.agency.travels;
import Agencia.dominio.Destino;
import Agencia.dominio.Agencia;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;
import Agencia.dominio.enums.EstadoViaje;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;

import java.util.*;
@XmlSeeAlso({LongDis.class,ShortDis.class})
public  abstract class Travel {
    private String id;//nombre; //formato "Ciudad -4
    private String patVehiculo;
    private String idDestino; //se cambio para no duplicar los datos
    private int cPasajeros; //cantidad de pasajeros a bordo del viaje
    private EstadoViaje estado;
    private float kmRec;
    public  Travel(){ //constructor predef para json
    }

    public Travel(String inId,String inpatVehiculo ,String inDestino, int inCPasajeros,float inkmRec){
        id = inId;
        patVehiculo=inpatVehiculo;
        idDestino = inDestino;
        cPasajeros = inCPasajeros;
        kmRec = inkmRec;
        estado=actualizaEstadoViaje(inDestino);
    }
    //getters
    @XmlElement
    public float getKmRec() {return kmRec;}
    @XmlTransient
    public String getId() {return id;}
    @XmlElement
    public String getPatVehiculo() {return patVehiculo;}
    @XmlElement
    public int getcPasajeros() {return cPasajeros;}
    @XmlElement
    public String getIdDestino() {return idDestino;}
    @XmlTransient
    public EstadoViaje getEstado() {return estado;}
    //
    //Setters //los setters los usa el jaxb
    public void setId(String id) {this.id = id;}
    public void setcPasajeros(int cPasajeros) {this.cPasajeros = cPasajeros;}
    public void setEstado(EstadoViaje estado) {this.estado = estado;}
    public void setIdDestino(String idDestino) {this.idDestino = idDestino;}
    public void setKmRec(float kmRec) {this.kmRec = kmRec;}
    public void setPatVehiculo(String patVehiculo) {this.patVehiculo = patVehiculo;}
    //
    public EstadoViaje actualizaEstadoViaje(String idDestino){
        TreeMap<String,Destino> aux= Agency.getInstancia().getDestinos();//traigo el hashmap
        Destino destinoaux=aux.get(idDestino);
        if(destinoaux.getKm()== kmRec){
            return  EstadoViaje.FINALIZADO;
        } else if (kmRec==0) {
            return EstadoViaje.PENDIENTE;
        }else {
            return EstadoViaje.EN_CURSO;
        }
    }

    public  TreeSet<String> getPerResponsables() {// Devuelve un TreeSet vacío
        return new TreeSet<>();
    }



    public abstract float devuelveValorCalculado(Vehiculo vehiculo, Destino destino, HashMap<String,Responsable> responsableABordo, int cantPas, int cantCamas);
}
