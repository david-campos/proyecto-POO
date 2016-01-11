package Objetos;

import Excepciones.ObjetoNoUsableException;
import Personajes.Personaje;
import java.util.Objects;

/**
 * Clase abstracta para los objetos del juego. Es la raíz de la jerarquía de
 * objetos.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class Objeto {

    /**
     * Nombre del objeto, debe ser único en cada mapa.
     * @see Mapa.Mapa#obtenerNombreObjeto
     */
    protected String nombre;

    /**
     * Descripción del objeto
     */
    protected String descripcion;
    private double peso;
    
    private static long nombreDef=0;
    
    /**
     * Crea un nuevo objeto
     * @param peso peso del objeto
     * @param nombre nombre del objeto, debe ser único en el mapa
     * @param descripcion descripción del objeto
     * @see Mapa.Mapa#obtenerNombreObjeto(java.lang.String, Objetos.Objeto)
     */
    public Objeto(double peso, String nombre, String descripcion){
        if(nombre != null && !nombre.isEmpty())
            setNombre(nombre);
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
     * @throws ObjetoNoUsableException si el objeto no se puede usar
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
     * @param Peso el peso que se le quiere dar al objeto.
     */
    public final void setPeso(double Peso) {
        if(Peso > 0)
            this.peso = Peso;
    }

    /**
     * Obtiene el valor de nombre del objeto
     * @return El nombre del objeto
     */
    public final String getNombre() {
        return nombre;
    }
    /**
     * Obtiene el valor de descripción del objeto
     * @return La descripción del objeto
     */
    public final String getDescripcion() {
        return descripcion;
    }
    /**
     * <p>Cambia el nombre del objeto, el nombre debe ser único para cada
     * mapa.</p>
     * <p>Para obtener un nombre válido en un mapa concreto, utilice
     * {@link Mapa.Mapa#obtenerNombreObjeto}.
     * @param nombre nombre que se le quiere dar al objeto
     */
    public final void setNombre(String nombre) {
        this.nombre = nombre.replace(" ", "_");
    }
    /**
     * Cambia la descripción del objeto
     * @param descripcion descripción del objeto que se quiere asignar
     */
    public final void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        return String.format("%s\n\t%s\n\tPeso: %.3fkg", nombre, descripcion, peso);
    }
    
}
