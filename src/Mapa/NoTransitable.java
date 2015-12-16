package Mapa;

import java.util.Random;

/**
 *
 * @author ElJüsticieroMisteryo
 */
public final class NoTransitable extends Celda{

    /**
     *
     */
    public NoTransitable() {
        super(0,null);
        Random r = new Random();
        tipo = (int)Math.round(r.nextInt(MConst.CE_REPR_NOTRANS.length));
    }
}