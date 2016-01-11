package Comandos;

import Excepciones.ComandoExcepcion;
import Excepciones.ObjetoNoEncontradoException;
import Personajes.Jugador;

/**
 * Comando que ojea la celda en la que se encuentra el jugador, con el fin de
 * o bien listar los objetos en la misma o bien examinar un objeto en particular.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ComandoMirarPorObjetos implements Comando{
    private String nombreObjeto;
    private Jugador jug;

    /**
     * Crea una instancia del comando para un nombre de objeto y jugador concretos
     * @param nombreObjeto nombre del objeto que se desea detallar (null para listar
     *          los objetos en la celda)
     * @param jug jugador que desea observar su celda
     */
    public ComandoMirarPorObjetos(String nombreObjeto, Jugador jug) {
        this.nombreObjeto = nombreObjeto;
        this.jug = jug;
    }

    /**
     * Obtiene el nombre del objeto
     * @return El nombre del objeto
     */
    public String getNombreObjeto() {
        return nombreObjeto;
    }

    /**
     * Cambia el nombre del objeto
     * @param nombreObjeto nuevo nombre del objeto
     */
    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    /**
     * Obtiene el jugador
     * @return El jugador que mirará
     */
    public Jugador getJug() {
        return jug;
    }

    /**
     * Cambia el jugador que realizará la acción del comando
     * @param jug el nuevo jugador
     */
    public void setJug(Jugador jug) {
        this.jug = jug;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        try{
            if(jug != null)
                if(nombreObjeto != null)
                    jug.mirar(nombreObjeto);
                else
                    jug.mirar();
        }catch(ObjetoNoEncontradoException e){
            throw new ComandoExcepcion(e.getMessage());
        }
    }
    
}
