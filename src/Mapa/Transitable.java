package Mapa;

import Excepciones.CeldaObjetivoNoValida;
import java.util.Random;
import Personajes.*;
import Objetos.*;
import java.util.ArrayList;

/**
 * Celdas transitables, pueden albergar enemigos y objetos.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class Transitable extends Celda {	
    private final ArrayList<Objeto> objetos;
    private final ArrayList<Enemigo> enemigos;

    /**
     * Crea una nueva celda transitable.
     * @param regular {@code true} si se desea que la celda tenga tipo 0,
     *          {@code false} si se desea un tipo aleatorio.
     */
    public Transitable(boolean regular){
        super(0,null);
        objetos = new ArrayList();
        enemigos = new ArrayList();
        if(!regular){            
            Random r = new Random();
            do
            {
                tipo = (int)Math.round(r.nextInt(ConstantesMapa.CE_REPR_TRANS.length));
            }while(tipo == ConstantesMapa.BOQUETE);
        }
    }
    /**
     * Crea una celda transitable de tipo aleatorio
     */
    public Transitable() {
        this(false);
    }

    /**
     * Devuelve el tipo de la celda.
     * @return Un int que indica el tipo de celda
     * @see ConstantesMapa
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
    /**
     * Obtiene el objeto con el nombre dado de la celda
     * @param nombre nombre del objeto a buscar
     * @return El objeto con el nombre dado o null si no se encuentra en la celda.
     */
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

    /**
     * Obtiene el enemigo i-ésimo en la celda
     * @param i índice del enemigo que se desea obtener
     * @return El enemigo i-ésimo
     */
    public Enemigo getEnemigo(int i){
        return enemigos.get(i);
    }
    /**
     * Asocia al enemigo a esta celda. (Es llamado por {@link Enemigo#setMapa} y {@link Enemigo#setPos}).
     * @param enemigo El enemigo a asociar. Puede ser null si lo que se quiere es simplemente desligar el enemigo actual.
     * @throws CeldaObjetivoNoValida si esta celda no está disponible
     * @see Celda#estaDisponible
     */
    public void addEnemigo(Enemigo enemigo) throws CeldaObjetivoNoValida {
        if (enemigo != null && !enemigos.contains(enemigo) && !this.mapa.addEnemigo(enemigo)) {
            this.enemigos.add(enemigo);
        }
    }
    /**
     * Devuelve el número de enemigos en esta celda
     * @return La cantidad de enemigos en la celda
     */
    public int getNumEnemigos() {
        return enemigos.size();
    }

    /**
     * Elimina el enemigo de la lista de enemigos de la celda (¡pero no modifica
     * su valor de posición! Cuidado).
     * @param ene enemigo que eliminar de la lista de enemigos de la celda
     */
    public void remEnemigo(Enemigo ene){
        if(ene != null){
            enemigos.remove(ene);
        }
    }
    @Override
    public boolean estaDisponible() {
        return (mapa.getJugador() == null || !mapa.getPosDe(this).equals(mapa.getJugador().getPos()));
    }
    /**
     * Añade un objeto a la celda.
     * @param ob el objeto a añadir
     */
    public void addObjeto(Objeto ob) {
        if(ob != null && !objetos.contains(ob)) 
            objetos.add(ob);
    } 
    /**
     * Elimina un objeto de la celda.
     * @param ob el objeto a eliminar
     */
    public void remObjeto(Objeto ob) {
        if(objetos.contains(ob))
            objetos.remove(ob);
    }
}