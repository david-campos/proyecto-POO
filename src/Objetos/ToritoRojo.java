/*
 * 
 */
package Objetos;

import Personajes.Personaje;

/**
 * Un objeto toritorojo, cura energía al portador al ser usado, pero luego se la resta en el siguiente turno.
 * @author david.campos
 */
public final class ToritoRojo extends Objeto{
    private int plusEnergia;
    
    public ToritoRojo(String nombre, double peso, int plus){
        super(peso, nombre, "Aumenta la energía al usar, pero la disminuye en el siguiente turno.");
        //plusVida puede ser negativo, se acepta cualquier valor
        this.plusEnergia = plus;
    }
    
    @Override
    public void usar(Personaje p){
        p.setEnergia(p.getEnergia() + plusEnergia);
        p.setToreado(p.getToreado() + plusEnergia);
        p.getMochila().remObjeto(this);
        Utilidades.Sonido.play("yeeha");
    }

    public final int getPlusEnergia() {
        return plusEnergia;
    }

    public void setPlusEnergia(int plusEnergia) {
        this.plusEnergia = plusEnergia;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s\n\tEnergía: +%d", super.toString(), plusEnergia);
    }
}
