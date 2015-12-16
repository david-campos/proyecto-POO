/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Excepciones.PersonajeException;
import Juego.*;
import Mapa.*;
import Objetos.*;
import java.util.Random;

/**
 *
 * @author crist
 */
public class Francotirador extends Jugador {
    private static Random r = new Random();

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
    protected boolean atacar(Personaje enemigo, int dano) throws PersonajeException{
        if(dano <= 0){
            throw new PersonajeException("Daño negativo? Güet?");    
        }
        
        if(enemigo != null)
        {
            if(r.nextFloat() < 0.25) {
                dano*=2;    //Daño crítico
                juego.log("¡Has causado un daño crítico!\t");
            }

            int danoReal;
            if(enemigo.getArmadura() != null)
                danoReal = (int) Math.round(dano * (1-enemigo.getArmadura().getDefensa()/200.0));
            else
                danoReal = (int) Math.round(dano);   

            //Comprobamos la distancia
            Punto pt = enemigo.getPos();
            double dist = this.getPos().dist(pt);
            if (dist > 4)	//Mayor daño a larga distancia
            	danoReal = danoReal + (int)Math.round(Math.pow(dist, 1.2));


            int vidaResultante = enemigo.getVida() - danoReal;

            enemigo.setVida(vidaResultante);
            if(vidaResultante <= 0) {
                if(enemigo instanceof Enemigo)
                    ((Enemigo)enemigo).morir();
                juego.impMapa();
            }
            else
                juego.log("Daño causado a " + enemigo.getNombre() + ": " + danoReal + ", vida restante: "+ enemigo.getVida());
        }else throw new PersonajeException("Enemigo null!");  

        return true;
    }
    
}