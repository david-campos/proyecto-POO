package Objetos;

import Personajes.Personaje;

/**
 * Un objeto binoculares, aumenta el rango del portador al ser equipado.
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class Binoculares extends Objeto{
    private int plusRango;
    private Personaje ultimoEquipado;
    
    /**
     * Crea unos nuevos binoculares
     * @param nombre nombre de los binoculares
     * @param peso peso en la mochila de los mismos
     * @param plus plus de radio de visión aportado
     */
    public Binoculares(String nombre, double peso, int plus){
        super(peso, nombre, "Al equipar, aumenta el rango de visión.");
        //plusVida puede ser negativo, se acepta cualquier valor
        this.plusRango = plus;
        ultimoEquipado = null;
    }
    
    @Override
    public void alEquipar(Personaje p) {
        if(p!=null){
            p.setRango(p.getRango() + plusRango);
            ultimoEquipado = p;
        }
    }
    @Override
    public void alDesequipar(Personaje p) {
        if(p!=null && p.equals(ultimoEquipado)){
            p.setRango(p.getRango() - plusRango);
            ultimoEquipado = null;
        }
    }

    /**
     * Obtiene el valor de rango extra aportado por estos binoculares al llevarlos
     * equipados
     * @return Valor de rango extra de los binoculares
     */
    public final int getPlusRango() {
        return plusRango;
    }
    /**
     * Modifica el valor de rango extra de estos binoculares al llevarlos equipados
     * @param plusRango nuevo valor de rango extra
     */
    public void setPlusRango(int plusRango) {
        Personaje p = ultimoEquipado;
        alDesequipar(p);
        this.plusRango = plusRango;
        alEquipar(p);
    }
    
    @Override
    public String toString() {
        return String.format("%s\n\tRango: +%d", super.toString(), plusRango);
    }
}