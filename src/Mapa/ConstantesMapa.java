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
    public String[] CE_REPR_TRANS={" ", " ", "\u001B[32mx\u001B[0m","\u001B[33mx\u001B[0m", "\u001B[35mO\u001B[0m"};
    public String[] CE_REPR_NOTRANS={"\u001B[34m\u00A7\u001B[0m", "\u001B[30mM\u001B[0m"};
    
    public int HIERBA1 = 0;
    public int HIERBA2 = 1;
    public int ARBUSTO1 = 2;
    public int ARBUSTO2 = 3;
    public int BOQUETE = 4;
    
    public int AGUA = 0;
    public int MURO = 1;
    
    /*IMAGENES*/
    public String[] CE_REPG_TRANS={"hierba","hierba","arbusto","arbusto2","boquete"};
    public String[] CE_REPG_NOTRANS={"agua", "muro"};
    
    /*EXPLOSIVO*/
    public int EXPLOSION_MAX_RAD = 3;
    
    /*PARÁMETROS*/
    public double P_OBJETOS_ALEATORIOS = 0.8; 
    public int DANO_MIN_ARMA = 5;
    public int DEFENSA_MIN_ARMADURA = 15;
    public double P_ENEMIGOS = 0.005;
}
