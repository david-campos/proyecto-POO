/*
 */
package Comandos;

import Excepciones.ComandoExcepcion;

/**
 * Interfaz para los comandos ejecutados en el juego
 * @author David Campos
 */
public interface Comando {

    /**
     * Ejecuta el comando, en caso de no poder completarse debido a un error
     * de usuario, lanza ComandoExcepcion con un mensaje para el mismo.
     * @throws ComandoExcepcion Si el comando no se ejecutó con éxito.
     */
    void ejecutar() throws ComandoExcepcion;
}
