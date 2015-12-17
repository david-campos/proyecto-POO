/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Personajes;

import Excepciones.PersonajeException;
import Juego.Juego;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class Floater extends Enemigo{

    public Floater(String nombre, int vida, int energiaPorTurno, int[] posicion, Juego juego) {
        super(nombre, vida, energiaPorTurno, posicion, juego);
    }

    @Override
    protected boolean atacar(Personaje enemigo, int dano) throws PersonajeException {
        if(dano <= 0){
            juego.log("Daño negativo? Güet?");  
            return false;
        }
        
        if(enemigo != null)
        {
            if(r.nextFloat() < 0.25) {
                dano*=2;    //Daño crítico
                if(enemigo instanceof Enemigo) juego.log("¡Has causado un daño crítico!\t");
            }

            int danoReal;
            if(enemigo.getArmadura() != null)
                danoReal = (int) Math.round(dano * (1-enemigo.getArmadura().getDefensa()/200.0));
            else
                danoReal = (int) Math.round(dano);   

            int vidaResultante = enemigo.getVida() - danoReal;

            enemigo.setVida(vidaResultante);
            juego.log(getNombre() + " te ha causado " + danoReal + " de daño.");
        }else{
            juego.log("Enemigo null!");    
            return false;
        }
        return true;
    }
    
    public Floater(String nombre, int[] posicion, Juego juego) {
        super(nombre, posicion, juego);
    }
    
}
