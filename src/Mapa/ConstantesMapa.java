/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public interface ConstantesMapa {
    /*CELDAS*/
    public String[] CE_REPR_TRANS={" ", " ", " ", "\u001B[32mx\u001B[0m","\u001B[33mx\u001B[0m", "x","\u001B[35mO\u001B[0m", " ", " ", " ", " ", "\u001B[31mx\u001B[0m"};
    public String[] CE_REPR_NOTRANS={"\u001B[34m\u00A7\u001B[0m", "\u001B[30mM\u001B[0m"};
    public String[] CE_REPG_TRANS={"hierba","hierba2","hierba3", "arbusto","arbusto2","arbusto3","boquete", "camino", "madera", "arena", "fuente","arbusto_rojo"};
    public String[] CE_REPG_NOTRANS={"agua", "muro"};
    
    public int HIERBA1 = 0;
    public int HIERBA2 = 1;
    public int HIERBA3 = 2;
    public int ARBUSTO1 = 3;
    public int ARBUSTO2 = 4;
    public int ARBUSTO3 = 5;
    public int BOQUETE = 6;
    public int CAMINO = 7;
    public int MADERA = 8;
    public int ARENA = 9;
    public int FUENTE = 10;
    public int ARBUSTO_ROJO = 11;
    
    public int AGUA = 0;
    public int MURO = 1;
    
    //Usados en generaciones aleatorias, deben tener la misma longitud
    int SUELOS[] = {HIERBA1, HIERBA2, HIERBA3};
    int VEGETACIONES[] = {ARBUSTO1, ARBUSTO2, ARBUSTO3};
    
    /*EXPLOSIVO*/
    public int EXPLOSION_MAX_RAD = 3;
    
    /*PARÁMETROS*/
    public double P_OBJETOS_ALEATORIOS = 0.8; 
    public int DANO_MIN_ARMA = 5;
    public int DEFENSA_MIN_ARMADURA = 15;
    public double P_ENEMIGOS = 0.005;
}
