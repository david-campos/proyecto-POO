package Excepciones;

/**
 * Excepción lanzada cuando el objeto no se puede usar.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ObjetoNoUsableException extends ObjetoException{

    public ObjetoNoUsableException() {
        super();
    }

    public ObjetoNoUsableException(String string) {
        super(string);
    }
    
}
