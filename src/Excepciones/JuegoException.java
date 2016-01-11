package Excepciones;

/**
 * Excepción lanzada por el juego. Cuando no se puede iniciar, por ejemplo.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class JuegoException extends Exception{
    public JuegoException(String message) {
        super(message);
    }
}
