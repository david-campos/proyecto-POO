package Objetos;

import Excepciones.ObjetoNoUsableException;
import Personajes.Personaje;
import java.util.Random;

/**
 * Escombros, que quedan tirados al destrozar un muro.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class Escombros extends Objeto{
    private static int id = 0;
    private static final Random r = new Random();
    private int usos = 0;
    
    /**
     * Crea escombros
     */
    public Escombros() {
        super(20+r.nextInt(20), "monton_de_escombros_"+(++id), "Pesan, y ocupan espacio.");
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
