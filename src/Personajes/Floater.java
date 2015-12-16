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
public class Floater extends Enemigo{

    public Floater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }

    public Floater(String nombre, int[] posicion, Juego juego) {
        super(nombre, posicion, juego);
    }
    
}
