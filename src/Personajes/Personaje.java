/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Excepciones.PersonajeException;
import Juego.Juego;
import Mapa.*;
import Objetos.*;
import java.util.Random;

/**
 *
 * @author crist
 */
public abstract class Personaje {
    protected String nombre;
    private int vida;
    private int maxVida;
    private int energia;
    private int energiaPorTurno;
    private Mochila mochila;
    protected Arma arma;
    protected Arma arma_izq;    
    private Armadura armadura;
    private Punto posicion;
    protected Mapa mapa; //Referencia al mapa en que se encuentra
    private int rango;
    protected Juego juego;  //Para las clases hijas
    private int plusEnergia = 0;
    
    private static Random r = new Random();

    /**
     * Crea un jugador con los atributos indicados
     * @param nombre Nombre del jugador
     * @param vida Vida inicial del jugador
     * @param energia Energía inicial del jugador
     * @param mochila Mochila que llevará
     * @param armadura Valor de armadura
     * @param arma Arma que empuña
     * @param mapa Mapa en el que está jugando
     * @param rango Radio de visión del jugador (en celdas)
     */
    public Personaje(String nombre, int vida, int energia, Mochila mochila, Armadura armadura, Arma arma, int rango, Juego juego){
        if(juego != null)
            this.juego = juego;

        if(nombre != null && !nombre.isEmpty())
            this.nombre = nombre.replace(" ", "_");
        
        maxVida = 100;
        setVida(vida);
        
        energiaPorTurno = 100;
        setEnergia(energiaPorTurno);
        
        if(mochila != null)
            this.mochila = mochila;
        else
            this.mochila = new Mochila();
        
        this.armadura = null; //setArmadura lo dejará si hay un error
        equipar(armadura);
        
        this.arma = null; //setArma lo dejará si hay un error
        equipar(arma);
        
        posicion = new Punto(-1, -1);
        
        this.rango = 5; //setRango lo dejará si hay un error
        setRango(rango);        
    }
    public Personaje(String Nombre, int vida, int mochilaMaxPeso, Juego juego){
        this(Nombre, vida, 100, new Mochila(10, mochilaMaxPeso), null, null, 5, juego);
    }
    public Personaje(String Nombre,Juego juego){
        this(Nombre, 100, 100, new Mochila(), null, null, 5, juego);
    }

    //GETTERS Y SETTERS

    /**
     * Devuelve el nombre de este jugador.
     * @return El nombre del jugador.
     */
    public final String getNombre() {
        return nombre;
    }
    /**
     * Settea el nombre de este jugador. Usado para impresión por consola.
     * @param nombre El nombre que se le quiere dar al jugador.
     */
    public void setNombre(String nombre) {
        if(nombre != null && !nombre.isEmpty())
            this.nombre = nombre;
    }
    /**
     * Obtiene la vida de este jugador
     * @return La vida del jugador
     */
    public final int getVida() {
        return vida;
    }
    /**
     * Settea la vida de este jugador
     * @param vida La vida a settear, debe estar entre 0 y la vida máxima
     */
    public final void setVida(int vida) {
        if(vida <= maxVida && vida >= 0)
            this.vida = vida;
        if(vida > maxVida)
            this.vida = maxVida;
        if(vida < 0)
            this.vida = 0;
    }
    /**
     * Devuelve la vida máxima del jugador
     * @return La vida máxima del jugador.
     */
    public int getMaxVida() {
        return maxVida;
    }
    /**
     * Define la vida máxima del jugador
     * @param mV Fija la vida máxima del jugador, debe ser mayor que 0
     */
    public void setMaxVida(int mV){
        if(mV > 0)
        {
            maxVida = mV;
            if(vida > maxVida)
                vida = maxVida;
        }
    }
    /**
     * Obtiene la energía del jugador.
     * @return La energía actual del jugador.
     */
    public int getEnergia() {
        return energia;
    }
    /**
     * Fija la energía del jugador.
     * @param energia La energía que se le quiere asignar al jugador, debe estar entre 0 y el máximo de energía por turno.
     */
    public final void setEnergia(int energia) {
        if(energia >= 0 && energia <= energiaPorTurno)
            this.energia = energia;
        if(energia > energiaPorTurno)
            this.energia = energiaPorTurno;
        if(energia < 0)
            this.energia = 0;
    }
    /**
     * Obtiene la energía por turno del jugador.
     * @return La energía que recibe el jugador en cada turno.
     */
    public int getEnergiaPorTurno() {
        return energiaPorTurno;
    }
    /**
     * Fija la energía por turno del jugador
     * @param epT Energía que se quiere fijar como energía para cada turno.
     */
    public void setEnergiaPorTurno(int epT){
        if(epT > 0)
        {
            energiaPorTurno = epT;
            if(energia > energiaPorTurno)
                energia = energiaPorTurno;
        }
    }
    /**
     * Obtiene la mochila del jugador.
     * @return La mochila del jugador.
     */
    public Mochila getMochila() {
        return mochila;
    }
    public void setMochila(Mochila m) {
        mochila = m;
    }
    /**
     * Devuelve la armadura portada por el jugador.
     * @return Devuelve la armadura portada por el jugador, o null de no llevarla.
     */
    public Armadura getArmadura() {
        return armadura;
    }
    //No se necesita setArmadura porque se usa el método "equipar"
    public Arma getArma() {
        return arma;
    }
    public Arma getArma_izq() {
        return arma_izq;
    }
    
