package Mapa;

import java.util.Random;

public final class NoTransitable extends Celda{

    public NoTransitable() {
        super(0,null);
        Random r = new Random();
        tipo = (int)Math.round(r.nextInt(ConstantesMapa.CE_REPR_NOTRANS.length));
    }
}