/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Mapa.Celda;
import Mapa.Transitable;
import Personajes.Jugador;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ComandoMirar implements Comando{
    private Jugador jug;
    private Celda c;
    private String nombre;
    public ComandoMirar(Jugador jug, Celda c, String nombre){
        this.jug = jug;
        this.c = c;
        this.nombre = nombre;
    }

    public Jugador getJugador() {
        return jug;
    }

    public void setJugador(Jugador jug) {
        this.jug = jug;
    }

    public Celda getCelda() {
        return c;
    }

    public void setCelda(Celda c) {
        this.c = c;
    }
    public void setCelda(int x, int y){
        if(jug != null)
            this.c = jug.getMapa().getCelda(jug.getPos().x + x, jug.getPos().y + y);
    }

    public String getNombre() {
        return nombre;
    }

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
                }catch(Exception e){
                    throw new ComandoExcepcion(e.getMessage());
                }
            }else
                throw new ComandoExcepcion("Celda a mirar no válida...");
    }
    
}
