package Personajes;

import Utilidades.Punto;
import Excepciones.CeldaObjetivoNoValida;
import Excepciones.DireccionMoverIncorrecta;
import Excepciones.EnemigoNoEncontradoException;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.ImposibleCogerExcepcion;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Excepciones.ObjetoNoDesequipableException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
import Excepciones.ObjetoNoUsableException;
import Juego.*;
import Mapa.*;
import Objetos.*;
import java.util.ArrayList;

/**
 * Clase abstracta jugador. Un jugador puede moverse por el mapa cargándose
 * enemigos.
 * @author crist
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class Jugador extends Personaje {
    private Binoculares binoculares;

    /**
     * Crea un nuevo jugador
     * @param nombre nombre del jugador
     * @param vida vida del jugador
     * @param energia energía que se le dará en cada turno
     * @param mochila mochila que portará el jugador
     * @param armadura armadura que portará
     * @param arma arma que llevará equipada
     * @param bn binoculares equipados
     * @param mapa mapa al que se enlaza
     * @param rango rango de visión inicial
     * @param juego juego al que se enlaza
     */
    public Jugador(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, Binoculares bn, Mapa mapa, int rango, Juego juego) {
        super(nombre, vida, energia, mochila, armadura, arma, rango, juego);      
        if(bn != null) {
        	binoculares = bn;
        }
        if(mapa != null)
            this.mapa = mapa;
    }
    /**
     * Crea un nuevo jugador
     * @param nombre nombre del jugador
     * @param vida vida del jugador
     * @param mochilaMaxPeso peso máximo de la mochila
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Jugador(String nombre, int vida, int mochilaMaxPeso, Mapa mapa, Juego juego){
        super(nombre, vida, mochilaMaxPeso, juego);
        binoculares = null; 
        if(mapa != null)
            this.mapa = mapa;
    }
    /**
     * Crea un nuevo jugador
     * @param nombre nombre del jugador
     * @param mapa mapa al que se enlaza
     * @param juego juego al que se enlaza
     */
    public Jugador(String nombre, Mapa mapa, Juego juego) {
        super(nombre, juego);
        binoculares = null;
        if(mapa != null)
            this.mapa = mapa;
    }

    /**
     * Obtiene los binoculares equipados por el jugador
     * @return Los binoculares equipados por el jugador
     */
    public Binoculares getBinoculares() {
            return binoculares;
    }
    /**
     * Cambia los binoculares equipados por el jugador
     * @param bn binoculares a fijar (recomendable usar {@link #equipar} para que
     *          tengan efecto sobre el rango.
     */
    public void setBinoculares (Binoculares bn) {
        if (bn != null)
                binoculares = bn;
    }
    /**
     * Consulta si el jugador porta binoculares
     * @return {@code true} si el jugador lleva equipados unos binoculares
     */
    public boolean tieneBinoculares(){
        return binoculares!=null;
    }
        
    /**
     * Examina la celda en la que se encuentra el jugador en busca de objetos.
     */
    public void mirar(){
        ArrayList<Objeto> list = ((Transitable)mapa.getCelda(getPos())).getObjetos();
        juego.getConsola().limpiar();
        if(list.size() > 0){
            int i = 1;
            for(Objeto ob : list) {
                juego.log(i + ". " + ob.getNombre());
                i++;
            }
        }else
            juego.log("Aquí no hay nada que ver...");
    }
    /**
     * Descripción detallada del objeto con el nombre dado dentro de la celda actual
     * @param nombre el nombre del objeto a buscar información detallada
     * @throws ObjetoNoEncontradoException si no se encuentra el objeto que se pretende
     *          detallar
     */
    public void mirar(String nombre) throws ObjetoNoEncontradoException{
        Objeto obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre);
        if(obj!= null){
                juego.log(obj.toString());
        }else
            throw new ObjetoNoEncontradoException("No se ha encontrado ese objeto en esta celda.");
    }
    /**
     * Mira los enemigos en una celda
     * @param c la celda que mira
     * @throws CeldaObjetivoNoValida si la celda no se encuentra en el mapa
     */
    public void mirar(Transitable c) throws CeldaObjetivoNoValida{
        if(c!=null)
            if(c.getNumEnemigos() > 0){
                juego.log("Enemigos:", true);
                for(Enemigo e: c.getEnemigos())
                    juego.log("\t"+e.getNombre());
            }else
                juego.log("No hay enemigos por aquí, amigo.");
        else
            throw new CeldaObjetivoNoValida("Jugador.mirar(Mapa.Celda): Celda nula?");
    }
    /**
     * Mira en una celda para dar los detalles de un enemigo concreto.
     * @param c la celda en la que buscar el enemigo
     * @param nombre el nombre del enemigo
     * @throws EnemigoNoEncontradoException si no se pudo encontrar al enemigo
     *          en la celda.
     */
    public void mirar(Transitable c, String nombre) throws EnemigoNoEncontradoException{
        if(c.getEnemigo(nombre)!=null){
            juego.log(c.getEnemigo(nombre).toString());
        }else{
            throw new EnemigoNoEncontradoException("No existe ese enemigo en esta celda...");
        }
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
    public void coger(String nombre) throws EnergiaInsuficienteException, ImposibleCogerExcepcion, MaximoObjetosException, MaximoPesoException{
        if(getEnergia() >= ConstantesPersonajes.GE_COGER) { 
            Objeto obj;
            if((obj = ((Transitable)mapa.getCelda(getPos())).getObjeto(nombre)) != null) {
                if(obj instanceof Binoculares && binoculares == null){
                    setEnergia(getEnergia() - ConstantesPersonajes.GE_COGER);
                    ((Transitable)mapa.getCelda(getPos())).remObjeto(obj);
                    getMochila().addObjeto(obj);
                    try {
                        equipar(obj.getNombre());
                    } catch (ObjetoNoEquipableException ex) {
                        //Los binoculares, son equipables
                    } catch (ObjetoNoEncontradoException ex) {
                        //Lo acabamos de meter en la mochila
                    }
                    juego.log("Coges " + obj.getNombre(), true);
                }else
                    super.coger(nombre);
            }
        }else
            throw new EnergiaInsuficienteException("No tienes suficiente energia.");
    }
    @Override
    public void equipar(Objeto ob) throws ObjetoNoEquipableException, ObjetoNoEncontradoException{
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
    public void usar(Objeto obj) throws ObjetoNoUsableException, ObjetoNoEncontradoException {
        super.usar(obj);
        juego.log("Has usado " + obj.getNombre());
    }

    @Override
    public void tirar(Objeto obj){
        if(obj != null){
            super.tirar(obj);
            juego.log("Tiras " + obj.getNombre() + " al suelo...");
        }
    }
    
    @Override
    public void desequipar(Objeto ob) throws ObjetoNoDesequipableException, ObjetoNoEncontradoException{
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

    @Override
    public void mover(String c) throws CeldaObjetivoNoValida, DireccionMoverIncorrecta, EnergiaInsuficienteException {
        super.mover(c);
        visitarRango();
    }
    
    /**
     * Marca las celdas en el rango del jugador como visitadas
     */
    public void visitarRango(){
        for(int y=Math.max(getPos().y-getRango(), 0); y < getPos().y+getRango() && y < mapa.getAlto(); y++)
            for(int x=Math.max(getPos().x-getRango(), 0); x < getPos().x+getRango() && x < mapa.getAncho(); x++)
                if(this.enRango(new Punto(x,y)))
                    mapa.getCelda(x, y).setVisitada(true);
    }

    @Override
    public boolean tiene(String intento, Objeto excluido) {
        return super.tiene(intento, excluido) || (getBinoculares() != null
                        && !getBinoculares().equals(excluido)
                        && getBinoculares().getNombre().equals(intento));
    }
}