package Personajes;

/**
 * Interfaz para las constantes de los personajes.
 * @author crist
 */
public interface ConstantesPersonajes {
    /**
     * Gasto de energía base de coger
     */
    int GE_COGER = 1;
    /**
     * Gasto de energía base de atacar
     */
    int GE_ATACAR = 10;
    /**
     * Gasto de energía base de mover
     */
    int GE_MOVER = 5;
    
    /**
     * Vida mínima por defecto de un enemigo aleatorio
     */
    int ENE_MIN_VIDAPORDEFECTO = 50;
    /**
     * Vida máxima por defecto de un enemigo aleatorio
     */
    int ENE_MAX_VIDAPORDEFECTO = 101;
    /**
     * Energía por turno mínima de un enemigo aleatorio
     */
    int ENE_MIN_ENERGIAPT = 31;
    /**
     * Energía por turno máxima de un enemigo aleatorio
     */
    int ENE_MAX_ENERGIAPT = 100;
}
