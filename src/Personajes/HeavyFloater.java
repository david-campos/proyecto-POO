/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Juego.Juego;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class HeavyFloater extends Floater{

    /**
     *
     * @param nombre
     * @param vida
     * @param energiaPorTurno
     * @param posicion
     * @param juego
     */
    public HeavyFloater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }

    /**
     *
     * @param nombre
     * @param posicion
     * @param juego
     */
    public HeavyFloater(String nombre, int[] posicion, Juego juego) {
        super(nombre, posicion, juego);
    }
    
}
