/*
 * 
 */
package Objetos;

import Personajes.Personaje;

/**
 * Un objeto botiquín, cura vida al portador al ser usado
 * @author david.campos
 */
public final class Botiquin extends Objeto{
    private final int plusVida;
    
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

    public final int getPlusVida() {
        return plusVida;
    }
    
    @Override
    public String toString() {
        return String.format("%s\n\tVida: +%d", super.toString(), plusVida);
    }
}