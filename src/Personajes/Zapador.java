/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Juego.*;
import Mapa.*;
import Objetos.*;
import Excepciones.*;
import java.util.Random;

/**
 *
 * @author crist
 */
public class Zapador extends Jugador {	
    private static Random r = new Random();

    /**
     *
     * @param nombre
     * @param vida
     * @param energia
     * @param mochila
     * @param armadura
     * @param arma
     * @param bn
     * @param mapa
     * @param rango
     * @param juego
     */
    public Zapador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego);      
    }

    /**
     *
     * @param nombre
     * @param vida
     * @param mochilaMaxPeso
     * @param mapa
     * @param juego
     */
    public Zapador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
    }

    /**
     *
     * @param nombre
     * @param mapa
     * @param juego
     */
    public Zapador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, mapa, juego);
    }

    /**
     *
     * @param enemigo
     * @param dano
     * @return
     * @throws PersonajeException
     */
    @Override
    protected boolean atacar(Personaje enemigo, int dano) throws PersonajeException {
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
            if (dist > 2)	//5% de daño a larga distancia
            	danoReal = (int)Math.round(danoReal * 0.05);
            int vidaResultante = enemigo.getVida() - danoReal;

            enemigo.setVida(vidaResultante);
            if(vidaResultante <= 0) {
                if(enemigo instanceof Enemigo)
                    ((Enemigo)enemigo).morir();
                juego.impMapa();
            }
            else
                juego.log("Daño causado a " + enemigo.getNombre() + ": " + danoReal + ", vida restante: "+ enemigo.getVida());
        }else{
            throw new PersonajeException("Enemigo null!"); 
        }
        return true;
    }
    @Override
    public boolean atacar(Personaje pj) throws PersonajeException{
        if(getEnergia() > PConst.GE_ATACAR-2)
        {
            if(pj != null) {
                Punto posAtaque = pj.getPos();

                if(!enRango(posAtaque))
                {
                    juego.log("La posición a atacar no está en rango! Como osas... :c");
                    return false;
                }
                if(!enAlcance(posAtaque))
                {
                    juego.log("La posición a atacar está fuera del alcance del arma... :s");
                    return false;
                }
                if(atacar(pj, getEfectoArmas(posAtaque))) {
                    setEnergia(getEnergia()-(PConst.GE_ATACAR-2));
                    return true;
                }
            }
        }else {
            if(pj instanceof Enemigo)
                juego.log("No tienes energía para atacar!");    
            return false;
        }
        return false;
    }

    @Override
    public boolean coger(String nombre) throws PersonajeException {
        Objeto obj;
        if((obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre)) != null) {
            if(obj instanceof Explosivo){
                if(getEnergia() >= PConst.GE_COGER) {     
                    setEnergia(getEnergia() - PConst.GE_COGER);
                    ((Transitable)mapa.getCelda(getPos())).remObjeto(obj);
                    getMochila().addObjeto(obj);
                    juego.log("Coges " + obj.getNombre());
                }else
                    throw new PersonajeException("Insuficiente energía");
            } else {
                return super.coger(nombre);
            }
        }
        return false;
    }
    
}