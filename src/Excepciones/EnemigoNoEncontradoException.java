package Excepciones;

/**
 * Excepción lanzada cuando no se encuentra el enemigo (por ejemplo, al atacar)
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class EnemigoNoEncontradoException extends PersonajeException{

    public EnemigoNoEncontradoException() {
        super();
    }

    public EnemigoNoEncontradoException(String message) {
        super(message);
    }
    
}
