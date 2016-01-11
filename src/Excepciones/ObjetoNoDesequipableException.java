package Excepciones;

/**
 * Excepción lanzada cuando el objeto indicado no es desequipable
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ObjetoNoDesequipableException extends ObjetoException{

    public ObjetoNoDesequipableException() {
        super();
    }

    public ObjetoNoDesequipableException(String message) {
        super(message);
    }
    
}
