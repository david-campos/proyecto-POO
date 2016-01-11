package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 * <p>Comando para aquellos que les gusta saber lo que llevan en su mochila.</p>
 * {@link #ejecutar()} simplemente delega en {@link Jugador#mirarMochila()}
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoMirarMochila implements Comando{
    private Jugador jug;
    
    /**
     * Crea una nueva instancia del comando
     * @param jug jugador que quiere ojear su mochila
     */
    public ComandoMirarMochila(Jugador jug){
        this.jug = jug;
    }

    /**
     * Obtiene el jugador
     * @return El jugador que quiere ojear su mochila
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Cambia el jugador
     * @param jug el nuevo jugador que quiere ojear su mochila
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            jug.mirarMochila();
    }
    
}
