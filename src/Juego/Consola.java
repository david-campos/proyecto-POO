/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Mapa;

/**
 * Interfaz para implementar la consola
 * @author crist
 */
public interface Consola {
    /**
     * Imprime de la forma que se requiera el mensaje
     * @param mensaje mensaje a mostrar
     */
    public void imprimirMapa();
    public void limpiar();
    public void imprimir (String mensaje);
    public void imprimirSinSalto(String mensaje);
    public void cerrar();
    public void imprimirEstado(String mensaje);
    /**
     * Sirve para conseguir datos del usuario
     * @param descripcion Texto a mostrar previo a la introducción de los datos
     * @return Cadena introducida por el usuario.
     */
    public String leer (String descripcion);  
    public String leer();
}
