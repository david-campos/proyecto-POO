/*
 */
package Comandos;

import Excepciones.ComandoExcepcion;

/**
 * Interfaz para los comandos ejecutados en el juego
 * @author david.campos
 */
public interface Comando {
    void ejecutar() throws ComandoExcepcion;
}
