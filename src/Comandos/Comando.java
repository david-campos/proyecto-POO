/*
 */
package Comandos;

import Excepciones.ComandoExcepcion;

/**
 * Interfaz para los comandos ejecutados en el juego
 * @author david.campos
 */
public interface Comando {

    /**
     *
     * @throws ComandoExcepcion
     */
    void ejecutar() throws ComandoExcepcion;
}
