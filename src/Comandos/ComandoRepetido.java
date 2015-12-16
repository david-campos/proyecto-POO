/*
 * 
 */
package Comandos;

import Excepciones.ComandoExcepcion;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public final class ComandoRepetido implements Comando{
    private final Comando comando;
    private int veces;

    /**
     *
     * @param comando
     * @param numeroVeces
     * @throws ComandoExcepcion
     */
    public ComandoRepetido(Comando comando, int numeroVeces) throws ComandoExcepcion{
        if(numeroVeces > 0)
            veces = numeroVeces;
        else
            throw new ComandoExcepcion("ComandoRepetido: número de veces incorrecto...");
        this.comando = comando;
    }

    /**
     *
     * @throws ComandoExcepcion
     */
    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(comando != null)
            for(int i=0; i < veces; i++)
                comando.ejecutar();
    }
}
