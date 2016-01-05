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
public class ComandoMatar implements Comando{
    private Jugador jugador;

    public ComandoMatar(Jugador jugador) {
        this.jugador = jugador;
    }
    
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(jugador != null){
            jugador.setVida(0);
        }
    }
    
}
