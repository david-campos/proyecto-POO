/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Excepciones.ObjetoNoUsableException;
import Personajes.Personaje;
import java.util.Objects;

/**
 *
 * @author david.campos
 */
public abstract class Objeto {
    //nombre y descripcion son protected, pues son finales y no suponen muchos problemas
    protected final String nombre;
    protected final String descripcion;
    private double peso;
    
    private static long nombreDef=0;
    
    public Objeto(double peso, String nombre, String descripcion){
        if(nombre != null && !nombre.isEmpty())
            this.nombre = nombre;
        else
            this.nombre = siguienteNombrePorDefecto();
        
        if(descripcion != null)
            this.descripcion = descripcion;
        else
            this.descripcion = "";
        
        setPeso(peso);
    }
    
    /**
     * Acción realizada al usar el objeto, no hace nada,
     * sobrecargar para realizar una acción.
     * @param p Personaje que usa el objeto
     */
    public void usar(Personaje p) throws ObjetoNoUsableException{ throw new ObjetoNoUsableException("Ouh... parece que esto no se usa."); }
    /**
     * Acción realizada al equipar el objeto, no hace nada,
     * sobrecargar para realizar una acción.
     * @param p Personaje que equipa el objeto
     */
    public void alEquipar(Personaje p){}
    /**
     * Acción realizada al desequipar el objeto, no hace nada,
     * sobrecargar para realizar una acción.
     * @param p Personaje que desequipa el objeto
     */
    public void alDesequipar(Personaje p){}
    
    /**
     * Devuelve el peso del objeto.
     * @return El peso del objeto.
     */
    public final double getPeso() {
        return peso;
    }
    /**
     * Fija el peso del objeto
     * @param Peso El peso que se le quiere dar al objeto.
     */
    public final void setPeso(double Peso) {
        if(Peso > 0)
            this.peso = Peso;
    }

    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    
    
    private String siguienteNombrePorDefecto(){
        return String.format("objeto_%d", nombreDef++);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Objeto other = (Objeto) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t%s\n\tPeso: %fkg", nombre, descripcion, peso);
    }
    
}
