package Comandos;

import Excepciones.ComandoExcepcion;
import java.util.ArrayList;

/**
 * Comando compuesto de otros comandos, al {@code ejecutar}, los ejecutará uno
 * a uno en orden.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ComandoCompuesto implements Comando{
    private final ArrayList<Comando> comandos;

    /**
     * Crea un {@code ComandoCompuesto} vacío
     * @see ArrayList#ArrayList() 
     */
    public ComandoCompuesto() {
        comandos = new ArrayList();
    }

    /**
     * Crea un {@code ComandoCompuesto} vacío con una capacidad inicial concreta.
     * @param tam capacidad inicial del comando
     * @see ArrayList#ArrayList(int) 
     */
    public ComandoCompuesto(int tam){
        comandos = new ArrayList(tam);
    }

    /**
     * Obtiene la cantidad de comandos que forman el comando compuesto.
     * @return La cantidad de comandos agregados
     * @see ArrayList#size() 
     */
    public int size() {
        return comandos.size();
    }

    /**
     * Añade un comando al comando compuesto
     * @param e comando que se desea añadir
     * @return {@code true}, ver {@link ArrayList#add(java.lang.Object)}
     * @see ArrayList#add(java.lang.Object) 
     */
    public boolean add(Comando e) {
        return comandos.add(e);
    }

    /**
     * Elimina un comando del comando compuesto
     * @param c comando que se desea eliminar
     * @return {@code true} si la lista contiene el comando indicado
     * @see ArrayList#remove(java.lang.Object) 
     */
    public boolean remove(Comando c) {
        return comandos.remove(c);
    }

    /**
     * Obtiene una copia de la lista de comandos
     * @return Una copia de la lista de comandos
     */
    public ArrayList<Comando> getComandos() {
        return (ArrayList) comandos.clone();
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        for(Comando c: comandos)
            c.ejecutar();
    }
}
