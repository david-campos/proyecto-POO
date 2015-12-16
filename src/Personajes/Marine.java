/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Excepciones.PersonajeException;
import Juego.Juego;
import Mapa.*;
import Objetos.*;
import java.util.Random;

/**
 *
 * @author crist
 */
public class Marine extends Jugador {
    private static Random r = new Random();
    private double consumoPor;

    public Marine(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego); 
        consumoPor = 1;
    }
    public Marine(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
        consumoPor = 1;
    }
    public Marine(String nombre, Mapa mapa, Juego juego) {
        super(nombre, mapa, juego);
        consumoPor = 1;
    }
    @Override
    public boolean atacar(Personaje pj) throws PersonajeException{
        if(getEnergia() > (int)Math.round(PConst.GE_ATACAR*consumoPor))
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
                    setEnergia(getEnergia()-(int)Math.round((PConst.GE_ATACAR+3)*consumoPor));
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
    protected boolean atacar(Personaje enemigo, int dano) throws PersonajeException {
        if(dano <= 0){
            juego.log("Daño negativo? Güet?");     
            return false;
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
            if (dist < 2)	//Doble de daño a corta distancia
            	danoReal = danoReal * 2;
            else if (dist > 2) 	//5% de daño a más de 2 casillas
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
            juego.log("Enemigo null!");  
            return false;
        }
        return true;
    }

    @Override
    public boolean mover(String c) throws PersonajeException {   
        c = c.toUpperCase();
        Punto pos = new Punto();
        int energiaConsumida = (int) (consumoPor * (PConst.GE_MOVER + 3) + consumoPor * Math.round(getMochila().pesoActual()/5));  
        if(energiaConsumida <= getEnergia()) {
                switch (c) {
                    case "O":
                        pos.x = getPos().x - 1;
                        pos.y = getPos().y;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "E":
                        pos.x = getPos().x + 1;
                        pos.y = getPos().y;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "S":
                        pos.x = getPos().x;
                        pos.y = getPos().y + 1;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "N":
                        pos.x = getPos().x;
                        pos.y = getPos().y - 1;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    default: 
                        throw new PersonajeException("Opción incorrecta");
                }
        }else {
            juego.log("No tienes suficiente energia para hacer esto");
            return false;
        }
        return true;
    }

    @Override
    public void equipar(Objeto ob) throws PersonajeException {
        super.equipar(ob);
        actualizarMultiplicadorConsumo();
    }

    public void actualizarMultiplicadorConsumo(){
        if(this.arma != null && this.arma.getTipo() == Arma.ARMA_DOS_MANOS && this.arma_izq != null && this.arma_izq.getTipo() == Arma.ARMA_DOS_MANOS)
            consumoPor = 1.5;
        else
            consumoPor = 1;
    }
    
    @Override
    public void desequipar(Objeto ob) throws PersonajeException {
        super.desequipar(ob);
        actualizarMultiplicadorConsumo();
    }
    
}