package Personajes;

import Utilidades.Punto;
import Excepciones.ObjetoNoDesequipableException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
import Juego.Juego;
import Mapa.*;
import Objetos.*;

/**
 * Tipo de enemigo marine. Tiene la capacidad de llevar dos armas a dos manos.
 * Un tipo con el que no deberías meterte.
 * @author crist
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Marine extends Jugador {
    private double consumoPor;

    /**
     * Crea un marine nuevo
     * @param nombre nombre del marine
     * @param vida vida del marine
     * @param energia energía del mismo
     * @param mochila mochila que portará
     * @param armadura armadura que porta
     * @param arma arma que lleva
     * @param bn binoculares equipados
     * @param mapa mapa al que se asocia
     * @param rango rango de visión
     * @param juego juego al que se asocia
     */
    public Marine(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego); 
        consumoPor = 1;
    }
    /**
     * Crea un marine
     * @param nombre nombre del marine
     * @param vida vida del marine
     * @param mochilaMaxPeso máximo peso de la mochila
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Marine(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
        consumoPor = 1;
    }
    /**
     * Crea un nuevo marine
     * @param nombre nombre del marine
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
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
    
    /**
     * Actualiza la multiplicación del consumo de energía del marine. <br>
     * Cuando el marine porta dos armas a dos manos, su consumo es 1.5 veces
     * el habitual.
     */
    public void actualizarMultiplicadorConsumo(){
        if(getArma() != null && getArma().getTipo() == Arma.ARMA_DOS_MANOS && getArma_izq() != null && getArma_izq().getTipo() == Arma.ARMA_DOS_MANOS)
            consumoPor = 1.5;
        else
            consumoPor = 1;
    }
    @Override
    public void equipar(Objeto ob) throws ObjetoNoEquipableException, ObjetoNoEncontradoException{
        super.equipar(ob);
        actualizarMultiplicadorConsumo();
    }
    @Override
    public void desequipar(Objeto ob) throws ObjetoNoDesequipableException, ObjetoNoEncontradoException {
        super.desequipar(ob);
        actualizarMultiplicadorConsumo();
    }
    
}