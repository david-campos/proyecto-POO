package Objetos;

import Personajes.Personaje;

/**
 * Un objeto botiquín, cura vida al portador al ser usado
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class Botiquin extends Objeto{
    private int plusVida;
    
    /**
     * Crea un nuevo botiquín
     * @param nombre nombre del botiquín (ver {@link Objeto#setNombre})
     * @param peso peso del botiquín en la mochila
     * @param plusVida plus de vida aportado al usar
     */
    public Botiquin(String nombre, double peso, int plusVida){
        super(peso, nombre, "Cura vida al usar.");
        //plusVida puede ser negativo, se acepta cualquier valor
        this.plusVida = plusVida;
    }
    
    @Override
    public void usar(Personaje p){
        p.setVida(p.getVida() + plusVida);
        p.getMochila().remObjeto(this);
    }

    /**
     * Obtiene la vida aportada por este botiquín al ser usado.
     * @return La vida aportada por el botiquín al ser usado.
     */
    public int getPlusVida() {
        return plusVida;
    }
    /**
     * Cambia la vida aportada por este botiquín al ser usado.
     * @param plusVida nueva vida aportada por este botiquín al ser usado.
     */
    public void setPlusVida(int plusVida) {
        this.plusVida = plusVida;
    }
    
    @Override
    public String toString() {
        return String.format("%s\n\tVida: +%d", super.toString(), plusVida);
    }
}