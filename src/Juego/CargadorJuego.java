package Juego;

import Excepciones.CargadorException;
import java.io.FileNotFoundException;

/**
 * Iinterfaz para los cargadores de juego
 * @author crist
 */
public interface CargadorJuego {

    /**
     * Crea un nuevo juego conforme a los parámetros que el cargador tenga setteados.
     * @param c consola que usará el nuevo juego
     * @return el juego ya creado e iniciado, listo para realizar {@link Juego#iniciar()}
     * @throws CargadorException un error genérico de carga
     * @throws FileNotFoundException cuando necesita cargar de algún archivo y no se encuentra
     * @throws IllegalArgumentException cuando alguno de los parámetros no es válido
     */
    public Juego cargarJuego(Consola c) throws CargadorException, FileNotFoundException, IllegalArgumentException ;    
}
