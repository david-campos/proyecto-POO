package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 * Comando que lleva al jugador a equipar un objeto determinado de su mochila.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * 
 * @see Jugador#equipar(java.lang.String)
 */
public final class ComandoEquipar implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     * Crea un nuevo ComandoEquipar para el jugador y nombre indicados
     * @param jug jugador sobre el que ejecutar el comando
     * @param nombre nombre del objeto que se desea equipar
     */
    public ComandoEquipar(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     * Obtiene el jugador
     * @return el jugador que equipará el objeto
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Fija el jugador
     * @param jug jugador que equipará el objeto
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el nombre de objeto que se tratará de equipar
     * @return nombre del objeto que se tratará de equipar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del objeto que se tratará de equipar
     * @param nombre nombre del objeto que se tratará de equipar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                try{
                    jug.equipar(nombre);
                }catch(Exception e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado el nombre del objeto a equipar...");
    }
    
}
