package Personajes;

import Utilidades.Punto;
import Juego.*;
import Mapa.*;
import Objetos.*;
import Excepciones.*;

/**
 * Tipo de personaje Zapador. Maneja explosivos.
 * @author crist
 */
public class Zapador extends Jugador {

    /**
     * Crea un zapador nuevo
     * @param nombre nombre del zapador
     * @param vida vida del zapador
     * @param energia energía del mismo
     * @param mochila mochila que portará
     * @param armadura armadura que porta
     * @param arma arma que lleva
     * @param bn binoculares equipados
     * @param mapa mapa al que se asocia
     * @param rango rango de visión
     * @param juego juego al que se asocia
     */
    public Zapador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, bn, mapa, rango, juego);      
    }
    /**
     * Crea un zapador
     * @param nombre nombre del zapador
     * @param vida vida del zapador
     * @param mochilaMaxPeso máximo peso de la mochila
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Zapador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego) {
        super(nombre, vida, mochilaMaxPeso, mapa, juego); 
    }
    /**
     * Crea un nuevo zapador
     * @param nombre nombre del zapador
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
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
    public void coger(String nombre) throws EnergiaInsuficienteException, ImposibleCogerExcepcion, MaximoObjetosException, MaximoPesoException {
        Objeto obj;
        if(mapa != null && (obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre)) != null) {
            if(obj instanceof Explosivo){
                if(getEnergia() >= ConstantesPersonajes.GE_COGER) {     
                    setEnergia(getEnergia() - ConstantesPersonajes.GE_COGER);
                    ((Transitable)mapa.getCelda(getPos())).remObjeto(obj);
                    getMochila().addObjeto(obj);
                    juego.log("Coges " + obj.getNombre(), true);
                }else
                    throw new EnergiaInsuficienteException("Insuficiente energía");
            } else 
                super.coger(nombre);
        }
    }
    
}