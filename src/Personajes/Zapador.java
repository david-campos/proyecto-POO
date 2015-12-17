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

    public Zapador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego);      
    }
    public Zapador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
    }
    public Zapador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, mapa, juego);
    }

    @Override
    public int calculoDano(Personaje atacado, int danoBase) {
        //Comprobamos la distancia
        Punto pt = atacado.getPos();
        double dist = this.getPos().dist(pt);
        if (dist > 2)	//5% de daño a larga distancia
            danoBase = (int)Math.round(danoBase * 0.05);
        return danoBase;
    }
    @Override
    protected int obtenerConsumoEnergiaAtacar() {
        return super.obtenerConsumoEnergiaAtacar() - 2;
    }
    @Override
    protected int obtenerConsumoEnergiaMover() {
        return super.obtenerConsumoEnergiaMover() - 2;
    }

    
    
    @Override
    public void coger(String nombre) throws EnergiaInsuficienteException, ObjetoNoEquipableException, ImposibleCogerExcepcion {
        Objeto obj;
        if(mapa != null && (obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre)) != null) {
            if(obj instanceof Explosivo){
                if(getEnergia() >= PConst.GE_COGER) {     
                    setEnergia(getEnergia() - PConst.GE_COGER);
                    ((Transitable)mapa.getCelda(getPos())).remObjeto(obj);
                    getMochila().addObjeto(obj);
                    juego.log("Coges " + obj.getNombre());
                }else
                    throw new EnergiaInsuficienteException("Insuficiente energía");
            } else 
                super.coger(nombre);
        }
    }
    
}