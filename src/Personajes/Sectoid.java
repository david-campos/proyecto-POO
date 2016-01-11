package Personajes;

import Excepciones.CeldaObjetivoNoValida;
import Juego.Juego;

/**
 * Un sectoid, un enemigo. Un bicho muy feo con el que no te gustaría encontrarte.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Sectoid extends Enemigo{
    /**
     * Crea un nuevo sectoid
     * @param nombre nombre del floater
     * @param enePos posición del floater
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public Sectoid(String nombre, int[] enePos, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, enePos, juego);
    }
    
    /**
     * Crea un nuevo sectoid
     * @param strnombre nombre para el sectoid
     * @param saludInicial salud inicial máxima del mismo
     * @param energiaPorTurno energia que se le dará al principio de cada turno
     * @param pos posición del sectoid en el mapa
     * @param juego juego al que se enlaza
     * @throws CeldaObjetivoNoValida si la posición no es válida
     */
    public Sectoid(String strnombre, int saludInicial, int energiaPorTurno, int[] pos, Juego juego) throws CeldaObjetivoNoValida {
        super(strnombre, saludInicial, energiaPorTurno, pos, juego);
    }
    
}
