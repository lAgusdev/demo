package Agencia.dominio.Vehiculos;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "combi")
public class Combi extends Vehiculo {
    private float valBase;
    private float valPasajero;

    public Combi(String patente,int capacidad,float velPerH) {
        super(patente,capacidad,velPerH);
        setCapacidad(16);
    }

    @Override
    public float calculaCosto(float km, int pasajeros, int cantCamas) {
        return valBase + km * valPasajero * pasajeros;
    }

}