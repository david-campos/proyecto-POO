package Excepciones;

/**
 * Excepción lanzada cuando el objeto no se puede coger.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ImposibleCogerExcepcion extends ObjetoException{

    public ImposibleCogerExcepcion() {
        super();
    }

    public ImposibleCogerExcepcion(String message) {
        super(message);
    }
    
}
