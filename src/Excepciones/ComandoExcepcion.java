/*
 * 
 */
package Excepciones;

/**
 * Excepción en la ejecución de un comando
 * @author david.campos
 */
public class ComandoExcepcion extends Exception{

    /**
     *
     */
    public ComandoExcepcion() {
    }

    /**
     *
     * @param message
     */
    public ComandoExcepcion(String message) {
        super(message);
    }
}
