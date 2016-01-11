package Objetos;

import Personajes.Jugador;
import Personajes.Personaje;

/**
 *
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Explosivo extends Objeto{

    /**
     * Crea un nuevo explosivo
     * @param peso peso del explosivo en la mochila (no afecta a la explosión)
     * @param nombre nombre del explosivo (ver {@link Objeto#setNombre})
     */
    public Explosivo(double peso, String nombre) {
        super(peso, nombre, "Al usarlo, explota una vez finalizado el turno");
    }

    @Override
    public void usar(Personaje p) {
        if(p != null){
            p.getMapa().getCelda(p.getPos()).setBomba(true);
            if(p instanceof Jugador) {
                Jugador jugador = (Jugador) p;
                jugador.getJuego().log("Has colocado una bomba en esta casilla... explotará finalizado el turno.", true);
            }
            p.getMochila().remObjeto(this);
        }
    }
    
    
}
