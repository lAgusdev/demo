package Agencia.dominio.Exceptions;

public class SinVehiculosDisponiblesException extends RuntimeException {
    public SinVehiculosDisponiblesException(String message) {
        super(message);
    }
}
