package com.java.tp.agency.travels;

import Agencia.dominio.Destino;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
@XmlRootElement(name= "cortaDis")
public class ShortDis extends Travel{

    public ShortDis(){
        super();
    }
    public ShortDis(String id, String patVehiculo, String destino, int cPasajeros, float kmRec){
        super(id,patVehiculo,destino,cPasajeros,kmRec);
        //public Viaje(String inId,String inpatVehiculo ,String inDestino, int inCPasajeros,float inkmRec)
    }
    @Override
    public float  devuelveValorCalculado(Vehiculo vehiculo,Destino destino,HashMap<String,Responsable> responsableABordo, int cantPas, int cantCamas){

         return vehiculo.calculaCosto(destino.getKm(),cantPas,cantCamas);
    }
}