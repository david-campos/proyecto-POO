package Mapa;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.MapaExcepcion;
import java.util.Random;
import Personajes.*;
import Objetos.*;
import java.util.ArrayList;

public final class Transitable extends Celda {	
    private final ArrayList<Objeto> objetos;
    private final ArrayList<Enemigo> enemigos;

    public Transitable() {
        super(0,null);
        objetos = new ArrayList();
        enemigos = new ArrayList();
        Random r = new Random();
        do
        {
            tipo = (int)Math.round(r.nextInt(ConstantesMapa.CE_REPR_TRANS.length));
        }while(tipo == ConstantesMapa.BOQUETE);
    }

    /**
     * Devuelve el tipo de la celda.
     * @return Un int que indica el tipo de celda, para obtener el nombre consultar el array estático en esta clase "tipos".
     */
    public int getTipo() {
        return super.tipo;
    }
    /**
     * Obtiene los objetos tirados en la celda del mapa.
     * @return La lista de objetos en la celda.
     */
    public ArrayList<Objeto> getObjetos() {
    	return (ArrayList<Objeto>) objetos.clone();
    }
    public Objeto getObjeto(String nombre) {
        if(nombre != null)
            for (Objeto ob: objetos)
                if(ob.getNombre().equals(nombre))
                    return ob;
        return null;
    }
    /**
     * Obtiene los enemigos en esta celda.
     * @return Los enemigos que se hallan en esta celda.
     */
    public ArrayList<Enemigo> getEnemigos() {
        return (ArrayList) enemigos.clone();
    }
    /**
     * Obtiene el enemigo en esta celda.
     * @param nombre El nombre del enemigo en la celda que se desea obtener
     * @return El enemigo que se halla en esta celda (null si no lo hay).
     */
    public Enemigo getEnemigo(String nombre) {
        for(Enemigo ene : enemigos)
            if(ene.getNombre().equals(nombre))
                return ene;
        return null;
    }
    public Enemigo getEnemigo(int i){
        return enemigos.get(i);
    }
    /**
     * Asocia al enemigo a esta celda. (Es llamado por Enemigo.setMapa y Enemigo.setPosicion).
     * @param enemigo El enemigo a asociar. Puede ser null si lo que se quiere es simplemente desligar el enemigo actual.
     */
    public void addEnemigo(Enemigo enemigo) throws CeldaObjetivoNoValida {
        if (enemigo != null && !enemigos.contains(enemigo) && !this.mapa.addEnemigo(enemigo)) {
            this.enemigos.add(enemigo);
        }
    }
    /**
     * Devuelve el número de enemigos en esta celda
     * @return
     */
    public int getNumEnemigos() {
        return enemigos.size();
    }
    public void remEnemigo(Enemigo ene){
        if(ene != null){
            enemigos.remove(ene);
        }
    }
    /**
     * Elimina al enemigo en esta celda.
     * @param nombre El nombre del enemigo a eliminar.
     */
    public void remEnemigo(String nombre) throws CeldaObjetivoNoValida {
        Enemigo ene = getEnemigo(nombre);
        remEnemigo(ene);
    }
    /**
     * Indica si la celda está disponible para el tránsito del jugador o de un enemigo.
     * Esto es así cuando la celda no contiene al jugador
     * @return Si la celda esta disponible para el tránsito.
     */
    public boolean estaDisponible() {
        return (mapa.getJugador() == null || !mapa.getPosDe(this).equals(mapa.getJugador().getPos()));
    }
    /**
     * Añade un objeto a la celda.
     * @param ob El objeto a añadir
     */
    public void addObjeto(Objeto ob) {
        if(ob != null) 
            objetos.add(ob);
    } 
    /**
     * Elimina un objeto de la celda.
     * @param ob El objeto a eliminar
     */
    public void remObjeto(Objeto ob) {
        if(objetos.contains(ob))
            objetos.remove(ob);
    }
}