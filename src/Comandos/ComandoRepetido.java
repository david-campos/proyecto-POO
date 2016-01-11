/*
 * 
 */
package Comandos;

import Excepciones.ComandoExcepcion;

/**
 * Comando repetido varias veces, se ejecuta el número de veces indicado
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public final class ComandoRepetido implements Comando{
    private final Comando comando;
    private int veces;

    /**
     * Crea una nueva instancia de comando a repetir
     * @param comando comando que se desea ejecutar repetidas veces
     * @param numeroVeces numero de veces a ejecutar susodicho comando
     * @throws ComandoExcepcion en caso de así la lance el comando en cuestión,
     *         o si el número de veces es negativo.
     */
    public ComandoRepetido(Comando comando, int numeroVeces) throws ComandoExcepcion{
        if(numeroVeces > 0)
            veces = numeroVeces;
        else
            throw new ComandoExcepcion("ComandoRepetido: número de veces incorrecto...");
        this.comando = comando;
    }

    @Override
    public void ejecutar() throws ComandoExcepcion {
        if(comando != null)
            for(int i=0; i < veces; i++)
                comando.ejecutar();
    }
}
