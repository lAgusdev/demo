package Agencia.dominio;
import Agencia.dominio.Exceptions.ResponsableInvalidoException;
import Agencia.dominio.enums.Disponibilidad;
import Agencia.dominio.enums.EstadoViaje;
import jakarta.xml.bind.annotation.*;


@XmlRootElement(name = "responsable")
public class ResponsableABordo {

    private String nombre;

    private String dni;

    private float salario;
    @XmlTransient
    private Disponibilidad estado;

    public ResponsableABordo(String inDni, String inNombre,float inSalario){
        if(inDni.length()!=8 || !inDni.matches("[0-9]{8}")){
            throw new ResponsableInvalidoException("dni invalido");
        }
        if(inSalario<=0){
            throw new ResponsableInvalidoException("salario negativo");
        }
        salario=inSalario;
        dni = inDni;
        nombre = inNombre;
    }

    public ResponsableABordo() {this.estado=Disponibilidad.DISPONIBLE;}
    //getters
    @XmlElement
    public float getSalario() {return salario;}
    @XmlElement
    public String getDni(){return dni;}
    @XmlElement
    public String getNombre() {return nombre;}
    public Disponibilidad getEstado(){return estado;}
    //
    //setters
    public void setSalario(float salario) { this.salario = salario; }
    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    @XmlTransient
    public void setEstado(Disponibilidad disponibilidad){this.estado=disponibilidad;}
    //
}