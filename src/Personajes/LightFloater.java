package Personajes;

import Excepciones.CeldaObjetivoNoValida;
import Juego.Juego;

/**
 * Tipo de enemigo LightFloater
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class LightFloater extends Floater{

    /**
     * Crea un nuevo LightFloater
     * @param nombre nombre del enemigo
     * @param vida vida del enemigo
     * @param energiaPorTurno energía con la que cuenta al principio de cada turno
     * @param posicion posición del enemigo
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public LightFloater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }

    /**
     * Crea un nuevo LightFloater
     * @param nombre nombre del enemigo
     * @param posicion posición del enemigo
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public LightFloater(String nombre, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, posicion, juego);
    }

}
