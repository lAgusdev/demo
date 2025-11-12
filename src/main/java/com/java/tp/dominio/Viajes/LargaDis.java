package Agencia.dominio.Viajes;

import Agencia.dominio.Agencia;
import Agencia.dominio.Destino;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;

import java.util.HashMap;
import java.util.TreeSet;


public class LargaDis extends Viaje{

    private TreeSet<String> dnisPerResponsables; //uso treeset para buscar con dni


    public LargaDis(String id,String patVehiculo, String destino, int cPasajeros, float kmRec, TreeSet<String> responsables){
        super(id,patVehiculo,destino,cPasajeros,kmRec);
        dnisPerResponsables=responsables;
    }

    public float calculaSueldos(){
        HashMap<String,ResponsableABordo> responsablesTotales= Agencia.getInstancia().getResponsables();
        float total=0;
        for (String dni: dnisPerResponsables){
            ResponsableABordo aux = responsablesTotales.get(dni);
            total+=aux.getSalario();
        }
        return total;
    }
    @Override
    public float devuelveValorCalculado(Vehiculo vehiculo, Destino destino, HashMap<String,ResponsableABordo> responsableABordo, int cantPas, int cantCamas){

        return vehiculo.calculaCosto(destino.getKm(),cantPas,cantCamas) + calculaSueldos();
    }

    public TreeSet<String> getPerResponsables() {
        return dnisPerResponsables;
    }
}
