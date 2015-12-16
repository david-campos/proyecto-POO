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
public class MConst {
    /*Celdas*/
    public static final String[] CE_REPR_TRANS={" ", " ", "\u001B[32mx\u001B[0m","\u001B[33mx\u001B[0m", "\u001B[35mO\u001B[0m"};
    public static final String[] CE_REPR_NOTRANS={"\u001B[34m\u00A7\u001B[0m", "\u001B[30mM\u001B[0m"};
    
    public static final int HIERBA1 = 0;
    public static final int HIERBA2 = 1;
    public static final int ARBUSTO1 = 2;
    public static final int ARBUSTO2 = 3;
    public static final int BOQUETE = 4;
    
    public static final int AGUA = 0;
    public static final int MURO = 1;
    
    /*IMAGENES*/
    public static final String[] CE_REPG_TRANS={"hierba","hierba","arbusto","arbusto2","boquete"};
    public static final String[] CE_REPG_NOTRANS={"agua", "muro"};
}
