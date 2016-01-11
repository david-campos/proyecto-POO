package Personajes;

import Excepciones.CeldaObjetivoNoValida;
import Juego.Juego;

/**
 * Tipo de enemigo "floater"
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Floater extends Enemigo{

    /**
     * Crea un nuevo floater
     * @param nombre nombre del floater
     * @param vida vida el floater
     * @param energiaPorTurno energía por turno
     * @param posicion posición del floater
     * @param juego juego del floater
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public Floater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }
    
    /**
     * Crea un nuevo floater
     * @param nombre nombre del floater
     * @param posicion posición del floater
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public Floater(String nombre, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, posicion, juego);
    }
    
}
