/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Excepciones.MapaExcepcion;
import Excepciones.PersonajeException;
import Juego.Juego;
import Mapa.Mapa;
import Mapa.Punto;
import Mapa.Transitable;
import Objetos.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public abstract class Enemigo extends Personaje{
    final static Random r = new Random();
    
    /**
     *
     * @param nombre
     * @param vida
     * @param energiaPorTurno
     * @param posicion
     * @param juego
     */
    public Enemigo(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) {
        super(nombre, vida, energiaPorTurno, new Mochila(), null, null, 5, juego);
        mapa = null;
        if(posicion.length == 2 && posicion[0] >= 0 && posicion[1] >= 0)
            setPos(new Punto(posicion[1], posicion[0]));
    }

    /**
     *
     * @param nombre
     * @param posicion
     * @param juego
     */
    public Enemigo(String nombre, int[] posicion, Juego juego) {
        this(nombre, Math.abs(r.nextInt())%51+50,Math.abs(r.nextInt())%70+31,posicion, juego);
    }

    /**
     *
     * @param mapa
     * @throws MapaExcepcion
     */
    public void setMapa(Mapa mapa) throws MapaExcepcion{
        if(mapa != null) {
            if(this.mapa == null || !this.mapa.equals(mapa)) {
                if(this.mapa != null)
                    this.mapa.remEnemigo(this);
                
                //La posición ha de ser válida.
                if(mapa.getCelda(getPos()) != null && mapa.getCelda(getPos()) instanceof Transitable && ((Transitable)mapa.getCelda(getPos())).estaDisponible()){
                    this.mapa = mapa;
                    ((Transitable)this.mapa.getCelda(getPos())).addEnemigo(this);
                }else
                    this.mapa = null;
            }
        }else
            this.mapa = null;
    }

    @Override
    public boolean setPos(Punto pos){
        super.setPos(pos);
        if(mapa != null && 
            mapa.getCelda(pos) != null &&
            mapa.getCelda(pos) instanceof Transitable &&
            ((Transitable)mapa.getCelda(pos)).estaDisponible()
        ){
            try {
                if(mapa.getCelda(getPos()) != null)
                    ((Transitable)mapa.getCelda(getPos())).remEnemigo(this);
                ((Transitable)mapa.getCelda(pos)).addEnemigo(this);
            } catch (MapaExcepcion ex) {
                juego.log(ex.getMessage());
            }
        }
        return true;
    }
    
    /**
     *
     * @throws PersonajeException
     */
    public void morir() throws PersonajeException {
        if(this.getVida() <= 0) {
            if(getArma() != null) tirar(getArma());
            if(getArma_izq() != null) tirar(getArma_izq());
            if(getArmadura() != null) tirar(getArmadura());
            for(Objeto ob: this.getMochila().getObjetos())
                this.tirar(ob);
            
            try{
                this.getMapa().remEnemigo(this);
            } catch (MapaExcepcion ex) {
               throw new PersonajeException(ex.getMessage());
            }
        }
    }

    /**
     *
     * @param a
     * @return
     */
    protected boolean decidirCogerArma(Arma a) {
        //El enemigo cogerá un arma si es mejor que las que lleva o simplemente puede equiparla
        //no la meterá en la mochila
        if(a.getTipo() != Arma.ARMA_DOS_MANOS) {
            //Arma a una mano
            if( (this.getArma() != null && this.getArma().getTipo() == Arma.ARMA_DOS_MANOS)
                || (this.getArma() != null && this.getArma_izq() != null)){
                    //Tiene todo ocupado
                    return getEfectoArmas() < a.getDano();
                }else{
                    //Tiene algo ocupado
                    return true;
                }
        }else{
            //Arma a dos manos
            return getEfectoArmas() < a.getDano();
        }
    }

    /**
     *
     * @return
     * @throws PersonajeException
     */
    protected boolean iaRecogerObjetos() throws PersonajeException{
        ArrayList<Objeto> obs;
        Transitable c = (Transitable) mapa.getCelda(getPos());
        if( (obs = c.getObjetos()).size() > 0)
            for(Objeto o: obs){
                if(o instanceof Arma){
                    Arma arma = (Arma) o;
                    if(decidirCogerArma(arma)){
                        if(manosOcupadasConArmas() > 1)
                            desequipar(getArma());
                        return coger(arma.getNombre());
                    }
                }else if(o instanceof Armadura){
                    Armadura armadura = (Armadura) o;
                    if( getArmadura() == null || armadura.getDefensa() > getArmadura().getDefensa())
                        return coger(armadura.getNombre());
                }else if(o instanceof Botiquin || o instanceof ToritoRojo){
                    return coger(o.getNombre());
                } //No coge binoculares
            }
        return false;
    }

    /**
     *
     * @return
     * @throws PersonajeException
     */
    protected boolean iaAtacar() throws PersonajeException{
        if(enAlcance(mapa.getJugador().getPos())){
            return this.atacar(mapa.getJugador());
        }
        return false;
    }

    /**
     *
     * @return
     */
    protected String iaDirJugador(){
        Punto vector = Punto.resta(mapa.getJugador().getPos(), getPos());
        if(Math.abs(vector.x) > Math.abs(vector.y))
            return vector.x>0?"E":"O";
        else
            return vector.y>0?"S":"N";
    }

    /**
     *
     * @return
     * @throws PersonajeException
     */
    protected boolean iaMover() throws PersonajeException{
        String dir="N";
        //Se mueve aleatoriamente si no ve al jugador
        if(!enRango(mapa.getJugador().getPos()))
        {
            switch(r.nextInt(4)){
                case 0: dir = "S"; break;
                case 1: dir = "E"; break;
                case 2: dir = "O"; break;
            }
        }else if(!enAlcance(mapa.getJugador().getPos()))
            dir = iaDirJugador();
        
        return mover(dir);
    }

    /**
     *
     * @throws PersonajeException
     */
    public void iaTurno() throws PersonajeException{
        while(getEnergia() > 0){
            boolean hizoAlgo = false;
            hizoAlgo = hizoAlgo || iaAtacar();
            hizoAlgo = hizoAlgo || iaMover();
            hizoAlgo = hizoAlgo || iaRecogerObjetos();
            if(!hizoAlgo)
                break;
        }
    }
}