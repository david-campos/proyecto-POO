package Excepciones;

/**
 * Excepción lanzada cuando se excede el máximo de objetos en la mochila.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class MaximoObjetosException extends Exception{

    public MaximoObjetosException() {
        super();
    }

    public MaximoObjetosException(String message) {
        super(message);
    }
    
}
