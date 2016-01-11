package Mapa;

import Utilidades.Punto;
import Personajes.Enemigo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Una celda del mapa. Las celdas contienen una referencia al mapa en que se
 * encuentran, y pueden ser transitables o no transitables.</p>
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @see Transitable
 * @see NoTransitable
 * @see Mapa
 */
public abstract class Celda {
    /**
     * Tipo de la celda, ver {@link ConstantesMapa}.
     */
    public int tipo; //tipo es público, tarde para cambiar esto a estas alturas :s

    /**
     * Mapa en el que se encuentra la celda.
     * @see #setMapa
     */
    protected Mapa mapa;
    private boolean bomba;
    private boolean visitada;

    /**
     * Crea una nueva instancia de celda, con el tipo y mapa especificados
     * @param tipo tipo de celda, indica la representación de la celda. Ver {@link ConstantesMapa}.
     * @param mapa mapa al que pertenece la celda
     */
    public Celda(int tipo, Mapa mapa) {
        this.tipo = tipo;
        this.mapa = mapa;
        this.bomba = false;
        visitada = false;
    }

    /**
     * Crea una instancia de Celda con tipo 0, mapa null, sin bomba y no visitada
     */
    public Celda() {
        tipo = 0;
        mapa = null;
        bomba = false;
        visitada = false;
    }
    
    /**
     * Indica si la celda es bomba, es decir, si explotará al finalizar el turno.
     * @return {@code true} si la celda es bomba
     * @see #detonar
     */
    public boolean isBomba() {
        return bomba;
    }

    /**
     * Cambia el estado de bomba de la celda. Una celda que es bomba explotará
     * finalizado el turno.
     * @param bomba {@code true} para hacer que la bomba explote finalizado el turno
     */
    public void setBomba(boolean bomba) {
        this.bomba = bomba;
    }

    /**
     * Devuelve una representación de texto adecuada para la celda
     * @return Un texto en UTF-8 que representa la celda adecuadamente en una
     *          consola.
     * @see ConstantesMapa
     */
    public String representacion(){
        if(this instanceof Transitable)
            return ConstantesMapa.CE_REPR_TRANS[tipo];
        else
            return ConstantesMapa.CE_REPR_NOTRANS[tipo];
    }

    /**
     * <p>Devuelve una cadena con el nombre de la imagen adecuada para la
     * representación de la celda (las imágenes están en 'img/' y tienen
     * extensión '.png').</p>
     * <p>Podría cargarse directamente pero es recomendable usar el manejador
     * de la caché de imágenes.</p>
     * @return El nombre del archivo de imagen.
     * @see ConstantesMapa
     * @see Editor.Editor#obtenerImagen(java.lang.String)
     * @see Juego.ConsolaGrafica#obtenerImagen(java.lang.String)
     */
    public String representacionGrafica(){
        if(this instanceof Transitable)
            return ConstantesMapa.CE_REPG_TRANS[tipo];
        else
            return ConstantesMapa.CE_REPG_NOTRANS[tipo];
    }
    /**
     * Obtiene la referencia al mapa en el que se sitúa la celda.
     * @return El mapa en que se sitúa la celda.
     */
    public Mapa getMapa(){
        return mapa;
    }
    /**
     * Setea el mapa al que pertenece la celda. No genera en mapa ninguna
     * referencia a la celda.
     * @param map El mapa al que asignar la celda.
     */
    public void setMapa(Mapa map){
        mapa = map;
    }

    /**
     * Detona la celda (la hace explotar)
     */
    public void detonar(){
        mapa.getJuego().log("¡Una bomba ha explotado!");
        detonar(0, null); //Llamada a la función recursiva
        setBomba(false);
        mapa.getJuego().impMapa();
    }
    private void detonar(int lejania, String dir){
        if(lejania >= ConstantesMapa.EXPLOSION_MAX_RAD)
            return;
        
        Utilidades.Sonido.play("explosion");
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Celda.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(this instanceof Transitable) {
            Transitable transitable = (Transitable) this;
            for(Enemigo e: transitable.getEnemigos())
            {
                mapa.getJuego().log(e.getNombre() + " ha muerto en la explosión.");
                e.setVida(0);
                e.comprobarMuerteMorir();
            }
            if(mapa.getJugador().getPos().equals(mapa.getPosDe(this))){
                mapa.getJuego().log("Has muerto en la explosión.");
                mapa.getJugador().setVida(0);
            }
            
            Punto pt = mapa.getPosDe(this);
            tipo = ConstantesMapa.BOQUETE;
            mapa.getJuego().impMapa();
            if(dir == null)
            {
                if(mapa.getCelda(pt.x, pt.y - 1) != null) mapa.getCelda(pt.x, pt.y - 1).detonar(lejania+1, "n");
                if(mapa.getCelda(pt.x, pt.y + 1) != null) mapa.getCelda(pt.x, pt.y + 1).detonar(lejania+1, "s");
                if(mapa.getCelda(pt.x + 1, pt.y) != null) mapa.getCelda(pt.x + 1, pt.y).detonar(lejania+1, "e");
                if(mapa.getCelda(pt.x - 1, pt.y) != null) mapa.getCelda(pt.x - 1, pt.y).detonar(lejania+1, "o");
            }else switch(dir){
                case "n":
                    if(mapa.getCelda(pt.x, pt.y - 1) != null)
                        mapa.getCelda(pt.x, pt.y - 1).detonar(lejania+1, "n");
                    break;
                case "s":
                    if(mapa.getCelda(pt.x, pt.y + 1) != null)
                       mapa.getCelda(pt.x, pt.y + 1).detonar(lejania+1, "s");
                    break;
                case "e":
                    if(mapa.getCelda(pt.x + 1, pt.y) != null)
                       mapa.getCelda(pt.x + 1, pt.y).detonar(lejania+1, "e");
                    break;
                case "o":
                    if(mapa.getCelda(pt.x - 1, pt.y) != null)
                        mapa.getCelda(pt.x - 1, pt.y).detonar(lejania+1, "o");
                    break;
            }
        }else{
            Punto pt = mapa.getPosDe(this);
            mapa.hacerTransitable(pt, true);
            mapa.getCelda(pt).tipo = 1;
            mapa.getJuego().impMapa();
        }
    }

    /**
     * Indica si la celda ha sido visitada por el jugador o no. Las celdas
     * visitadas se dibujan en un tono más oscuro, sin mostrar los enemigos
     * que en ellas se encuentran, si no están en tu rango de visión.
     * @return {@code true} cuando la celda ha sido visitada
     */
    public boolean isVisitada() {
        return visitada;
    }

    /**
     * Fija la celda como visitada o no.
     * @param visitada {@code true} si ha sido visitada
     * @see #isVisitada
     */
    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }
    
    /**
     * Indica si la celda está disponible o no para el tránsito de enemigos
     * y jugador.
     * @return {@code true} si la celda está disponible.
     */
    public abstract boolean estaDisponible();
}