    public Mapa getMapa() {
        return mapa;
    }
    /**
     * Indica si el jugador porta armas en alguna mano o no.
     * @return True si porta arma en alguna mano, false si no porta arma en niguna. 
     */
    public boolean tieneArmas(){
        return arma!=null||arma_izq!=null;
    }
    /**
     * Devuelve la suma del efecto de las armas que el jugador lleve equipadas.
     * @return La suma del efecto de las armas equipadas por el jugador.
     */
    public int getEfectoArmas(){
        int efecto=0;
        if(arma != null)
            efecto=arma.getDano();
        
        if(arma_izq != null && !arma_izq.equals(arma))
            efecto+=arma_izq.getDano();
        
        if(efecto==0)
            efecto = 10; //Arma stándard
        return efecto;
    }
    /**
     * Get efecto de las armas teniendo en cuenta la distancia (si no tiene alcance no suma).
     * @param pt
     * @return el efecto de las armas sobre un punto
     */
    public int getEfectoArmas(Punto pt){
        int efecto=0;
        if(arma != null && arma.getRango() >= posicion.dist(pt))
            efecto=arma.getDano();
        
        if(arma_izq != null && !arma_izq.equals(arma) && arma_izq.getRango() >= posicion.dist(pt))
            efecto+=arma_izq.getDano();
        
        if(arma == null && arma_izq == null)
            efecto = 10; //Arma stándard
        return efecto;
    }
    /**
     * Devuelve la posición del jugador
     * @return La posición del jugador.
     */
    public Punto getPos() {
        return (Punto) posicion.clone();
    }
    /**
     * Settea la posición del jugador al valor indicado.
     * @param pos Posición nueva del jugador.
     * @return True si pudo moverse a la posición, false si no.
     */
    public boolean setPos(Punto pos) {
        if(mapa == null || (
                mapa.getCelda(pos) != null &&
                mapa.getCelda(pos) instanceof Transitable &&
                ((Transitable)mapa.getCelda(pos)).estaDisponible()) &&
                (this instanceof Enemigo || ((Transitable)mapa.getCelda(pos)).getEnemigos().isEmpty()))
        {
            posicion = pos.clone();
            return true;
        }else    
            return false;
    }
    /**
     * Obtiene el rango de visión del jugador.
     * @return El rango de visión del jugador.
     */
    public int getRango() {
        return rango;
    }
    /**
     * Settea el rango de visión del jugador.
     * @param rango Nuevo valor para el rango de visión, debe ser positivo.
     */
    public final  void setRango(int rango) {
        if(rango > 0)
            this.rango = rango;
    }

    //MÉTODOS

