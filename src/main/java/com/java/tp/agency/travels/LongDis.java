package com.java.tp.agency.travels;

import Agencia.dominio.Agencia;
import Agencia.dominio.Destino;
import Agencia.dominio.Exceptions.CamasLargaDisException;
import Agencia.dominio.ResponsableABordo;
import Agencia.dominio.Vehiculos.Vehiculo;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.TreeSet;

@XmlRootElement(name = "largaDis")
public class LongDis extends Travel{

    private TreeSet<String> dnisPerResponsables; //uso treeset para buscar con dni

    public LongDis(){
        super();
    }

    public LongDis(String id,String patVehiculo, String destino, int cPasajeros, float kmRec, TreeSet<String> responsables){
        super(id,patVehiculo,destino,cPasajeros,kmRec);
        dnisPerResponsables=responsables;
        for (String dni : dnisPerResponsables) {
            Agency.getInstancia().ActualizaResKm(dni, kmRec);
        }
    }
    //Getter
    @XmlElement
    public TreeSet<String> getDnisPerResponsables() {return dnisPerResponsables;}
    //
    //Setters
    public void setDnisPerResponsables(TreeSet<String> dnisPerResponsables) {this.dnisPerResponsables = dnisPerResponsables;}
    //
    public float calculaSueldos(){
        HashMap<String,Responsable> responsablesTotales= Agency.getInstancia().getResponsables();
        float total=0;
        for (String dni: dnisPerResponsables){
            Responsable aux = responsablesTotales.get(dni);
            total+=aux.getSalario();
        }
        return total;
    }
    @Override
    public float devuelveValorCalculado(Vehiculo vehiculo, Destino destino, HashMap<String,Responsable> responsableABordo, int cantPas, int cantCamas){
        if (cantPas-cantCamas>=6){
            throw new CamasLargaDisException("No hay tantos asientos sin cama disponibles");
        }
        return vehiculo.calculaCosto(destino.getKm(),cantPas,cantCamas) + calculaSueldos();
    }

    public TreeSet<String> getPerResponsables() {
        return dnisPerResponsables;
    }
}
