package Excepciones;

/**
 * Excepción lanzada cuando la posición a atacar se encuentra fuera de alcance del arma.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @see Objetos.Arma#setRango
 */
public class PosicionFueraDeAlcanceException extends PersonajeException{

    public PosicionFueraDeAlcanceException() {
        super();
    }

    public PosicionFueraDeAlcanceException(String message) {
        super(message);
    }
    
}
