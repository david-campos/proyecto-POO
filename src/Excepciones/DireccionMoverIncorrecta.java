package Excepciones;

/**
 * Excepción de dirección de movimiento incorrecta
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class DireccionMoverIncorrecta extends PersonajeException{

    public DireccionMoverIncorrecta() {
        super();
    }

    public DireccionMoverIncorrecta(String message) {
        super(message);
    }
    
}
