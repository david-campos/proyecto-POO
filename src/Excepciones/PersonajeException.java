package Excepciones;

/**
 * Excepción genérica referida a un personaje
 * @author crist
 */
public abstract class PersonajeException extends Exception{

    public PersonajeException() {
        super();
    }

    public PersonajeException(String message) {
        super(message);
    }
    
}
