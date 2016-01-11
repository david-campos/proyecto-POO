package Excepciones;

/**
 * Excepción lanzada cuando se excede el máximo de peso en la mochila
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class MaximoPesoException extends Exception{

    public MaximoPesoException() {
    }

    public MaximoPesoException(String message) {
        super(message);
    }
    
}
