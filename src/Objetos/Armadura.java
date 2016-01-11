package Objetos;

import Personajes.Personaje;

/**
 * Una armadura, que no tiene por que ser arma ni dura, puede ser un chaleco, o una chaqueta de plátanos.
 * David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Armadura extends Objeto{
    private int defensa;
    private int boostVida;
    private int boostEnergia;
    private Personaje ultimoEquipado;
    
    /**
     * Crea una nueva armadura
     * @param nombre nombre de la armadura
     * @param descripcion descripcion de la armadura
     * @param peso peso de la armadura en la mochila
     * @param defensa defensa aportada por la armadura
     * @param boostVida cantidad de vida máxima agregada cuando se lleva equipada
     * @param boostEnergia cantidad de energía máxima agregada cuando se lleva equipada
     */
    public Armadura(String nombre, String descripcion, double peso, int defensa, int boostVida, int boostEnergia){
        super(peso, nombre, descripcion);
        setDefensa(defensa);
        setBoostVida(boostVida);
        setBoostEnergia(boostEnergia);
        ultimoEquipado = null;
    }

    @Override
    public void alDesequipar(Personaje p) {
        if(p!=null && p.equals(ultimoEquipado)){
            p.setMaxVida(p.getMaxVida() - getBoostVida());
            p.setEnergiaPorTurno(p.getEnergiaPorTurno() - getBoostEnergia());
            ultimoEquipado = null;
        }
    }
    @Override
    public void alEquipar(Personaje p) {
        if(p!=null){
            p.setMaxVida(p.getMaxVida() + getBoostVida());
            p.setEnergiaPorTurno(p.getEnergiaPorTurno() + getBoostEnergia());
            ultimoEquipado = p;
        }
    }
    
    /**
     * Cambia la defensa aportada por la armadura
     * @param defensa nueva defensa
     */
    public final void setDefensa(int defensa){
        if(defensa > 0)
            this.defensa = defensa;
    }
    /**
     * Obtiene la defensa aportada por la armadura
     * @return La defensa aportada por esta armadura
     */
    public final int getDefensa() {
        return defensa;
    }
    /**
     * Obtiene la vida máxima agregada por esta armadura al ser equipada.
     * @return La vida máxima agregada por la armadura al llevarla equipada.
     */
    public final int getBoostVida() {
        return boostVida;
    }
    /**
     * Cambia la vida máxima agregada por esta armadura al ser equipada.
     * @param boostVida la vida máxima agregada por la armadura al llevarla equipada.
     */
    public final void setBoostVida(int boostVida) {
        //Pueden quitar vida... así que vale cualquier cosa
        Personaje p = ultimoEquipado;
        alDesequipar(p);
        this.boostVida = boostVida;
        alEquipar(p);
    }
    /**
     * Boost de energía de la armadura
     * @return La cantidad de energía extra que la armadura aporta al llevarla
     *          equipada.
     */
    public final int getBoostEnergia() {
        return boostEnergia;
    }
    /**
     * Cambia el boost de energía.
     * @param boostEnergia energía extra aportada por la armadura al llevarla equipada
     */
    public final void setBoostEnergia(int boostEnergia) {
        //Puede quitar energía el boost, vale cualquier valor
        Personaje p = ultimoEquipado;
        alDesequipar(p);
        this.boostEnergia = boostEnergia;
        alEquipar(p);
    }

    @Override
    public String toString() {
        return String.format("%s\n\tDefensa: %d\n\t+%d vida\n\t+%d energía", super.toString(), defensa, boostVida, boostEnergia);
    } 
}
