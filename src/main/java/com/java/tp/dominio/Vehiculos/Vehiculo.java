package Agencia.dominio.Vehiculos;
import Agencia.dominio.Exceptions.VehiculoInvalidoException;
import Agencia.dominio.enums.Disponibilidad;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({Auto.class, Combi.class, ColectivoSC.class, ColectivoCC.class})
public abstract class Vehiculo {
    private String patente;
    private int capacidad; // no lo usa json
    private float velPerH; //velocidad promedio por hora
    private Disponibilidad estado; //enum no lo usa json

    public Vehiculo(String patente,int capacidad,float velPerH){ //constructor predef para json
        this.estado=Disponibilidad.DISPONIBLE; //un auto creado siempre esta disponible
        if(!patente.matches("[0-9]{3}[A-Z]{3}") || !patente.matches("[0-9]{2}[A-Z]{3}[0-9]{2}")){
            throw new VehiculoInvalidoException("patente invalida"); //estos errores se controlan para el json esto desde el crea viaje no se puede
        }
        if(capacidad<=0){
            throw new VehiculoInvalidoException("capacidad por debajo de 1");
        }
        if(velPerH<=0){
            throw new VehiculoInvalidoException("velocidad invalida");
        }
        this.capacidad=capacidad;
        this.patente=patente;
        this.velPerH=velPerH;
    }
    //getters
    public Disponibilidad getEstado() {return estado;}
    public String getPatente(){return patente;}
    //
    //setters
    public void setCapacidad(int capacidad) {this.capacidad = capacidad;}
    public void setEstado(Disponibilidad disponibilidad){this.estado=disponibilidad;}
    //

    public abstract float calculaCosto(float km, int pasajeros, int cantCamas);
    public void agregaVehiculo(){
        // se tiene que usar una funcion para agregar el vehiculo a la lista de vehiculos o lo hace el constructor?
        // recordar analizar si la patente ya existe debe tirar una ecxecion
    }

}

