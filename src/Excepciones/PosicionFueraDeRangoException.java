package Excepciones;

/**
 * Excepción lanzada cuando la posición a atacar está fuera del rango del personaje
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @see Personajes.Personaje#setRango
 */
public class PosicionFueraDeRangoException extends PersonajeException{

    public PosicionFueraDeRangoException() {
        super();
    }

    public PosicionFueraDeRangoException(String message) {
        super(message);
    }
    
}
