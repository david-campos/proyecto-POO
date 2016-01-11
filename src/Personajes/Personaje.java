package Personajes;

import Utilidades.Punto;
import Excepciones.CeldaObjetivoNoValida;
import Excepciones.DireccionMoverIncorrecta;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.ImposibleCogerExcepcion;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Excepciones.ObjetoNoDesequipableException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
import Excepciones.ObjetoNoUsableException;
import Excepciones.PosicionFueraDeAlcanceException;
import Excepciones.PosicionFueraDeRangoException;
import Juego.Juego;
import Mapa.*;
import Objetos.*;
import java.util.Objects;
import java.util.Random;

/**
 * <p>Clase abstracta personaje que representa a cualquier personaje del juego.</p>
 * <p>Los personajes se mueven, atacan a otros personajes, tienen vida, energía,
 * portan armas, armaduras, y disponen de un rango de visión y una posición en
 * el mapa.</p>
 * @author crist
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class Personaje {
    private String nombre;
    private int vida;
    private int maxVida;
    private int energia;
    private int energiaPorTurno;
    private Mochila mochila;
    private Arma arma;
    private Arma arma_izq;    
    private Armadura armadura;
    private Punto posicion;
    /**
     * Mapa al que está enlazado el personaje
     */
    protected Mapa mapa; //Referencia al mapa en que se encuentra
    private int rango;
    /**
     * Juego al que está enlazado el personaje
     */
    protected Juego juego;  //Para las clases hijas
    private int plusEnergia = 0;

    /**
     * Crea un personaje con los atributos indicados
     * @param nombre Nombre del personaje
     * @param vida Vida inicial del personaje
     * @param energia Energía inicial del personaje
     * @param mochila Mochila que llevará
     * @param armadura Valor de armadura
     * @param arma Arma que empuña
     * @param juego El juego, siempre el juego
     * @param rango Radio de visión del personaje (en celdas)
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
    /**
     * Crea un personaje
     * @param Nombre nombre del personaje
     * @param vida vida máxima
     * @param mochilaMaxPeso peso máximo en la mochila
     * @param juego juego al que se enlaza
     */
    public Personaje(String Nombre, int vida, int mochilaMaxPeso, Juego juego){
        this(Nombre, vida, 100, new Mochila(10, mochilaMaxPeso), null, null, 5, juego);
    }
    /**
     * Crea un nuevo personaje
     * @param Nombre nombre del personaje
     * @param juego juego al que se enlaza
     */
    public Personaje(String Nombre,Juego juego){
        this(Nombre, 100, 100, new Mochila(), null, null, 5, juego);
    }

    //GETTERS Y SETTERS

    /**
     * Obtiene el juego
     * @return El juego al que se encuentra enlazado el personaje.
     */
    public Juego getJuego() {
        return juego;
    }
    /**
     * Cambia el juego al que se encuentra enlazado el personaje.
     * @param juego el nuevo juego al que se enlaza el personaje
     */
    public void setJuego(Juego juego) {
        this.juego = juego;
    }
    /**
     * Devuelve el nombre de este personaje.
     * @return el nombre del personaje.
     */
    public final String getNombre() {
        return nombre;
    }
    /**
     * Settea el nombre de este personaje. Usado para impresión por consola.
     * @param nombre el nombre que se le quiere dar al personaje.
     */
    public void setNombre(String nombre) {
        if(nombre != null && !nombre.isEmpty())
            this.nombre = nombre;
    }
    /**
     * Obtiene la vida de este personaje
     * @return La vida del personaje
     */
    public final int getVida() {
        return vida;
    }
    /**
     * Settea la vida de este personaje
     * @param vida la vida a settear, debe estar entre 0 y la vida máxima
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
     * Devuelve la vida máxima del personaje
     * @return La vida máxima del personaje.
     */
    public int getMaxVida() {
        return maxVida;
    }
    /**
     * Define la vida máxima del personaje
     * @param mV fija la vida máxima del personaje, debe ser mayor que 0
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
     * Obtiene la energía del personaje.
     * @return La energía actual del personaje.
     */
    public int getEnergia() {
        return energia;
    }
    /**
     * Fija la energía del personaje.
     * @param energia la energía que se le quiere asignar al personaje, debe estar entre 0 y el máximo de energía por turno.
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
     * Obtiene la energía por turno del personaje.
     * @return La energía que recibe el personaje en cada turno.
     */
    public int getEnergiaPorTurno() {
        return energiaPorTurno;
    }
    /**
     * Fija la energía por turno del personaje
     * @param epT energía que se quiere fijar como energía para cada turno.
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
     * Obtiene la mochila del personaje.
     * @return La mochila del personaje.
     */
    public Mochila getMochila() {
        return mochila;
    }

    /**
     * Cambia la mochila del personaje
     * @param m nueva mochila
     */
    public void setMochila(Mochila m) {
        mochila = m;
    }
    /**
     * Devuelve la armadura portada por el personaje.
     * @return Devuelve la armadura portada por el personaje, o null de no llevarla.
     */
    public Armadura getArmadura() {
        return armadura;
    }

    /**
     * Cambia el arma derecha del personaje. Para mantener coherencia con las
     * armas a dos manos y demás, usar {@link #equipar(Objetos.Arma)}.
     * @param arma arma a asignar
     */
    public void setArma(Arma arma) {
        this.arma = arma;
    }
    /**
     * Cambia el arma izquierda del personaje. Para mantener coherencia con las
     * armas a dos manos y demás, usar {@link #equipar(Objetos.Arma)}.
     * @param arma_izq arma a asignar
     */
    public void setArma_izq(Arma arma_izq) {
        this.arma_izq = arma_izq;
    }
    /**
     * Cambia la armadura del personaje, para aplicar los boosts usar
     * {@link #equipar(Objetos.Armadura)}.
     * @param armadura armadura asiganada
     */
    public void setArmadura(Armadura armadura) {
        this.armadura = armadura;
    }
    
    /**
     * Obtiene el arma equipada en la mano derecha
     * @return El arma equipada en la mano derecha
     */
    public Arma getArma() {
        return arma;
    }

    /**
     * Obtiene el arma equipada en la mano izquierda.
     * @return El arma equipada en la mano izquierda.
     */
    public Arma getArma_izq() {
        return arma_izq;
    }
    
    /**
     * Obtiene el mapa al que se encuentra asociado el jugador.
     * @return El mapa al que se encuentra asociado el jugador.
     */
    public Mapa getMapa() {
        return mapa;
    }
    /**
     * Indica si el personaje porta armas en alguna mano o no.
     * @return True si porta arma en alguna mano, false si no porta arma en niguna. 
     */
    public boolean tieneArmas(){
        return arma!=null||arma_izq!=null;
    }
    /**
     * Devuelve la suma del efecto de las armas que el personaje lleve equipadas.
     * @return La suma del efecto de las armas equipadas por el personaje.
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
     * @param pt el punto sobre que se desea obtener el efecto causado por el arma
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
     * Devuelve la posición del personaje
     * @return La posición del personaje.
     */
    public Punto getPos() {
        return (Punto) posicion.clone();
    }
    /**
     * Settea la posición del personaje al valor indicado.
     * @param pos Posición nueva del personaje.
     * @throws CeldaObjetivoNoValida cuando la posición a la que se trata de mover es una celda
     *          no disponible.
     */
    public void setPos(Punto pos) throws CeldaObjetivoNoValida {
        if(mapa == null || (
                mapa.getCelda(pos) != null &&
                mapa.getCelda(pos).estaDisponible() &&
                (this instanceof Enemigo || ((Transitable)mapa.getCelda(pos)).getEnemigos().isEmpty()))
          ){
            posicion = pos.clone();
        }else
            throw new CeldaObjetivoNoValida("La celda a mover no es valida");
    }
    /**
     * Obtiene el rango de visión del personaje.
     * @return El rango de visión del personaje.
     */
    public int getRango() {
        return rango;
    }
    /**
     * Settea el rango de visión del personaje.
     * @param rango nuevo valor para el rango de visión, debe ser positivo.
     */
    public final  void setRango(int rango) {
        if(rango > 0)
            this.rango = rango;
        else
            this.rango = 1;
    }

    //MÉTODOS

    /**
     * Obtiene el consumo de energía del personaje cuando se mueve. <br>
     * Sobrecargar para modificar este consumo.
     * @return El consumo de energía del personaje al moverse.
     */
    protected int obtenerConsumoEnergiaMover(){
        return (int) (ConstantesPersonajes.GE_MOVER + Math.round(mochila.pesoActual()/5));
    }
    /**
     * Obtiene el consumo de energía del personaje al atacar. <br>
     * Sobrecargar para modificar este consumo.
     * @return El consumo de energía del personaje al atacar.
     */
    protected int obtenerConsumoEnergiaAtacar(){
        return (int) (ConstantesPersonajes.GE_ATACAR);
    }
    /**
     * Mueve al personaje en la dirección indicada una casilla.
     * La casilla debe estarDisponible para ello y la acción consume energía según el peso de la mochila.
     * @param c String con uno de los valores siguientes: "N","S","E" u "O". No
     *          distingue mayúsculas y minúsculas.
     * @throws CeldaObjetivoNoValida si la celda objetivo no es válida para moverse
     * @throws DireccionMoverIncorrecta si la dirección no toma los valores "n", "s", "e" u "o".
     * @throws EnergiaInsuficienteException si la energía del personaje no es suficiente para realizar la acción
     */
    public void mover(String c) throws CeldaObjetivoNoValida, DireccionMoverIncorrecta, EnergiaInsuficienteException {   
        c = c.toUpperCase();
        Punto pos = new Punto();
        int energiaConsumida = obtenerConsumoEnergiaMover();
        if(energiaConsumida <= getEnergia()) {
                switch (c) {
                    case "O":
                        pos.x = posicion.x - 1;
                        pos.y = posicion.y;
                        setPos(pos);
                        setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "E":
                        pos.x = posicion.x + 1;
                        pos.y = posicion.y;
                        setPos(pos);
                        setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "S":
                        pos.x = posicion.x;
                        pos.y = posicion.y + 1;
                        setPos(pos);
                        setEnergia(getEnergia()-energiaConsumida);
                        break;
                    case "N":
                        pos.x = posicion.x;
                        pos.y = posicion.y - 1;
                        setPos(pos);
                        setEnergia(getEnergia()-energiaConsumida);
                        break;
                    default: 
                        throw new DireccionMoverIncorrecta("Opción incorrecta para la dirección de mover");
                }
        }else
            throw new EnergiaInsuficienteException("No tienes suficiente energia para hacer mover");
    }
    /**
     * Coge el objeto (indicado por el nombre) de la celda en la que se encuentra.
     * @param nombre el nombre del objeto a coger.
     * @throws ImposibleCogerExcepcion si el objeto no se puede coger
     * @throws EnergiaInsuficienteException si la energía del jugador no es la
     *          suficiente para coger un objeto.
     * @throws MaximoObjetosException si se alcanzó el máximo de objetos de la mochila
     * @throws MaximoPesoException si el objeto a coger excede el peso máximo de la mochila
     */
    public void coger(String nombre) throws ImposibleCogerExcepcion, EnergiaInsuficienteException, MaximoObjetosException, MaximoPesoException{
        if(getEnergia() >= ConstantesPersonajes.GE_COGER) {     
            Objeto obj;
            if((obj = ((Transitable)mapa.getCelda(posicion)).getObjeto(nombre)) != null) {
                setEnergia(getEnergia() - ConstantesPersonajes.GE_COGER);
            
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
                    try {
                        equipar(obj);
                    } catch (ObjetoNoEquipableException ex) {
                        //Una armadura siempre es equipable
                    } catch (ObjetoNoEncontradoException ex) {
                        //La acabamos de coger
                    }
                    if(this instanceof Jugador) juego.log("Coges " + obj.getNombre() + " y lo equipas.");
                }else if(! (obj instanceof Explosivo))
                    cogido = mochila.addObjeto(obj);
                else
                    throw new ImposibleCogerExcepcion("No puedes coger esa cosa!");
                    
                if(cogido)
                {
                    if(this instanceof Marine) ((Marine)this).actualizarMultiplicadorConsumo();
                    
                    ((Transitable)mapa.getCelda(posicion)).remObjeto(obj);
                    if(this instanceof Jugador) juego.log(obj.getNombre() + " cogido.");
                }
            } 
        } else throw new EnergiaInsuficienteException("No tienes energía suficiente para coger nada, unútil.");
    }       
    /**
     * Ataca al personaje indicado.
     * @param pj personaje al que atacar
     * @throws PosicionFueraDeRangoException si el personaje a atacar no se encuentra en rango
     * @throws PosicionFueraDeAlcanceException si el personaje a atacar no está al alcance del arma
     * @throws EnergiaInsuficienteException si la energía del personaje no es la suficiente para atacar
     */
    public final void atacar(Personaje pj) throws PosicionFueraDeRangoException, PosicionFueraDeAlcanceException, EnergiaInsuficienteException{
        if(getEnergia() > obtenerConsumoEnergiaAtacar()){
            if(pj != null) {
                Punto posAtaque = pj.getPos();

                if(!enRango(posAtaque))
                    throw new PosicionFueraDeRangoException("La posición a atacar no está en rango! Como osas... :c");
                
                if(!enAlcance(posAtaque))
                    throw new PosicionFueraDeAlcanceException("La posición a atacar está fuera del alcance del arma... :s");
                
                atacar(pj, getEfectoArmas(posAtaque));
                setEnergia(getEnergia()-obtenerConsumoEnergiaAtacar());
            }
        }else
            throw new EnergiaInsuficienteException("No tienes energía para atacar!");
    }
    /**
     * Ataca una celda transitable (repartiendo el daño entre todos los enemigos que en ella se encuentren).
     * @param c la celda atacada
     * @throws PosicionFueraDeRangoException si el personaje a atacar no se encuentra en rango
     * @throws PosicionFueraDeAlcanceException si el personaje a atacar no está al alcance del arma
     * @throws EnergiaInsuficienteException si la energía del personaje no es la suficiente para atacar
     */
    public final void atacar(Transitable c) throws PosicionFueraDeRangoException, PosicionFueraDeAlcanceException, EnergiaInsuficienteException{
        if(getEnergia() > obtenerConsumoEnergiaAtacar()){
            if(c != null){
                Punto posAtaque = c.getMapa().getPosDe(c);
                if(!enRango(posAtaque))
                    throw new PosicionFueraDeRangoException("La posición a atacar no está en rango! Como osas... :c");

                if(!enAlcance(posAtaque))
                    throw new PosicionFueraDeAlcanceException("La posición a atacar está fuera del alcance del arma... :s");

                for(Enemigo ene : c.getEnemigos()){
                    atacar(ene, (int) Math.ceil(getEfectoArmas()/(double)c.getNumEnemigos()));
                    setEnergia(getEnergia()-obtenerConsumoEnergiaAtacar());
                }
            }
        }else
            throw new EnergiaInsuficienteException("No tienes energía para atacar!");
    }
    /**
     * Calculo de daño causado a un personaje sobre un daño base tras aplicar la 
     * defensa del mismo, generar el aleatorio de crítico, etc.
     * @param atacado personaje atacado
     * @param danoBase daño base para el cálculo (el causado por el arma)
     * @return Daño real que ha de causársele al personaje.
     */
    public abstract int calculoDano(Personaje atacado, int danoBase);
    //Ataca a un personaje concreto para causarle un daño concreto, llama a calculoDano
    //para saber cuando daño causar
    private void atacar(Personaje enemigo, int dano){
        if(dano <= 0){
            juego.log("Daño negativo? Güet?"); //Nunca debería salir, si se hace un buen uso  
            return;
        }
        
        if(enemigo != null)
        {
            if(new Random().nextFloat() < 0.25*(1/juego.getModDificultad())) { //Es más difícil hacer daño crítico con mayor dificultad
                dano*=2;    //Daño crítico
                if(enemigo instanceof Enemigo) juego.log("¡Has causado un daño crítico!\t");
                else juego.log(getNombre() + " te ha causado un daño crítico...");
            }

            int danoReal;
            //Daño según la armadura
            if(enemigo.getArmadura() != null)
                danoReal = (int) Math.round(dano * (1-enemigo.getArmadura().getDefensa()/200.0));
            else
                danoReal = (int) Math.round(dano);   

            danoReal = calculoDano(enemigo, danoReal);
            
            int vidaResultante = enemigo.getVida() - danoReal;

            enemigo.setVida(vidaResultante);
            if(enemigo instanceof Enemigo){
                juego.log("Has causado " + danoReal + " de daño.");
                ((Enemigo)enemigo).comprobarMuerteMorir();
            }else{
                juego.log(getNombre() + " te ha causado " + danoReal + " de daño.");
            }
        }else{
            juego.log("Enemigo null!");    
        }
    }
    
    /**
     * Tira el objeto indicado a la celda.
     * @param obj el objeto a tirar.
     */
    public void tirar (Objeto obj){
        if(obj != null){
            ((Transitable)mapa.getCelda(posicion)).addObjeto(obj);
            mochila.remObjeto(obj);
        }
    }

    /**
     * Equipa el objeto de nombre dado
     * @param nombre nombre del objeto
     * @throws ObjetoNoEquipableException si el objeto no se puede equipar
     * @throws ObjetoNoEncontradoException si el objeto no se encuentra en la mochila
     */
    public void equipar(String nombre) throws ObjetoNoEquipableException, ObjetoNoEncontradoException{
        if(mochila.getObjeto(nombre) != null)
            equipar(mochila.getObjeto(nombre));
        else
            throw new ObjetoNoEncontradoException("El objeto a equipar no se encuentra en la mochila.");
    }
    
    /**
     * Equipa el objeto, que debe estar en la mochila.
     * @param ob objeto a equipar
     * @throws ObjetoNoEquipableException si el objeto no es equipable
     * @throws ObjetoNoEncontradoException si el objeto no se encuentra en la mochila
     */
    public void equipar(Objeto ob) throws ObjetoNoEquipableException, ObjetoNoEncontradoException{
        if(mochila.getObjetos().contains(ob)) {
            if(ob instanceof Arma) {
                mochila.remObjeto(ob);
                equipar((Arma)ob);
            } else if (ob instanceof Armadura) {
                mochila.remObjeto(ob);
                equipar((Armadura)ob);
            } else throw new ObjetoNoEquipableException("Objeto no equipable");
        }else throw new ObjetoNoEncontradoException("El objeto no está en la mochila");
    }
    /**
     * Equipa el arma indicada al personaje, retirando apropiadamente las armas previas y guardándolas en la mochila o tirándolas al suelo si en la mochila no caben.
     * @param arma el arma que se le asigna al personaje.
     */
    private void equipar(Arma arma){  
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
                
                try {
                    desequipar(this.arma);
                } catch (ObjetoNoEncontradoException ex) {/*No puede dar esta excepcion*/}
                this.arma = arma;
            }
            arma.alEquipar(this);   
        }else if(arma != null && arma.getTipo() == Arma.ARMA_DOS_MANOS) {    
            //Arma a dos manos
            if(this.arma != null && this.arma.getTipo() == Arma.ARMA_DOS_MANOS) {
                this.arma_izq = null;
                try{
                    desequipar(this.arma);
                } catch (ObjetoNoEncontradoException ex) {/*No puede dar esta excepcion*/}
            }
            if(this.arma_izq != null)
                try{
                    desequipar(this.arma_izq);
                } catch (ObjetoNoEncontradoException ex) {/*No puede dar esta excepcion*/}
            this.arma = this.arma_izq = arma;
            arma.alEquipar(this);
        }
    }
    /**
     * Equipa la armadura portada por el personaje, si ya llevaba una quita la anterior y la guarda en la mochila o la tira al suelo.
     * @param armadura la nueva armadura a portar
     */
    private void equipar(Armadura armadura){ 
        if(armadura != null){
            try {
                desequipar(this.armadura);
            } catch (ObjetoNoEncontradoException ex) { /*No puede pasar*/ }
            this.armadura = armadura;
            armadura.alEquipar(this);
        }
    }
    /**
     * Desequipa el objeto
     * @param ob objeto a desequipar
     * @throws ObjetoNoDesequipableException si el objeto no se puede desequipar
     * @throws ObjetoNoEncontradoException si no se encuentra el objeto que se pretende desequipar equipado
     */
    public void desequipar(Objeto ob) throws ObjetoNoDesequipableException, ObjetoNoEncontradoException{
        if(ob instanceof Arma) {
            desequipar((Arma)ob);
        } else if (ob instanceof Armadura) {
            desequipar((Armadura)ob);
        } else throw new ObjetoNoDesequipableException("Objeto no desequipable");
    }
    /**
    * Desequipa un arma, que pasará a la mochila si cabe y si no al suelo.
    * @param arma arma a desequipar
    * @throws ObjetoNoEncontradoException si intentas desequipar un arma que no tienes equipada
    */
    private void desequipar(Arma arma) throws ObjetoNoEncontradoException {
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
                throw new ObjetoNoEncontradoException("Intentas desequipar un arma que no tienes");
        }
    }
    /**
    * Desequipa un armadura, que pasará a la mochila si cabe y si no al suelo.
    * @param armadura armadura a desequipar
    * @throws ObjetoNoEncontradoException si intentas desequipar una armadura que no tienes equipada
    */
    private void desequipar(Armadura armadura) throws ObjetoNoEncontradoException {
        if(armadura != null) {
            if(armadura.equals(this.armadura)) {
                armadura.alDesequipar(this);
                intentarMeterMochila(armadura);
                this.armadura=null;
            }
            else 
                throw new ObjetoNoEncontradoException("Intentas desequipar una armadura que no tienes");
        }
    }
    /**
     * Devuelve verdadero en caso de que la celda en la posición indicada se encuentre
     * a la vista del personaje.
     * @param pt coordenadas de la celda
     * @return {@code true} si la celda está a la vista del personaje.
     */
    public boolean enRango (Punto pt){
        return (posicion.dist(pt) <= rango);
    }
    /**
     * Comprueba si las coordenadas indicadas se encuentran en el rango de ataque
     * @param pt coordenadas
     * @return {@code true} en caso de que las coordenadas indicadas estén en el rango de ataque para el arma actual.
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
    /**
     * Usa el objeto de nombre indicado de la mochila
     * @param nombre nombre del objeto
     * @throws ObjetoNoUsableException si el objeto no se puede usar
     * @throws ObjetoNoEncontradoException si el objeto no se encuentra en la mochila
     */
    public void usar (String nombre) throws ObjetoNoUsableException, ObjetoNoEncontradoException {
        usar(mochila.getObjeto(nombre));
    }
    /**
     * Usa un objeto de la mochila
     * @param obj el objeto en la mochila
     * @throws ObjetoNoUsableException si el objeto no se puede usar
     * @throws ObjetoNoEncontradoException si el objeto no se encuentra en la mochila
     */
    public void usar (Objeto obj) throws ObjetoNoUsableException, ObjetoNoEncontradoException {
        if(obj != null && mochila.getObjetos().contains(obj))
            obj.usar(this);
        else
            throw new ObjetoNoEncontradoException("No se puede usar lo que no se tiene, pirata.");
    }
    
    /**
     * Devuelve el número de manos ocupadas con armas
     * @return 0, 1 o 2 según el número de manos ocupadas.
     */
    public int manosOcupadasConArmas(){
        int manos=0;
        if(arma != null) manos++;
        if(arma_izq != null) manos++;
        return manos;
    }
    /**
     * Trata de meter el objeto en la mochila, y si no puede, lo tira al suelo.
     * @param obj el objeto a introducir en la mochila del personaje
     */
    protected final void intentarMeterMochila(Objeto obj){
        try{
            mochila.addObjeto(obj);
            //Cabe en la mochila, lo metemos
            if(this instanceof Jugador)
                juego.log("Metes " + obj.getNombre() + " en la mochila.");
        }catch(MaximoPesoException | MaximoObjetosException e){
            //No cabe en la mochila, se tira al suelo.
            ((Transitable)mapa.getCelda(posicion)).addObjeto(obj);
            if(this instanceof Jugador)
                juego.log("Tiras " + obj.getNombre() + " al suelo (no cabía en la mochila).");
        }
    }
    
    /**
     * Marca al personaje como toreado con el plus de energía indicado, esta
     * energía se le restará al comienzo del próximo turno.
     * @param plusEnergia plus de energía con el que ha sido toreado.
     * @see Objetos.ToritoRojo
     */
    public void setToreado(int plusEnergia) {
        this.plusEnergia = plusEnergia;                
    }

    /**
     * Obtiene el plus de energía con el que ha sido toreado el personaje.
     * @return El plus de energía que se le restará en el próximo turno.
     * @see Objetos.ToritoRojo
     */
    public int getToreado() {
        return plusEnergia;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.nombre);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personaje other = (Personaje) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
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

    /**
     * Comprueba si el personaje tiene un objeto con el nombre indicado que no
     * sea el objeto excluído.
     * @param intento nombre a buscar en el personaje
     * @param excluido objeto excluído de la búsqueda
     * @return {@code true} si la mochila ya contiene un objeto con ese nombre
     *         que no es el excluído.
     */
    public boolean tiene(String intento, Objeto excluido){
         return (getMochila().getObjeto(intento) != null
                 && !getMochila().getObjeto(intento).equals(excluido) )
                    || (getArma() != null
                        && !getArma().equals(excluido)
                        && getArma().getNombre().equals(intento))
                    || (getArma_izq() != null
                        && !getArma_izq().equals(excluido)
                        && getArma_izq().getNombre().equals(intento))
                    || (getArmadura() != null
                        && !getArmadura().equals(excluido)
                        && getArmadura().getNombre().equals(intento));
    }
}
