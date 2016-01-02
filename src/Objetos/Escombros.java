/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Excepciones.ObjetoNoUsableException;
import Personajes.Personaje;
import java.util.Random;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class Escombros extends Objeto{
    private static int id = 0;
    private static final Random r = new Random();
    private int usos = 0;
    
    public Escombros() {
        super(20+r.nextInt(20), "Monton_de_escombros_"+(++id), "Pesan, y ocupan espacio");
    }

    @Override
    public void usar(Personaje p) throws ObjetoNoUsableException {
        if(++usos < 10)
            super.usar(p);
        else{
            p.getMapa().getJuego().log("Estás pesadito... a ver, venga va... 'te has comido los escombros'. Hala, ya no hay escombros.");
            p.getMochila().remObjeto(this);
        }
    }
    
    
}
