package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.EnergiaInsuficienteException;
import Excepciones.ImposibleCogerExcepcion;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Personajes.Jugador;

/**
 * Comando que lleva al jugador a recoger un objeto de la celda.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoCoger implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     * Crea un nuevo {@code ComandoCoger}
     * @param jug jugador que recoger el objeto
     * @param nombre nombre del objeto
     */
    public ComandoCoger(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     * Obtiene el jugador
     * @return el jugador que recogerá el objeto
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Fija el jugador
     * @param jug jugador que recogerá el objeto
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el nombre del objeto a coger
     * @return el nombre del objeto a coger
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija el nombre del objeto a coger
     * @param nombre nombre del objeto a coger
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                try{
                    jug.coger(nombre);
                }catch(EnergiaInsuficienteException | ImposibleCogerExcepcion | MaximoObjetosException
                        | MaximoPesoException e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("No se ha indicado el nombre del objeto a coger...");
    }
    
}
