package Agencia.dominio.Viajes;

import Agencia.dominio.Destino;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;

import java.util.HashMap;

public class CortaDis extends Viaje{

    public CortaDis(String id, String patVehiculo, String destino, int cPasajeros, float kmRec){
        super(id,patVehiculo,destino,cPasajeros,kmRec);
        //public Viaje(String inId,String inpatVehiculo ,String inDestino, int inCPasajeros,float inkmRec)
    }
    @Override
    public float  devuelveValorCalculado(Vehiculo vehiculo,Destino destino,HashMap<String,ResponsableABordo> responsableABordo, int cantPas, int cantCamas){

         return vehiculo.calculaCosto(destino.getKm(),cantPas,cantCamas);
    }
}