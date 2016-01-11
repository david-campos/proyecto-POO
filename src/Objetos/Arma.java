package Objetos;

/**
 * Un arma, las hay a una mano y a dos manos, hacen dañito. No usar contra cosas lindas :c
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Arma extends Objeto{
    /**
     * Constante que representa que el arma es a una mano.
     */
    public static final boolean ARMA_UNA_MANO = false;
    /**
     * Constante que representa que el arma se porta a dos manos.
     */
    public static final boolean ARMA_DOS_MANOS = true;
    private int dano;
    private int rango;
    private boolean tipo;
    
    /**
     * Crea una nueva arma.
     * @param peso peso del arma en la mochila
     * @param nombre nombre del arma (debe ser único para cada mapa,
     *          ver {@link Mapa.Mapa#obtenerNombreObjeto})
     * @param descripcion descripción del arma
     * @param rango rango en el que el arma puede atacar
     * @param dano daño base causado por el arma
     * @param tipo tipo de arma ({@link #ARMA_UNA_MANO} o {@link #ARMA_DOS_MANOS})
     */
    public Arma(double peso, String nombre, String descripcion, int rango, int dano, boolean tipo){
        super(peso, nombre, descripcion);
        
        if(rango < 0)
            this.rango = 1;
        else
            this.rango = rango;
        if(dano < 0)
            this.dano = 1;
        else
            this.dano = dano;
        
        this.tipo = tipo;
    }

    /**
     * Obtiene el tipo de arma
     * @return {@link #ARMA_UNA_MANO} si es a una mano o {@link #ARMA_DOS_MANOS}
     *          si es a dos manos.
     */
    public boolean getTipo() {
        return tipo;
    }

    /**
     * Cambia el tipo de arma
     * @param tipo tipo que se desea fijar
     * @see #ARMA_DOS_MANOS
     * @see #ARMA_UNA_MANO
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el daño base causado por el arma
     * @return El daño base causado por el arma
     */
    public final int getDano() {
        return dano;
    }
    /**
     * Obtiene el rango de ataque del arma
     * @return El rango de ataque del arma
     */
    public final int getRango() {
        return rango;
    }

    /**
     * Cambia el daño base causado por el arma
     * @param dano nuevo daño causado por el arma
     */
    public void setDano(int dano) {
        if(dano > 0)
            this.dano = dano;
    }
    /**
     * Cambia el rango de ataque del arma
     * @param rango rango de ataque del arma
     */
    public void setRango(int rango) {
        if(rango > 0)
            this.rango = rango;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t%s\n\tDaño causado: %d\n\tRango de ataque: %d",super.toString(), getTipo()==Arma.ARMA_UNA_MANO?"A una mano":"A dos manos", dano, rango);
    }
}