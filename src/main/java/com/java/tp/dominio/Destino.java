package Agencia.dominio;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "destino") // Necesario para JAXB
public class Destino {
    private String id;
    private float km;
    private String Nombre; // Nota: Se recomienda minúscula para convención (nombre)

    public Destino() {
        // JAXB lo usa para crear la instancia antes de llamar a los setters.
    }

    @XmlElement
    public float getKm() {
        return km;
    }

    @XmlElement
    public String getNombre() {
        return Nombre;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String inid) {
        id = inid;
    }
    public void setKm(float inkm) {
        km = inkm;
    }
    public void setNombre(String innombre) {
        Nombre = innombre;
    }
}
