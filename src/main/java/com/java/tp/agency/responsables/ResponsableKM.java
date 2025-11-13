package com.java.tp.agency.responsables;

import Agencia.dominio.Exceptions.ResponsableInvalidoException;
import Agencia.dominio.enums.Disponibilidad;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;


public class ResponsableKM {
    private String dni;
    private float KmRecorridos;

    public ResponsableKM(String dni, float KmRecorridos){
        this.dni=dni;
        this.KmRecorridos=KmRecorridos;
    }
    //getters
    public String getDni() {return dni;}
    public float getKmRecorridos() {return KmRecorridos;}
}