package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoUsableException;
import Personajes.Jugador;

/**
 * Comando que lleva al jugador a usar un objeto, el efecto del objeto
 * al ser usado, depende del objeto en cuestión.<br>
 * El objeto debe estar en la mochila del jugador.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @see Objetos.Objeto#usar(Personajes.Personaje) 
 */
public final class ComandoUsar implements Comando{
    private Jugador jug;
    private String nombre;
    
    /**
     * Instancia el comando para un jugador y nombre de objeto concretos
     * @param jug jugador que usa el objeto
     * @param nombre nombre del objeto usado
     */
    public ComandoUsar(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     * Obtiene el jugador que usa el objeto
     * @return El jugador que usará el objeto
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Cambia el jugador que usará el objeto
     * @param jug jugador que usará el objeto
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el nombre del objeto que se desea usar
     * @return El nombre del objeto que se desea usar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del objeto que se desea usar
     * @param nombre nombre del objeto que se desea usar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                try {
                    jug.usar(nombre);
                } catch (ObjetoNoUsableException | ObjetoNoEncontradoException ex) {
                    throw new ComandoExcepcion(ex.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado el nombre del objeto a usar...");
    }
    
}
