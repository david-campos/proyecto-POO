package Mapa;

import Personajes.Enemigo;

public abstract class Celda {
    public int tipo;
    protected Mapa mapa;
    private boolean bomba;

    public Celda(int tipo, Mapa mapa) {
        this.tipo = tipo;
        this.mapa = mapa; //Puede ser null
        this.bomba = false;
    }

    public boolean isBomba() {
        return bomba;
    }
    public void setBomba(boolean bomba) {
        this.bomba = bomba;
    }

    public String representacion(){
        if(this instanceof Transitable)
            return ConstantesMapa.CE_REPR_TRANS[tipo];
        else
            return ConstantesMapa.CE_REPR_NOTRANS[tipo];
    }
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
     * Setea el mapa al que pertenece la celda, esta acción solo puede realizarse
     * una vez (las celdas no se pueden mover de mapa).
     * Esta función solo debería ser llamada por el constructor de Mapa, pues la
     * clase Mapa no dispone de un método setCelda por lo que se generaría desincronización.
     * @param map El mapa al que asignar la celda.
     */
    public void setMapa(Mapa map){
        if(map != null && mapa == null) //Comprobamos que mapa sea null porque solo se puede setear el mapa una vez
            mapa = map;
    }
    public void detonar(){
        mapa.getJuego().log("¡Una bomba ha explotado!");
        detonar(0, null);
        setBomba(false);
        mapa.getJuego().impMapa();
    }
    private void detonar(int lejania, String dir){
        if(lejania >= ConstantesMapa.EXPLOSION_MAX_RAD)
            return;
        if(this instanceof Transitable) {
            Transitable transitable = (Transitable) this;
            for(Enemigo e: transitable.getEnemigos())
            {
                mapa.getJuego().log(e.getNombre() + " ha muerto en la explosión.");
                e.morir();
            }
            if(mapa.getJugador().getPos().equals(mapa.getPosDe(this))){
                mapa.getJuego().log("Has muerto en la explosión.");
                mapa.getJugador().setVida(0);
            }
            
            Punto pt = mapa.getPosDe(this);
            tipo = ConstantesMapa.BOQUETE;
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
            mapa.hacerTransitable(mapa.getPosDe(this), true);
        }
    }
}