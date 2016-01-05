/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Utilidades.Punto;
import Juego.*;
import Mapa.*;
import Objetos.*;

/**
 *
 * @author crist
 */
public class Francotirador extends Jugador {

    public Francotirador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego);      
    }
    public Francotirador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
    }
    public Francotirador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, mapa, juego);
    }

    @Override
    public int calculoDano(Personaje atacado, int danoBase) {
            //Comprobamos la distancia
            Punto pt = atacado.getPos();
            double dist = this.getPos().dist(pt);
            if (dist > 4)	//Mayor daño a larga distancia
            	return danoBase + (int)Math.round(Math.pow(dist, 1.2));
            return danoBase;
    }
}