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
import java.util.ArrayList;

/**
 *
 * @author crist
 */
public abstract class Jugador extends Personaje {
    private Binoculares binoculares;

    public Jugador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, rango, juego);      
        if(bn != null) {
        	binoculares = bn;
        }
        if(mapa != null)
            this.mapa = mapa;
    }
    public Jugador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego){
        super(nombre, vida, mochilaMaxPeso, juego);
        binoculares = null; 
        if(mapa != null)
            this.mapa = mapa;
    }
    public Jugador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, juego);
        binoculares = null;
        if(mapa != null)
            this.mapa = mapa;
    }

    public Binoculares getBinoculares() {
            return binoculares;
    }
    public void setBinoculares (Binoculares bn) {
        if (bn != null)
                binoculares = bn;
    }
    public boolean tieneBinoculares(){
        return binoculares!=null;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    @Override
    public boolean setPos(Punto pos) {
        if(!super.setPos(pos)){
            juego.log("La dirección no es válida.\n");
            return false;
        }
        return true;
    }
    
    
    
    /**
     * Examina la celda en la que se encuentra el jugador en busca de objetos.
     */
    public void mirar() {
        ArrayList<Objeto> list = ((Transitable)mapa.getCelda(getPos())).getObjetos();
        if(getEnergia() >= PConst.GE_MIRAR) {
            setEnergia(getEnergia() - PConst.GE_MIRAR);       //Gasto de energia
            juego.getConsola().limpiar();
            if(list.size() > 0) {
                int i = 1;
                for(Objeto ob : list) {
                    juego.log(i + ". " + ob.getNombre());
                    i++;
                }
            }else
                juego.log("Aquí no hay nada que ver...");
        }else
            juego.log("No tienes suficiente energia para hacer esto");
	}
    /**
     * Descripción detallada del objeto con el nombre dado dentro de la celda actual
     * @param nombre El nombre del objeto a buscar información detallada
     */
    public void mirar(String nombre){
        for(Objeto obj: ((Transitable)mapa.getCelda(getPos())).getObjetos())
            if(obj.getNombre().equals(nombre)){
                juego.log(obj.toString());
                return;
            }
        juego.log("No se ha encontrado ese objeto en esta celda.");
    }
    /**
     * Mira los enemigos en una celda
     * @param c La celda que mira
     * @throws Excepciones.PersonajeException
     */
    public void mirar(Transitable c) throws PersonajeException{
        juego.log("Enemigos:", true);
        if(c!=null)
            for(Enemigo e: c.getEnemigos())
                juego.log("\t"+e.getNombre());
        else
            throw new PersonajeException("Jugador.mirar(Mapa.Celda): Celda nula?");
    }
    /**
     * Mira en una celda para dar los detalles de un enemigo concreto.
     * @param c La celda en la que buscar el enemigo
     * @param nombre El nombre del enemigo
     */
    public void mirar(Transitable c, String nombre) throws PersonajeException{
        if(c.getEnemigo(nombre)!=null){
            juego.log(c.getEnemigo(nombre).toString());
        }else{
            throw new PersonajeException("No existe ese enemigo en esta celda...");
        }
    }
    public void atacar(Transitable c) throws PersonajeException {
        for(Enemigo ene : c.getEnemigos())
            if(atacar(ene, (int) Math.ceil(getEfectoArmas()/(double)c.getNumEnemigos())))
                setEnergia(getEnergia()-10);
    }   
    /**
     * Examina la mochila y ofrece un resumen de sus valores y una lista de los objetos contenidos.
     */
    public void mirarMochila() {
        Mochila mochila = getMochila();
        juego.log("Capacidad: "+ mochila.getMaxObjetos(), true);
        juego.log("Ocupación: "+mochila.getNumObj());
        juego.log("Peso máximo: "+mochila.getMaxPeso());
        juego.log("Peso actual: "+mochila.pesoActual());
        for(int i=0;i<mochila.getNumObj(); i++)
            juego.log((i+1)+". "+mochila.getObjeto(i));
    }
    @Override
    public boolean coger(String nombre) throws PersonajeException {
        if(getEnergia() >= PConst.GE_COGER) {     
            Objeto obj;
            if((obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre)) != null) {
                if(obj instanceof Binoculares && binoculares == null){
                    setEnergia(getEnergia() - PConst.GE_COGER);
                    ((Transitable)mapa.getCelda(getPos())).remObjeto(obj);
                    getMochila().addObjeto(obj);
                    equipar(obj.getNombre());
                    juego.log("Coges " + obj.getNombre());
                } else {
                    return super.coger(nombre);
                }
            }
        }else
            throw new PersonajeException("No tienes suficiente energia.");
        return false;
    }
    @Override
    public void equipar(Objeto ob) throws PersonajeException {
        if(ob instanceof Binoculares) 
            equipar((Binoculares) ob);        
        else
            super.equipar(ob);
    }
    private void equipar(Binoculares bn) {
        binoculares = bn;
        getMochila().remObjeto(bn);
    	bn.alEquipar(this);
    }

    @Override
    public void usar(Objeto obj) throws PersonajeException {
        if(obj != null && getMochila().getObjetos().contains(obj)) {
            if(obj.usar(this))
                juego.log("Has usado " + obj.getNombre());
            else
                throw new PersonajeException("No se puede usar la cosa esa...");
        }else
            throw new PersonajeException("No se puede usar lo que no se tiene, pirata.");
    }

    @Override
    public boolean tirar(Objeto obj) {
        if(super.tirar(obj)){
            juego.log("Tiras " + obj.getNombre() + " al suelo...");
            return true;
        }
        return false;
    }
    
    @Override
    public void desequipar(Objeto ob) throws PersonajeException {
        if(ob instanceof Binoculares)
            desequipar((Binoculares)ob);
        else 
            super.desequipar(ob);
    }
    private void desequipar(Binoculares bn) {
    	if(bn!= null) {
            if(bn.equals(binoculares)) {
                bn.alDesequipar(this);
                intentarMeterMochila(bn);
                binoculares=null;
            }
    	}
    }

}