/*
 * 
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.PersonajeException;
import Personajes.Jugador;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class ComandoUsar implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     *
     * @param jug
     * @param nombre
     */
    public ComandoUsar(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     *
     * @param jug
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     *
     * @throws ComandoExcepcion
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                try{
                    jug.usar(nombre);
                }catch(PersonajeException e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado el nombre del objeto a usar...");
    }
    
}
