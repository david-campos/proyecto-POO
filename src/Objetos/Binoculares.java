/*
 * 
 */
package Objetos;

import Personajes.Personaje;

/**
 * Un objeto binoculares, aumenta el rango del portador al ser equipado.
 * @author david.campos
 */
public final class Binoculares extends Objeto{
    private final int plusRango;
    
    public Binoculares(String nombre, double peso, int plus){
        super(peso, nombre, "Al equipar, aumenta el rango de visión.");
        //plusVida puede ser negativo, se acepta cualquier valor
        this.plusRango = plus;
    }
    
    @Override
    public void alEquipar(Personaje p) {
        p.setRango(p.getRango() + plusRango);
    }
    @Override
    public void alDesequipar(Personaje p) {
        p.setRango(p.getRango() - plusRango);
    }

    public final int getPlusRango() {
        return plusRango;
    }
    
    @Override
    public String toString() {
        return String.format("%s\n\tRango: +%d", super.toString(), plusRango);
    }
}