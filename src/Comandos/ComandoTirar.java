package Comandos;

import Excepciones.ComandoExcepcion;
import Objetos.Objeto;
import Personajes.Jugador;

/**
 * Tira un objeto de la mochila del jugador a la celda
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoTirar implements Comando{
    private Jugador jug;
    private String nombre;

    /**
     * Crea una nueva instancia, debe indicarse jugador y nombre de objeto
     * @param jug jugador que tira el objeto
     * @param nombre nombre del objeto que se desea tirar, que ha de encontrarse
     *          en la mochila del jugador
     */
    public ComandoTirar(Jugador jug, String nombre){
        this.jug = jug;
        this.nombre = nombre;
    }

    /**
     * Obtiene el jugador
     * @return El jugador que va a tirar el objeto
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Cambia el jugador
     * @param jug jugador que va a tirar el objeto
     */
    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    /**
     * Obtiene el nombre de objeto a tirar
     * @return nombre del objeto que se desea tirar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del objeto que se desea tirar
     * @param nombre nombre del objeto que se desea tirar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug!=null)
            if(nombre != null && !nombre.isEmpty()){
                Objeto o = jug.getMochila().getObjeto(nombre);
                if(o != null)
                    jug.tirar(o);
                else
                    throw new ComandoExcepcion("El objeto no se encuentra en la mochila.");
            }else
                throw new ComandoExcepcion("No se ha indicado el nombre del objeto a tirar...");
    }
    
}
