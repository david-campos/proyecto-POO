package Comandos;

import Excepciones.CeldaObjetivoNoValida;
import Excepciones.ComandoExcepcion;
import Excepciones.EnemigoNoEncontradoException;
import Mapa.Celda;
import Mapa.Transitable;
import Personajes.Jugador;

/**
 * Si te gusta ojear, ¡este es tu comando! Revisa celdas en busca de enemigos.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoMirar implements Comando{
    private Jugador jug;
    private Celda c;
    private String nombre;
    
    /**
     * Crea un nuevo ComandoMirar, úsese con discreción.
     * @param jug jugador que realizará el comando
     * @param c celda que se desea mirar
     * @param nombre nombre del enemigo que se desea mirar en detalle (en caso
     *        de ser null, se listan los enemigos en la celda)
     */
    public ComandoMirar(Jugador jug, Celda c, String nombre){
        this.jug = jug;
        this.c = c;
        this.nombre = nombre;
    }

    /**
     * Crea un nuevo ComandoMirar, úsese con discreción.
     * @param jug jugador que realizará el comando
     * @param x componente x, relativa al jugador, de la posición de la celda a mirar
     * @param y componente y, relativa al jugador, de la posición de la celda a mirar
     * @param nombre nombre del enemigo que se desea mirar en detalle (en caso
     *        de ser null, se listan los enemigos en la celda)
     */
    public ComandoMirar(Jugador jug, int x, int y, String nombre) {
        this.jug = jug;
        setCelda(x, y);
        this.nombre = nombre;
    }

    
    
    /**
     * Obtiene el jugador mirón
     * @return El jugador que realizará la acción
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Fija/cambia el jugador mirón
     * @param jug el jugador que realizará la acción
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene la celda que está fijada para ser observada
     * @return La celda que se va a observar
     */
    public Celda getCelda() {
        return c;
    }

    /**
     * Fija/cambia la celda que va a ser observada
     * @param c la nueva celda que se quiere mirar
     */
    public void setCelda(Celda c) {
        this.c = c;
    }

    /**
     * Fija/cambia la celda que va a ser observada
     * @param x coordenada x de la nueva celda a mirar (relativa al jugador)
     * @param y coordenada y de la nueva celda a mirar (relativa al jugador)
     */
    public void setCelda(int x, int y){
        if(jug != null)
            this.c = jug.getMapa().getCelda(jug.getPos().x + x, jug.getPos().y + y);
    }

    /**
     * Obtiene el nombre del enemigo buscado (null, en caso de querer listar
     * los enemigos en la celda)
     * @return el nombre del enemigo a mirar en la celda
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Fija/cambia el nombre del enemigo observado (null para listar los enemigos
     * en la celda)
     * @param nombre nuevo nombre del enemigo que se pretende mirar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(c != null && c instanceof Transitable){
                try{
                    if(nombre != null)
                        jug.mirar((Transitable) c, nombre);
                    else
                        jug.mirar((Transitable) c);
                }catch(EnemigoNoEncontradoException | CeldaObjetivoNoValida e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("Celda a mirar no válida...");
    }
    
}
