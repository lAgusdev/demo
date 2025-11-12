package Agencia.dominio.Vehiculos;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "auto")
public class Auto extends Vehiculo {
    private float valBase;
    private float valKm;
    public Auto(String patente,int capacidad,float velPerH){
        super(patente,capacidad,velPerH);
        setCapacidad(4);
    }

    @Override
    public float calculaCosto(float km, int cantpas, int cantCamas){
        return valBase + valKm * km;
    }

}

