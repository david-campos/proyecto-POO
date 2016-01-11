package Mapa;

import Utilidades.Punto;
import Excepciones.CeldaObjetivoNoValida;
import Juego.Juego;
import Objetos.*;
import Personajes.Jugador;
import Personajes.Enemigo;
import Personajes.HeavyFloater;
import Personajes.LightFloater;
import Personajes.Sectoid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * <p>Mapa de juego. El mapa está formado por un array de celdas distribuidas
 * en filas.</p>
 * <p>Contiene referencias al jugador, un array de los enemigos que en él
 * se encuentran y una referencia al juego.</p>
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class Mapa {

    /**
     * Generador aleatorio empleado en la generación aleatoria
     */
    public static final Random r = new Random();
    
    /**
     * Recibe un alto y un ancho y devuelve una plantilla de mapa generada aleatoriamente
     * que puede usarse al llamar al constructor del mapa.
     * La plantilla asegura que ninguna zona del mapa será inaccesible.
     * @see Mapa#Mapa(java.lang.String, java.lang.String, int, int, boolean[], Juego.Juego)
     * @param ancho El ancho del mapa
     * @param alto El alto del mapa
     * @return La plantilla para la creación del mapa.
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
     * @param nombre nombre del mapa
     * @param descripcion descripción del mapa
     * @param celdas celdas que formarán el mapa
     * @param posicionInicial posición inicial del jugador en el mapa
     * @param juego juego en el que se juega el mapa
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
     * @param nombre nombre del mapa
     * @param descripcion descripción
     * @param ancho ancho del mapa
     * @param alto alto del mapa
     * @param juego juego en el que se desarrolla el mapa
     */
    public Mapa(String nombre, String descripcion, int ancho, int alto, Juego juego){
        this(nombre, descripcion, ancho, alto, new boolean[0], juego);
    }
    /**
     * <p>Creador que lanza el generador aleatorio.</p>
     * <p>Crea el mapa mediante un array de tipos, si el array de tipos contiene null's pone tipo 0 a todas las celdas.
     * La posición inicial siempre es una celda transitable, independientemente del tipo de la celda en cuestión.
     * Sobre la plantilla se aplican ciertos cambios (generador de lagos,
     * generador de caminos).</p>
     * @param nombre nombre del mapa
     * @param descripcion descripción del mapa
     * @param ancho ancho del mapa
     * @param alto alto del mapa
     * @param tipos array con la lista de tipos de las celdas (de izq. a der. de arriba abajo)
     * @param juego juego en el que corre el mapa
    */
    public Mapa(String nombre, String descripcion, int ancho, int alto, boolean tipos[], Juego juego) {
        MapaBase(nombre, descripcion, juego);
        
        posicionInicial.y = (int) Math.round(alto * 0.75);
        posicionInicial.x = (int) Math.round(ancho / 2);
        
        int i=0;
        for(int k=0; k < alto; k++){
            ArrayList<Celda> fila = new ArrayList();
            celdas.add(fila);
            for(int j=0; j < ancho; j++){
                Celda c;
                if(i >= tipos.length || tipos[i] || (posicionInicial.x == j && posicionInicial.y == k) )
                    fila.add(c = new Transitable(true));
                else{
                    fila.add(c = new NoTransitable());
                    c.tipo = ConstantesMapa.MURO;
                }
                i++;
                c.setMapa(this);
            }
        }
        
        //Una vez generadas las celdas, recorremos el mapa
        //y generamos círculos de tipos iguales (trata de homogenizar
        //un poco la generación para darle belleza)
        //Elegimos centros y radios aleatorios:
        int numCentros = r.nextInt( (getAlto()+getAncho())/4 ) + (getAlto()+getAncho())/4;
        int suma = 2;
        int limit = Math.max(getAlto(), getAncho()) / 4;
        int tipoCirculo = -1;
        //Generamos suelos pues
        for(int k=0; k < numCentros; k++){
            Punto pt = new Punto(r.nextInt(getAncho()), r.nextInt(getAlto())); //Si se solapan no pasa nada
            int radio = r.nextInt( limit ) + suma;
            tipoCirculo = r.nextInt(ConstantesMapa.SUELOS.length+1)%ConstantesMapa.SUELOS.length;
            for(i=Math.max(pt.y -  radio, 0); i < pt.y + radio && i < getAlto(); i++)
                for(int j=Math.max(pt.x - radio, 0); j < pt.x + radio && j < getAncho(); j++)
                    if(pt.dist(new Punto(j,i)) <= radio && celdas.get(i).get(j) instanceof Transitable)
                        celdas.get(i).get(j).tipo = ConstantesMapa.SUELOS[tipoCirculo];
        }
        
        //--- Lagos ---//
        int numLagos = r.nextInt( (int) Math.ceil(getAncho()*getAlto()/90.0) );
        for(i=0; i < numLagos; i++)
            iniciarLago();
        
        //---Caminos---//
        int numCaminos = r.nextInt( getAncho()*getAncho()/1000 + getAlto()*getAlto()/1000 + 1);
        for(i = 0; i < numCaminos; i++)
            iniciarCamino();
        
        //Corregir componentes inconexas
        corregirComponentesInconexas();
        
        //Grupos de vegetación y decorados puntuales
        numCentros++;
        limit = (int) Math.ceil(Math.max(getAlto(), getAncho()) / 20.0);
        int fuentes = 0;
        int arbustosRojos = 0;
        int maxFuentes = r.nextInt(numCentros/4+1);
        int maxArbustosR = r.nextInt(numCentros/2+1);
        for(int k=0; k < numCentros; k++){
            Punto pt = new Punto(r.nextInt(getAncho()), r.nextInt(getAlto())); //Si se solapan no pasa nada, aunque será raro
            int radio = r.nextInt( limit ) + suma;
            for(i=Math.max(pt.y -  radio, 0); i < pt.y + radio && i < getAlto(); i++)
                for(int j=Math.max(pt.x - radio, 0); j < pt.x + radio && j < getAncho(); j++)
                    if(pt.dist(new Punto(j,i)) <= radio && celdas.get(i).get(j) instanceof Transitable)
                        for(int l=0; l < ConstantesMapa.SUELOS.length; l++)
                            if(celdas.get(i).get(j).tipo == ConstantesMapa.SUELOS[l]){
                                if(fuentes < maxFuentes && ConstantesMapa.SUELOS[l] == ConstantesMapa.HIERBA1 && r.nextDouble() < 0.002){
                                    celdas.get(i).get(j).tipo = ConstantesMapa.FUENTE;
                                    fuentes++;
                                }else if(arbustosRojos < maxArbustosR && ConstantesMapa.SUELOS[l] == ConstantesMapa.HIERBA2 && r.nextDouble() < 0.04){
                                    celdas.get(i).get(j).tipo = ConstantesMapa.ARBUSTO_ROJO;
                                    arbustosRojos++;
                                }else
                                    celdas.get(i).get(j).tipo = ConstantesMapa.VEGETACIONES[l];
                                break;
                            }
        }
    }
    
    //Devuelve un punto adyacente aleatorio al punto dado
    private Punto adyacenteAleatorio(Punto pt){
        Punto ady;
        if(!pt.en(getAncho(), getAlto())) return null;
        do{
            ady = new Punto(pt.x+r.nextInt(3)-1, pt.y + r.nextInt(3)-1);
        }while(ady.x < 0 || ady.y < 0 || ady.x >= getAncho() || ady.y >= getAlto());
        return ady;
    }
    //Devuelve los puntos adyacentes al punto dado
    private ArrayList<Punto> adyacentes(Punto pt){
            ArrayList<Punto> pts = new ArrayList(4);
            Punto der = new Punto(pt.x +1, pt.y);
            Punto izq = new Punto(pt.x -1, pt.y);
            Punto sur = new Punto(pt.x, pt.y+1);
            Punto nor = new Punto(pt.x, pt.y-1);
            if(der.en(getAncho(), getAlto())) pts.add(der);
            if(izq.en(getAncho(), getAlto())) pts.add(izq);
            if(nor.en(getAncho(), getAlto())) pts.add(nor);
            if(sur.en(getAncho(), getAlto())) pts.add(sur);
            return pts;
    }
    //Devuelve todos los puntos adyacentes a un punto dado
    private ArrayList<Punto> adyacentes(ArrayList<Punto> grupo){
        ArrayList<Punto> prf = new ArrayList();
        for(Punto pt: grupo){
            Punto der = new Punto(pt.x +1, pt.y);
            Punto izq = new Punto(pt.x -1, pt.y);
            Punto sur = new Punto(pt.x, pt.y+1);
            Punto nor = new Punto(pt.x, pt.y-1);
            if(der.en(getAncho(), getAlto()) && !grupo.contains(der)) prf.add(der);
            if(izq.en(getAncho(), getAlto()) && !grupo.contains(izq)) prf.add(izq);
            if(nor.en(getAncho(), getAlto()) && !grupo.contains(nor)) prf.add(nor);
            if(sur.en(getAncho(), getAlto()) && !grupo.contains(sur)) prf.add(sur);
        }
        return prf;
    }
    
    //Inicia la generación de un lago aleatorio
    private void iniciarLago(){
        int tam = r.nextInt(Math.min(getAlto(), getAncho())-4)+2; //El tamaño mínimo es 5 :d
        //Seleccionamos dos celdas aleatorias de semilla
        ArrayList<Punto> lago = new ArrayList(tam);
        Punto pt = new Punto(r.nextInt(getAncho()), r.nextInt(getAlto()));
        //Seleccionamos una celda adyacente
        Punto pt2 = adyacenteAleatorio(pt);
        lago.add(pt);
        lago.add(pt2);
        generarLago(lago, tam);
        
        for(Punto p: lago){
            if(!p.equals(getPosicionInicial())){
                this.setCelda(p, new NoTransitable());
                this.getCelda(p).tipo = ConstantesMapa.AGUA;
            }
        }
    }
    //Función recursiva que genera el lago añadiendo puntos adyacentes a la
    //periferia del mismo
    private void generarLago(ArrayList<Punto> lago, int tam){
        if(lago.size() < tam){
            ArrayList<Punto> prf = adyacentes(lago);
            int i = r.nextInt(prf.size());
            lago.add(prf.get(i));
            generarLago(lago, tam);
        }
    }
    //Inicia un nuevo camino aleatorio, eligiendo uno de los bordes del mapa
    //aleatoriamente (N, S, E u O) e iniciando un vector inicial perpendicular
    //al borde en una parte aleatoria del mismo.
    private void iniciarCamino(){
        double[] ptInicial = new double[]{0, 0};
        double[] vecInicial = new double[]{1,1};
        int zonaInicial = r.nextInt(4);
        //0=S,1=N,2=E,3=O
        switch(zonaInicial){
            case 0:
                ptInicial[0] = getAlto();
                vecInicial[0] = -1;
            case 1:
                ptInicial[1] = r.nextInt(getAncho());
                vecInicial[1] = 0;
                break;
            case 2:
                ptInicial[1] = getAncho();
                vecInicial[1] = -1;
            case 3:
                ptInicial[0] = r.nextInt(getAlto());
                vecInicial[0] = 0;
                break;
        }
        unitario(vecInicial);
        int energia = getAncho()+getAlto();
        generarCaminos(ptInicial, vecInicial, energia);
    }
    //Convierte un vector dado en unitario
    private void unitario(double[] vector){
        double norma = Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]);
        vector[0] = vector[0]/norma;
        vector[1] = vector[1]/norma;
    }
    //Rota el vector dado unos radianes determinados
    private void rotar(double[] v, double radianes){
        double cs = Math.cos(radianes);
        double sn = Math.sin(radianes);

        double j = v[1] * cs - v[0] * sn;
        double i = v[1] * sn + v[0] * cs;
        v[0] = i;
        v[1] = j;
    }
    //Genera el camino de forma recursiva, guiándose por un vector que va
    //variando su dirección poco a poco, y a veces se divide dando origen
    //a dos caminos.
    private void generarCaminos(double[] punto, double[] vector, int energia){
        //El algoritmo varia ligeramente a veces la dirección
        //en cada llamada recursiva se pintan las celdas a menos de x de distancia
        //del punto.
        if(energia > 0 &&
                punto[0] < getAlto()+3 && punto[1] < getAncho()+3
                && punto[0] > -3 && punto[1] > -3){
            for(int i = (int) Math.max(Math.floor(punto[0] - 2), 0); i < Math.ceil(punto[0])+2 && i < getAlto(); i++)
                for(int j = (int) Math.max(Math.floor(punto[1] - 2), 0); j < Math.ceil(punto[1])+2 && j < getAncho(); j++){
                    double di = i - punto[0];
                    double dj = j - punto[1];
                    if( Math.sqrt(di*di+dj*dj) <= 1){
                        if(getCelda(j, i) instanceof NoTransitable)
                            this.hacerTransitable(new Punto(j,i), false);
                        
                        this.getCelda(j, i).tipo = ConstantesMapa.CAMINO;
                    }
                }
            if(r.nextFloat() < 0.015){
                //Dividimos el camino
                double[] v1 = Arrays.copyOf(vector, vector.length);
                double[] v2 = Arrays.copyOf(vector, vector.length);
                rotar(v1, Math.PI/3);
                rotar(v2, -Math.PI/3);
                generarCaminos(new double[]{punto[0]+1.3*v1[0], punto[1]+1.3*v1[1]}, v1, energia-1);
                generarCaminos(new double[]{punto[0]+1.3*v2[0], punto[1]+1.3*v2[1]}, v2, energia-1);
            }else{
                if(r.nextFloat() < 0.3)
                    rotar(vector, r.nextDouble() * (Math.PI/16) - (Math.PI/16));
                generarCaminos(new double[]{punto[0]+1.3*vector[0], punto[1]+1.3*vector[1]}, vector, energia-1);
            }
        }
    }
    
    //Añade a un array list de puntos 'area' el punto pasado y todas sus adyacentes
    //provenientes de 'celdis' de forma recursiva. El resultado es un array
    //que contiene un conjunto de celdas transitables adyacentes unas a otras.
    private void meterCercanas(ArrayList<Punto> area, Punto pt, ArrayList<Punto> celdis){
        celdis.remove(pt);
        area.add(pt);
        if(celdis.isEmpty()) return;
        Punto der = new Punto(pt.x +1, pt.y);
        Punto izq = new Punto(pt.x -1, pt.y);
        Punto sur = new Punto(pt.x, pt.y+1);
        Punto nor = new Punto(pt.x, pt.y-1);
        if(celdis.contains(der)) meterCercanas(area, der, celdis);
        if(celdis.isEmpty()) return;
        if(celdis.contains(izq)) meterCercanas(area, izq, celdis);
        if(celdis.isEmpty()) return;
        if(celdis.contains(nor)) meterCercanas(area, nor, celdis);
        if(celdis.isEmpty()) return;
        if(celdis.contains(sur)) meterCercanas(area, sur, celdis);
    }
    //Corrije las componentes inconexas en el mapa, es decir, asegura que todas
    //las celdas transitables del mapa se encuentran en una única area completa-
    //mente conexa.
    private void corregirComponentesInconexas(){
        ArrayList<ArrayList<Punto>> areasConexas = new ArrayList();
        ArrayList<Punto> celdis = copiaPosicionesTransitables();
        //Mientras no se vacía celdis, vamos extrayendo celdas que forman áreas conexas
        //El problema se puede plantear como un grafo donde cada celda transitable
        //es un vértice, de forma que las adyacentes se conectan mediante aristas.
        //Para buscar las componentes conexas de las mismas basta con recorrer el grafo
        //añadiendo los vertices recorridos a la componente, y repetir hasta que
        //todos los vértices hayan sido visitados.
        while(!celdis.isEmpty()){
            Punto c = celdis.get(0);
            ArrayList<Punto> nuevaArea = new ArrayList();
            meterCercanas(nuevaArea, c, celdis);
            areasConexas.add(nuevaArea);
        }
        //Mientras haya más de un área conexa, unimos la primera con la más cercana
        //para luego agregar la nueva área ahora conectada a la primera y repetir.
        //Si vemos el problema como un grafo nuevamente, donde las áreas conexas son
        //vértices, y se hallan conectadas mediante aristas cuyo peso es la distancia
        //entre las áreas. El problema consiste en encontrar las aristas que forman
        //el árbol de expansión de coste mínimo del grafo. Mi solución es una
        //implementación del algoritmo de Prim.
        while(areasConexas.size() > 1){
            Punto[] linea = masCercanas(areasConexas.get(0), areasConexas.get(1));
            double menor = linea[0].dist(linea[1]);
            int indice = 1;
            for(int l=2; l < areasConexas.size(); l++){
                Punto[] nlinea = masCercanas(areasConexas.get(0), areasConexas.get(l));
                if(nlinea[0].dist(nlinea[1]) < menor){
                    linea = nlinea;
                    indice = l;
                    menor = nlinea[0].dist(nlinea[1]);
                }
            }
            
            //El dibujo de la conexión se hace en L, y no en diagonal, pues las
            //distancias suelen ser pequeñas y generar diagonales no permite
            //el paso del jugador.
            int minimoX = linea[0].x < linea[1].x?0:1;
            int j;
            for(j=Math.min(linea[0].x, linea[1].x); j <= Math.max(linea[0].x, linea[1].x); j++)
                this.hacerTransitable(new Punto(j, linea[minimoX].y), false);
            for(int i=Math.min(linea[0].y, linea[1].y); i <= Math.max(linea[0].y, linea[1].y); i++){
                this.hacerTransitable(new Punto(j, i), false);
            }
            areasConexas.get(0).addAll(areasConexas.remove(indice)); //Añadimos a la primera la unida
        }
    }
    //Busca los dos puntos más cercanos entre las dos áreas dadas.
    private Punto[] masCercanas(ArrayList<Punto> a1, ArrayList<Punto> a2){
        Punto masCercana1 = a1.get(0);
        Punto masCercana2 = a2.get(0);
        double menorDist = masCercana1.dist(masCercana2);
        for(Punto t: a1)
            for(Punto t2: a2)
                if(t.dist(t2) < menorDist){
                    masCercana1 = t;
                    masCercana2 = t2;
                    menorDist = t.dist(t2);
                }
        
        return new Punto[]{masCercana1, masCercana2};
    }
    
    /**
     * Devuelve el juego en el que corre el mapa
     * @return El juego en el que corre el mapa
     */
    public Juego getJuego() {
        return juego;
    }
    /**
     * Cambia el juego en el que corre el mapa. Esto no provoca el cambio de
     * juego ni en el jugador ni en los enemigos.
     * @param juego el juego en el que se desea hacer correr el mapa
     */
    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    /**
     * Obtiene una copia del array de celdas que conforman el mapa, para poder
     * iterar sobre él.
     * @return Una copia del array de las celdas que conforman el mapa
     */
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
    /**
     * Cambia el nombre del mapa
     * @param nombre nuevo nombre para el mapa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Cambia la descripción del mapa
     * @param descripcion nueva descripción para el mapa
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * Obtiene la celda situada en la posición indicada, si no existe celda en la posición indicada devuelve null.
     * @param posicion la posición de la celda a obtener
     * @return La celda en la posición indicada.
     */
    public Celda getCelda(Punto posicion){
        if(posicion != null && posicion.en(getAncho(), getAlto()))
            return celdas.get(posicion.y).get(posicion.x);
        else
            return null;
    }
    /**
     * Obtiene la celda situada en la posición indicada, si no existe celda en la posición indicada devuelve null.
     * @param x coordenada x
     * @param y coordenada y
     * @return La celda en la posición indicada.
     */
    public Celda getCelda(int x, int y){
        return getCelda(new Punto(x, y));
    }
    
    /**
     * ¡Precaución, esto cambia la celda y borra lo que contiene de la existencia!
     * Incluídos los enemigos...
     * @param pt punto de la celda a cambiar
     * @param c celda nueva
     */
    public void setCelda(Punto pt, Celda c){
        if(getCelda(pt) instanceof Transitable){
            enemigos.removeAll(((Transitable)getCelda(pt)).getEnemigos());
        }
        celdas.get(pt.y).get(pt.x).setMapa(null);
        celdas.get(pt.y).set(pt.x, c);
        c.setMapa(this);
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
     * Esta posición debería ser siempre transitable.
     * @see Personajes.Jugador#setPos
     * @param jugador el jugador que se desea asignar.
     * @throws CeldaObjetivoNoValida si la posición inicial es una celda no transitable
     */
    public void setJugador(Jugador jugador) throws CeldaObjetivoNoValida {
        if(jugador != null)
        {
            this.jugador = jugador;
            jugador.setPos(posicionInicial);
            jugador.visitarRango();
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
     * @param enemigo el enemigo a añadir.
     * @return {@code true} si el enemigo fue agregado al mapa
     * @throws CeldaObjetivoNoValida si la posición del enemigo corresponde a una
     *          celda no disponible.
     * @see Celda#estaDisponible
     */
    public boolean addEnemigo(Enemigo enemigo) throws CeldaObjetivoNoValida {
        if(!enemigos.contains(enemigo))
            if(getCelda(enemigo.getPos()) != null){
                if(getCelda(enemigo.getPos()).estaDisponible()){
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
     * @param enemigo el enemigo a eliminar.
     */
    public void remEnemigo(Enemigo enemigo){
        if(enemigos.contains(enemigo)){
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
     * @return Punto con la posición inicial para el jugador en este mapa.
     */
    public Punto getPosicionInicial() {
        return posicionInicial.clone();
    }
    /**
     * Cambia la posición inicial del mapa
     * @param posicionInicial nuevo punto para la posición inicial del
     * jugador en el mapa. Debe ser una celda transitable
     * @throws CeldaObjetivoNoValida si la celda correspondiente al nuevo punto
     *          no existe o es no transitable.
     */
    public void setPosicionInicial(Punto posicionInicial) throws CeldaObjetivoNoValida {
        if(getCelda(posicionInicial) instanceof Transitable)
            this.posicionInicial = posicionInicial;
        else
            throw new CeldaObjetivoNoValida(String.format("Error al fijar la posición inicial: %s no es transitable.", posicionInicial.toString()));
    }
    /**
     * Busca una celda y devuelve su posición
     *
     * @param celda la celda a buscar
     * @return posición de la celda buscada, o null si no se encuentra en este mapa
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
     * @see ConstantesMapa#P_OBJETOS_ALEATORIOS
     */
    public void setObjetosAleatorio(){
        double p_objetos = ConstantesMapa.P_OBJETOS_ALEATORIOS;
        int dano_min_arma = ConstantesMapa.DANO_MIN_ARMA;
        int defensa_min_armadura = ConstantesMapa.DEFENSA_MIN_ARMADURA;
        if(juego != null){
            p_objetos *=(1/juego.getModDificultad());
            dano_min_arma *= juego.getModDificultad();
            defensa_min_armadura *= juego.getModDificultad();
        }
        for(ArrayList<Celda> fila: celdas)
            for(Celda ce: fila)
                if(ce instanceof Transitable && Math.abs(r.nextFloat()) <   p_objetos){   
                    Transitable c = (Transitable) ce;
                    do{
                        int rand = Math.abs(r.nextInt()) % 6;  //Añadimos objetos aleatoriamente
                        switch(rand){
                            case 0:
                                c.addObjeto(
                                        new Arma(
                                                Math.round(r.nextDouble() * 100)/100.0,
                                                obtenerNombreObjeto("arma", null),
                                                "Es un arma, y mata.",
                                                r.nextInt(5)+4,
                                                r.nextInt(16)+dano_min_arma,
                                                r.nextBoolean()
                                        )
                                );
                                break;
                            case 1:
                                c.addObjeto(
                                        new Armadura(
                                                obtenerNombreObjeto("armadura", null),
                                                "Es una armadura, y así te matan menos.",
                                                Math.round(r.nextDouble() * 500)/100.0,
                                                r.nextInt(6)+defensa_min_armadura,
                                                r.nextInt(21)+5,
                                                r.nextInt(21)+5
                                        )
                                );
                                break;
                            case 2:
                                c.addObjeto(
                                        new Binoculares(
                                                obtenerNombreObjeto("binoculares", null),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(5)+1
                                        )
                                );
                                break;
                            case 3:
                                c.addObjeto(
                                        new Botiquin(
                                                obtenerNombreObjeto("botiquin", null),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(16)+5
                                        )
                                );
                                break;
                            case 4:
                                c.addObjeto(
                                        new ToritoRojo(
                                                obtenerNombreObjeto("toritoColorado", null),
                                                Math.round(r.nextDouble()*50)/100.0,
                                                r.nextInt(16)+5
                                        )
                                );
                                break;
                            case 5:
                                c.addObjeto(
                                        new Explosivo(
                                            Math.round(r.nextDouble()*100)/100.0,
                                                obtenerNombreObjeto("explosivo", null)
                                        )
                                );
                                break;
                        }
                    }while(r.nextFloat() < 0.35);
                }
    }
    /**
     * Genera enemigos de forma aleatoria por el mapa.
     * @see ConstantesMapa#P_ENEMIGOS
     */
    public void setEnemigosAleatorio(){
        int[] enePos = new int[2];
        double p_enemigos;
        if(juego != null)
            p_enemigos = ConstantesMapa.P_ENEMIGOS*juego.getModDificultad();
        else
            p_enemigos = ConstantesMapa.P_ENEMIGOS;
        
        for(int pasada=0; pasada < 10; pasada++)
            for(enePos[0]=0; enePos[0] < getAlto(); enePos[0]++)
                for(enePos[1]=0; enePos[1] < getAncho(); enePos[1]++)
                {
                    
                    if(Math.abs(r.nextFloat()) < p_enemigos
                                && getCelda(new Punto(enePos[1], enePos[0])) instanceof Transitable)
                    {
                        try
                        {
                            Enemigo ene;
                            String[] nombres = {"Samuel","David","Lara","Patri","Elisa","Jose","Luís","Bea","Tere","Antonio","Alfredo","Eustaquio","Daniel","Einstein","Locke","Kant","Turing","Allan","Tesla","Java","Beethoven","Trotsky","Alfred","Pinochet","Bot","xX_n00bKiller69_Xx","Javi","Snoopy","María","Colón","Euclides","Platón","Epicuro","Aristarco de Samos","Isabel","Felipe","Carlos","Juan","Filipino","Alien","Marciano","Enemigo","Alberto"};
                            String[] adjetivos = {" el sabio",""," el necio"," el valiente"," el que foca"," el masticatanques"," el rebientapollos"," el encofrador"," el artista"," el empalador"," el inútil"," el bárbaro"," el pesado"," el afilalápices"," el unicornio"," el alien"," el marciano"," el sabrosón"};
                            String nombreEne = obtenerNombreEnemigo(
                                                String.format("%s%s", nombres[r.nextInt(nombres.length)], r.nextFloat()>0.75?adjetivos[r.nextInt(adjetivos.length)]:"")
                                            ,null);
                            if(r.nextFloat() > 0.5)
                                ene = new Sectoid(nombreEne, enePos, juego);
                            else if(r.nextFloat() > 0.25)
                                ene = new LightFloater(nombreEne, enePos, juego);
                            else
                                ene = new HeavyFloater(nombreEne, enePos, juego);
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
     * el jugador puede ver, el resto del mapa lo cubrirán almohadillas('#'). <br>
     * Usada en {@code ConsolaNormal}.
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
    
    /**
     * Obtiene una copia del array de enemigos del mapa
     * @return Una copia de la lista de enemigos en el mapa
     */
    public ArrayList<Enemigo> getEnemigos(){
        return (ArrayList) enemigos.clone();
    }
    
    /**
     * Hace la celda indicada transitable, y deja en ella unos escombros si así se desea.
     * @param posDe posición de la celda a hacer transitable
     * @param dejarEscombros {@code true} si se desea dejar escombros
     */
    public void hacerTransitable(Punto posDe, boolean dejarEscombros) {
        if(posDe.en(getAncho(), getAlto())){
            Celda c = getCelda(posDe);
            if(c instanceof NoTransitable){
                Transitable t = new Transitable();
                if(dejarEscombros && c.tipo == ConstantesMapa.MURO)
                    t.addObjeto(new Escombros());
                t.tipo = ConstantesMapa.SUELOS[r.nextInt(ConstantesMapa.SUELOS.length)];
                celdas.get(posDe.y).set(posDe.x, t);
                t.setMapa(this);
            }
        }
    }

    /**
     * Obtiene una lista de las posiciones transitables del mapa
     * @return Lista de puntos en los que se encuentran las celdas transitables del mapa
     */
    public ArrayList<Punto> copiaPosicionesTransitables() {
        ArrayList<Punto> al = new ArrayList();
        for(int i=0; i < celdas.size(); i++)
            for(int j=0; j < celdas.get(i).size(); j++)
                if(celdas.get(i).get(j) instanceof Transitable){
                    al.add(new Punto(j, i));
                }
        return al;
    }
    
    /**
     * <p>Obtiene un nombre válido para un objeto en el mapa. </p>
     * <p>Un nombre válido para un objeto es aquel que no contiene espacios y no
     * coincide con el de ningún otro objeto del mapa.</p>
     * @param objeto nombre base del cual se trata de encontrar un nombre válido
     *          igual o similar.
     * @param excluido objeto que no se tiene en cuenta al buscar el nombre
     * @return Un nombre válido basado en el nombre proporcionado.
     */
    public String obtenerNombreObjeto(String objeto, Objeto excluido) {
        int i = -1;
        boolean valido;
        objeto = objeto.replace(" ", "_");
        do{ //Comprobar si está usado hasta encontrar un no usado...
            i++;
            String intento = objeto + (i>0?"_"+i:"");
            valido = true;
            for(Celda c: getCeldas())
                if(c instanceof Transitable && ((Transitable)c).getObjeto(intento) != null
                        && !((Transitable)c).getObjeto(intento).equals(excluido)){
                    valido = false; break;
                }
            if(!valido) continue;
            for(Enemigo e: getEnemigos())
                if(e.tiene(intento, excluido)){
                    valido = false; break;
                }
            if(!valido) continue;
            if(jugador != null && jugador.tiene(intento, excluido))
                valido = false;
        }while(!valido);
        
        return objeto + (i>0?"_"+i:"");
    }
    
    /**
     * <p>Obtiene un nombre de enemigo válido para este mapa.</p>
     * <p>Un nombre de enemigo válido es aquel que no contiene espacios y no
     * coincide con el de ningún otro enemigo en el mapa.</p>
     * @param nombreBase nombre sobre el cual trabajar para obtener un nombre válido
     * @param excluido enemigo no tenido en cuenta a la hora de buscar el nombre
     * @return Un nombre basado en el nombre aportado válido para un nuevo enemigo.
     */
    public String obtenerNombreEnemigo(String nombreBase, Enemigo excluido) {
        String name;
        int veces = 0;
        name = nombreBase.replace(" ", "_");
        boolean exito;
        do{
            exito = true;
            for(Enemigo e: getEnemigos())
                if(e.getNombre().equals(name) && !e.equals(excluido)){
                    name = String.format("%s_%d", nombreBase, ++veces);
                    exito = false;
                    break;
                }
        }while(!exito);
        
        return name;
    }
}
