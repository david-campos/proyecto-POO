/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.Mapa;
import java.util.Scanner;

/**
 * Consola sencilla que muestra los mensajes en la consola por defecto
 * @author crist
 */
public final class ConsolaNormal implements Consola{
    
    /**
     * Imprime en la consola por defecto con System.out.println
     * @param mensaje Mensaje a imprimir
     */
    @Override
    public void imprimir(String mensaje) {
        if(mensaje != null)
            System.out.println(mensaje);    
    }
    @Override
    public void imprimirMapa(Mapa map) {
        if(map != null)
            System.out.println(map.visionJugador());
    }
    @Override
    public void imprimirSinSalto(String mensaje) {
        if(mensaje != null)
            System.out.print(mensaje);
    }
    
    /**
     * Pide datos al usario en la consola por defecto
     * @param descripcion Texto a mostrar previo a que el usuario escriba
     * @return Entrada del usuario
     */
    @Override
    public String leer(String descripcion) {
        Scanner scnr = new Scanner(System.in);
        if(descripcion!=null)
            System.out.print(descripcion);
        else
            System.out.print("> ");
        return scnr.nextLine();
    }
        /**
     * Pide datos al usario en la consola por defecto
     * @return Entrada del usuario
     */
    @Override
    public String leer() {
        Scanner scnr = new Scanner(System.in);
        System.out.print("> ");
        return scnr.nextLine();
    }

    /**
     *
     */
    public void cerrar(){}
}
