/*
 * 
 */
package Objetos;

import Personajes.Personaje;

/**
 * Una armadura, que no tiene por que ser arma ni dura, puede ser un chaleco, o una chaqueta de plátanos.
 * @author david.campos
 */
public class Armadura extends Objeto{
    private int defensa;
    private int boostVida;
    private int boostEnergia;
    
    /**
     *
     * @param nombre
     * @param descripcion
     * @param peso
     * @param defensa
     * @param boostVida
     * @param boostEnergia
     */
    public Armadura(String nombre, String descripcion, double peso, int defensa, int boostVida, int boostEnergia){
        super(peso, nombre, descripcion);
        setDefensa(defensa);
        setBoostVida(boostVida);
        setBoostEnergia(boostEnergia);
    }

    @Override
    public void alDesequipar(Personaje p) {
        p.setMaxVida(p.getMaxVida() - getBoostVida());
        p.setEnergiaPorTurno(p.getEnergiaPorTurno() - getBoostEnergia());
    }
    @Override
    public void alEquipar(Personaje p) {
        p.setMaxVida(p.getMaxVida() + getBoostVida());
        p.setEnergiaPorTurno(p.getEnergiaPorTurno() + getBoostEnergia());
    }
    
    /**
     *
     * @param defensa
     */
    public final void setDefensa(int defensa){
        if(defensa > 0)
            this.defensa = defensa;
    }

    /**
     *
     * @return
     */
    public int getDefensa() {
        return defensa;
    }

    /**
     *
     * @return
     */
    public int getBoostVida() {
        return boostVida;
    }

    /**
     *
     * @param boostVida
     */
    public final void setBoostVida(int boostVida) {
        //Pueden quitar vida... así que vale cualquier cosa
        this.boostVida = boostVida;
    }

    /**
     *
     * @return
     */
    public int getBoostEnergia() {
        return boostEnergia;
    }

    /**
     *
     * @param boostEnergia
     */
    public final void setBoostEnergia(int boostEnergia) {
        //Puede quitar energía el boost, vale cualquier valor
        this.boostEnergia = boostEnergia;
    }

    @Override
    public String toString() {
        return String.format("%s\n\tDefensa: %d\n\t+%d vida\n\t+%d energía", super.toString(), defensa, boostVida, boostEnergia);
    } 
}
