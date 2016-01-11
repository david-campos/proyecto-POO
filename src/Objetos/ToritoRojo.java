package Objetos;

import Personajes.Personaje;

/**
 * Un objeto toritorojo, cura energía al portador al ser usado, pero luego se la resta en el siguiente turno.
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ToritoRojo extends Objeto{
    private int plusEnergia;
    
    /**
     * Crea un nuevo torito rojo
     * @param nombre nombre de la lata de torito rojo
     * @param peso peso de la lata
     * @param plus plus de energía aportado al valiente que lo consume (usa)
     */
    public ToritoRojo(String nombre, double peso, int plus){
        super(peso, nombre, "Aumenta la energía al usar, pero la disminuye en el siguiente turno.");
        //plusVida puede ser negativo, se acepta cualquier valor
        this.plusEnergia = plus;
    }
    
    @Override
    public void usar(Personaje p){
        if(p != null){
            p.setEnergia(p.getEnergia() + plusEnergia);
            p.setToreado(p.getToreado() + plusEnergia);
            p.getMochila().remObjeto(this);
            Utilidades.Sonido.play("yeeha");
        }
    }

    /**
     * Obtiene la energía aportada por el torito al ser usado
     * @return La energía aportada por el torito rojo al ser usado
     */
    public final int getPlusEnergia() {
        return plusEnergia;
    }
    /**
     * Cambia la energía aportada por el torito al ser usado.
     * @param plusEnergia nueva energía aportada por el torito al ser usado
     */
    public void setPlusEnergia(int plusEnergia) {
        this.plusEnergia = plusEnergia;
    }
    
    @Override
    public String toString() {
        return String.format("%s\n\tEnergía: +%d", super.toString(), plusEnergia);
    }
}
