package Mapa;

import java.util.Random;

/**
 * Celda no transitable, no puede hacer nada... más que estorbar.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class NoTransitable extends Celda{
    /**
     * Crea una celda no transitable, de tipo aleatorio.
     */
    public NoTransitable() {
        super(0,null);
        Random r = new Random();
        tipo = (int)Math.round(r.nextInt(ConstantesMapa.CE_REPR_NOTRANS.length));
    }

    @Override
    public boolean estaDisponible() {
        return false;
    }
}