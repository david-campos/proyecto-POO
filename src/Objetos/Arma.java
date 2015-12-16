/*
 * 
 */
package Objetos;

/**
 * Un arma, las hay a una mano y a dos manos, hacen dañito. No usar contra cosas lindas :c
 * @author david.campos
 */
public class Arma extends Objeto{

    /**
     *
     */
    public static final boolean ARMA_UNA_MANO = false;

    /**
     *
     */
    public static final boolean ARMA_DOS_MANOS = true;
    private final int dano;
    private final int rango;
    private boolean tipo;
    
    /**
     *
     * @param peso
     * @param nombre
     * @param descripcion
     * @param rango
     * @param dano
     * @param tipo
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
     *
     * @return
     */
    public boolean getTipo() {
        return tipo;
    }

    /**
     *
     * @param tipo
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    /**
     *
     * @return
     */
    public final int getDano() {
        return dano;
    }

    /**
     *
     * @return
     */
    public final int getRango() {
        return rango;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t%s\n\tDaño causado: %d\n\tRango de ataque: %d", getTipo()==Arma.ARMA_UNA_MANO?"A una mano":"A dos manos",super.toString(), dano, rango);
    }
}