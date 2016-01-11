package Juego;

import Mapa.Mapa;
import java.util.Scanner;

/**
 * Consola sencilla que muestra los mensajes en la consola por defecto
 * @author crist
 */
public final class ConsolaNormal implements Consola{
    private Mapa map;

    /**
     * Crea una nueva consola normal
     * @param map mapa en que se desarrolla el juego
     */
    public ConsolaNormal(Mapa map) {
        this.map = map;
    }

    /**
     * Obtiene el mapa asociado a esta consola
     * @return El mapa asociado
     */
    public Mapa getMap() {
        return map;
    }

    /**
     * Cambia el mapa asociado a esta consola
     * @param map nuevo mapa a asociar
     */
    public void setMap(Mapa map) {
        this.map = map;
    }
    
    @Override
    public void imprimir(String mensaje) {
        if(mensaje != null)
            System.out.println(mensaje);    
    }
    @Override
    public void limpiar(){
        System.out.println();
    }
    @Override
    public void imprimirMapa() {
        if(map != null)
            System.out.println(map.visionJugador());
    }
    @Override
    public void imprimirSinSalto(String mensaje) {
        if(mensaje != null)
            System.out.print(mensaje);
    }
    
    @Override
    public String leer(String descripcion) {
        Scanner scnr = new Scanner(System.in);
        if(descripcion!=null)
            System.out.print(descripcion);
        else
            System.out.print("> ");
        return scnr.nextLine();
    }
    @Override
    public void imprimirEstado(String mensaje){
         imprimirSinSalto(mensaje);
    }
    @Override
    public String leer() {
        Scanner scnr = new Scanner(System.in);
        System.out.print("> ");
        return scnr.nextLine();
    }

    /**
     * {@inheritDoc}<br>
     * En este caso, no hace nada.
     */
    @Override
    public void cerrar(){}

}
