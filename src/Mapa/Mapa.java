/*
 * 
 */
package Mapa;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.MapaExcepcion;
import Juego.Juego;
import Objetos.Arma;
import Objetos.Armadura;
import Objetos.Binoculares;
import Objetos.Botiquin;
import Objetos.Escombros;
import Objetos.Explosivo;
import Objetos.ToritoRojo;
import Personajes.Jugador;
import Personajes.Enemigo;
import Personajes.Sectoid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class Mapa {
    public static final Random r = new Random();
    
    /**
     * Recibe un alto y un ancho y devuelve una plantilla de mapa generada aleatoriamente
     * que puede usarse al llamar al constructor del mapa.
     * La plantilla asegura que ninguna zona del mapa será inaccesible.
     * @see Mapa#Mapa(java.lang.String, java.lang.String, Mapa.Celda[][], int[]) 
     * @param ancho El ancho del mapa
     * @param alto El alto
     * @return La plantilla
     */
    public static boolean[] getPlantillaAleatoria(int ancho, int alto) {
        boolean [] tipos = new boolean[ancho*alto];
        for(int i=0; i < ancho*alto; i++)
            tipos[i] = true;
        
        for(int i = 0; i < alto; i++)
            for(int j=0; j<ancho; j++)
            {
                int diagonalesOcupadas=0;
                if( i>=alto-1 || j >= ancho-1 || !tipos[(i+1) * ancho + j + 1]) diagonalesOcupadas++; //+1+1
                if( i>=alto-1 || j <= 0       || !tipos[(i+1) * ancho + j - 1]) diagonalesOcupadas++; //+1-1
                if( i<=0      || j >= ancho-1 || !tipos[(i-1) * ancho + j + 1]) diagonalesOcupadas++; //-1+1
                if( i<=0      || j <= 0       || !tipos[(i-1) * ancho + j - 1]) diagonalesOcupadas++; //-1-1
                tipos[i*ancho+j] = !(Math.abs(r.nextFloat()) < 0.25 && (diagonalesOcupadas < 2) &&
                        ( (i>0&&tipos[(i-1)*ancho+j]) || (j>0&&tipos[i*ancho+j-1]) ||
                        (i<alto-1&&tipos[(i+1)*ancho+j]) || (j<ancho-1 && tipos[i*ancho+j+1]) ));
            }
        return tipos;
    }
    
    private String nombre;
    private String descripcion;
    private ArrayList<ArrayList<Celda>> celdas;
    private Punto posicionInicial;
    private ArrayList<Enemigo> enemigos;
    private Jugador jugador;
    private Juego juego;

    private void MapaBase(String nombre, String descripcion, Juego juego){
        if(!nombre.isEmpty())
            this.nombre = nombre;
        if(!descripcion.isEmpty())
            this.descripcion = descripcion;
        this.juego = juego; //Vale null
        posicionInicial = new Punto();
        celdas = new ArrayList();
        enemigos = new ArrayList();
        jugador = null;
    }
    /**
    * Usada por los otros creadores...
     * @param nombre Nombre del mapa
     * @param descripcion Descripción del mapa
     * @param celdas Celdas que formarán el mapa
     * @param posicionInicial Posición inicial del jugador en el mapa
    */
    public Mapa(String nombre, String descripcion, Celda[][] celdas, Punto posicionInicial, Juego juego) {
        MapaBase(nombre, descripcion, juego);
        if(celdas.length >= 5 && celdas[0].length >= 5) //25 celdas al menos
        {
            for(Celda[] fila: celdas)
                for(Celda c: fila)
                    if(c!=null)
                        if(c.getMapa() == null)
                            c.setMapa(this);
            for(Celda[] fila : celdas)
                this.celdas.add(new ArrayList(Arrays.asList(fila)));
        }
        if(posicionInicial.en(celdas[0].length, celdas.length))
            this.posicionInicial = posicionInicial;
    }
    /**
     * Crea el mapa como un conjunto de celdas vacías transitables.
     * @param nombre Nombre del mapa
     * @param descripcion Descripción
     * @param ancho Ancho del mapa
     * @param alto Alto del mapa
     */
    public Mapa(String nombre, String descripcion, int ancho, int alto, Juego juego){
        this(nombre, descripcion, ancho, alto, new boolean[0], juego);
    }
    /**
     * Crea el mapa mediante un array de tipos, si el array de tipos contiene null's pone tipo 0 a todas las celdas.
     * La posición inicial siempre es una celda transitable, independientemente del tipo de la celda en cuestión.
     * @param nombre Nombre del mapa
     * @param descripcion Descripción del mapa
     * @param ancho Ancho del mapa
     * @param alto Alto del mapa
     * @param tipos Array con la lista de tipos de las celdas (de izq. a der. de arriba abajo)
    */
    public Mapa(String nombre, String descripcion, int ancho, int alto, boolean tipos[], Juego juego) {
        MapaBase(nombre, descripcion, juego);
        
        posicionInicial.y = (int) Math.round(alto * 0.75);
        posicionInicial.x = (int) Math.round(ancho / 2);
        
        int i=0;
        for(int k=0; k < alto; k++){
            ArrayList<Celda> fila = new ArrayList();
            celdas.add(fila);
            for(int j=0; j < ancho; j++)
            {
                Celda c;
                if(i >= tipos.length || tipos[i] || (posicionInicial.x == j && posicionInicial.y == k) )
                    fila.add(c = new Transitable());
                else
                    fila.add(c = new NoTransitable());
                i++;
                c.setMapa(this);
            }
        }
    }

    public Juego getJuego() {
        return juego;
    }
    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public ArrayList<Celda> getCeldas() {
        ArrayList<Celda> lista = new ArrayList(celdas.size() * celdas.get(0).size());
        for(ArrayList<Celda> fila : celdas)
            lista.addAll(fila);
        return lista;
    }
    
    /**
     * Permite obtener el nombre del mapa.
     * @return El nombre del mapa.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Permite obtener la descripción del mapa.
     * @return La descripción del mapa.
     */
    public String getDescripcion() {
        return descripcion;
    }
    //Nombre y descripcion no necesitan setters, no cambiarán una vez creado el mapa
    /**
     * Obtiene la celda situada en la posición indicada, si no existe celda en la posición indicada devuelve null.
     * @param posicion La posición de la celda a obtener como int[2] (i,j).
     * @return La celda en la posición indicada.
     */
    public Celda getCelda(Punto posicion){
        if(posicion.en(getAncho(), getAlto()))
            return celdas.get(posicion.y).get(posicion.x);
        else
            return null;
    }
    /**
     * Obtiene la celda situada en la posición indicada, si no existe celda en la posición indicada devuelve null.
     * @param x Coordenada x
     * @param y Coordenada y
     * @return La celda en la posición indicada.
     */
    public Celda getCelda(int x, int y){
        return getCelda(new Punto(x, y));
    }
    
    /**
     * Precaución, esto cambia la celda y borra lo que contiene de la existencia!!!
     * @param pt Punto de la celda a cambiar
     * @param c Celda nueva
     */
    public void setCelda(Punto pt, Celda c){
        if(getCelda(pt) instanceof Transitable){
            enemigos.removeAll(((Transitable)getCelda(pt)).getEnemigos());
        }
        celdas.get(pt.y).set(pt.x, c);
    }
    
    /**
     * Obtiene el alto del mapa en número de celdas
     * @return El número de filas de celdas del mapa.
     */
    public int getAlto(){
        return celdas.size();
    }
    /**
     * Obtiene el ancho del mapa en número de celdas
     * @return El número de columnas de celdas del mapa.
     */
    public int getAncho(){
        return celdas.get(0).size();
    }
    /**
     * Asigna el jugador al mapa y lo situa en la posicionInicial del mapa.
     * Esta posición siempre es transitable (ver creadores).
     * @see ElementosDeJuego.Jugador#setPos(int[])
     * @param jugador El jugador que se desea asignar.
     */
    public void setJugador(Jugador jugador) throws CeldaObjetivoNoValida {
        if(jugador != null)
        {
            this.jugador = jugador;
            jugador.setPos(posicionInicial);
        }
    }
    /**
     * Obtiene el jugador asociado al mapa.
     * @return El jugador asociado a este mapa.
     */
    public Jugador getJugador() {
        return jugador;
    }
    /**
     * Añade un enemigo al mapa, se hace una llamada a setMapa, que ajusta las referencias
     * de las Celdas y demás según su posición.
     * @param enemigo El enemigo a añadir.
     */
    public boolean addEnemigo(Enemigo enemigo) throws CeldaObjetivoNoValida {
        if(!enemigos.contains(enemigo))
            if(getCelda(enemigo.getPos()) != null){
                if(((Transitable)getCelda(enemigo.getPos())).estaDisponible())
                {
                    enemigos.add(enemigo);
                    enemigo.setMapa(this);
                    return true;
                }else
                    return false; //Celda no disponible
            }else
                throw new CeldaObjetivoNoValida("La posición para añadir el enemigo no existe (no hay celda)");
        return false;
    }
    /**
     * Elimina un enemigo del mapa. Las referencias del enemigo y al enemigo se borran completamente.
     * @param enemigo El enemigo a eliminar.
     */
    public void remEnemigo(Enemigo enemigo){
        if(enemigos.contains(enemigo))
        {
            enemigos.remove(enemigo);
            try {
                enemigo.setMapa(null);
            } catch (CeldaObjetivoNoValida ex) {
                //No puede darse, siendo null
            }
            ((Transitable)getCelda(enemigo.getPos())).remEnemigo(enemigo);
        }
    }  
    /**
     * Obtiene la posición inicial del mapa
     * @return Punto con la posición
     */
    public Punto getPosicionInicial() {
        return posicionInicial.clone();
    }
    public void setPosicionInicial(Punto posicionInicial) {
        this.posicionInicial = posicionInicial;
    }
    /**
     * Busca una celda y devuelve su posición
     *
     * @param celda la celda a buscar
     * @return posición de la celda buscada
     */
    public Punto getPosDe(Celda celda) {
        for(int i=0; i < celdas.size(); i++){
            int pos;
            ArrayList<Celda> fila = celdas.get(i);
            if( (pos = fila.indexOf(celda)) != -1)
                    return new Punto(pos, i);
        }
        return null; //Si no está devolvemos null
    }
    
    /**
     * Genera objetos por el mapa de forma aleatoria.
     */
    public void setObjetosAleatorio(){
        int i=0;
        for(ArrayList<Celda> fila: celdas)
            for(Celda ce: fila)
                if(ce instanceof Transitable && Math.abs(r.nextFloat()) < ConstantesMapa.P_OBJETOS_ALEATORIOS){    //TODO: Modificar valor?
                    Transitable c = (Transitable) ce;
                    do{
                        int rand = Math.abs(r.nextInt()) % 6;  //Añadimos objetos aleatoriamente
                        switch(rand){
                            case 0:
                                c.addObjeto(
                                        new Arma(
                                                Math.round(r.nextDouble() * 500)/100.0,
                                                "arma_"+(i++),
                                                "Es un arma, y mata.",
                                                r.nextInt(5)+4,
                                                r.nextInt(16)+ConstantesMapa.DANO_MIN_ARMA,
                                                r.nextBoolean()
                                        )
                                );
                                break;
                            case 1:
                                c.addObjeto(
                                        new Armadura(
                                                "armadura_"+(i++),
                                                "Es una armadura, y así te matan menos.",
                                                Math.round(r.nextDouble() * 1000)/100.0,
                                                r.nextInt(6)+ConstantesMapa.DEFENSA_MIN_ARMADURA,
                                                r.nextInt(21)+5,
                                                r.nextInt(21)+5
                                        )
                                );
                                break;
                            case 2:
                                c.addObjeto(
                                        new Binoculares(
                                                "binoculares_"+(i++),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(5)+1
                                        )
                                );
                                break;
                            case 3:
                                c.addObjeto(
                                        new Botiquin(
                                                "botiquin_"+(i++),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(16)+5
                                        )
                                );
                                break;
                            case 4:
                                c.addObjeto(
                                        new ToritoRojo(
                                                "toritoColorado_"+(i++),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(16)+5
                                        )
                                );
                                break;
                            case 5:
                                c.addObjeto(
                                        new Explosivo(
                                            Math.round(r.nextDouble()*50)/100.0,
                                                "explosivo_"+(i++)
                                        )
                                );
                                break;
                        }
                    }while(r.nextFloat() < 0.35);
                }
    }
    /**
     * Genera enemigos de forma aleatoria por el mapa.
     */
    public void setEnemigosAleatorio(){
        int[] enePos = new int[2];
        int enemigoId=0;
        for(int pasada=0; pasada < 10; pasada++)
            for(enePos[0]=0; enePos[0] < getAlto(); enePos[0]++)
                for(enePos[1]=0; enePos[1] < getAncho(); enePos[1]++)
                {
                    if(Math.abs(r.nextFloat()) < ConstantesMapa.P_ENEMIGOS && getCelda(new Punto(enePos[1], enePos[0])) instanceof Transitable)
                    {
                        try
                        {
                            Enemigo ene = new Sectoid("Enemigo " + enemigoId++, enePos, juego); //TODO: Variedad hombre!
                            addEnemigo(ene);
                        }catch(CeldaObjetivoNoValida e){
                            //Béh, no pasará, ignorar.
                        }
                        enePos[1]++;
                    }
                }
    }
    
    /**
     * Devuelve un string con el mapa pero donde sólo son visibles las celdas que
     * el jugador puede ver, el resto del mapa lo cubrirán almohadillas('#').
     * @return La cadena lista para imprimir.
     */
    public String visionJugador() {
        String dibujoJugador = "\u001B[35mP\u001B[0m";
        String dibujoEnemigo = "\u001B[31mE\u001B[0m";
        String salida = "Mapa: "+nombre+"\n";

        //Borde
        salida+="+-";
        for (Celda celda : celdas.get(0))
            salida+="--";
        salida+="-+\n";
        
        for(int i=0; i<getAlto();i++){
                salida+="| "; //Borde
                for(int j=0; j<celdas.get(i).size();j++)
                {
                    if(celdas.get(i).get(j) == null)
                        salida += "?";
                    else if(jugador != null && jugador.getPos().equals(new Punto(j, i)))
                        salida += dibujoJugador;
                    else if(!jugador.enRango(new Punto(j, i)))
                    {
                        salida += "##"; continue;
                    }else if(getCelda(new Punto(j, i)) instanceof Transitable && ((Transitable)getCelda(new Punto(j, i))).getNumEnemigos() > 0) {
                        salida += dibujoEnemigo;
                    } else
                        salida += getCelda(new Punto(j, i)).representacion();
                    salida += " ";
                }
                salida += " |\n";
        }
        
        //Borde
        salida+="+-";
        for (int i=0; i<getAncho(); i++)
            salida+="--";
        salida+="-+\n";
        
        return salida;
    }
    
    public ArrayList<Enemigo> getEnemigos(){
        return (ArrayList) enemigos.clone();
    }
    
    @Override
    public String toString() {
        String dibujoJugador = "\u001B[35mP\u001B[0m";
        String dibujoEnemigo = "\u001B[31mE\u001B[0m";
        String salida = "Mapa: "+nombre+"\n";

        //Borde
        salida+="+-";
        for (Celda celda : celdas.get(0))
            salida+="--";
        salida+="-+\n";
        
        for(int i=0; i<getAlto();i++){
                salida+="| "; //Borde
                for(int j=0; j<celdas.get(i).size();j++)
                {
                    if(celdas.get(i).get(j) == null)
                        salida += "?";
                    else if(jugador != null && jugador.getPos().equals(new Punto(j, i)))
                        salida += dibujoJugador;
                    else if(getCelda(new Punto(j, i)) instanceof Transitable && ((Transitable)getCelda(new Punto(j, i))).getNumEnemigos() > 0)
                        salida += dibujoEnemigo;
                    else
                        salida += getCelda(new Punto(j, i)).representacion();
                    salida += " ";
                }
                salida += " |\n";
        }
        
        //Borde
        salida+="+-";
        for (int i=0; i<getAncho(); i++)
            salida+="--";
        salida+="-+\n";
        
        return salida;
    }

    public void hacerTransitable(Punto posDe, boolean dejarEscombros) {
        if(posDe.en(getAncho(), getAlto()))
        {
            Transitable t = new Transitable();
            if(dejarEscombros && getCelda(posDe).tipo == ConstantesMapa.MURO)
                t.addObjeto(new Escombros());
            celdas.get(posDe.y).set(posDe.x, t);
            t.setMapa(this);
        }
    }
}
