/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Personajes.Jugador;
import Personajes.Personaje;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class Explosivo extends Objeto{

    /**
     *
     * @param peso
     * @param nombre
     */
    public Explosivo(double peso, String nombre) {
        super(peso, nombre, "Al usarlo, explota una vez finalizado el turno");
    }

    @Override
    public boolean usar(Personaje p) {
        p.getMapa().getCelda(p.getPos()).setBomba(true);
        if(p instanceof Jugador) {
            Jugador jugador = (Jugador) p;
            jugador.getJuego().log("Has colocado una bomba en esta casilla... explotará finalizado el turno.");
        }
        p.getMochila().remObjeto(this);
        return true;
    }
    
    
}
