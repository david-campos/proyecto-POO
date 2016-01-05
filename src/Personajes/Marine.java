/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Utilidades.Punto;
import Excepciones.ObjetoNoDesequipableException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
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
    public int calculoDano(Personaje atacado, int danoBase) {
        //Comprobamos la distancia
        Punto pt = atacado.getPos();
        double dist = this.getPos().dist(pt);
        if (dist < 2)	//Doble de daño a corta distancia
            return danoBase * 2;
        else if (dist > 2) 	//5% de daño a más de 2 casillas
            return (int)Math.round(danoBase * 0.05);
        else
            return danoBase;
    }
    @Override
    protected int obtenerConsumoEnergiaMover() {
        return (int) (consumoPor * (super.obtenerConsumoEnergiaMover() + 3));
    }
    @Override
    protected int obtenerConsumoEnergiaAtacar() {
        return (int) (consumoPor * (super.obtenerConsumoEnergiaAtacar() + 3)); 
    }
    
    public void actualizarMultiplicadorConsumo(){
        if(this.arma != null && this.arma.getTipo() == Arma.ARMA_DOS_MANOS && this.arma_izq != null && this.arma_izq.getTipo() == Arma.ARMA_DOS_MANOS)
            consumoPor = 1.5;
        else
            consumoPor = 1;
    }
    @Override
    public void equipar(Objeto ob) throws ObjetoNoEquipableException{
        super.equipar(ob);
        actualizarMultiplicadorConsumo();
    }
    @Override
    public void desequipar(Objeto ob) throws ObjetoNoDesequipableException, ObjetoNoEncontradoException {
        super.desequipar(ob);
        actualizarMultiplicadorConsumo();
    }
    
}