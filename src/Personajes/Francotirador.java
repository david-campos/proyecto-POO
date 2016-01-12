package Personajes;

import Utilidades.Punto;
import Juego.*;
import Mapa.*;
import Objetos.*;

/**
 * Tipo de jugador francotirador. Tiene una gran ventaja en daño a distancia.
 * @author crist
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Francotirador extends Jugador {

    /**
     * Crea un francotirador nuevo
     * @param nombre nombre del francotirador
     * @param vida vida del francotirador
     * @param energia energía del mismo
     * @param mochila mochila que portará
     * @param armadura armadura que porta
     * @param arma arma que lleva
     * @param bn binoculares equipados
     * @param mapa mapa al que se asocia
     * @param rango rango de visión
     * @param juego juego al que se asocia
     */
    public Francotirador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego);      
    }

    /**
     * Crea un francotirador
     * @param nombre nombre del francotirador
     * @param vida vida del francotirador
     * @param mochilaMaxPeso máximo peso de la mochila
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Francotirador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
    }

    /**
     * Crea un nuevo francotirador
     * @param nombre nombre del francotirador
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Francotirador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, mapa, juego);
    }

    @Override
    public int calculoDano(Personaje atacado, int danoBase) {
            //Comprobamos la distancia
            Punto pt = atacado.getPos();
            double dist = this.getPos().dist(pt);
            if (dist > 5)	//Mayor daño a larga distancia
            	return danoBase + (int)Math.round(Math.pow(dist, 1.2));
            return danoBase;
    }
}