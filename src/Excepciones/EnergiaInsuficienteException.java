package Excepciones;

/**
 * Excepción lanzada cuando el personaje no dispone de energía suficiente
 * para llevar a cabo la acción requerida.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class EnergiaInsuficienteException extends PersonajeException{
    public EnergiaInsuficienteException() {
        super();
    }
    public EnergiaInsuficienteException(String message) {
        super(message);
    }   
}
