package Agencia.dominio;
import Agencia.dominio.Vehiculos.Vehiculo;
import Agencia.dominio.Viajes.CortaDis;
import Agencia.dominio.Viajes.LargaDis;
import Agencia.dominio.Viajes.Viaje;
import Agencia.dominio.enums.Disponibilidad;
import Agencia.dominio.Exceptions.*;

import java.util.*;
public class Agencia {
    private  static  Agencia instancia;
    private HashMap<String, Viaje> viajes = new HashMap<>();    //ordenado por id hashmap
    private TreeMap<String,Destino>destinos=new TreeMap<>();    //treemap por que el enunciado pide que esten ordenados alfabeticamente
    private HashMap<String, Vehiculo>vehiculos=new HashMap<>(); //ordenado por patente
    private HashMap<String,ResponsableABordo>responsables=new HashMap<>(); //ordenado por dni
    private HashMap<String,Integer>cantViajes= new HashMap<>();

    private Agencia() {
        System.out.println("Se creo la instancia Singleton de agencia");
    }

    public static Agencia getInstancia(){
        if (instancia==null)
            instancia=new Agencia();
        return instancia;
    }

    //getters
    public HashMap<String,ResponsableABordo> getResponsables() {return responsables;}
    public HashMap<String, Integer> getCantViajes() {return cantViajes;}
    public HashMap<String, Viaje> getViajes() {return viajes;}
    public TreeMap<String,Destino> getDestinos(){return destinos;}
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
    public HashMap<String, ResponsableABordo> ResponsablesDisponibles(){ //devuelve una lista con los responsables disponibles
        HashMap<String, ResponsableABordo> aux=new HashMap<>();
        for(ResponsableABordo r: responsables.values()){
            if(r.getEstado()== Disponibilidad.DISPONIBLE){
                aux.put(r.getDni(),r);
            }
        }
        if (aux.isEmpty()) {
            throw new SinResponsablesDisponiblesException("No hay responsables disponibles");
        }
        return aux;
    }

    public void crearViaje(String destino,String patVeh ,int pasajeros,float kmRec, TreeSet <String>res){
        Viaje viajenuevo;
        Destino desAct=destinos.get(destino);
        String id=creaIdViaje(destino);

        if(desAct.getKm()<100){//viaje corta
            viajenuevo =new CortaDis(id,patVeh,destino,pasajeros,kmRec);
        }else{
            if(res.isEmpty()){
                throw new SinResLargaDisException("la lista de responsables esta vacia");
            }
            viajenuevo =new LargaDis(id,patVeh,destino,pasajeros,kmRec,res);
        }
        viajes.put(id,viajenuevo);
        Vehiculo v=vehiculos.get(patVeh);
        v.setEstado(Disponibilidad.OCUPADO);
        for(String r: res){
            responsables.get(r).setEstado(Disponibilidad.OCUPADO);
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

    private String creaIdViaje(String destino){
        String numid= String.valueOf(cantViajes.get(destino));
        String id= destino +"-"+ numid;
        cantViajes.replace(destino, cantViajes.get(destino) + 1);
        return id;
    }

}