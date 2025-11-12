package Agencia.dominio.Vehiculos;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "colectivocc")
public class ColectivoCC extends Vehiculo{
    private float valPasCama;
    private  float valPas;

    public ColectivoCC(String patente,int capacidad,float velPerH){
        super(patente,capacidad,velPerH);
        setCapacidad(32); //pasajeros-cantCamas < 6, porque solo 6 plazas son comunes
    }
    @Override
    public float calculaCosto(float km, int pasajeros, int cantCamas) {
        return km*pasajeros*valPas+valPasCama*cantCamas;
    }
}