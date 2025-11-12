package Agencia.dominio.Vehiculos;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "colectivosc")
public class ColectivoSC extends Vehiculo{
    private float valPas;

    public ColectivoSC(String patente,int capacidad,float velPerH){
        super(patente,capacidad,velPerH);
        setCapacidad(40);
    }

    @Override
    public float calculaCosto(float km, int cantPas, int cantCamas){
        return valPas*km*cantPas;
    }
}