    /**
     * Mueve al personaje en la dirección indicada una casilla.
     * La casilla debe estarDisponible para ello y la acción consume energía según el peso de la mochila.
     * @param c String con uno de los valores siguientes: "N","S","E" u "O".
     */
    public boolean mover(String c) throws PersonajeException {   
        c = c.toUpperCase();
        Punto pos = new Punto();
        int energiaConsumida = (int) (PConst.GE_MOVER + Math.round(mochila.pesoActual()/5));  
        if(energiaConsumida <= getEnergia()) {
                switch (c) {
                    case "O":
                        pos.x = posicion.x - 1;
                        pos.y = posicion.y;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "E":
                        pos.x = posicion.x + 1;
                        pos.y = posicion.y;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "S":
                        pos.x = posicion.x;
                        pos.y = posicion.y + 1;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "N":
                        pos.x = posicion.x;
                        pos.y = posicion.y - 1;
                        if( setPos(pos) ) setEnergia(getEnergia()-energiaConsumida);
                        break;
                    default: 
                        throw new PersonajeException("Opción incorrecta");
                }
        }else {
            juego.log("No tienes suficiente energia para hacer esto");
            return false;
        }
        return true;
    }
    /**
     * Coge el objeto (indicado por el nombre) de la celda en la que se encuentra, este índice se conoce
     * llamando al método "mirar".
     * @param nombre El nombre del objeto a coger.
     */
    public boolean coger(String nombre) throws PersonajeException {
        if(getEnergia() >= PConst.GE_COGER) {     
            Objeto obj;
            if((obj = ((Transitable)mapa.getCelda(posicion)).getObjeto(nombre)) != null) {
                setEnergia(getEnergia() - PConst.GE_COGER);
            
                boolean cogido=false;

                //Si no tienes arma y es arma la coges en la mano
                /*ARMAS*/
                if(obj instanceof Arma && (this instanceof Marine || ((Arma)obj).getTipo() == Arma.ARMA_UNA_MANO) && arma == null)
                {
                    arma = (Arma)obj; cogido = true;
                    if(this instanceof Jugador) juego.log("Coges " + obj.getNombre() + " en la mano derecha...");
                }else if(obj instanceof Arma && (this instanceof Marine || ((Arma)obj).getTipo() == Arma.ARMA_UNA_MANO) && arma_izq == null){
                    arma_izq = (Arma)obj; cogido = true;
                    if(this instanceof Jugador) juego.log("Coges " + obj.getNombre() + " en la mano izquierda...");
                }else if(obj instanceof Arma && ((Arma)obj).getTipo() == Arma.ARMA_DOS_MANOS && arma == null && arma_izq == null){
                    arma_izq = arma = (Arma)obj; cogido = true;
                    if(this instanceof Jugador) juego.log("Coges " + obj.getNombre() + " con ambas manos...");
                /*ARMADURA*/
                }else if(obj instanceof Armadura && armadura == null){
                    cogido = true;
                    mochila.addObjeto(obj);
                    equipar(obj);
                }else if(! (obj instanceof Explosivo))
                    cogido = mochila.addObjeto(obj);
                else if(this instanceof Jugador)
                    juego.log("No puedes coger eso...");
                    
                if(cogido)
                {
                    if(this instanceof Marine) ((Marine)this).actualizarMultiplicadorConsumo();
                    
                    ((Transitable)mapa.getCelda(posicion)).remObjeto(obj);
                    if(this instanceof Jugador) juego.log(obj.getNombre() + " cogido.");
                }
            } 
        } else //juego.log("No tienes suficiente energia para hacer esto");
            return false;
        return true;
    }       
    /**
     * Ataca la celda indicada. Si se le da un nombre, al enemigo indicado.
     * @param pj
     * @param personaje Personaje al que atacar
     */
    public boolean atacar(Personaje pj) throws PersonajeException{
        if(getEnergia() > PConst.GE_ATACAR)
        {
            if(pj != null) {
                Punto posAtaque = pj.getPos();

                if(!enRango(posAtaque))
                {
                    juego.log("La posición a atacar no está en rango! Como osas... :c");
                    return false;
                }
                if(!enAlcance(posAtaque))
                {
                    juego.log("La posición a atacar está fuera del alcance del arma... :s");
                    return false;
                }
                if(atacar(pj, getEfectoArmas(posAtaque))) {
                    setEnergia(getEnergia()-PConst.GE_ATACAR);
                    return true;
                }
            }
        }else {
            if(pj instanceof Enemigo)
                juego.log("No tienes energía para atacar!");    
            return false;
        }
        return false;
    }

