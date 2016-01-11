package Mapa;

/**
 * Archivo de referencia de las constantes generales para temas relacionados
 * con el mapa y las celdas.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public interface ConstantesMapa {
    /*CELDAS*/

    /**
     * Representación de las celdas transitables por texto (según su tipo)
     */
    public String[] CE_REPR_TRANS={" ", " ", " ", "\u001B[32mx\u001B[0m","\u001B[33mx\u001B[0m", "x","\u001B[35mO\u001B[0m", " ", " ", " ", " ", "\u001B[31mx\u001B[0m"};
    /**
     * Representación de las celdas no transitables por texto (según su tipo)
     */
    public String[] CE_REPR_NOTRANS={"\u001B[34m\u00A7\u001B[0m", "\u001B[30mM\u001B[0m"};
    /**
     * Representación gráfica de las celdas transitables (según su tipo)
     */
    public String[] CE_REPG_TRANS={"hierba","hierba2","hierba3", "arbusto","arbusto2","arbusto3","boquete", "camino", "madera", "arena", "fuente","arbusto_rojo"};
    /**
     * Representación gráfica de las celdas no transitables (según su tipo)
     */
    public String[] CE_REPG_NOTRANS={"agua", "muro"};
    
    /**
     * Tipo de la celda transitable hierba1
     */
    public int HIERBA1 = 0;
    /**
     * Tipo de la celda transitable hierba2
     */
    public int HIERBA2 = 1;
    /**
     * Tipo de la celda transitable hierba3
     */
    public int HIERBA3 = 2;
    /**
     * Tipo de la celda transitable arbusto1
     */
    public int ARBUSTO1 = 3;
    /**
     * Tipo de la celda transitable arbusto2
     */
    public int ARBUSTO2 = 4;
    /**
     * Tipo de la celda transitable arbusto3
     */
    public int ARBUSTO3 = 5;
    /**
     * Tipo de la celda transitable boquete
     */
    public int BOQUETE = 6;
    /**
     * Tipo de la celda transitable camino
     */
    public int CAMINO = 7;
    /**
     * Tipo de la celda transitable madera
     */
    public int MADERA = 8;
    /**
     * Tipo de la celda transitable arena
     */
    public int ARENA = 9;
    /**
     * Tipo de la celda transitable fuente
     */
    public int FUENTE = 10;
    /**
     * Tipo de la celda transitable arbusto rojo
     */
    public int ARBUSTO_ROJO = 11;
    
    /**
     * Tipo de celda no transitable agua
     */
    public int AGUA = 0;
    /**
     * Tipo de celda no transitable muro
     */
    public int MURO = 1;
    
    //Usados en generaciones aleatorias, deben tener la misma longitud

    /**
     * Array con los tipos de celdas transitables que el generador aleatorio considerará
     * suelos.
     */
    int SUELOS[] = {HIERBA1, HIERBA2, HIERBA3};
    /**
     * Array con los tipos de celdas transitables que el generador aleatorio considerará
     * arbustos. La posición en el array se corresponde con la de su suelo
     * correspondiente en {@link #SUELOS}.
     */
    int VEGETACIONES[] = {ARBUSTO1, ARBUSTO2, ARBUSTO3};
    
    /*EXPLOSIVO*/
    /**
     * Máxima distancia alcanzada en cada eje por la explosión de una celda.
     * Incluye la propia celda inicial.
     */
    public int EXPLOSION_MAX_RAD = 3;
    
    /*PARÁMETROS*/

    /**
     * Probabilidad de los objetos aleatorios.
     */
    public double P_OBJETOS_ALEATORIOS = 0.8; 
    /**
     * Daño mínimo que causa un arma generada aleatoriamente
     */
    public int DANO_MIN_ARMA = 5;
    /**
     * Defensa mínima de una armadura generada aleatoriamente
     */
    public int DEFENSA_MIN_ARMADURA = 15;
    /**
     * Probabilidad base de la aparición de enemigos
     */
    public double P_ENEMIGOS = 0.005;
}
