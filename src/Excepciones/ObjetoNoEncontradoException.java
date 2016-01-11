package Excepciones;

/**
 * Excepción lanzada cuando no se encuentra el objeto solicitado
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ObjetoNoEncontradoException extends ObjetoException{

    public ObjetoNoEncontradoException() {
        super();
    }

    public ObjetoNoEncontradoException(String string) {
        super(string);
    }
    
}