    protected boolean atacar(Personaje enemigo, int dano) throws PersonajeException {
        if(dano <= 0){
            juego.log("Daño negativo? Güet?");  
            return false;
        }
        
        if(enemigo != null)
        {
            if(r.nextFloat() < 0.25) {
                dano*=2;    //Daño crítico
                if(enemigo instanceof Enemigo) juego.log("¡Has causado un daño crítico!\t");
            }

            int danoReal;
            if(enemigo.getArmadura() != null)
                danoReal = (int) Math.round(dano * (1-enemigo.getArmadura().getDefensa()/200.0));
            else
                danoReal = (int) Math.round(dano);   

            int vidaResultante = enemigo.getVida() - danoReal;

            enemigo.setVida(vidaResultante);
            if(vidaResultante <= 0) {
                if(enemigo instanceof Enemigo){
                    ((Enemigo)enemigo).morir();
                    juego.impMapa();
                    juego.log("Enemigo eleminado...");
                }
            }else{
                if(enemigo instanceof Enemigo)
                    juego.log("Daño causado a " + enemigo.getNombre() + ": " + danoReal + ", vida restante: "+ enemigo.getVida());
                else
                    juego.log(getNombre() + " te ha causado " + danoReal + " de daño.");
            }
        }else{
            juego.log("Enemigo null!");    
            return false;
        }
        return true;
    }
    /**
     * Tira el objeto indicado a la celda.
     * @param obj El objeto a tirar.
     * @return si se tiró o no algo
     */
    public boolean tirar (Objeto obj) {
        if(obj != null) {     
            ((Transitable)mapa.getCelda(posicion)).addObjeto(obj);
            mochila.remObjeto(obj);
            return true;
        }
        return false;
    }
    public void equipar(String nombre) throws PersonajeException {
        equipar(mochila.getObjeto(nombre));
    }
    /**
     * Equipa el objeto
     * @param ob
     */
    public void equipar(Objeto ob) throws PersonajeException {
        if(mochila.getObjetos().contains(ob)) {
            if(ob instanceof Arma) {
                mochila.remObjeto(ob);
                equipar((Arma)ob);
            } else if (ob instanceof Armadura) {
                mochila.remObjeto(ob);
                equipar((Armadura)ob);
            } else throw new PersonajeException("Objeto no equipable");
        }else throw new PersonajeException("El objeto no está en la mochila");
    }
    /**
     * Equipa el arma indicada al jugador, retirando apropiadamente las armas previas y guardándolas en la mochila o tirándolas al suelo si en la mochila no caben.
     * @param arma El arma que se le asigna al jugador.
     */
    private void equipar(Arma arma) {  
        if(arma != null && (this instanceof Marine || arma.getTipo() == Arma.ARMA_UNA_MANO))  
        {
            //Arma a una mano
            if(this.arma == null)
                this.arma = arma; //Un arma null indica pistola simple
            else if(this.arma_izq == null)
                this.arma_izq = arma;
            else{
                //Si ambas manos están ocupadas
                if(this.arma.equals(this.arma_izq)) //Si es un arma a dos manos
                    this.arma_izq = null;
                
                desequipar(this.arma);
                this.arma = arma;
            }
            arma.alEquipar(this);   
        }else if(arma != null && arma.getTipo() == Arma.ARMA_DOS_MANOS) {    
            //Arma a dos manos
            if(this.arma != null && this.arma.getTipo() == Arma.ARMA_DOS_MANOS) {
                this.arma_izq = null;
                desequipar(this.arma);
            }
            if(this.arma_izq != null)
                desequipar(this.arma_izq);
            this.arma = this.arma_izq = arma;
            arma.alEquipar(this);
        }else if(arma != null)
            juego.log("No puedes equipar eso como arma, no es un arma!");  //Se usa la consola a través del juego
    }
    /**
     * Fija la armadura portada por el jugador, si ya llevaba una quita la anterior y la guarda en la mochila o la tira al suelo.
     * @param armadura La nueva armadura a portar, null para quitar la que lleva.
     */
    private void equipar(Armadura armadura){ 
        if(armadura != null){
            desequipar(this.armadura);
            this.armadura = armadura;
            armadura.alEquipar(this);
        }
    }
    /**
     * Equipa el objeto
     * @param ob
     */
    public void desequipar(Objeto ob) throws PersonajeException {
        if(ob instanceof Arma) {
            desequipar((Arma)ob);
        } else if (ob instanceof Armadura) {
            desequipar((Armadura)ob);
        } else throw new PersonajeException("Objeto no desequipable");
    }
    /**
    * Desequipa un arma, que pasará a la mochila si cabe y si no al suelo.
    * @param arma Arma a desequipar
    */
    private void desequipar(Arma arma) {
        if(arma != null) {
            if(arma.equals(this.arma) || arma.equals(this.arma_izq)) {
                arma.alDesequipar(this);
                intentarMeterMochila(arma);
                if(arma.equals(this.arma) && arma.equals(this.arma_izq)){
                    //A dos manos
                    this.arma = null;
                    this.arma_izq = null;
                }else if(arma.equals(this.arma))
                    this.arma=null;
                else
                    this.arma_izq = null;
            }
            else 
                juego.log("Intentas desequipar un arma que no tienes");
        }
    }
    /**
    * Desequipa un armadura, que pasará a la mochila si cabe y si no al suelo.
    * @param armadura Armadura a desequipar
    */
    private void desequipar(Armadura armadura) {
        if(armadura != null) {
            if(armadura.equals(this.armadura)) {
                armadura.alDesequipar(this);
                intentarMeterMochila(armadura);
                this.armadura=null;
            }
            else 
                juego.log("Intentas desequipar una armadura que no tienes");
        }
    }
    /**
     * Devuelve verdadero en caso de que la celda en la posición indicada se encuentre
     * a la vista del jugador.
     * @param pt coordenadas de la celda
     * @return Si la celda está a la vista.
     */
    public boolean enRango (Punto pt) {
        return (posicion.dist(pt) <= rango);
    }
    /**
     * Comprueba si las coordenadas indicadas se encuentran en el rango de ataque
     * @param pt Coordenadas
     * @return true en caso de que las coordenadas indicadas estén en el rango de ataque para el arma actual.
     */
    public boolean enAlcance (Punto pt) {
        int rangoAtaque = 1;    //Por defecto
        if(arma != null)
            rangoAtaque = arma.getRango();
        if(arma_izq != null)
            rangoAtaque = rangoAtaque < arma_izq.getRango() ? rangoAtaque : arma_izq.getRango();
        rangoAtaque = Math.min(rangoAtaque, getRango());
        return (posicion.dist(pt) <= rangoAtaque);
    }
    public void usar (String nombre) throws PersonajeException {
        usar(mochila.getObjeto(nombre));
    }
    /**
     * Usa el objeto 
     * @param obj El objeto en la mochila.
     */
    public void usar (Objeto obj) throws PersonajeException {
        if(obj != null && mochila.getObjetos().contains(obj))
            if(!obj.usar(this))
                throw new PersonajeException("Ouh... parece que esto no se usa.");
        else
            throw new PersonajeException("No se puede usar lo que no se tiene, pirata.");
    }

    public int manosOcupadasConArmas(){
        int manos=0;
        if(arma != null) manos++;
        if(arma_izq != null) manos++;
        return manos;
    }
    
    protected void intentarMeterMochila(Objeto obj) {
        if(!mochila.addObjeto(obj)){
            //No cabe en la mochila, se tira al suelo.
            ((Transitable)mapa.getCelda(posicion)).addObjeto(obj);
            juego.log("Tiras " + obj.getNombre() + " al suelo (no cabía en la mochila).");
        }else{
            //Cabe en la mochila, lo metemos
            juego.log("Metes " + obj.getNombre() + " en la mochila.");
        }
    }
    
    public void setToreado(int plusEnergia) {
        this.plusEnergia = plusEnergia;                
    }
    public int getToreado() {
        return plusEnergia;
    }
    
    @Override
    public String toString() {
        int ataque= getEfectoArmas(); //Arma stándard
        return nombre +
                "\n\tVida: " + vida +
                "\n\tEnergia: " + getEnergiaPorTurno() +
                "\n\tAtaque: " + ataque +
                "\n\tDefensa: " + (armadura!=null?armadura.getDefensa():0);
    }

}
