package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 * Comando que lleva al jugador a suicidarse (triste, pero cierto).
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ComandoMatar implements Comando{
    private Jugador jugador;

    /**
     * Hace saber al jugador que pronto llegará su muerte.
     * @param jugador el jugador que se pretende llevar al suicidio
     */
    public ComandoMatar(Jugador jugador) {
        this.jugador = jugador;
    }
    
    /**
     * Obtiene el chiquillo en problemas que desea terminar su vida
     * @return El jugador que se suicidará
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Fija otro pobre miserable para un triste final
     * @param jugador jugador que se suicidará
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    /**
     * <p>{@inheritDoc}</p>
     * <p>)': Era un buen chico...</p>
     * @throws ComandoExcepcion Si el comando no se puede ejecutar
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jugador != null){
            jugador.setVida(0);
        }
    }
    
}
