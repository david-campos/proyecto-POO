package Excepciones;

/**
 * Excepción lanzada por el cargador
 * @author crist
 */
public class CargadorException extends Exception {
    
    /**
     * Crea una nueva excepción de cargador
     */
    public CargadorException() {        
        super();
    }

    /**
     * Crea una nueva excepción de cargador
     * @param string mensaje de la excepción
     */
    public CargadorException(String string) {
        super(string);
    }
    
}
