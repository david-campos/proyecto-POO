/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comandos;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class ComandoMirarPorObjetos implements Comando{
    private String nombreObjeto;
    private Jugador jug;

    public ComandoMirarPorObjetos(String nombreObjeto, Jugador jug) {
        this.nombreObjeto = nombreObjeto;
        this.jug = jug;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public Jugador getJug() {
        return jug;
    }

    public void setJug(Jugador jug) {
        this.jug = jug;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jug != null)
            if(nombreObjeto != null)
                jug.mirar(nombreObjeto);
            else
                jug.mirar();
    }
    
}
