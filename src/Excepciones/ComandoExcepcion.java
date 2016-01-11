package Excepciones;

/**
 * Excepción en la ejecución de un comando
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ComandoExcepcion extends Exception{
    public ComandoExcepcion() {
        super();
    }
    public ComandoExcepcion(String message) {
        super(message);
    }
}
