package Personajes;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.DireccionMoverIncorrecta;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.ImposibleCogerExcepcion;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Excepciones.ObjetoNoDesequipableException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
import Excepciones.PosicionFueraDeAlcanceException;
import Excepciones.PosicionFueraDeRangoException;
import Juego.ConsolaGrafica;
import Juego.Juego;
import Mapa.Mapa;
import Utilidades.Punto;
import Mapa.Transitable;
import Objetos.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase abstracta que representa un enemigo del juego.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class Enemigo extends Personaje{
    /**
     * Generador aleatorio para la IA de los enemigos
     */
    protected final static Random r = new Random();
    
    /**
     * Crea un nuevo enemigo
     * @param nombre nombre del enemigo (debe ser único en el mapa)
     * @param vida vida del enemigo
     * @param energiaPorTurno energía por turno del enemigo
     * @param posicion posición del enemigo
     * @param juego juego al que se une el enemigo
     * @throws CeldaObjetivoNoValida si la posición no es válida
     * 
     * @see Mapa#obtenerNombreEnemigo
     */
    public Enemigo(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        super(nombre, vida, energiaPorTurno, new Mochila(), null, null, 5, juego);
        mapa = null;
        if(posicion.length == 2 && posicion[0] >= 0 && posicion[1] >= 0)
            setPos(new Punto(posicion[1], posicion[0]));
    }

    /**
     * Crea un nuevo enemigo con valores aleatorios de energía y vida
     * @param nombre nombre del enemigo (debe ser único en el mapa)
     * @param posicion posición del enemigo
     * @param juego juego al que se agrega el enemigo
     * @throws CeldaObjetivoNoValida si la posición no es válida
     * 
     * @see Mapa#obtenerNombreEnemigo
     */
    public Enemigo(String nombre, int[] posicion, Juego juego) throws CeldaObjetivoNoValida {
        this(nombre,
                Math.abs(r.nextInt())% 
                    (ConstantesPersonajes.ENE_MAX_VIDAPORDEFECTO - ConstantesPersonajes.ENE_MIN_VIDAPORDEFECTO)
                    +ConstantesPersonajes.ENE_MIN_VIDAPORDEFECTO,
                Math.abs(r.nextInt())%
                    (ConstantesPersonajes.ENE_MAX_ENERGIAPT - ConstantesPersonajes.ENE_MIN_ENERGIAPT)
                    +ConstantesPersonajes.ENE_MIN_ENERGIAPT,posicion, juego);
    }

    /**
     * Cambia el mapa al que se asocia este enemigo. Llama a {@link Mapa#addEnemigo}.
     * @param mapa mapa al que se asocia el enemigo
     * @throws CeldaObjetivoNoValida si la posición que el enemigo tiene fijada
     *          es una celda no disponible en el mapa que se setea
     */
    public void setMapa(Mapa mapa) throws CeldaObjetivoNoValida{
        if(mapa != null) {
            if(this.mapa == null || !this.mapa.equals(mapa)) {
                if(this.mapa != null)
                    this.mapa.remEnemigo(this);
                
                //La posición ha de ser válida.
                if(mapa.getCelda(getPos()) != null && mapa.getCelda(getPos()).estaDisponible()){
                    this.mapa = mapa;
                    ((Transitable)this.mapa.getCelda(getPos())).addEnemigo(this);
                }else
                    this.mapa = null;
            }
        }else
            this.mapa = null;
    }

    @Override
    public final void setPos(Punto pos) throws CeldaObjetivoNoValida{
        Punto posPrevia = getPos();
        super.setPos(pos);
        if(mapa != null){
            if(mapa.getCelda(posPrevia) != null)
                ((Transitable)mapa.getCelda(posPrevia)).remEnemigo(this);
            ((Transitable)mapa.getCelda(getPos())).addEnemigo(this);
        }
    }
    @Override
    public int calculoDano(Personaje atacado, int danoBase){return danoBase;}
    
    /**
     * Comprueba si la vida del enemigo es menor o igual que 0, tira sus cosas
     * y lo elimina del mapa.
     */
    public void comprobarMuerteMorir(){
        if(this.getVida() <= 0) {
            if(getArma() != null) tirar(getArma());
            if(getArma_izq() != null) tirar(getArma_izq());
            if(getArmadura() != null) tirar(getArmadura());
            for(Objeto ob: this.getMochila().getObjetos())
                this.tirar(ob);
            this.getMapa().remEnemigo(this);
        }
    }

    @Override
    protected int obtenerConsumoEnergiaAtacar() {
        return super.obtenerConsumoEnergiaAtacar()*2;
    }
    
    /**
     * Decide si coger el arma indicada o no
     * @param a arma a coger
     * @return {@code true} si a la IA le parece buena idea coger el arma
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
     * Maneja la recogida de objetos por parte de la IA. El enemigo tratará de
     * coger las armas que considere útiles, las armaduras, y los botiquines o
     * toritos rojos que se encuentre.
     * @return {@code true} si cogió algo finalmente
     * @throws EnergiaInsuficienteException si la energía no le llega para coger algún objeto
     * @throws MaximoObjetosException si su mochila está llena
     * @throws MaximoPesoException si el objeto que trata de coger excede ya el
     *          peso máximo de su mochila.
     */
    protected boolean iaRecogerObjetos() throws EnergiaInsuficienteException, MaximoObjetosException, MaximoPesoException{
        ArrayList<Objeto> obs;
        Transitable c = (Transitable) mapa.getCelda(getPos());
        boolean cogioAlgo = false;
        if( (obs = c.getObjetos()).size() > 0){
            for(Objeto o: obs){
                if(o instanceof Arma){
                    Arma arma = (Arma) o;
                    if(decidirCogerArma(arma)){
                        if(manosOcupadasConArmas() > 1)
                            try {
                                desequipar(getArma());
                            } catch (ObjetoNoDesequipableException | ObjetoNoEncontradoException ex) {
                                /*No puede pasar*/
                            }
                        try {
                            coger(arma.getNombre());
                        } catch (ImposibleCogerExcepcion ex) {
                            //Las armas se pueden coger y son equipables
                        }
                        cogioAlgo = true;
                    }
                }else if(o instanceof Armadura){
                    Armadura armadura = (Armadura) o;
                    if( getArmadura() == null || armadura.getDefensa() > getArmadura().getDefensa()){
                        try {
                            coger(armadura.getNombre());
                        } catch (ImposibleCogerExcepcion ex) {
                            //Las armaduras se pueden coger
                        }
                        try {
                            desequipar(getArmadura());
                        } catch (ObjetoNoDesequipableException | ObjetoNoEncontradoException ex) {
                            //Ignorar
                        }
                        try {
                            equipar(armadura);
                        } catch (ObjetoNoEquipableException | ObjetoNoEncontradoException ex) {
                            //Las armaduras se pueden equipar y la acaba de coger
                        }
                        cogioAlgo = true;
                    }
                }else if(o instanceof Botiquin || o instanceof ToritoRojo){
                    try {
                        coger(o.getNombre());
                    } catch (ImposibleCogerExcepcion ex) {
                        //Se pueden coger
                    }
                        cogioAlgo = true;
                } //No coge binoculares
            }
        }
        return cogioAlgo;
    }
    /**
     * Trata de atacar al jugador, siempre
     * @throws PosicionFueraDeRangoException si la posición del jugador no está en el rango de visión
     * @throws PosicionFueraDeAlcanceException si la posición del jugador no está en el alcance del arma
     * @throws EnergiaInsuficienteException si no tiene energía suficiente para atacar
     */
    protected void iaAtacar() throws PosicionFueraDeRangoException, PosicionFueraDeAlcanceException, EnergiaInsuficienteException{
        this.atacar(mapa.getJugador());
    }
    /**
     * Obtiene la dirección de movimiento más adecuada para ir hasta el jugador.
     * @return "N" para ir hacia el norte, "E" para ir hacia el este, "O" para ir hacia el oeste y "S" para ir hacia el sur.
     */
    protected String iaDirJugador(){
        Punto vector = Punto.resta(mapa.getJugador().getPos(), getPos());
        if(Math.abs(vector.x) > Math.abs(vector.y))
            return vector.x>0?"E":"O";
        else if(Math.abs(vector.x) == Math.abs(vector.y))
            if(r.nextDouble() > 0.5)
                return vector.x>0?"E":"O";
            else
                return vector.y>0?"S":"N";
        else
            return vector.y>0?"S":"N";
    }
    /**
     * IA de movimiento del jugador, lo hace de manera aleatoria si no tiene el
     * jugador en rango. Si no, se mueve hacia él.
     * @throws EnergiaInsuficienteException si no dispone de energía suficiente para moverse
     * @throws CeldaObjetivoNoValida si la celda objetivo del movimiento elegido no es válida
     */
    protected void iaMover() throws EnergiaInsuficienteException, CeldaObjetivoNoValida{
        String dir="N";
        //Se mueve aleatoriamente si no ve al jugador
        if(!enRango(mapa.getJugador().getPos())){
            dir = direccionAleatoria();
        }else if(!enAlcance(mapa.getJugador().getPos()))
            dir = iaDirJugador();
        try {
            try {
                mover(dir);
            } catch (DireccionMoverIncorrecta ex) {
                //Las direcciones de movimiento son correctas
            }
        } catch (CeldaObjetivoNoValida ex) {
            try {
                mover(direccionAleatoria());    //Vuelve a intentarlo
            } catch (DireccionMoverIncorrecta ex1) {
               //Las direcciones de movimiento devueltas son correctas
            }
        }
    }
    /**
     * Genera una dirección de movimiento aleatoria
     * @return La cadena de la dirección de movimiento
     */
    protected String direccionAleatoria() {
        String dir = "";
        switch(r.nextInt(4)){
                case 0: dir = "S"; break;
                case 1: dir = "E"; break;
                case 2: dir = "O"; break;
                case 3: dir = "N"; break;
        }
        return dir;
    }
    /**
     * Ejecuta las acciones del turno de la ia hasta que se le acabe la energía. <br>
     * Si el enemigo está a la vista del jugador, realiza las acciones con una pequeña
     * pausa, repintando el mapa para que el jugador pueda ver lo que hace.
     */
    public void iaTurno(){
        while(getEnergia() > 0){
            boolean hizoAlgo = false;
            try {
                iaAtacar();
                hizoAlgo = true;
                juego.imprimirEstado();
            } catch (PosicionFueraDeRangoException | PosicionFueraDeAlcanceException | EnergiaInsuficienteException ex){/*No hace nada*/}
            int intentos = 0;
            for(;;){
                try {
                    if(!hizoAlgo)
                        iaMover();
                    hizoAlgo = true;
                } catch (EnergiaInsuficienteException ex) {
                    /*No hace nada*/
                } catch (CeldaObjetivoNoValida ex) {
                    if(++intentos < 4) 
                        continue;
                }
                break;
            }
            try {
                hizoAlgo = hizoAlgo || iaRecogerObjetos();
            } catch (MaximoObjetosException | MaximoPesoException | EnergiaInsuficienteException ex) {/*No hace nada*/}           
            
            if(mapa.getJugador().enRango(getPos())){
                if(juego.getConsola() instanceof ConsolaGrafica){
                    ((ConsolaGrafica)juego.getConsola()).imprimirMapa(true);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Pues nada...
                    }
                }
            }
            if(!hizoAlgo)
                break;
        }
    }

    @Override
    public String toString() {
        if(this instanceof HeavyFloater)
            return String.format("%s\n\t[Heavy Floater]", super.toString());
        if(this instanceof LightFloater)
            return String.format("%s\n\t[Light Floater]", super.toString());
        if(this instanceof Floater)
            return String.format("%s\n\t[Floater]", super.toString());
        if(this instanceof Sectoid)
            return String.format("%s\n\t[Sectoid]", super.toString());
        return String.format("%s\n\t[Raza desconocida]", super.toString());
        
    }
}